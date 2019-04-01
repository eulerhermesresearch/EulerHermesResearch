package com.eulerhermes.research.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.yabeman.android.extended.adapter.TypeAdapter;
import com.yabeman.android.extended.items.BaseType;
import com.eulerhermes.research.R;
import com.eulerhermes.research.R.drawable;
import com.eulerhermes.research.core.CoreUtil;
import com.eulerhermes.research.model.CountryDetail;

public class CountryHeaderAdapter extends TypeAdapter<BaseType> {
    protected int getLayoutRes() {
        return R.layout.item_country_header;
    }

    public void bindData(View view, BaseType data, int position) {
        CountryDetail cd = (CountryDetail) data;
        int risklevel = drawable.ic_risklevel_unknown;
        try {
            risklevel = drawable.class.getField("ic_risklevel_" + cd.getRiskLevel()).getInt(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
        } catch (NoSuchFieldException e3) {
            e3.printStackTrace();
        }
        ((ImageView) view.findViewById(R.id.flag)).setImageResource(CoreUtil.getCountryFlagForIso(cd.getIso()));
        ((ImageView) view.findViewById(R.id.risklevel)).setImageResource(risklevel);
        ((TextView) view.findViewById(R.id.grade)).setText(cd.getGrade() + " " + cd.getRiskLevel());
        ((TextView) view.findViewById(R.id.country)).setText(CoreUtil.getCountryNameForIso(cd.getIso()));
    }

    protected void attachViewHolder(View view) {
    }

    public void setLayoutParams(View view) {
    }

    public boolean isEnabled(BaseType object) {
        return false;
    }
}
