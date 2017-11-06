package com.resurrect.filmpopuler.activity.main.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.resurrect.filmpopuler.activity.main.store.MainItem;

/**
 * Created by root on 24/10/17.
 */

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    BaseViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bindView(MainItem item);
}