package com.eulerhermes.research.app;

import android.os.Bundle;
import android.view.Window;
import androidx.fragment.app.FragmentActivity;
import com.eulerhermes.research.R;
import com.eulerhermes.research.fragment.CategoryFragment;
import com.eulerhermes.research.fragment.IDialogListener;

public class CategoryActivity extends FragmentActivity implements IDialogListener
{
    private int[] titles = new int[]{
            R.string.category_economic_outlooks,
            R.string.category_weekly_export_risk_outlooks,
            R.string.category_country_reports,
            R.string.category_economic_insights,
            R.string.category_industry_reports};

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_category);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        setTitle(this.titles[getIntent().getIntExtra("category", 1) - 1]);
    }

    public void onDialogActionComplete(int dialog)
    {
        CategoryFragment fragment = (CategoryFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        if (fragment != null) {
            fragment.refresh();
        }
    }
}
