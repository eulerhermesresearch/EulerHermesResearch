package com.eulerhermes.research.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.eulerhermes.research.R;
import com.eulerhermes.research.core.CorePrefs;

public class SplashscreenActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        findViewById(R.id.logo).postDelayed(new Runnable() {
            public void run() {
                SplashscreenActivity.this.openNext();
            }
        }, 1000);
    }

    private void openNext() {
        if (CorePrefs.hasAcceptedTermsAndConditions()) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            startActivity(new Intent(this, TermsActivity.class));
        }
        finish();
    }
}
