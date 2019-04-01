package com.eulerhermes.research.app;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.eulerhermes.research.R;
import com.eulerhermes.research.fragment.ProfileFragment;
import com.eulerhermes.research.fragment.UpdateProfileFragment;

public class ProfileActivity extends FragmentActivity
{
    public static final int LINKED_IN_LOGIN_CODE = 103;
    private ProfileFragment profileFragment;

    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_login);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setLogo(R.drawable.ic_arrow_left);
        getActionBar().setTitle(R.string.profile);
        this.profileFragment = ProfileFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add((int) R.id.fragment_container, this.profileFragment).commit();
    }

    public void updateInfos() {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right).replace(R.id.fragment_container, UpdateProfileFragment.newInstance()).addToBackStack(null).commit();
    }
}
