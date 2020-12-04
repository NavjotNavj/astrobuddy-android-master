package in.appnow.astrobuddy.conversation_module.view_holders;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.vanniktech.emoji.EmojiTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.conversation_module.rest_service.models.response.ConversationResponse;
import in.appnow.astrobuddy.conversation_module.utils.ConversationUtils;
import in.appnow.astrobuddy.rest.response.Messages;
import in.appnow.astrobuddy.utils.DateUtils;
import in.appnow.astrobuddy.utils.ImageUtils;


/**
 * Created by SONU on 01/04/16.
 */
public class ChatItemViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = ChatItemViewHolder.class.getSimpleName();
    @BindView(R.id.timestamp)
    TextView timestamp;
    @BindView(R.id.message)
    EmojiTextView message;
    @BindView(R.id.message_status_icon)
    AppCompatImageView messageStatusIcon;
    @BindView(R.id.message_layout)
    RelativeLayout message_layout;
    private Context mContext;

    public ChatItemViewHolder(Context mContext, View view) {
        super(view);
        ButterKnife.bind(this, view);
        this.mContext = mContext;
    }

    public void bindData(ConversationResponse conversationModel, boolean isMessageMine) {
        message.setText(conversationModel.getMessage());
        //  stripUnderlines(((ViewHolder) holder).message);
        //   String timestamp = getTimeStamp(message.getCreated_at());
        /// String timestamp = BlurtDateUtils.getFormattedDateAndTime(message.getCreated_at());
        Long date = conversationModel.getSendDateTime();
        String displayDate = "";
        if (date != 0) {
            displayDate = DateUtils.getDateAndTime(date, DateUtils.CHAT_DISPLAY_DATE_FORMAT);
        }

        if (!TextUtils.isEmpty(displayDate)) {
            timestamp.setText(displayDate);
            timestamp.setVisibility(View.VISIBLE);
        } else {
            timestamp.setVisibility(View.INVISIBLE);
        }

        //if (conversationModel.getIsMessageMine() == ConversationUtils.MESSAGE_MINE) {
        if (conversationModel.getMessageId() == 1) {
            messageStatusIcon.setVisibility(View.GONE);
        } else if (isMessageMine && !TextUtils.isEmpty(conversationModel.getMessageStatus())) {
            switch (conversationModel.getMessageStatus()) {
                case ConversationUtils.MESSAGE_SEND:
                    messageStatusIcon.setImageResource(R.drawable.ic_access_time_black_24dp);
                    ImageUtils.changeImageColor(messageStatusIcon, mContext, R.color.dark_grey);
                    break;
                case ConversationUtils.MESSAGE_SENT:
                    messageStatusIcon.setImageResource(R.drawable.ic_done_black_24dp);
                    ImageUtils.changeImageColor(messageStatusIcon, mContext, R.color.dark_grey);
                    break;
                case ConversationUtils.MESSAGE_RECEIVED:
                    messageStatusIcon.setImageResource(R.drawable.ic_done_all_black_24dp);
                    ImageUtils.changeImageColor(messageStatusIcon, mContext, R.color.dark_grey);
                    break;
                case ConversationUtils.MESSAGE_READ:
                    messageStatusIcon.setImageResource(R.drawable.ic_done_all_black_24dp);
                    ImageUtils.changeImageColor(messageStatusIcon, mContext, R.color.blue);
                    break;
                case ConversationUtils.MESSAGE_FAILED:
                    messageStatusIcon.setImageResource(R.drawable.ic_report_problem_black_24dp);
                    ImageUtils.changeImageColor(messageStatusIcon, mContext, R.color.red);
                    break;
            }
            messageStatusIcon.setVisibility(View.VISIBLE);
        } else {
            messageStatusIcon.setVisibility(View.GONE);
        }

      /*  message_layout
                .setBackgroundColor(mSelectedItemsIds.get(position) ? 0x9934B5E4
                        : Color.TRANSPARENT);*/

        //  if (message.getUser().getName() != null)
        //    timestamp = message.getUser().getName() + ", " + timestamp;
    }

    public void bindChatHistoryData(Messages messages) {
        message.setText(messages.getMessage());
        //  stripUnderlines(((ViewHolder) holder).message);
        //   String timestamp = getTimeStamp(message.getCreated_at());
        /// String timestamp = BlurtDateUtils.getFormattedDateAndTime(message.getCreated_at());
        Long date = messages.getTimestamp();
        String displayDate = "";
        if (date != 0) {
            displayDate = DateUtils.getDateAndTime(date, DateUtils.CHAT_DISPLAY_DATE_FORMAT);
        }

        if (!TextUtils.isEmpty(displayDate)) {
            timestamp.setText(displayDate);
            timestamp.setVisibility(View.VISIBLE);
        } else {
            timestamp.setVisibility(View.INVISIBLE);
        }


        messageStatusIcon.setVisibility(View.GONE);
    }
}
