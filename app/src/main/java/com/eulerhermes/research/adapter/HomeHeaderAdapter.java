package com.eulerhermes.research.adapter;

import android.view.View;
import com.yabeman.android.extended.adapter.TypeAdapter;
import com.yabeman.android.extended.items.BaseType;
import com.eulerhermes.research.R;

public class HomeHeaderAdapter extends TypeAdapter<BaseType> {
    protected int getLayoutRes() {
        return R.layout.item_home_header;
    }

    public void bindData(View view, BaseType data, int position) {
    }

    protected void attachViewHolder(View view) {
    }

    public void setLayoutParams(View view) {
    }

    public boolean isEnabled(BaseType object) {
        return false;
    }
}
