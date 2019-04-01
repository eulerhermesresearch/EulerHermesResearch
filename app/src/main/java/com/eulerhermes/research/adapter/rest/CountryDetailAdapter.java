package com.eulerhermes.research.adapter.rest;

import android.view.View;
import android.widget.TextView;
import com.yabeman.android.extended.items.BaseType;
import com.eulerhermes.research.R;
import com.eulerhermes.research.adapter.holder.KeyFiguresHolder;
import com.eulerhermes.research.model.CountryDetail;

public class CountryDetailAdapter extends RestAdapter {
    protected int getLayoutRes() {
        return R.layout.item_key_figures;
    }

    public void bindData(View view, BaseType data, int position) {
        KeyFiguresHolder holder = (KeyFiguresHolder) view.getTag();
        CountryDetail cd = (CountryDetail) data;
        holder.gdp.setText(String.valueOf(cd.getGdp()));
        holder.gdpPerCapital.setText(String.valueOf(cd.getGdppc()));
        holder.population.setText(String.valueOf(cd.getPop()));
    }

    protected void attachViewHolder(View view) {
        view.setTag(new KeyFiguresHolder((TextView) view.findViewById(R.id.gdp), (TextView) view.findViewById(R.id.gdp_per_capital), (TextView) view.findViewById(R.id.population)));
    }
}
