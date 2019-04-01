package com.eulerhermes.research.adapter.items;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.yabeman.android.extended.adapter.TypeAdapter;
import com.yabeman.android.extended.items.BaseType;
import com.yabeman.android.extended.items.TitleTextItem;
import com.eulerhermes.research.R;

public class TitleTextAdapter extends TypeAdapter<BaseType> {
    protected int getLayoutRes() {
        return R.layout.item_title_text;
    }

    public void bindData(View view, BaseType data, int position) {
        TitleTextItem item = (TitleTextItem) data;
        ((TextView) view.findViewById(R.id.title)).setText(item.title);
        ((TextView) view.findViewById(R.id.text)).setText(item.text);
    }

    protected void attachViewHolder(View view) {
    }

    public void setLayoutParams(View view) {
    }

    public boolean isEnabled(BaseType object) {
        Log.d("TitleTextAdapter", "isEnabled: ");
        return false;
    }
}
