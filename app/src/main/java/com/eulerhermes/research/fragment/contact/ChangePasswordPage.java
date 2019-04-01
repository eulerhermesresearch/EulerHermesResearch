package com.eulerhermes.research.fragment.contact;

import android.text.TextUtils;
import android.util.Log;
import androidx.fragment.app.Fragment;
import com.eulerhermes.research.fragment.contact.wizardpager.wizard.model.ModelCallbacks;
import com.eulerhermes.research.fragment.contact.wizardpager.wizard.model.Page;
import com.eulerhermes.research.fragment.contact.wizardpager.wizard.model.ReviewItem;
import java.util.ArrayList;

public class ChangePasswordPage extends Page {
    public static final String NEW_PASSWORD2_DATA_KEY = "new_password2";
    public static final String NEW_PASSWORD_DATA_KEY = "new_password";
    public static final String OLD_PASSWORD_DATA_KEY = "old_password";

    public ChangePasswordPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    public Fragment createFragment() {
        return ContactChangePasswordFragment.newInstance(getKey());
    }

    public void getReviewItems(ArrayList<ReviewItem> arrayList) {
    }

    public boolean isCompleted() {
        boolean z;
        String str = "ChangePasswordPage";
        StringBuilder stringBuilder = new StringBuilder("isCompleted: ");
        if (TextUtils.isEmpty(this.mData.getString(OLD_PASSWORD_DATA_KEY)) || TextUtils.isEmpty(this.mData.getString(NEW_PASSWORD_DATA_KEY)) || TextUtils.isEmpty(this.mData.getString(NEW_PASSWORD2_DATA_KEY)) || !this.mData.getString(NEW_PASSWORD_DATA_KEY).equals(this.mData.getString(NEW_PASSWORD2_DATA_KEY))) {
            z = false;
        } else {
            z = true;
        }
        Log.d(str, stringBuilder.append(z).toString());
        if (TextUtils.isEmpty(this.mData.getString(OLD_PASSWORD_DATA_KEY)) || TextUtils.isEmpty(this.mData.getString(NEW_PASSWORD_DATA_KEY)) || TextUtils.isEmpty(this.mData.getString(NEW_PASSWORD2_DATA_KEY)) || !this.mData.getString(NEW_PASSWORD_DATA_KEY).equals(this.mData.getString(NEW_PASSWORD2_DATA_KEY))) {
            return false;
        }
        return true;
    }
}
