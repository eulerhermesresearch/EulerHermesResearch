package com.eulerhermes.research.adapter;

import android.view.View;
import com.yabeman.android.extended.adapter.TypeAdapter;
import com.yabeman.android.extended.items.BaseType;
import com.eulerhermes.research.R;

public class ProgressAdapter extends TypeAdapter<BaseType> {
    public void setLayoutParams(View view) {
    }

    public void bindData(View view, BaseType data, int position) {
    }

    public boolean isEnabled(BaseType object) {
        return false;
    }

    protected void attachViewHolder(View view) {
    }

    protected int getLayoutRes() {
        return R.layout.item_progress;
    }
}
