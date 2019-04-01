package com.eulerhermes.research.adapter.rss;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.eulerhermes.research.R;
import com.eulerhermes.research.adapter.holder.VideoHolder;
import com.eulerhermes.research.core.CoreUtil;
import com.eulerhermes.research.imageloader.ImageLoader;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Item;

public class VideoAdapter extends RssItemAdapter {
    protected int getLayoutRes() {
        return R.layout.item_video;
    }

    public void bindData(View view, Item data, int position) {
        VideoHolder holder = (VideoHolder) view.getTag();
        holder.title.setText(data.getTitle());
        holder.date.setText(CoreUtil.getFormattedDateFromItem(data));
        holder.author.setText(data.getAuthor());
        ImageLoader.display(holder.image, data.getSource().getValue(), false, CoreUtil.getImagePlaceholder());
    }

    protected void attachViewHolder(View view) {
        view.setTag(new VideoHolder((ImageView) view.findViewById(R.id.image), (TextView) view.findViewById(R.id.title), (TextView) view.findViewById(R.id.date), (TextView) view.findViewById(R.id.author)));
    }
}
