package com.eulerhermes.research.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.eulerhermes.research.R;
import com.eulerhermes.research.app.CategoryActivity;

public class HomeDocsFragment extends Fragment
{
    private OnClickListener onClickListener = new OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.category_eco_outlooks:
                    HomeDocsFragment.this.openCategoryActivity(1);
                    return;
                case R.id.category_risks:
                    HomeDocsFragment.this.openCategoryActivity(2);
                    return;
                case R.id.category_country_reports:
                    HomeDocsFragment.this.openCategoryActivity(3);
                    return;
                case R.id.category_eco_insights:
                    HomeDocsFragment.this.openCategoryActivity(4);
                    return;
                case R.id.category_industry_reports:
                    HomeDocsFragment.this.openCategoryActivity(5);
                    return;
                default:
                    return;
            }
        }
    };

    public static HomeDocsFragment newInstance() {
        return new HomeDocsFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_docs, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.category_eco_outlooks).setOnClickListener(this.onClickListener);
        view.findViewById(R.id.category_risks).setOnClickListener(this.onClickListener);
        view.findViewById(R.id.category_country_reports).setOnClickListener(this.onClickListener);
        view.findViewById(R.id.category_eco_insights).setOnClickListener(this.onClickListener);
        view.findViewById(R.id.category_industry_reports).setOnClickListener(this.onClickListener);
    }

    private void openCategoryActivity(int category) {
        Intent intent = new Intent(getActivity(), CategoryActivity.class);
        intent.putExtra("category", category);
        getActivity().startActivity(intent);
    }
}
