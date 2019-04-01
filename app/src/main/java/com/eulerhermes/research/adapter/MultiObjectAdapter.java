package com.eulerhermes.research.adapter;

import com.yabeman.android.extended.adapter.MultiTypeAdapter;
import com.yabeman.android.extended.adapter.TypeAdapter;
import com.yabeman.android.extended.items.BaseType;
import com.crashlytics.android.Crashlytics;
import java.util.ArrayList;
import java.util.List;

public class MultiObjectAdapter extends MultiTypeAdapter<BaseType> {
    private List<Integer> types;

    public MultiObjectAdapter() {
        this(0);
    }

    public MultiObjectAdapter(int layoutType) {
        super(layoutType);
        this.types = new ArrayList();
    }

    protected TypeAdapter<BaseType> getAdapter(BaseType object, int layoutType) {
        TypeAdapter<BaseType> adapter = AdapterSelector.getAdapter(object, layoutType, this);
        if (adapter == null) {
            Crashlytics.setString("MultiObjectAdapter_object", object != null ? object.toString() : "object is null");
            Crashlytics.setString("MultiObjectAdapter_layout", String.valueOf(layoutType));
        }
        return adapter;
    }

    protected int getItemViewType(BaseType object) {
        int type = object.getItemViewType();
        int index = this.types.indexOf(Integer.valueOf(type));
        if (index != -1) {
            return index;
        }
        index = this.types.size();
        this.types.add(Integer.valueOf(type));
        return index;
    }
}
