package in.appnow.astrobuddy.conversation_module.activity.mvp;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.base.BasePresenter;
import in.appnow.astrobuddy.conversation_module.rest_service.models.request.FetchMessageRequest;
import in.appnow.astrobuddy.conversation_module.rest_service.models.request.UpdateMessageRequest;
import in.appnow.astrobuddy.conversation_module.rest_service.models.response.ConversationResponse;
import in.appnow.astrobuddy.conversation_module.utils.ConversationUtils;

import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dialog.UnAuthoriseUserDialog;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.rest.CallbackWrapper;
import in.appnow.astrobuddy.rest.RestUtils;
import in.appnow.astrobuddy.rest.request.UpdateSocketIdRequest;
import in.appnow.astrobuddy.rest.response.BaseResponseModel;
import in.appnow.astrobuddy.utils.FragmentUtils;
import in.appnow.astrobuddy.utils.Logger;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.OkHttpClient;

public class ConversationPresenter implements BasePresenter {
    private static final String TAG = ConversationPresenter.class.getSimpleName();
    private static final long QUEUE_HIDE_DELAY = 2000;
    private final ConversationActivityView view;
    private final ConversationModel model;
    private final PreferenceManger blurtPreferenceManger;
    private final ABDatabase abDatabase;

    private final CompositeDisposable disposable = new CompositeDisposable();

    private boolean typingStarted;
    private boolean isChatMessageStatusUpdated;
    private boolean isConversationGoingOn;
    private boolean isChatMessageFetched;
    private boolean isAgentJoined;
    private String agentId = "";

    public ConversationPresenter(ConversationActivityView view, ConversationModel conversationModel, PreferenceManger blurtPreferenceManger, ABDatabase abDatabase) {
        this.view = view;
        this.model = conversationModel;
        this.blurtPreferenceManger = blurtPreferenceManger;
        this.abDatabase = abDatabase;
    }

    @Override
    public void onCreate() {

        setUpSocket();
        disposable.add(observerTextChange());
        disposable.add(observeEmojiButtonClick());
        disposable.add(observeSendButtonClick());
        disposable.add(observeBackArrowPress());
        onEditTextFocusListener();
    }

    private Disposable observerTextChange() {
        return view.observeTextChange().subscribe(text -> {
            text = text.toString().trim();
            view.onChatTextChange(text);
            if (!TextUtils.isEmpty(text) && text.length() == 1) {
                updateTypingStatus(ConversationUtils.TYPING_STATUS);
            } else if (TextUtils.isEmpty(text) && text.length() == 0 && typingStarted) {
                updateTypingStatus(ConversationUtils.NO_TYPING_STATUS);
            }
        });
    }

    private void onEditTextFocusListener() {
        view.typeMessage.setOnFocusChangeListener((view, b) -> {
            if (!b && typingStarted) {
                updateTypingStatus(ConversationUtils.NO_TYPING_STATUS);
            }
        });
    }

    private Disposable observeEmojiButtonClick() {
        return view.observeEmojiButton().subscribe(__ -> view.toggleEmojiPopup());
    }

    private Disposable observeBackArrowPress() {
        return view.observeBackArrowPress().subscribe(__ -> model.onBackArrowPress());
    }

    private Disposable observeSendButtonClick() {
        return view.observeSendButton()
                .map(isValidate -> !TextUtils.isEmpty(view.getTypedMessage()) && AstroApplication.getInstance().isInternetConnected(true))
                .subscribe(isValidate -> {
                    if (isValidate) {
                        long timeStamp = System.currentTimeMillis();

                        ConversationResponse response = new ConversationResponse();
                        response.setMessage(view.getTypedMessage());
                        response.setMessageStatus(ConversationUtils.MESSAGE_SEND);
                        response.setSendDateTime(timeStamp);
                        response.setSessionId(model.getSessionId());
                        response.setSenderId(blurtPreferenceManger.getUserDetails().getUserId());
                        response.setMessageType("TEXT");

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("msg", view.getTypedMessage());
                        jsonObject.put("timestamp", timeStamp);
                        jsonObject.put("chat_id", model.getSessionId());
                        jsonObject.put("sentBy", blurtPreferenceManger.getUserDetails().getUserId());
                        jsonObject.put("status", ConversationUtils.MESSAGE_SENT);
                        jsonObject.put("notify", agentId);
                        jsonObject.put("msg_type", "TEXT");

                        view.updateTypeMessage("");

                        Logger.DebugLog(TAG, "Send message : " + jsonObject.toString());

                        mSocket.emit(EVENT_SEND_MESSAGE, jsonObject, (Ack) args -> Logger.DebugLog(TAG, "Message Send : " + args[0]));

                        AsyncTask.execute(() -> abDatabase.conversationDao().insert(response));

                    }
                });
    }

