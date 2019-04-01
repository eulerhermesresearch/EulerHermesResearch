package com.eulerhermes.research.adapter;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import com.yabeman.android.extended.adapter.TypeAdapter;
import com.yabeman.android.extended.items.BaseType;
import com.eulerhermes.research.R;
import com.eulerhermes.research.adapter.holder.HomeHorizontalListHolder;
import com.eulerhermes.research.app.InfographicsActivity;
import com.eulerhermes.research.app.ReportsActivity;
import com.eulerhermes.research.app.RiskMapActivity;
import com.eulerhermes.research.app.VideoActivity;
import com.eulerhermes.research.core.CoreDevice;

public class HomeHorizontalListHeaderAdapter extends TypeAdapter<BaseType> {
    public void setLayoutParams(View view) {
    }

    public void bindData(View view, BaseType data, int position) {
        final HomeHorizontalListHolder holder = (HomeHorizontalListHolder) view.getTag();
        holder.scrollView.post(new Runnable() {
            public void run() {
                holder.scrollView.scrollTo((int) (((double) CoreDevice.getDeviceWidth()) * 0.28d), 0);
            }
        });
        holder.item1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(v.getContext(), InfographicsActivity.class));
            }
        });
        holder.item2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(v.getContext(), RiskMapActivity.class));
            }
        });
        holder.item3.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(v.getContext(), ReportsActivity.class));
            }
        });
        holder.item4.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(v.getContext(), VideoActivity.class));
            }
        });
    }

    public boolean isEnabled(BaseType object) {
        return false;
    }

    protected void attachViewHolder(View view) {
        view.setTag(new HomeHorizontalListHolder((HorizontalScrollView) view.findViewById(R.id.scrollview), view.findViewById(R.id.item1), view.findViewById(R.id.item2), view.findViewById(R.id.item3), view.findViewById(R.id.item4)));
    }

    protected int getLayoutRes() {
        return R.layout.item_home_horizontal_list_header;
    }
}
