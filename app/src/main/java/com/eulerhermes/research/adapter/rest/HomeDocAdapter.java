package com.eulerhermes.research.adapter.rest;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.yabeman.android.extended.items.BaseType;
import com.eulerhermes.research.R;
import com.eulerhermes.research.adapter.holder.HomeDocHolder;
import com.eulerhermes.research.core.CorePrefs;
import com.eulerhermes.research.model.Doc;

public class HomeDocAdapter extends RestAdapter {
    protected int getLayoutRes() {
        return R.layout.item_doc_home;
    }

    public void bindData(View view, BaseType data, int position) {
        HomeDocHolder holder = (HomeDocHolder) view.getTag();
        Doc doc = (Doc) data;
        if (doc.isInfographic()) {
            holder.icon.setImageResource(R.drawable.ic_infographic);
        } else if (doc.isReport()) {
            holder.icon.setImageResource(R.drawable.ic_report);
        } else if (doc.isVideo()) {
            holder.icon.setImageResource(R.drawable.ic_video);
        } else {
            holder.icon.setImageResource(0);
        }
        holder.title.setText(doc.getTitle());
        if (doc.getPubDate() != null) {
            holder.date.setText(doc.getFormattedDate());
        } else {
            holder.date.setText("");
        }
        float f = (!doc.isSecured() || CorePrefs.isLoggedIn()) ? 1.0f : 0.6f;
        view.setAlpha(f);
    }

    protected void attachViewHolder(View view) {
        view.setTag(new HomeDocHolder((ImageView) view.findViewById(R.id.icon), (TextView) view.findViewById(R.id.title), (TextView) view.findViewById(R.id.desc), (TextView) view.findViewById(R.id.date)));
    }
}
