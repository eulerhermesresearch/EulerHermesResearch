package com.eulerhermes.research.adapter.items;

import android.view.View;
import android.widget.TextView;
import com.yabeman.android.extended.adapter.TypeAdapter;
import com.yabeman.android.extended.items.BaseType;
import com.yabeman.android.extended.items.Header;
import com.eulerhermes.research.R;

public class HeaderAdapter extends TypeAdapter<BaseType> {
    protected int getLayoutRes() {
        return R.layout.item_header;
    }

    public void bindData(View view, BaseType data, int position) {
        ((TextView) view).setText(((Header) data).name);
    }

    protected void attachViewHolder(View view) {
    }

    public void setLayoutParams(View view) {
    }

    public boolean isEnabled(BaseType object) {
        return false;
    }
}
