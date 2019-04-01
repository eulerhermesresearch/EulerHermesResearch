package com.eulerhermes.research.adapter;

import android.view.View;
import android.widget.TextView;
import com.yabeman.android.extended.adapter.TypeAdapter;
import com.yabeman.android.extended.items.BaseType;
import com.eulerhermes.research.R;
import com.eulerhermes.research.core.CoreUtil;
import com.eulerhermes.research.model.Doc;

public class RecentsHeaderAdapter extends TypeAdapter<BaseType> {
    protected int getLayoutRes() {
        return R.layout.item_category_header;
    }

    public void bindData(View view, BaseType data, int position) {
        int category = ((Doc) data).getIntCategory();
        TextView textView = (TextView) view.findViewById(R.id.header_title);
        textView.setText(CoreUtil.getNameForCategory(category));
        textView.setText(view.getResources().getStringArray(R.array.categories)[category - 1]);
        textView.setBackgroundResource(CoreUtil.getColorForCategory(category));
    }

    protected void attachViewHolder(View view) {
    }

    public void setLayoutParams(View view) {
    }

    public boolean isEnabled(BaseType object) {
        return false;
    }
}
