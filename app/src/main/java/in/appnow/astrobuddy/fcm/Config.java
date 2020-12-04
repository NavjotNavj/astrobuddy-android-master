package in.appnow.astrobuddy.fcm;

import com.google.firebase.messaging.FirebaseMessaging;

import in.appnow.astrobuddy.BuildConfig;

/**
 * Created by Sajeev on 06-04-2017.
 */
public class Config {

    public static final String TITLE = "title";
    public static final String BODY = "body";
    public static final String IMAGE_FILE = "img_file";
    public static final String MESSAGE_TYPE = "noti_type";
    public static final String EVENT_ID = "event_id";
    public static final String USER_ID = "user_id";
    public static final String MESSAGE_DATA = "message_data";
    public static final String ACTION = "action";
    public static final String MYTH_ACTION_ID = "mythbuster_id";
    public static final String MYTH_ACTION_VIDEO_ID = "fileName";


    /* Screen Names */
    public static final String TRANS_ACTION = "TRANS_PAGE";
    public static final String HOME_ACTION = "HOME_SCREEN";
    public static final String MYTH_BUSTER_ACTION = "MYTH_BUSTER";
    public static final String MYTH_BUSTER_VIDEO_ACTION = "MYTH_BUSTER_VIDEO";
    public static final String MY_PROFILE_ACTION = "MY_PROFILE";
    public static final String CHAT_ACTION = "CHAT_SCREEN";
    public static final String REFER_ACTION = "REFER_SCREEN";
    public static final String CHART_ACTION = "CHART_SCREEN";
    public static final String CONTACT_US_ACTION = "CONTACT_US_SCREEN";
    public static final String CHAT_HISTORY_ACTION = "CHAT_HISTORY_SCREEN";
    public static final String ADD_TOPICS_ACTION = "ADD_TOPICS";
    public static final String ACTION_NONE = "NONE";
    public static final String FORECAST_ACTION = "FORECAST";
    public static final String FORECAST_DETAIL_ACTION ="FORECAST DETAIL" ;
    public static final String PANCHANG_ACTION = "PANCHANG";
    public static final String MATCH_MAKING_ACTION = "MATCH_MAKING";

    /* Banner/Promo types */
    public static final String CONTENT_TYPE = "CONTENT";
    public static final String AD_TYPE = "ADS";
    public static final String PROMO_TYPE = "PROMO";
    public static final String GREETING_TYPE = "GREETING";


    // flag to identify whether to show single line
    // or multi line text in push notification tray
    public static boolean appendNotificationMessages = true;

    // type of Push
    public static final String PICK_REQUEST_PUSH = "PICK_CHAT";
    public static final String CHAT_MESSAGE_PUSH = "MESSAGE";
    public static final String END_CHAT_PUSH = "END_CHAT";
    public static final String SEND_MESSAGE_PUSH = "send_message_push";
    public static final String MESSAGE_STATUS_UPDATE = "UPDATE_MESSAGE_STATUS";
    public static final String HANDLER_STATUS_PUSH = "HANDLER_AVAIL_STATUS";
    public static final String HANDLER_TYPING_STATUS_PUSH = "HANDLER_TYPING_STATUS";
    public static final String TRANSACTION_REPORT_PUSH = "TRANSACTION_REPORT";
    public static final String MYTH_BUSTER_PUSH = "MYTH_BUSTER";
    public static final String PING_PUSH = "PING";
    public static final String LOGOUT_PUSH = "LOGOUT";
    public static final String TOD_APP_NOTIFICATION = "tip_of_the_app_notification";

    //Promotion/Campaign Push types
    public static final String CAMPAIGN_PUSH = "CAMPAIGN";
    public static final String VOUCHER_PUSH = "VOUCHER";
    public static final String SUBSCRIPTION_PUSH = "SUBSCRIPTION";
    public static final String NORMAL_ALERT_PUSH = "NORMAL_ALERT";
    public static final String OFFER_PUSH = "OFFER";


    // id to handle the notification in the notification tray
// id to handle the notification in the notification try
    public static final int CHAT_MESSAGE_NOTIFICATION_ID = 100;
    public static final int PICK_REQUEST_NOTIFICATION_ID = 101;
    public static final int END_CHAT_NOTIFICATION_ID = 102;
    public static final int GENERAL_NOTIFICATION = 103;
    public static final int TOD_NOTIFICATION_ID = 104;

    //Payment Types
    public static final String CREDITS = "CREDITS";
    public static final String CALL = "CALL";
    public static final String SUBSCRIPTION = "SUBSCRIPTIONS";


    public static final int MESSAGE_NOTIFICATION_ID_BIG_IMAGE = 103;


    public static final String UPDATE_TOPIC = "update";
    private static final String TEST_TOPIC = "_test";//should append with other topics at end

    public static void doSubscriptionTopic(String topicName, boolean subscribe) {
        if (BuildConfig.DEBUG) {
            topicName = topicName + TEST_TOPIC;
        }
        if (subscribe)
            FirebaseMessaging.getInstance().subscribeToTopic(topicName);
        else
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topicName);

    }


}
