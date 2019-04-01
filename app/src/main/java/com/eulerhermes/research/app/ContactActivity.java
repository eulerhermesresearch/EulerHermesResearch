package com.eulerhermes.research.app;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.eulerhermes.research.R;
import com.eulerhermes.research.fragment.ContactFragment;

public class ContactActivity extends FragmentActivity
{
    public static final String DISPLAY_PASSWORD = "password";
    public static final String MORE = "more";

    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_contact);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setLogo(R.drawable.ic_arrow_left);
        getActionBar().setTitle(R.string.contact_us);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, ContactFragment.newInstance(getIntent().getBooleanExtra(MORE, false), getIntent().getBooleanExtra(DISPLAY_PASSWORD, false))).commit();
    }
}
