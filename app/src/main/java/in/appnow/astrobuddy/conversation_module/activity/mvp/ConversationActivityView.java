package in.appnow.astrobuddy.conversation_module.activity.mvp;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;

import java.util.Collections;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseView;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.conversation_module.adapters.ConversationMessageAdapter;
import in.appnow.astrobuddy.conversation_module.rest_service.models.response.ConversationResponse;
import in.appnow.astrobuddy.conversation_module.utils.ConversationUtils;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.utils.ImageUtils;
import in.appnow.astrobuddy.utils.Logger;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.Observable;

@SuppressLint("ViewConstructor")
public class ConversationActivityView extends BaseViewClass implements BaseView {

    private static final String TAG = ConversationActivityView.class.getSimpleName();
    @BindView(R.id.conversation_toolbar)
    Toolbar toolbar;
    @BindView(R.id.conversation_recycler_view)
    RecyclerView conversationRecyclerView;
    @BindView(R.id.conversation_toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.conversation_toolbar_subtitle)
    TextView toolbarSubtitle;
    @BindView(R.id.conversation_toolbar_user_icon)
    CircleImageView toolbarUserIcon;
    @BindView(R.id.conversation_enter_a_message)
    EmojiEditText typeMessage;
    @BindView(R.id.conversation_send_button)
    ImageView sendButton;
    @BindView(R.id.emojiButton)
    ImageView emojiButton;
    @BindView(R.id.no_chats_label)
    TextView emptyChatsLabel;
    @BindView(R.id.conversation_progress_bar)
    ProgressBar conversationProgressBar;
    @BindView(R.id.conversation_type_layout)
    LinearLayout conversationTypeLayout;
    @BindView(R.id.conversation_navigation_layout)
    LinearLayout backArrowLayout;
    @BindView(R.id.chat_queue_sequence_label)
    TextView queueSequenceLabel;

    @BindString(R.string.chat_wait_message)
    String waitMessage;
    @BindString(R.string.chat_sequence)
    String chatSequenceString;
    @BindString(R.string.astrobuddy_join_message)
    String handlerJoinString;
    @BindString(R.string.typing_status)
    String typingStatusString;
    @BindString(R.string.online_status)
    String onlineStatus;
    @BindString(R.string.offline_status)
    String offlineStatus;
    @BindString(R.string.away_status)
    String awayStatus;

    @BindString(R.string.unknown_error)
    String unknownErrorString;

    private EmojiPopup emojiPopup;

    private AppCompatActivity appCompatActivity;

    private String handlerStatus;
    private boolean isLastItem;

    @BindView(R.id.background_image)
    ImageView backgroundImage;

    public final ConversationMessageAdapter adapter = new ConversationMessageAdapter(getContext());

    public ConversationActivityView(@NonNull AppCompatActivity appCompatActivity) {
        super(appCompatActivity);
        this.appCompatActivity = appCompatActivity;
        inflate(getContext(), R.layout.activity_conversation, this);
        ButterKnife.bind(this);
        appCompatActivity.setSupportActionBar(toolbar);
        ImageUtils.setBackgroundImage(appCompatActivity, backgroundImage);
        setUpEmojiconPopup();
        setUpRecyclerView();
        implementScrollListener();
    }

