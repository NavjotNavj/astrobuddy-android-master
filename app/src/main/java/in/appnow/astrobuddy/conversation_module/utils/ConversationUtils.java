package in.appnow.astrobuddy.conversation_module.utils;

/**
 * Created by sonu on 18/12/17.
 */

public class ConversationUtils {

    /**
     * message type static constants
     */
    public static final String MESSAGE_TEXT_TYPE = "text";
    public static final int MESSAGE_IMAGE_TYPE = 2;
    public static final int MESSAGE_DOCUMENT_TYPE = 3;
    public static final int MESSAGE_VIDEO_TYPE = 4;
    public static final int MESSAGE_LOCATION_TYPE = 5;

    public static final String TEXT_CHAT_ALERT = "SYSTEM";//for system generated alert when completing one topic for on-going conversations


    /**
     * message status static constants
     */
    public static final String MESSAGE_SEND = "SEND";
    public static final String MESSAGE_SENT = "SENT";
    public static final String MESSAGE_RECEIVED = "DEL";
    public static final String MESSAGE_READ = "SEEN";
    public static final String MESSAGE_FAILED = "FAILED";

    /**
     * intent extra static constants
     */
    public static final String MESSAGE_MODEL = "message_model";
    public static final String MESSAGE_SUCCESS = "message_success";
    public static final String MESSAGE_STATUS_MODEL = "message_status_model";
    public static final String QUEUE_SEQUENCE = "queue_sequence";
    public static final String SESSION_ID = "session_id";

    public static final String CHAT_WAITING = "WAIT";

    /**
     * Status static constants
     */
    public static final int ONLINE = 1;
    public static final int OFFLINE = 0;
    public static final int AWAY = 2;
    public static final int TYPING_STATUS = 1;
    public static final int NO_TYPING_STATUS = 0;

    public static final String TYPING = "typing";


    /**
     * message mine/other static constants
     */
    public static final int MESSAGE_OTHER = 0;
    public static final int MESSAGE_MINE = 1;


    /* conversation notify constants */
    public static final int NOTIFY_NONE = 0;
    public static final int NOTIFY_CANCEL = 1;
    public static final int NOTIFY_ME = 2;


    public static final String STATUS_DATA = "typing_status_data";
    public static final String USER_STATUS = "user_status";

    public static final String HANDLER_STATUS = "hanlder_status";
}
