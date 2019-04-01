package com.eulerhermes.research.fragment.contact.wizardpager.wizard.model;

import android.text.TextUtils;
import androidx.fragment.app.Fragment;
import com.eulerhermes.research.fragment.contact.wizardpager.wizard.ui.SingleChoiceFragment;

import java.util.ArrayList;
import java.util.Arrays;

public class SingleFixedChoicePage extends Page
{
    protected ArrayList<String> mChoices = new ArrayList();

    public SingleFixedChoicePage(ModelCallbacks callbacks, String title)
    {
        super(callbacks, title);
    }

    public Fragment createFragment()
    {
        return SingleChoiceFragment.create(getKey());
    }

    public String getOptionAt(int position)
    {
        return (String) this.mChoices.get(position);
    }

    public int getOptionCount()
    {
        return this.mChoices.size();
    }

    public void getReviewItems(ArrayList<ReviewItem> dest)
    {
        dest.add(new ReviewItem(getTitle(), this.mData.getString(Page.SIMPLE_DATA_KEY), getKey()));
    }

    public boolean isCompleted()
    {
        return !TextUtils.isEmpty(this.mData.getString(Page.SIMPLE_DATA_KEY));
    }

    public SingleFixedChoicePage setChoices(String... choices)
    {
        this.mChoices.addAll(Arrays.asList(choices));
        return this;
    }

    public SingleFixedChoicePage setValue(String value)
    {
        this.mData.putString(Page.SIMPLE_DATA_KEY, value);
        return this;
    }
}
