package com.eulerhermes.research.fragment.contact;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.eulerhermes.research.R;
import com.eulerhermes.research.fragment.contact.wizardpager.wizard.ui.PageFragmentCallbacks;

public class ContactChangePasswordFragment extends Fragment
{
    private static final String ARG_KEY = "key";
    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private TextView mNewPassword;
    private TextView mNewPassword2;
    private TextView mOldPassword;
    private ChangePasswordPage mPage;
    private TextWatcher newPassword2Watcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            String editable2;
            Bundle data = ContactChangePasswordFragment.this.mPage.getData();
            String str = ChangePasswordPage.NEW_PASSWORD2_DATA_KEY;
            if (editable != null) {
                editable2 = editable.toString();
            } else {
                editable2 = null;
            }
            data.putString(str, editable2);
            ContactChangePasswordFragment.this.mPage.notifyDataChanged();
            ContactChangePasswordFragment.this.checkPassword();
        }
    };
    private TextWatcher newPasswordWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            String editable2;
            Bundle data = ContactChangePasswordFragment.this.mPage.getData();
            String str = ChangePasswordPage.NEW_PASSWORD_DATA_KEY;
            if (editable != null) {
                editable2 = editable.toString();
            } else {
                editable2 = null;
            }
            data.putString(str, editable2);
            ContactChangePasswordFragment.this.mPage.notifyDataChanged();
            ContactChangePasswordFragment.this.checkPassword();
        }
    };
    private TextWatcher oldPasswordWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            String editable2;
            Bundle data = ContactChangePasswordFragment.this.mPage.getData();
            String str = ChangePasswordPage.OLD_PASSWORD_DATA_KEY;
            if (editable != null) {
                editable2 = editable.toString();
            } else {
                editable2 = null;
            }
            data.putString(str, editable2);
            ContactChangePasswordFragment.this.mPage.notifyDataChanged();
        }
    };

    private void checkPassword() {
        if (this.mNewPassword2.getText().length() > 0) {
            int resId;
            if (this.mNewPassword.getText().toString().equals(this.mNewPassword2.getText().toString())) {
                resId = R.drawable.ic_done_green_24dp;
            } else {
                resId = R.drawable.ic_clear_red_24dp;
            }
            this.mNewPassword2.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, resId, 0);
        }
    }

    public static ContactChangePasswordFragment newInstance(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        ContactChangePasswordFragment fragment = new ContactChangePasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mKey = getArguments().getString(ARG_KEY);
        this.mPage = (ChangePasswordPage) this.mCallbacks.onGetPage(this.mKey);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact_change_password, container, false);
        ((TextView) rootView.findViewById(16908310)).setText(this.mPage.getTitle());
        this.mOldPassword = (TextView) rootView.findViewById(R.id.old_password);
        String oldPassword = this.mPage.getData().getString(ChangePasswordPage.OLD_PASSWORD_DATA_KEY);
        if (oldPassword != null && oldPassword.length() > 0) {
            this.mOldPassword.setText(oldPassword);
        }
        this.mNewPassword = (TextView) rootView.findViewById(R.id.new_password);
        String newPassword = this.mPage.getData().getString(ChangePasswordPage.NEW_PASSWORD_DATA_KEY);
        if (newPassword != null && newPassword.length() > 0) {
            this.mNewPassword.setText(newPassword);
        }
        this.mNewPassword2 = (TextView) rootView.findViewById(R.id.new_password2);
        String newPassword2 = this.mPage.getData().getString(ChangePasswordPage.NEW_PASSWORD2_DATA_KEY);
        if (newPassword2 != null && newPassword2.length() > 0) {
            this.mNewPassword2.setText(newPassword2);
        }
        return rootView;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (getParentFragment() instanceof PageFragmentCallbacks) {
            this.mCallbacks = (PageFragmentCallbacks) getParentFragment();
            return;
        }
        throw new ClassCastException("Parent fragment must implement PageFragmentCallbacks");
    }

    public void onDetach() {
        super.onDetach();
        this.mCallbacks = null;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.mOldPassword != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (!menuVisible) {
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        }
    }

    public void onPause() {
        super.onPause();
        this.mOldPassword.removeTextChangedListener(this.oldPasswordWatcher);
        this.mNewPassword.removeTextChangedListener(this.newPasswordWatcher);
        this.mNewPassword2.removeTextChangedListener(this.newPassword2Watcher);
    }

    public void onResume() {
        super.onResume();
        this.mOldPassword.addTextChangedListener(this.oldPasswordWatcher);
        this.mNewPassword.addTextChangedListener(this.newPasswordWatcher);
        this.mNewPassword2.addTextChangedListener(this.newPassword2Watcher);
    }
}
