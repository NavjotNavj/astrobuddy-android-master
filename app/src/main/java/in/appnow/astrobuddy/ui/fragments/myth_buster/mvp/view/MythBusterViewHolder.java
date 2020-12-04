package in.appnow.astrobuddy.ui.fragments.myth_buster.mvp.view;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.Date;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.BuildConfig;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.rest.RestUtils;
import in.appnow.astrobuddy.rest.response.MythBuster;
import in.appnow.astrobuddy.rest.response.MythBusterResponse;
import in.appnow.astrobuddy.utils.DateUtils;
import in.appnow.astrobuddy.utils.ImageUtils;
import in.appnow.astrobuddy.utils.Logger;

/**
 * Created by sonu on 13:21, 16/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class MythBusterViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = MythBusterViewHolder.class.getSimpleName();
    @BindView(R.id.myth_buster_row_image_view)
    YouTubeThumbnailView imageView;
    @BindView(R.id.myth_buster_row_title)
    TextView rowTitle;
    @BindView(R.id.myth_buster_row_description)
    TextView rowDescription;
    @BindView(R.id.myth_buster_row_publish_date)
    TextView rowPublishDate;
    @BindString(R.string.published_on_format)
    String publishedOnFormat;

    MythBusterViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }


    public void bindData(Context context, MythBuster mythBuster) {
        rowTitle.setText(mythBuster.getTitle());
        rowDescription.setText(mythBuster.getDescription());
        rowPublishDate.setText(String.format(publishedOnFormat, DateUtils.parseStringDate(mythBuster.getDate(), DateUtils.SERVER_DATE_FORMAT, DateUtils.DOB_DATE_FORMAT)));
        if (mythBuster.getMythType().equalsIgnoreCase("VIDEO")) {


            /*  initialize the thumbnail image view , we need to pass Developer Key */
            imageView.initialize(RestUtils.getYoutubeAPIKey(), new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                    //when initialization is sucess, set the video id to thumbnail to load
                    youTubeThumbnailLoader.setVideo(mythBuster.getSource());

                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                        @Override
                        public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                            //when thumbnail loaded successfully release the thumbnail loader as we are showing thumbnail in adapter
                            youTubeThumbnailLoader.release();
                        }

                        @Override
                        public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                            //print or show error when thumbnail load failed
                            Logger.DebugLog(TAG, "Youtube Thumbnail Error");
                        }
                    });
                }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                    //print or show error when initialization failed
                    Logger.ErrorLog(TAG, "Youtube Initialization Failure");
                }
            });
        } else {
            //its an article
            ImageUtils.loadImageUrl(context, imageView, R.mipmap.ic_launcher, mythBuster.getSource());
        }
    }

}
