package com.eulerhermes.research.adapter.rest;

import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import com.yabeman.android.extended.items.BaseType;
import com.eulerhermes.research.R;
import com.eulerhermes.research.adapter.holder.LatestDocHolder;
import com.eulerhermes.research.core.CorePrefs;
import com.eulerhermes.research.core.CoreUtil;
import com.eulerhermes.research.imageloader.ImageLoader;
import com.eulerhermes.research.model.Doc;

public class LastDocAdapter extends RestAdapter {
    private int[] images = new int[]{R.drawable.doc_category_last_1, R.drawable.doc_category_last_2, R.drawable.doc_category_last_3, R.drawable.doc_category_last_4, R.drawable.doc_category_last_5};

    protected int getLayoutRes() {
        return R.layout.item_doc_last;
    }

    public void bindData(View view, BaseType data, int position) {
        LatestDocHolder holder = (LatestDocHolder) view.getTag();
        Doc doc = (Doc) data;
        holder.title.setText(doc.getTitle());
        holder.desc.setText(doc.getDescription());
        holder.date.setText(doc.getFormattedDate());
        holder.image.setScaleType(ScaleType.CENTER_CROP);
        ImageLoader.display(holder.image, this.images[position % this.images.length], CoreUtil.getImagePlaceholder());
        float f = (!doc.isSecured() || CorePrefs.isLoggedIn()) ? 1.0f : 0.6f;
        view.setAlpha(f);
    }

    protected void attachViewHolder(View view) {
        view.setTag(new LatestDocHolder((ImageView) view.findViewById(R.id.image), (TextView) view.findViewById(R.id.title), (TextView) view.findViewById(R.id.desc), (TextView) view.findViewById(R.id.date)));
    }
}
