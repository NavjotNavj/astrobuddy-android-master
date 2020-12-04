package in.appnow.astrobuddy.fcm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.conversation_module.activity.ConversationActivity;
import in.appnow.astrobuddy.conversation_module.rest_service.models.request.UpdateMessageRequest;
import in.appnow.astrobuddy.conversation_module.rest_service.models.response.ConversationResponse;
import in.appnow.astrobuddy.conversation_module.utils.ConversationUtils;
import in.appnow.astrobuddy.dagger.component.DaggerMyServiceComponent;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.jobs.ChatQuerySendJob;
import in.appnow.astrobuddy.jobs.TrackNotificationClickJob;
import in.appnow.astrobuddy.jobs.UpdateMessageStatusJob;
import in.appnow.astrobuddy.models.LogoutModel;
import in.appnow.astrobuddy.models.PendingFeedbackModel;
import in.appnow.astrobuddy.services.MyIntentService;
import in.appnow.astrobuddy.ui.activities.home.HomeActivity;
import in.appnow.astrobuddy.rest.response.UserProfile;
import in.appnow.astrobuddy.utils.Logger;
import in.appnow.astrobuddy.utils.StringUtils;


/**
 * Created by Sajeev on 06-04-2017.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    @Inject
    PreferenceManger preferenceManger;
    @Inject
    ABDatabase abDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerMyServiceComponent.builder()
                .appComponent(AstroApplication.get(this).component())
                .build()
                .inject(this);
    }


    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {

        // Saving reg id to shared preferences
        storeRegIdInPref(token);

        //subscribe to update topics
        Config.doSubscriptionTopic(Config.UPDATE_TOPIC, true);

    }

    private void storeRegIdInPref(String token) {
        // TODO: 10/04/18 Implement dagger preference manager
        preferenceManger.putString(PreferenceManger.FCM_TOKEN, token);
    }

        @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            Logger.ErrorLog(TAG, "From: " + remoteMessage.getFrom());

            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                Logger.DebugLog(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
                //ToastUtils.longToast("Notification:\n"+remoteMessage.getNotification().getBody());

                handleNotification(remoteMessage.getNotification());
            }

            // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0) {
                Logger.DebugLog(TAG, "Data Payload: " + remoteMessage.getData().toString());
                // ToastUtils.longToast("Data:\n"+remoteMessage.getData().toString());
                try {
                    //JSONObject json = new JSONObject(remoteMessage.getData().toString());
                    handleDataMessage(remoteMessage.getData());
                } catch (Exception e) {
                    Logger.ErrorLog(TAG, "Exception: " + e.getMessage());
                }
            }
        }
        catch (Exception ignored){

        }
    }

    private void handleNotification(RemoteMessage.Notification notification) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            //app is in foreground, broadcast the push message


        } else {
            // If app is in background, firebase itself handles the notification
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void handleDataMessage(Map<String, String> dataPayload) {
        try {

            Iterator myVeryOwnIterator = dataPayload.keySet().iterator();
            JSONObject jsonObject = new JSONObject();
            while (myVeryOwnIterator.hasNext()) {
                String key = (String) myVeryOwnIterator.next();
                String value = dataPayload.get(key);
                Logger.DebugLog(TAG, "Data : " + key + " : " + value + "\n");
                jsonObject.put(key, value);
            }

            String title = jsonObject.getString(Config.TITLE);
            String body = jsonObject.getString(Config.BODY);
            String msg_type = jsonObject.getString(Config.MESSAGE_TYPE);
            String sessionId = "";
            Logger.DebugLog(TAG, "title: " + title + "\n" + "message: " + body + "\n" + "msg_type : " + msg_type);

            //create main activity intent
            Intent resultIntent = new Intent(getApplicationContext(), HomeActivity.class);

            //put message type as extra in intent to open respective screen
            resultIntent.putExtra(Config.MESSAGE_TYPE, msg_type);
            resultIntent.putExtra(Config.TITLE, title);
            resultIntent.putExtra(Config.BODY, body);

            UserProfile user = preferenceManger.getUserDetails().getUserProfile();

            if (user == null) {
                checkIfPingPush(jsonObject, msg_type);
                return;
            }
            //get user id
            String loggedInUserId = user.getUserId();
            Logger.DebugLog(TAG, "logged in user id : " + loggedInUserId);

            //return if no user is logged in
            if (TextUtils.isEmpty(loggedInUserId)) {
                checkIfPingPush(jsonObject, msg_type);
                return;
            }

            switch (msg_type) {
                case Config.PICK_REQUEST_PUSH:

                    if (!ConversationActivity.isVisible) {
                        showNotificationMessage(getApplicationContext(), title, body, resultIntent, Config.PICK_REQUEST_NOTIFICATION_ID, R.drawable.ic_launcher_notification, NotificationUtils.CHAT_UPDATES_CHANNEL);
                    }
                    break;
                case Config.CHAT_MESSAGE_PUSH:
                    try {
                        String toUserId = jsonObject.getString("sentBy");
                        String fromUserId = jsonObject.getString("sentBy");
                        String msgType = jsonObject.getString("msg_type");
                        String status = jsonObject.getString("status");
                        sessionId = jsonObject.getString("chat_id");
                        long msgId = jsonObject.getLong("msg_id");
                        long dateTime = jsonObject.getLong("timestamp");

                        ConversationResponse conversationResponse = new ConversationResponse();
                        conversationResponse.setSenderId(fromUserId);
                        conversationResponse.setMessageId(msgId);
                        conversationResponse.setSendDateTime(dateTime);
                        conversationResponse.setMessage(body);
                        conversationResponse.setMessageType(msgType);
                        conversationResponse.setMessageStatus(status);

                        conversationResponse.setSessionId(sessionId);

                        String finalSessionId = sessionId;
                        new AsyncTask<Void, Void, Void>() {

                            @Override
                            protected Void doInBackground(Void... voids) {
                                long tempId = abDatabase.conversationDao().insert(conversationResponse);
                                Logger.DebugLog(TAG, "Chat message added to Database : " + tempId + "\n" + conversationResponse.toString());
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);
                                List<Long> messageIdList = new ArrayList<>();
                                messageIdList.add(msgId);
                                UpdateMessageRequest updateMessageRequest = new UpdateMessageRequest();
                                updateMessageRequest.setUserId(preferenceManger.getUserDetails().getUserProfile().getUserId());
                                updateMessageRequest.setHandlerId(fromUserId);
                                updateMessageRequest.setSessionId(finalSessionId);
                                updateMessageRequest.setMessageStatus(ConversationUtils.MESSAGE_READ);
                                updateMessageRequest.setMessageIds(messageIdList);

                                if (!ConversationActivity.isVisible) {
                                    showNotificationMessage(getApplicationContext(), title, body, resultIntent, Config.CHAT_MESSAGE_NOTIFICATION_ID, R.drawable.ic_launcher_notification, NotificationUtils.CHAT_UPDATES_CHANNEL);
                                }
//else {
//                                    updateMessageRequest.setMessageStatus(ConversationUtils.MESSAGE_READ);
//                                    Bundle bundle = new Bundle();
//                                    bundle.putParcelable(Config.MESSAGE_DATA, conversationResponse);
//                                    Intent messageSent = new Intent(Config.CHAT_MESSAGE_PUSH);
//                                    messageSent.putExtras(bundle);
//                                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(messageSent);
//                                }

                                //updateMessageStatus(updateMessageRequest);
                            }
                        }.execute();
                    } catch (Exception e) {
                       // Crashlytics.logException(e);
                    }

                    break;
                case Config.END_CHAT_PUSH:
//chat_session_id, end_timestamp, timestamp,fdbk_pending
                    try {

                            String chatSessionId = "";
                            String startTimeStamp = "";
                            String endTimestamp = "";
                            if (jsonObject.has("chat_id")) {
                                chatSessionId = jsonObject.getString("chat_id");
                                startTimeStamp = jsonObject.getString("start_time");
                                endTimestamp = jsonObject.getString("end_time");
                            }

                            PendingFeedbackModel pendingFeedbackModel = new PendingFeedbackModel(true, chatSessionId, startTimeStamp, endTimestamp);
                            preferenceManger.putPendingFeedback(pendingFeedbackModel);

                            if (!ConversationActivity.isVisible) {
                                showNotificationMessage(getApplicationContext(), title, body, resultIntent, Config.END_CHAT_NOTIFICATION_ID, R.drawable.ic_launcher_notification, NotificationUtils.CHAT_UPDATES_CHANNEL);
                            }
                        NotificationUtils.clearSingleNotification(Config.CHAT_MESSAGE_NOTIFICATION_ID);
                        AsyncTask.execute(() -> {
                            abDatabase.conversationDao().deleteChatTable();
                            preferenceManger.putString(PreferenceManger.HANDLER_ID, "");
                            preferenceManger.putString(PreferenceManger.CHAT_SESSION_ID, "");
                        });
                    } catch (Exception e) {
                       // Crashlytics.logException(e);
                        Logger.DebugLog("END CHAT EXCEPTION",e.getMessage());
                    }
                    break;

                case Config.MESSAGE_STATUS_UPDATE:
                    String messageIds = jsonObject.getString("message_ids");
                    String messageStatus = jsonObject.getString("status");
                    if (!TextUtils.isEmpty(messageIds)) {
                        messageIds = messageIds.replace("[", "").replace("]", "");
                        Logger.DebugLog(TAG, "Messg ids: " + messageIds);
                        List<String> messageIdList = StringUtils.convertStringToStringArrayList(messageIds);
                        Logger.DebugLog(TAG, "Mesge array sisxe:  " + messageIdList.size());
                        AsyncTask.execute(() -> {
                            for (int i = 0; i < messageIdList.size(); i++) {
                                if (!TextUtils.isEmpty(messageIdList.get(i))) {
                                    abDatabase.conversationDao().updateChatMessageStatus(messageStatus, Long.parseLong(messageIdList.get(i)));
                                }
                            }
                        });
                    }
                    break;
                case Config.TRANSACTION_REPORT_PUSH:
                case Config.CAMPAIGN_PUSH:
                case Config.VOUCHER_PUSH:
                case Config.SUBSCRIPTION_PUSH:
                case Config.NORMAL_ALERT_PUSH:
                case Config.OFFER_PUSH:
                case Config.MYTH_BUSTER_PUSH:
                    if (jsonObject.has("action")) {
                        String action = jsonObject.getString("action");
                        resultIntent.putExtra(Config.ACTION, action);
                    }
                    String imagePath = "";
                    if (jsonObject.has("fileName"))
                        imagePath = jsonObject.getString("fileName");

                    if (jsonObject.has("action")) {
                        String action = jsonObject.getString("action");
                        if (action.equalsIgnoreCase(Config.MYTH_BUSTER_VIDEO_ACTION)){
                            resultIntent.putExtra(Config.MYTH_ACTION_VIDEO_ID,imagePath);
                        }
                    }


                    if (jsonObject.has("noti_id")) {
                        int notificationId = jsonObject.getInt("noti_id");
                        resultIntent.putExtra(MyIntentService.NOTIFICATION_ID, notificationId);
                    }

                    if (jsonObject.has(Config.MYTH_ACTION_ID)){
                        resultIntent.putExtra(Config.MYTH_ACTION_ID,jsonObject.getInt(Config.MYTH_ACTION_ID));
                    }




                    if (!TextUtils.isEmpty(imagePath)) {
                        showNotificationMessageWithBigImage(getApplicationContext(), title, body, resultIntent, imagePath, Config.GENERAL_NOTIFICATION, R.drawable.ic_launcher_notification, NotificationUtils.PROMOTIONAL_CHANNEL);
                    } else {
                        showNotificationMessage(getApplicationContext(), title, body, resultIntent, Config.GENERAL_NOTIFICATION, R.drawable.ic_launcher_notification, NotificationUtils.PROMOTIONAL_CHANNEL);
                    }

                    break;
                case Config.PING_PUSH:
                    checkIfPingPush(jsonObject, msg_type);
                    break;
                case Config.LOGOUT_PUSH:
                    //force logout
                    LogoutModel logoutModel = new LogoutModel();
                    logoutModel.setBody(body);
                    logoutModel.setTitle(title);
                    logoutModel.setForceLogout(true);
                    preferenceManger.putSessionExpiredModel(logoutModel);
                    break;
            }

        } catch (Exception e) {
            //Crashlytics.logException(e);
        }
    }

    private void checkIfPingPush(JSONObject jsonObject, String msg_type) throws JSONException {
        if (msg_type.equalsIgnoreCase(Config.PING_PUSH)) {
            if (jsonObject.has("noti_id")) {
                int notificationId = jsonObject.getInt("noti_id");
                trackNotificationEvent(notificationId);
            }
        }
    }

    /* method to track notification click event */
    public void trackNotificationEvent(int notificationId) {
        try {
            TrackNotificationClickJob.schedule(notificationId);
        } catch (Exception e) {
            //Crashlytics.logException(e);
        }
    }


    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, Intent intent, int notificationID, int icon, String channelId) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, intent, notificationID, icon, channelId, R.mipmap.ic_launcher);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, Intent intent, String imageUrl, int notificationID, int icon, String channelId) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, intent, imageUrl, notificationID, icon, channelId, R.mipmap.ic_launcher);
    }


    @SuppressLint("StaticFieldLeak")
    private void sendSavedQueryToHandler(String handlerId, String sessionId) {
        try {
//            AsyncTask.execute(() -> {
//                int id = abDatabase.conversationDao().updateReceiverId(Integer.parseInt(handlerId));
//                Logger.DebugLog(TAG, "Id : " + id);
//            });

            String query = preferenceManger.getStringValue(PreferenceManger.CHAT_QUERY);
            if (!TextUtils.isEmpty(query)) {

                preferenceManger.putString(PreferenceManger.CHAT_QUERY, "");

                ConversationResponse conversationModel = new ConversationResponse();
                conversationModel.setMessage(query);

                conversationModel.setMessageStatus(ConversationUtils.MESSAGE_SEND);
                //conversationModel.setIsMessageMine(ConversationUtils.MESSAGE_MINE);
                conversationModel.setSenderId(preferenceManger.getUserDetails().getUserProfile().getUserId());//receiver Id
              //  conversationModel.setReceiverId(handlerId);//sender Id
              //  conversationModel.setMessageType(ConversationUtils.MESSAGE_TEXT_TYPE);
                conversationModel.setSessionId(sessionId);
                conversationModel.setMessageType("TEXT");

                conversationModel.setSendDateTime(System.currentTimeMillis());

                //add Message to Database
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... voids) {
                        long messageTempId = abDatabase.conversationDao().insert(conversationModel);
                        //add temporary message id to model
                        conversationModel.setMessageTemporaryId(messageTempId);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        sendQueryInBackground(conversationModel);

                    }
                }.execute();
            }
        } catch (Exception e) {
           // Crashlytics.logException(e);
        }
    }

    private void sendQueryInBackground(ConversationResponse conversationResponse) {
        try {
            ChatQuerySendJob.schedule(conversationResponse);
        } catch (Exception e) {
           // Crashlytics.logException(e);
        }
    }

    private void updateMessageStatus(UpdateMessageRequest updateMessageRequest) {
        try {
            UpdateMessageStatusJob.schedule(updateMessageRequest);
        } catch (Exception e) {
            //Crashlytics.logException(e);
        }

    }
}
