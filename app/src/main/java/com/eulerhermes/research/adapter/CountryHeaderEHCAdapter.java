package com.eulerhermes.research.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.eulerhermes.research.R;
import com.eulerhermes.research.R.drawable;
import com.eulerhermes.research.core.CoreUtil;
import com.eulerhermes.research.model.CountryDetailEHC;

public class CountryHeaderEHCAdapter extends BaseAdapter {
    private final CountryDetailEHC country;

    private static class ViewHolder {
        public final TextView complexity;
        public final ImageView flag;
        public final ImageView icon;
        public final TextView name;

        public ViewHolder(TextView name, TextView complexity, ImageView flag, ImageView icon) {
            this.name = name;
            this.complexity = complexity;
            this.flag = flag;
            this.icon = icon;
        }
    }

    public CountryHeaderEHCAdapter(CountryDetailEHC country) {
        this.country = country;
    }

    public int getCount() {
        return 1;
    }

    public Object getItem(int position) {
        return this.country;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public boolean isEnabled(int position) {
        return false;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country_header_ehc, parent, false);
            convertView.setTag(new ViewHolder((TextView) convertView.findViewById(R.id.name), (TextView) convertView.findViewById(R.id.complexity), (ImageView) convertView.findViewById(R.id.flag), (ImageView) convertView.findViewById(R.id.icon)));
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        int risklevel = drawable.ic_risklevel_unknown;
        try {
            risklevel = drawable.class.getField("ic_risklevel_" + this.country.getRiskLevel()).getInt(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
        } catch (NoSuchFieldException e3) {
            e3.printStackTrace();
        }
        holder.name.setText(CoreUtil.getCountryNameForIso(this.country.getIso()));
        holder.flag.setImageResource(CoreUtil.getCountryFlagForIso(this.country.getIso()));
        holder.icon.setImageResource(risklevel);
        switch (this.country.getRiskLevel()) {
            case 1:
                holder.complexity.setText(R.string.notable_complexity);
                break;
            case 2:
                holder.complexity.setText(R.string.significant_complexity);
                break;
            case 3:
                holder.complexity.setText(R.string.major_complexity);
                break;
            case 4:
                holder.complexity.setText(R.string.severe_complexity);
                break;
            default:
                holder.complexity.setText(R.string.not_rated);
                break;
        }
        return convertView;
    }
}
