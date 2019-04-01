package com.eulerhermes.research.fragment.contact.wizardpager.wizard.model;

public class ReviewItem {
    public static final int DEFAULT_WEIGHT = 0;
    private String mDisplayValue;
    private String mPageKey;
    private String mTitle;
    private int mWeight;

    public ReviewItem(String title, String displayValue, String pageKey) {
        this(title, displayValue, pageKey, 0);
    }

    public ReviewItem(String title, String displayValue, String pageKey, int weight) {
        this.mTitle = title;
        this.mDisplayValue = displayValue;
        this.mPageKey = pageKey;
        this.mWeight = weight;
    }

    public String getDisplayValue() {
        return this.mDisplayValue;
    }

    public void setDisplayValue(String displayValue) {
        this.mDisplayValue = displayValue;
    }

    public String getPageKey() {
        return this.mPageKey;
    }

    public void setPageKey(String pageKey) {
        this.mPageKey = pageKey;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public int getWeight() {
        return this.mWeight;
    }

    public void setWeight(int weight) {
        this.mWeight = weight;
    }
}
