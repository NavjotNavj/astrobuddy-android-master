package in.appnow.astrobuddy.conversation_module.chat_history.mvp.view;

import android.content.Context;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.rest.response.Conversations;
import in.appnow.astrobuddy.utils.DateUtils;

/**
 * Created by sonu on 13:21, 16/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ChatHistoryViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = ChatHistoryViewHolder.class.getSimpleName();

    @BindView(R.id.chat_history_row_topics_used_label)
    TextView topicsUsedLabel;
    @BindView(R.id.chat_history_row_date_label)
    TextView dateLabel;
    @BindView(R.id.chat_history_row_rating_bar)
    RatingBar ratingBar;
    @BindView(R.id.chat_history_row_message_count_label)
    TextView messageCountLabel;
    @BindView(R.id.chat_history_row_view_all_button)
    TextView viewAllButton;

    private Context context;

    public ChatHistoryViewHolder(Context context,View itemView) {
        super(itemView);
        this.context=context;
        ButterKnife.bind(this, itemView);
    }


    public void bindData(Conversations conversations) {
        topicsUsedLabel.setText("Credits Used : " + conversations.getTopicsCount());

        String startDate = DateUtils.parseStringDate(conversations.getStartTimestamp(),DateUtils.SERVER_DATE_FORMAT,DateUtils.CHAT_HISTORY_DATE_FORMAT);
        String[] splitStartDate = startDate.split(" ");
        startDate = splitStartDate[0];

        String endDate = DateUtils.parseStringDate(conversations.getEndTimestamp(),DateUtils.SERVER_DATE_FORMAT,DateUtils.CHAT_HISTORY_DATE_FORMAT);
        endDate = endDate.split(" ")[0];

        //VectorUtils.setVectorCompoundDrawable(dateLabel,context,R.drawable.ic_access_time_black_24dp,0,0,0,R.color.gunmetal);
        dateLabel.setText(startDate + " - " + endDate + "\n" + splitStartDate[1]);
        messageCountLabel.setText("Message Count : " + conversations.getMessageCount());
        ratingBar.setRating(conversations.getFeedbackRating());

    }

}
