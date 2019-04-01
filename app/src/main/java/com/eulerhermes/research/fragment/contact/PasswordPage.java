package com.eulerhermes.research.fragment.contact;

import java.util.ArrayList;
import android.text.TextUtils;
import android.util.Log;
import androidx.fragment.app.Fragment;
import com.eulerhermes.research.fragment.contact.wizardpager.wizard.model.ModelCallbacks;
import com.eulerhermes.research.fragment.contact.wizardpager.wizard.model.Page;
import com.eulerhermes.research.fragment.contact.wizardpager.wizard.model.ReviewItem;

public class PasswordPage extends Page
{
    public static final String	FIRST_NAME_DATA_KEY	= "password_firstname";
    public static final String	LAST_NAME_DATA_KEY	= "password_lastname";
    public static final String	EMAIL_DATA_KEY		= "password_email";

    public PasswordPage(ModelCallbacks callbacks, String title)
    {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment()
    {
        return ContactPasswordFragment.newInstance(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> arg0)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isCompleted()
    {
        Log.d("PasswordPage", "isCompleted: " + (!TextUtils.isEmpty(mData.getString(FIRST_NAME_DATA_KEY))
                && !TextUtils.isEmpty(mData.getString(LAST_NAME_DATA_KEY))
                && !TextUtils.isEmpty(mData.getString(EMAIL_DATA_KEY))));
        return !TextUtils.isEmpty(mData.getString(FIRST_NAME_DATA_KEY))
                && !TextUtils.isEmpty(mData.getString(LAST_NAME_DATA_KEY))
                && !TextUtils.isEmpty(mData.getString(EMAIL_DATA_KEY));
    }
}
