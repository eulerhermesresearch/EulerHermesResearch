package com.eulerhermes.research.app;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.eulerhermes.research.R;

public class VideoActivity extends FragmentActivity
{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        getActionBar().setLogo(R.drawable.ic_arrow_left);
        getActionBar().setTitle("Videos & interviews");
    }
}
