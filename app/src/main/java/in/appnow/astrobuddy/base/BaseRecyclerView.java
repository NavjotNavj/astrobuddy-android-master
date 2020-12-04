package in.appnow.astrobuddy.base;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by sonu on 11:56, 07/06/18
 * Copyright (c) 2018 . All rights reserved.
 */
public abstract class BaseRecyclerView<T> extends RecyclerView.Adapter<BaseViewHolder<T>> {

    protected abstract T getItem(int position);

    @Override
    public void onBindViewHolder(BaseViewHolder<T> holder, int position) {
        holder.populateItem(getItem(position));
    }
}
