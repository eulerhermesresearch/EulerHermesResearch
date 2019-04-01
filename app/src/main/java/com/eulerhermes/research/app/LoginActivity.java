package com.eulerhermes.research.app;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.eulerhermes.research.R;
import com.eulerhermes.research.fragment.LoginFragment;
import com.eulerhermes.research.fragment.RegisterFragment;
import org.brickred.socialauth.AuthProvider;

public class LoginActivity extends FragmentActivity
{
    public static final int LINKED_IN_LOGIN_CODE = 103;
    private LoginFragment loginFragment;

    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_login);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setLogo(R.drawable.ic_arrow_left);
        getActionBar().setTitle(R.string.contact_us);
        this.loginFragment = LoginFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add((int) R.id.fragment_container, this.loginFragment).commit();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 103 && resultCode == -1) {
            String firstname = data.getStringExtra("firstName");
            String lastname = data.getStringExtra("lastName");
            String email = data.getStringExtra(AuthProvider.EMAIL);
            String accessToken = data.getStringExtra("accessToken");
            long expireDate = data.getExtras().getLong("expireDate");
            if (this.loginFragment != null) {
                this.loginFragment.onLinkedInLoginComplete(firstname, lastname, email);
            }
        }
    }

    public void register() {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right).replace(R.id.fragment_container, RegisterFragment.newInstance()).addToBackStack(null).commit();
    }
}
