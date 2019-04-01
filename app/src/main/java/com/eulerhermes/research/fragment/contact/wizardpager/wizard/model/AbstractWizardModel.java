package com.eulerhermes.research.fragment.contact.wizardpager.wizard.model;

import android.content.Context;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractWizardModel implements ModelCallbacks {
    protected Context mContext;
    private List<ModelCallbacks> mListeners = new ArrayList();
    private PageList mRootPageList;

    protected abstract PageList onNewRootPageList();

    public AbstractWizardModel(Context context) {
        this.mContext = context;
        this.mRootPageList = onNewRootPageList();
    }

    public void onPageDataChanged(Page page) {
        for (int i = 0; i < this.mListeners.size(); i++) {
            ((ModelCallbacks) this.mListeners.get(i)).onPageDataChanged(page);
        }
    }

    public void onPageTreeChanged() {
        for (int i = 0; i < this.mListeners.size(); i++) {
            ((ModelCallbacks) this.mListeners.get(i)).onPageTreeChanged();
        }
    }

    public Page findByKey(String key) {
        return this.mRootPageList.findByKey(key);
    }

    public void load(Bundle savedValues) {
        for (String key : savedValues.keySet()) {
            this.mRootPageList.findByKey(key).resetData(savedValues.getBundle(key));
        }
    }

    public void registerListener(ModelCallbacks listener) {
        this.mListeners.add(listener);
    }

    public Bundle save() {
        Bundle bundle = new Bundle();
        for (Page page : getCurrentPageSequence()) {
            bundle.putBundle(page.getKey(), page.getData());
        }
        return bundle;
    }

    public List<Page> getCurrentPageSequence() {
        ArrayList<Page> flattened = new ArrayList();
        this.mRootPageList.flattenCurrentPageSequence(flattened);
        return flattened;
    }

    public void unregisterListener(ModelCallbacks listener) {
        this.mListeners.remove(listener);
    }
}
