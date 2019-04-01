package com.eulerhermes.research.fragment.contact;

import java.util.ArrayList;
import android.text.TextUtils;
import androidx.fragment.app.Fragment;
import com.eulerhermes.research.fragment.contact.wizardpager.wizard.model.ModelCallbacks;
import com.eulerhermes.research.fragment.contact.wizardpager.wizard.model.Page;
import com.eulerhermes.research.fragment.contact.wizardpager.wizard.model.ReviewItem;

public class FeedbackPage extends Page
{
    public static final String	FIRST_NAME_DATA_KEY	= "feedback_firstname";
    public static final String	LAST_NAME_DATA_KEY	= "feedback_lastname";
    public static final String	EMAIL_DATA_KEY		= "feedback_email";
    public static final String	FEEDBACK_DATA_KEY	= "feedback_feedback";

    public FeedbackPage(ModelCallbacks callbacks, String title)
    {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment()
    {
        return ContactFeedbackFragment.newInstance(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> arg0)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isCompleted()
    {
        return !TextUtils.isEmpty(mData.getString(FIRST_NAME_DATA_KEY))
                && !TextUtils.isEmpty(mData.getString(LAST_NAME_DATA_KEY))
                && !TextUtils.isEmpty(mData.getString(EMAIL_DATA_KEY))
                && !TextUtils.isEmpty(mData.getString(FEEDBACK_DATA_KEY));
    }
}
