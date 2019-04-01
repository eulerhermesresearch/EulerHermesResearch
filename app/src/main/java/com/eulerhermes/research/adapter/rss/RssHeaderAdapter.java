package com.eulerhermes.research.adapter.rss;

import android.view.View;
import android.widget.TextView;
import com.eulerhermes.research.R;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Category;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Item;

public class RssHeaderAdapter extends RssItemAdapter {
    private static final String MACROECONOMIC = "Macroeconomic and Country Risk Research";
    private static final String SECTOR = "Sector and insolvency Research";
    private static final String SUPPORT = "Support";

    protected int getLayoutRes() {
        return R.layout.item_team_header;
    }

    public void bindData(View view, Item data, int position) {
        String category = ((Category) data.getCategories().get(0)).getValue();
        TextView textView = (TextView) view.findViewById(R.id.header_title);
        textView.setText(category);
        if (category.equals(MACROECONOMIC)) {
            textView.setBackgroundResource(R.color.category_1);
        } else if (category.equals(SECTOR)) {
            textView.setBackgroundResource(R.color.category_4);
        } else if (category.equals(SUPPORT)) {
            textView.setBackgroundResource(R.color.category_5);
        } else {
            textView.setBackgroundResource(R.color.category_0);
        }
    }

    protected void attachViewHolder(View view) {
    }
}
