package com.eulerhermes.research.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.eulerhermes.research.R;

public class ContactUsAdapter extends BaseAdapter {
    private ContactUsItem item = new ContactUsItem();

    public static class ContactUsItem {
    }

    public int getCount() {
        return 1;
    }

    public Object getItem(int position) {
        return this.item;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public int getViewTypeCount() {
        return 1;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("ContactUsAdapter", "getView: " + convertView);
        if (convertView == null) {
            return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact_us, parent, false);
        }
        return convertView;
    }
}
