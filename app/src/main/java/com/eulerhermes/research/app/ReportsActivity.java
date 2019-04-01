package com.eulerhermes.research.app;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.eulerhermes.research.R;
import com.eulerhermes.research.fragment.InfographicsFragment;

public class ReportsActivity extends FragmentActivity
{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infographics);
        setTitle("Reports & analysis");
        getActionBar().setLogo(R.drawable.ic_arrow_left);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, InfographicsFragment.newInstance(InfographicsFragment.TYPE_REPORTS)).commit();
    }
}
