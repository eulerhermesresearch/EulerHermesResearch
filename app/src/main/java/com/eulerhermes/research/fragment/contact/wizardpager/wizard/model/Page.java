package com.eulerhermes.research.fragment.contact.wizardpager.wizard.model;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public abstract class Page implements PageTreeNode {
    public static final String SIMPLE_DATA_KEY = "_";
    protected ModelCallbacks mCallbacks;
    protected Bundle mData = new Bundle();
    protected String mParentKey;
    protected boolean mRequired = false;
    protected String mTitle;

    public abstract Fragment createFragment();

    public abstract void getReviewItems(ArrayList<ReviewItem> arrayList);

    protected Page(ModelCallbacks callbacks, String title) {
        this.mCallbacks = callbacks;
        this.mTitle = title;
    }

    public Bundle getData() {
        return this.mData;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public boolean isRequired() {
        return this.mRequired;
    }

    void setParentKey(String parentKey) {
        this.mParentKey = parentKey;
    }

    public Page findByKey(String key) {
        return getKey().equals(key) ? this : null;
    }

    public void flattenCurrentPageSequence(ArrayList<Page> dest) {
        dest.add(this);
    }

    public String getKey() {
        return this.mParentKey != null ? this.mParentKey + ":" + this.mTitle : this.mTitle;
    }

    public boolean isCompleted() {
        return true;
    }

    public void resetData(Bundle data) {
        this.mData = data;
        notifyDataChanged();
    }

    public void notifyDataChanged() {
        this.mCallbacks.onPageDataChanged(this);
    }

    public Page setRequired(boolean required) {
        this.mRequired = required;
        return this;
    }
}
