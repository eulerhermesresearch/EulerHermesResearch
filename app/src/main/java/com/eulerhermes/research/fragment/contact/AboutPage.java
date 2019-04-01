package com.eulerhermes.research.fragment.contact;

import android.text.TextUtils;
import androidx.fragment.app.Fragment;
import com.eulerhermes.research.fragment.contact.wizardpager.wizard.model.ModelCallbacks;
import com.eulerhermes.research.fragment.contact.wizardpager.wizard.model.Page;
import com.eulerhermes.research.fragment.contact.wizardpager.wizard.model.ReviewItem;
import java.util.ArrayList;

public class AboutPage extends Page {
    public static final String EMAIL_DATA_KEY = "about_email";
    public static final String FEEDBACK_DATA_KEY = "about_feedback";
    public static final String FIRST_NAME_DATA_KEY = "about_firstname";
    public static final String LAST_NAME_DATA_KEY = "about_lastname";
    public static final String TOPIC_DATA_KEY = "about_topic";

    public AboutPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    public Fragment createFragment() {
        return ContactAboutFragment.newInstance(getKey());
    }

    public void getReviewItems(ArrayList<ReviewItem> arrayList) {
    }

    public boolean isCompleted() {
        return (TextUtils.isEmpty(this.mData.getString(FIRST_NAME_DATA_KEY)) || TextUtils.isEmpty(this.mData.getString(LAST_NAME_DATA_KEY)) || TextUtils.isEmpty(this.mData.getString(EMAIL_DATA_KEY)) || TextUtils.isEmpty(this.mData.getString(FEEDBACK_DATA_KEY))) ? false : true;
    }
}