    @SuppressLint("StaticFieldLeak")
    private void addWelcomeMessage() {
        ConversationResponse conversationModel = new ConversationResponse();
        conversationModel.setMessage("Hi Welcome. How may i help you?");
        conversationModel.setMessageId(1);
        conversationModel.setMessageStatus(ConversationUtils.MESSAGE_SEND);
        conversationModel.setMessageType("TEXT");
        conversationModel.setSenderId("0");//receiver Id
        conversationModel.setSessionId(model.getSessionId());

        conversationModel.setSendDateTime(System.currentTimeMillis());

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                model.insertData(conversationModel);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }.execute();


    }

    @Override
    public void onDestroy() {
        if (mSocket != null) {
            mSocket.disconnect();
            mSocket.off(EVENT_NOTIFICATION, onHandlerCallback);
            mSocket.off(EVENT_MESSAGES, onNewMessage);
            mSocket.off(EVENT_MSG_HELPER, onMsgHelper);
        }
        disposable.clear();
    }

    @Override
    public void unAuthorizeUserAccess(String message) {
        UnAuthoriseUserDialog.getInstance().showLogOutDialog(
                model.getAppCompatActivity(),
                message,
                blurtPreferenceManger,
                abDatabase,
                new UnAuthoriseUserDialog.OnLogoutListener() {
                    @Override
                    public void OnClickLogOut() {
                        model.showProgressBar();
                    }

                    @Override
                    public void OnLogOut() {
                        model.hideProgressBar();
                        FragmentUtils.onLogoutSuccess(model.getAppCompatActivity());
                    }
                });
    }


    private void fetchAllChatMessages() {
        //  this.conversationViewModel = conversationViewModel;
        model.getAllMessages().observe(model.getAppCompatActivity(), conversationResponses -> {

            if (conversationResponses == null) {
                return;
            }

            if (conversationResponses.isEmpty()) {
                //fetchData(topicItemViewModel);
                if (isConversationGoingOn) {
                    //End chat happened
                    isConversationGoingOn = false;
                    // model.close();
                    model.openChatFeedbackActivity(model.getSessionId());
                    return;
                }
                addWelcomeMessage();

            } else {
                isConversationGoingOn = true;
                view.setData(conversationResponses, blurtPreferenceManger);
                //checkIfAnyMessageStatusIsNotRead(conversationResponses);
                // model.updateEndChatMenu(!view.isChatEmpty());
            }
            // }
        });
    }


    public boolean onBackPress() {
        return view.dismissEmojiPopup();
    }

    public void openChatFeedbackScreen() {
        model.openChatFeedbackActivity(model.getSessionId());
    }


    private Disposable chatTopLabelHideDelay() {
        return Observable.timer(QUEUE_HIDE_DELAY, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(__ -> {
                    view.showHideQueueLabel(View.GONE);
                });

    }

    /* =====   Update queue label Methods ENDS!!!!  ===== */


    public void updateTypingStatus(int typingStatus) {
        typingStarted = typingStatus == ConversationUtils.TYPING_STATUS;
        if (mSocket != null) {
            try {
                String typing = typingStatus == ConversationUtils.TYPING_STATUS ? "TYPING" : "NOT_TYPING";
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("chat_id", model.getSessionId());
                jsonObject.put("TYPE", typing);
                jsonObject.put("sender", blurtPreferenceManger.getUserDetails().getUserId());
                mSocket.emit(EVENT_NOTIFICATION, jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void checkIfAnyMessageStatusIsNotRead(List<ConversationResponse> conversationResponses) {
        if (isChatMessageStatusUpdated)
            return;
        AsyncTask.execute(() -> {
            isChatMessageStatusUpdated = true;
            if (conversationResponses != null && conversationResponses.size() > 0) {
                UpdateMessageRequest request = new UpdateMessageRequest();
                List<Long> messageIds = new ArrayList<>();
                for (ConversationResponse response : conversationResponses) {
                    if (response != null) {
                        if ((TextUtils.isEmpty(response.getMessageStatus()) || response.getMessageStatus().equalsIgnoreCase(ConversationUtils.MESSAGE_RECEIVED) || response.getMessageStatus().equalsIgnoreCase(ConversationUtils.MESSAGE_SENT))) {
                            messageIds.add(response.getMessageId());
                        }
                    }
                }
                if (messageIds.size() > 0) {
                    request.setMessageIds(messageIds);
                    request.setUserId(blurtPreferenceManger.getUserDetails().getUserId());
                    request.setSessionId(model.getSessionId());
                    request.setMessageStatus(ConversationUtils.MESSAGE_READ);
                    model.updateMessageStatus(request);
                }
            }
        });

    }

    private static final String EVENT_START_CHAT_ROOM = "startChatRoom";
    private static final String EVENT_SEND_MESSAGE = "send_msg";
    private static final String EVENT_NOTIFICATION = "notification";
    private static final String EVENT_MESSAGES = "messages";
    private static final String EVENT_MSG_HELPER = "msg_helper";

    private Socket mSocket;

    {
        try {
            @SuppressLint("BadHostnameVerifier")
            HostnameVerifier hostnameVerifier = (hostname, session) -> true;
            @SuppressLint("TrustAllX509TrustManager")
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {

                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {

                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }
            }};
            X509TrustManager trustManager = (X509TrustManager) trustAllCerts[0];

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, null);
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .hostnameVerifier(hostnameVerifier)
                    .sslSocketFactory(sslSocketFactory, trustManager)
                    .build();

            IO.Options opts = new IO.Options();
            opts.timeout = -1;
            //opts.hostnameVerifier = hostnameVerifier;
            //opts.sslContext = sslContext;
            opts.callFactory = okHttpClient;
            opts.webSocketFactory = okHttpClient;
            mSocket = IO.socket(RestUtils.getEndPoint(), opts);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setUpSocket() {

        mSocket.on(Socket.EVENT_PING, args -> {
            if (args.length > 0) {
                JSONObject data = (JSONObject) args[0];
                String ping;
                try {
                    ping = data.getString("beat");
                } catch (JSONException e) {
                    return;
                }
                if (ping.equals("1")) {
                    mSocket.emit(Socket.EVENT_PONG, "pong");
                }
            }
        });

        mSocket.on(Socket.EVENT_CONNECT, args -> disposable.add(updateSocketId(mSocket.id())));
        mSocket.on(Socket.EVENT_ERROR, args -> Logger.ErrorLog(TAG, "EVENT ERROR : " + args[0].toString()));

        mSocket.on(Socket.EVENT_RECONNECT, args -> Logger.ErrorLog(TAG, "EVENT_RECONNECT: " + args[0].toString()));
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, args -> Logger.ErrorLog(TAG, "EVENT_CONNECT_TIMEOUT: " + args[0].toString()));

        mSocket.on(Socket.EVENT_RECONNECT_ERROR, args -> Logger.ErrorLog(TAG, "EVENT_RECONNECT_ERROR: " + args[0].toString()));

        mSocket.on(Socket.EVENT_RECONNECT_FAILED, args -> Logger.ErrorLog(TAG, "EVENT_RECONNECT_FAILED: " + args[0].toString()));

        mSocket.on(EVENT_NOTIFICATION, onHandlerCallback);
        mSocket.on(EVENT_MESSAGES, onNewMessage);
        mSocket.on(EVENT_MSG_HELPER, onMsgHelper);

        mSocket.connect();

    }

    private void joinChatRoom() {
        Object[] obj = new Object[]{model.getSessionId(), blurtPreferenceManger.getUserDetails().getUserId()};
        mSocket.emit(EVENT_START_CHAT_ROOM, obj, args -> Logger.DebugLog(TAG, "START CHAT ROOM : " + args[0].toString()));
        updateClientStatus();
    }

    private void updateClientStatus() {
        if (mSocket != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("chat_id", model.getSessionId());
                jsonObject.put("TYPE", "USER_IS_LIVE");
                jsonObject.put("sender", blurtPreferenceManger.getUserDetails().getUserId());
                mSocket.emit(EVENT_NOTIFICATION, jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Emitter.Listener onHandlerCallback = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Logger.DebugLog(TAG, "EVENT NOTIFICATION : " + args[0].toString());
            try {
                JSONObject data = (JSONObject) args[0];
                String sessionId = "", type = "", sender = "";
                if (data.has("chat_id")) {
                    sessionId = data.getString("chat_id");
                }
                if (data.has("TYPE")) {
                    type = data.getString("TYPE");
                }
                if (data.has("sender")) {
                    sender = data.getString("sender");
                }
                String finalType = type;
                String finalSender = sender;
                model.getAppCompatActivity().runOnUiThread(() -> {
                    switch (finalType) {
                        case "WAITING_TO_JOIN":
                            if (!isAgentJoined) {
                                view.updateQueueLabelText("Waiting to join by astrobuddy.");
                            }
                            break;
                        case "AGENT_JOINED":
                            if (!isAgentJoined) {
                                view.updateQueueLabelText("AstroBuddy joined.");
                                disposable.add(chatTopLabelHideDelay());
                                isAgentJoined = true;
                            }
                            String agent_Id = "";
                            if (data.has("agent_id")) {
                                try {
                                    agent_Id = data.getString("agent_id");
                                    agentId = agent_Id;
                                } catch (Exception ignored) {

                                }
                            }
                            view.setAstroBuddyStatus("Online");
                            updateClientStatus();
                            break;
                        case "USER_DISCONNECTED":
                            view.setAstroBuddyStatus("Offline");
                            break;
                        case "TYPING":
                            if (isCurrentUser(finalSender)) {
                                view.updateTypingStatus(ConversationUtils.TYPING_STATUS);
                            }
                            break;
                        case "NOT_TYPING":
                            if (isCurrentUser(finalSender)) {
                                view.updateTypingStatus(ConversationUtils.NO_TYPING_STATUS);
                            }
                            break;
                        case "END_CHAT":
                            AsyncTask.execute(() -> abDatabase.conversationDao().deleteChatTable());
                            ToastUtils.longToast("Chat finished");
                            break;
                        case "STORED_MSG":
                            //get all messages
                            try {
                                if (data.has("result")) {
                                    JSONArray chatArray = data.getJSONArray("result");
                                    if (chatArray != null && chatArray.length() > 0) {
                                        List<ConversationResponse> conversationResponseList = new ArrayList<>();
                                        for (int i = 0; i < chatArray.length(); i++) {
                                            JSONObject jsonObject = chatArray.getJSONObject(i);
                                            long msgId = jsonObject.getLong("msg_id");
                                            String message = jsonObject.getString("msg");
                                            long timeStamp = jsonObject.getLong("timestamp");
                                            String sentBy = jsonObject.getString("sentBy");
                                            String status = jsonObject.getString("status");
                                            String msgType = jsonObject.getString("msg_type");
                                            String chatId = jsonObject.getString("chat_id");

                                            ConversationResponse response = new ConversationResponse();
                                            response.setMessage(message);
                                            response.setMessageStatus(ConversationUtils.MESSAGE_RECEIVED);
                                            response.setSendDateTime(timeStamp);
                                            response.setMessageId(msgId);
                                            response.setMessageType(msgType);
                                            response.setMessageStatus(status);
                                            response.setSessionId(chatId);
                                            response.setSenderId(sentBy);
                                            conversationResponseList.add(response);
                                        }
                                        updateMessageStatus(conversationResponseList);
                                    }
                                }
                            } catch (Exception ignored) {
                            }
                            break;
                    }
                });


            } catch (Exception ignored) {
            }
        }
    };

    private boolean isCurrentUser(String senderId) {
        return !senderId.equalsIgnoreCase(blurtPreferenceManger.getUserDetails().getUserId());
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Logger.DebugLog(TAG, "EVENT NEW MESSAGE : " + args[0].toString());
            model.getAppCompatActivity().runOnUiThread(() -> {

                try {
                    JSONObject data = (JSONObject) args[0];
                    String sessionId = "", message = "", sender = "" , msgType = "";
                    long timeStamp = 0, messageId = 0;
                    if (data.has("chat_id")) {
                        sessionId = data.getString("chat_id");
                    }
                    if (data.has("msg")) {
                        message = data.getString("msg");
                    }
                    if (data.has("timestamp")) {
                        timeStamp = data.getLong("timestamp");
                    }
                    if (data.has("sentBy")) {
                        sender = data.getString("sentBy");
                    }
                    if (data.has("msg_id")) {
                        messageId = data.getLong("msg_id");
                    }
                    if (data.has("msg_type")) {
                        msgType = data.getString("msg_type");
                    }
                    if (!sender.equalsIgnoreCase(blurtPreferenceManger.getUserDetails().getUserId())) {
                        ConversationResponse response = new ConversationResponse();
                        response.setMessage(message);
                        response.setMessageStatus(ConversationUtils.MESSAGE_RECEIVED);
                        response.setSendDateTime(timeStamp);
                        response.setSessionId(sessionId);
                        response.setSenderId(sender);
                        response.setMessageType(msgType);
                        response.setMessageId(messageId);
                        AsyncTask.execute(() -> abDatabase.conversationDao().insert(response));
                        if (messageId > 0) {
                            updateMessageStatus(new JSONArray().put(messageId));
                        }
                    } else {
                        long finalTimeStamp = timeStamp;
                        long finalMessageId = messageId;
                        AsyncTask.execute(() -> abDatabase.conversationDao().updateMessageStatusForTimeStamp(ConversationUtils.MESSAGE_SENT, finalTimeStamp, finalMessageId));

                    }
                } catch (Exception ignored) {
                }
            });
        }
    };

    private Disposable updateSocketId(String socketId) {
        UpdateSocketIdRequest request = new UpdateSocketIdRequest();
        request.setSocketId(socketId);
        return model.updateSocketId(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new CallbackWrapper<BaseResponseModel>(view, this) {
                    @Override
                    protected void onSuccess(BaseResponseModel data) {
                        joinChatRoom();
                        if (!isChatMessageFetched) {
                            fetchAllChatMessages();
                            FetchMessageRequest requestModel = new FetchMessageRequest();
                            requestModel.setSessionId(model.getSessionId());
                            model.getAllMessagesFromServer(requestModel);
                            isChatMessageFetched = true;
                        }
                    }
                });
    }

    private void updateMessageStatus(List<ConversationResponse> conversationResponseList) {
        if (isChatMessageStatusUpdated)
            return;

        isChatMessageStatusUpdated = true;
        if (conversationResponseList != null && conversationResponseList.size() > 0) {
            JSONArray messageIds = new JSONArray();
            for (ConversationResponse conversationResponse : conversationResponseList) {
                if (conversationResponse.getMessageId() > 1) {//1 is for default welcome message
                    if (isCurrentUser(conversationResponse.getSenderId())) {
                        if (!conversationResponse.getMessageStatus().equals(ConversationUtils.MESSAGE_READ)) {
                            messageIds.put(conversationResponse.getMessageId());
                        }
                    }
                }
            }
            if (messageIds.length() > 0)
                updateMessageStatus(messageIds);
        }
    }

    private void updateMessageStatus(JSONArray messageIds) {
        if (mSocket != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("chat_id", model.getSessionId());
                jsonObject.put("TYPE", "MSG_DELIVERY");
                jsonObject.put("sender", blurtPreferenceManger.getUserDetails().getUserId());
                jsonObject.put("notify", agentId);//user id
                jsonObject.put("msg_status", ConversationUtils.MESSAGE_READ);//user id
                jsonObject.put("msg_id", messageIds);//user id
                Logger.DebugLog(TAG, "UPDATE STATUS : " + jsonObject.toString());
                mSocket.emit(EVENT_MSG_HELPER, jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Emitter.Listener onMsgHelper = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Logger.DebugLog(TAG, "EVENT MSG CALLBACK : " + args[0].toString());

            model.getAppCompatActivity().runOnUiThread(() -> {
                try {
                    JSONObject data = (JSONObject) args[0];
                    String sessionId = "", status = "", sender = "";
                    long[] messageId = null;

                    if (data.has("chat_id")) {
                        sessionId = data.getString("chat_id");
                    }
                    if (data.has("sender")) {
                        sender = data.getString("sender");
                    }
                    if (data.has("msg_id")) {
                        JSONArray messageIdArray = data.getJSONArray("msg_id");
                        JSONArray messageIdArry = messageIdArray.getJSONArray(0);
                        messageId = new long[messageIdArry.length()];
                        for (int i = 0; i < messageIdArry.length(); i++) {
                            messageId[i] = messageIdArry.getLong(i);
                        }
                    }
                    if (data.has("msg_status")) {
                        status = data.getString("msg_status");
                    }
                    Logger.DebugLog(TAG, "MSG CALLBACK : " + blurtPreferenceManger.getUserDetails().getUserId() + " - " + sender);
                    if (!sender.equalsIgnoreCase(blurtPreferenceManger.getUserDetails().getUserId()) && messageId != null && messageId.length > 0) {
                        //  view.updateChatStatus(messageId, status);
                        updateMessageStatus(messageId, status);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        }
    };

    private void updateMessageStatus(long[] messageIds, String status) {
        Logger.DebugLog(TAG, "UPDATE MESSAGE : " + Arrays.toString(messageIds) + " - " + status);
        AsyncTask.execute(() -> {
            for (Long messageId : messageIds) {
                int id = abDatabase.conversationDao().updateChatMessageStatus(status, messageId);
                Logger.DebugLog(TAG, "UPDATE DONE : " + id);
            }
        });

    }


}
