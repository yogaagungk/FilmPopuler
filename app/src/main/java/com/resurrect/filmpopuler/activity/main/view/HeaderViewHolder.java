package com.resurrect.filmpopuler.activity.main.view;

import android.view.View;
import android.widget.TextView;

import com.resurrect.filmpopuler.R;
import com.resurrect.filmpopuler.activity.main.store.HeaderItem;
import com.resurrect.filmpopuler.activity.main.store.MainItem;

/**
 * Created by root on 25/10/17.
 */

public class HeaderViewHolder extends BaseViewHolder {
    TextView headerField;

    public HeaderViewHolder(View itemView) {
        super(itemView);
        headerField = (TextView) itemView.findViewById(R.id.main_header_field);
    }

    @Override
    public void bindView(MainItem item) {
        final HeaderItem headerItem = (HeaderItem) item;
        headerField.setText(headerItem.getHeaderTitle());
    }
}
