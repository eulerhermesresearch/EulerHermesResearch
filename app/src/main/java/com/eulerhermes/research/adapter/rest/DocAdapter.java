package com.eulerhermes.research.adapter.rest;

import android.view.View;
import android.widget.TextView;
import com.yabeman.android.extended.items.BaseType;
import com.eulerhermes.research.R;
import com.eulerhermes.research.adapter.holder.DocHolder;
import com.eulerhermes.research.core.CorePrefs;
import com.eulerhermes.research.model.Doc;

public class DocAdapter extends RestAdapter {
    protected int getLayoutRes() {
        return R.layout.item_doc;
    }

    public void bindData(View view, BaseType data, int position) {
        DocHolder holder = (DocHolder) view.getTag();
        Doc doc = (Doc) data;
        holder.title.setText(doc.getTitle());
        holder.desc.setText(doc.getDescription());
        if (doc.getPubDate() != null) {
            holder.date.setText(doc.getFormattedDate());
        }
        float f = (!doc.isSecured() || CorePrefs.isLoggedIn()) ? 1.0f : 0.6f;
        view.setAlpha(f);
    }

    protected void attachViewHolder(View view) {
        view.setTag(new DocHolder((TextView) view.findViewById(R.id.title), (TextView) view.findViewById(R.id.desc), (TextView) view.findViewById(R.id.date)));
    }
}