    private void setUpRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        conversationRecyclerView.setLayoutManager(linearLayoutManager);
        conversationRecyclerView.setNestedScrollingEnabled(false);
        conversationRecyclerView.setAdapter(adapter);
        conversationRecyclerView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            Logger.DebugLog(TAG, "ON LAYOUT CHANGES");
            if (isLastItem) {
                //  if (bottom < oldBottom) {
                Logger.DebugLog(TAG, "BOTTOM SCROLL");
                conversationRecyclerView.postDelayed(this::scrollToBottom, 100);
                //}
            }
        });
    }

    private void implementScrollListener() {
        final LinearLayoutManager mLayoutManager = (LinearLayoutManager) conversationRecyclerView.getLayoutManager();

        conversationRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // Here get the child count, item count and visibleitems
                // from layout manager

               /* int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int firstVisibleItemPosition = mLayoutManager
                        .findFirstVisibleItemPosition();

                // Now check if userScrolled is true and also check if
                // the item is end then update recycler view and set
                // userScrolled to false
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    isLastItem = true;
                } else {
                    isLastItem = false;
                }
                BlurtLogger.DebugLog(TAG, "IS LAST ITEM : " + isLastItem);*/
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                try {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (layoutManager == null)
                        return;
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstCompletelyVisibleItemPosition();

                    if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                        isLastItem = true;
                    } else {
                        isLastItem = false;
                    }
                    Logger.DebugLog(TAG, "IS LAST ITEM : " + isLastItem);
                } catch (Exception e) {
                }
            }
        });
    }

    public void setAstroBuddyStatus(String handlerStatus) {
        this.handlerStatus = handlerStatus;
        updateTypingStatus(ConversationUtils.NO_TYPING_STATUS);
    }


    public Observable<Object> observeSendButton() {
        return RxView.clicks(sendButton);
    }

    public Observable<Object> observeEmojiButton() {
        return RxView.clicks(emojiButton);
    }

    public Observable<Object> observeBackArrowPress() {
        return RxView.clicks(backArrowLayout);
    }

    public Observable<CharSequence> observeTextChange() {
        return RxTextView.textChanges(typeMessage);
    }

    public void onChatTextChange(CharSequence charSequence) {
        String message = charSequence.toString().trim();
        changeSendButtonImageColor(!TextUtils.isEmpty(message));
    }

    private void changeSendButtonImageColor(boolean isMessageEmpty) {
        if (isMessageEmpty) {
            ImageUtils.changeImageColor(sendButton, appCompatActivity, R.color.colorPrimary);
        } else {
            ImageUtils.changeImageColor(sendButton, appCompatActivity, R.color.white_transparent_70);
        }
    }

    public void updateTypeMessage(String text) {
        typeMessage.setText(text);
    }

    public String getTypedMessage() {
        return typeMessage.getText().toString().trim();
    }


    public void setData(List<ConversationResponse> conversationList, PreferenceManger blurtPreferenceManger) {
        Collections.sort(conversationList, (conversationResponse, t1) -> (int) conversationResponse.getSendDateTime() - (int) t1.getSendDateTime());
        adapter.setBlurtPreferenceManger(blurtPreferenceManger);
        adapter.swapData(conversationList);
        scrollToBottom();
    }

    public void setMessage(String message) {
        ToastUtils.longToast(message);
    }

    private void setUpEmojiconPopup() {
        emojiPopup = EmojiPopup.Builder.fromRootView(this).setOnEmojiBackspaceClickListener(v -> {

        }).setOnEmojiClickListener((emoji, imageView) -> {

        }).setOnEmojiPopupDismissListener(() -> {
            changeEmojiKeyboardIcon(emojiButton, R.drawable.ic_tag_faces_black_24dp);
        }).setOnSoftKeyboardCloseListener(() -> {

        }).setOnSoftKeyboardOpenListener(keyBoardHeight -> {

        }).setOnEmojiPopupShownListener(() -> {
            changeEmojiKeyboardIcon(emojiButton, R.drawable.ic_keyboard_black_24dp);
        }).build(typeMessage);
    }

    public boolean dismissEmojiPopup() {
        if (emojiPopup != null && emojiPopup.isShowing()) {
            emojiPopup.dismiss();
            return false;
        }
        return true;
    }

    private void changeEmojiKeyboardIcon(ImageView iconToBeChanged, int drawableResourceId) {
        iconToBeChanged.setImageResource(drawableResourceId);
    }

    public void toggleEmojiPopup() {
        emojiPopup.toggle();
    }

    private void scrollToBottom() {
        if (adapter.getConversationModelArrayList() != null && adapter.getConversationModelArrayList().size() > 1) {
            conversationRecyclerView.smoothScrollToPosition(adapter.getConversationModelArrayList().size());
        }
    }


    /* =====   Update queue label Methods STARTED!!!!  ===== */

    public void updateQueueLabelText(String text) {
        queueSequenceLabel.setText(text);
        showHideQueueLabel(View.VISIBLE);
    }

    public void showHideQueueLabel(int visibility) {
        queueSequenceLabel.setVisibility(visibility);
    }

    /* =====   Update queue label Methods ENDS!!!!  ===== */

    public void updateTypingStatus(int typingStatus) {
        if (typingStatus == ConversationUtils.TYPING_STATUS) {
            toolbarSubtitle.setText(typingStatusString);
            toolbarSubtitle.setVisibility(View.VISIBLE);
        } else {
            if (!TextUtils.isEmpty(handlerStatus)) {
                toolbarSubtitle.setText( handlerStatus);
                toolbarSubtitle.setVisibility(View.VISIBLE);
            } else {
                toolbarSubtitle.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onUnknownError(String error) {

    }

    @Override
    public void onTimeout() {

    }

    @Override
    public void onNetworkError() {

    }

    @Override
    public boolean isNetworkConnected() {
        return false;
    }

    @Override
    public void onConnectionError() {

    }


}
