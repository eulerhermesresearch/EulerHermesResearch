package com.eulerhermes.research.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.eulerhermes.research.R;
import com.eulerhermes.research.R.drawable;
import com.eulerhermes.research.model.CountryDetailEHC;

public class CountryDetailEHCAdapter extends BaseAdapter {
    private final CountryDetailEHC country;

    private static class ViewHolder {
        public final ImageView courtProceedings;
        public final ImageView insolvencyProceedings;
        public final ImageView payments;

        public ViewHolder(ImageView payments, ImageView courtProceedings, ImageView insolvencyProceedings) {
            this.payments = payments;
            this.courtProceedings = courtProceedings;
            this.insolvencyProceedings = insolvencyProceedings;
        }
    }

    public CountryDetailEHCAdapter(CountryDetailEHC country) {
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country_detail, parent, false);
            convertView.setTag(new ViewHolder((ImageView) convertView.findViewById(R.id.payments), (ImageView) convertView.findViewById(R.id.court_proceedings), (ImageView) convertView.findViewById(R.id.insolvency_proceedings)));
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        int paymentsLevel = 0;
        try {
            paymentsLevel = drawable.class.getField("ic_paymentlevel_" + this.country.getPaymentLevel()).getInt(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
        } catch (NoSuchFieldException e3) {
            e3.printStackTrace();
        }
        holder.payments.setImageResource(paymentsLevel);
        int courtLevel = 0;
        try {
            courtLevel = drawable.class.getField("ic_courtlevel_" + this.country.getCourtLevel()).getInt(null);
        } catch (IllegalAccessException e4) {
            e4.printStackTrace();
        } catch (IllegalArgumentException e22) {
            e22.printStackTrace();
        } catch (NoSuchFieldException e32) {
            e32.printStackTrace();
        }
        holder.courtProceedings.setImageResource(courtLevel);
        int insolvencyLevel = 0;
        try {
            insolvencyLevel = drawable.class.getField("ic_insolvencylevel_" + this.country.getInsolvencyLevel()).getInt(null);
        } catch (IllegalAccessException e42) {
            e42.printStackTrace();
        } catch (IllegalArgumentException e222) {
            e222.printStackTrace();
        } catch (NoSuchFieldException e322) {
            e322.printStackTrace();
        }
        holder.insolvencyProceedings.setImageResource(insolvencyLevel);
        return convertView;
    }
}
