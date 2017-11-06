package com.resurrect.filmpopuler.activity.main.store;

import com.resurrect.filmpopuler.R;

/**
 * Created by root on 25/10/17.
 */

public class HeaderItem implements MainItem {
    public final String headerTitle;

    public HeaderItem(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    @Override
    public int getType() { return R.layout.main_header_item;}

    @Override
    public int getItemSize() {
        return 2;
    }
}
