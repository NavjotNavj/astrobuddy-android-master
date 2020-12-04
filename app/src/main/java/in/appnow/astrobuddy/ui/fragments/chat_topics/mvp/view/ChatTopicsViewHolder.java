package in.appnow.astrobuddy.ui.fragments.chat_topics.mvp.view;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.rest.response.ChatSampleResponse;
import in.appnow.astrobuddy.utils.ImageUtils;

/**
 * Created by sonu on 18:57, 03/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ChatTopicsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.chat_topic_row_image_view)
    ImageView chatTopicsImageView;
    @BindView(R.id.chat_topic_row_label)
    TextView chatTopicLabel;

    public ChatTopicsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void onBindData(Context context,ChatSampleResponse.ChatSample chatSample) {
        chatTopicLabel.setText(chatSample.getSampleQuestion());
        ImageUtils.loadImageUrl(context,chatTopicsImageView,R.mipmap.ic_launcher,chatSample.getTopicImage());
    }
}
