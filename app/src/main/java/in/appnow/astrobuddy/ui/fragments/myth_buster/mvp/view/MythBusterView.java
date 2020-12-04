package in.appnow.astrobuddy.ui.fragments.myth_buster.mvp.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseView;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.rest.response.MythBuster;
import in.appnow.astrobuddy.rest.response.MythBusterResponse;
import in.appnow.astrobuddy.utils.ImageUtils;
import io.reactivex.Observable;

/**
 * Created by sonu on 16:03, 23/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class MythBusterView extends BaseViewClass implements BaseView {
    @BindView(R.id.myth_buster_recycler_view)
    RecyclerView mythBusterRecyclerView;
    @BindView(R.id.empty_myth_busters_label)
    TextView emptyUpgradePlanLabel;
    @BindView(R.id.background_image)
    ImageView backgroundImage;
    @BindString(R.string.unknown_error)
    String unknownErrorString;
    private boolean isMythTypeVideo;
    private final MythBusterAdapter adapter = new MythBusterAdapter(getContext());

    public MythBusterView(@NonNull Context context) {
        super(context);
        inflate(context, R.layout.myth_buster_fragment, this);
        ButterKnife.bind(this, this);
        ImageUtils.setBackgroundImage(context, backgroundImage);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        mythBusterRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mythBusterRecyclerView.setHasFixedSize(true);
        mythBusterRecyclerView.setNestedScrollingEnabled(false);
        mythBusterRecyclerView.setAdapter(adapter);
    }

    public Observable<MythBuster> onListClick() {
        return adapter.getItemViewClickSubject();
    }

    public void setMythTypeVideo(boolean isVideo) {
        this.isMythTypeVideo = isVideo;
    }

    public void updateView(MythBusterResponse response) {
        if (response.isErrorStatus()) {
            showHideView(true, response.getErrorMessage());
        } else {
            if (response.getMythBuster() != null && response.getMythBuster().size() > 0) {
                int count = 0;
                for (int i = 0; i < response.getMythBuster().size(); i++) {
                    if (isMythTypeVideo) {
                        if (response.getMythBuster().get(i).getMythType().equalsIgnoreCase("VIDEO")) {
                            count++;
                        }
                    } else {
                        if (response.getMythBuster().get(i).getMythType().equalsIgnoreCase("ARTICLE")) {
                            count++;
                        }
                    }
                }
                if (count > 0) {
                    showHideView(false, "");
                    adapter.swapData(response.getMythBuster(), isMythTypeVideo);
                } else {
                    showHideView(true, isMythTypeVideo ? "No videos" : "No myth busters.");
                }

            } else {
                showHideView(true, response.getErrorMessage());
            }
        }
    }

    public void showHideView(boolean isEmpty, String message) {
        if (isEmpty) {
            emptyUpgradePlanLabel.setText(message);
            emptyUpgradePlanLabel.setVisibility(View.VISIBLE);
            mythBusterRecyclerView.setVisibility(View.GONE);
        } else {
            emptyUpgradePlanLabel.setVisibility(View.GONE);
            mythBusterRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onUnknownError(String error) {
        showHideView(true, error);
    }

    @Override
    public void onTimeout() {
        showHideView(true, unknownErrorString);
    }

    @Override
    public void onNetworkError() {
        showHideView(true, unknownErrorString);
    }

    @Override
    public boolean isNetworkConnected() {
        return false;
    }

    @Override
    public void onConnectionError() {
        showHideView(true, unknownErrorString);
    }


}
