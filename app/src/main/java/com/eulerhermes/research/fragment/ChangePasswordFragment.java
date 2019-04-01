package com.eulerhermes.research.fragment;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;
import androidx.fragment.app.Fragment;
import com.eulerhermes.research.R;
import com.eulerhermes.research.core.CorePrefs;
import com.eulerhermes.research.model.ChangePasswordResult;
import com.eulerhermes.research.network.rest.ChangePasswordRequest;
import com.octo.android.robospice.Jackson2GoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class ChangePasswordFragment extends Fragment
{
    private Dialog      dialog;
    private Button      dialogButton;
    private View        dialogContentView;
    private TextView    dialogMessage;
    private TextView    dialogTitle;
    private ViewFlipper dialogViewFlipper;
    private Button      mButton;
    private TextView    mNewPassword;
    private TextView    mNewPassword2;
    private TextView    mOldPassword;

    private final SpiceManager spiceManager = new SpiceManager(Jackson2GoogleHttpClientSpiceService.class);


    private TextWatcher newPassword2Watcher = new TextWatcher()
    {
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {
        }

        public void afterTextChanged(Editable editable)
        {
            ChangePasswordFragment.this.checkPassword();
        }
    };
    private TextWatcher newPasswordWatcher  = new TextWatcher()
    {
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {
        }

        public void afterTextChanged(Editable editable)
        {
            ChangePasswordFragment.this.checkPassword();
        }
    };

    public final class ChangePasswordRequestListener implements RequestListener<ChangePasswordResult>
    {
        public void onRequestFailure(SpiceException spiceException)
        {
            ChangePasswordFragment.this.displayErrorDialog(R.string.forgot_password_error_title, R.string.contact_error_message);
        }

        public void onRequestSuccess(ChangePasswordResult result)
        {
            if (result.isSuccess()) {
                ChangePasswordFragment.this.displaySuccessDialog(R.string.change_password_success_title, R.string.change_password_success_message);
            }
            else {
                ChangePasswordFragment.this.displayErrorDialog(R.string.change_password_error_title, R.string.change_password_error_message);
            }
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_contact_change_password, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        this.mOldPassword = (TextView) view.findViewById(R.id.old_password);
        this.mNewPassword = (TextView) view.findViewById(R.id.new_password);
        this.mNewPassword2 = (TextView) view.findViewById(R.id.new_password2);
        this.mButton = (Button) view.findViewById(R.id.button);
        this.mButton.setEnabled(false);
        this.mButton.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                ChangePasswordFragment.this.sendRequest();
            }
        });
    }

    private void checkPassword()
    {
        boolean isOK = false;
        if (this.mNewPassword2.getText().length() > 0) {
            int resId;
            isOK = this.mNewPassword.getText().toString().equals(this.mNewPassword2.getText().toString());
            if (isOK) {
                resId = R.drawable.ic_done_green_24dp;
            }
            else {
                resId = R.drawable.ic_clear_red_24dp;
            }
            this.mNewPassword2.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, resId, 0);
        }
        this.mButton.setEnabled(isOK);
    }

    public static ChangePasswordFragment newInstance()
    {
        return new ChangePasswordFragment();
    }

    private void sendRequest()
    {
        displayLoadingDialog();
        this.spiceManager.execute(new ChangePasswordRequest(CorePrefs.getUser().getUsername(), this.mOldPassword.getText().toString(), this.mNewPassword.getText().toString()), null, -1, new ChangePasswordRequestListener());
    }

    private void createDialogContentView()
    {
        this.dialogContentView = LayoutInflater.from(getActivity()).inflate(R.layout.view_alert_dialog, null, false);
        this.dialogViewFlipper = (ViewFlipper) this.dialogContentView.findViewById(R.id.view_flipper);
        this.dialogTitle = (TextView) this.dialogContentView.findViewById(R.id.title);
        this.dialogMessage = (TextView) this.dialogContentView.findViewById(R.id.message);
        this.dialogButton = (Button) this.dialogContentView.findViewById(R.id.button);
    }

    private void displayLoadingDialog()
    {
        if (this.dialog == null) {
            createDialogContentView();
        }
        this.dialogViewFlipper.setDisplayedChild(0);
        this.dialogMessage.setVisibility(View.GONE);
        this.dialog = new Builder(getActivity()).setView(this.dialogContentView).create();
        this.dialog.setCancelable(false);
        this.dialog.show();
    }

    private void displayErrorDialog(int title, int message)
    {
        this.dialogMessage.setVisibility(View.VISIBLE);
        this.dialogTitle.setText(title);
        this.dialogMessage.setText(message);
        this.dialogViewFlipper.setDisplayedChild(1);
        this.dialog.setCancelable(true);
        this.dialogButton.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                ChangePasswordFragment.this.dialog.dismiss();
                ChangePasswordFragment.this.dialog = null;
            }
        });
    }

    private void displaySuccessDialog(int title, int message)
    {
        this.dialogMessage.setVisibility(View.VISIBLE);
        this.dialogTitle.setText(title);
        this.dialogMessage.setText(message);
        this.dialogViewFlipper.setDisplayedChild(1);
        this.dialog.setCancelable(true);
        this.dialogButton.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                ChangePasswordFragment.this.dialog.dismiss();
                ChangePasswordFragment.this.dialog = null;
                ChangePasswordFragment.this.getActivity().finish();
            }
        });
    }

    public void setMenuVisibility(boolean menuVisible)
    {
        super.setMenuVisibility(menuVisible);
        if (this.mOldPassword != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (!menuVisible) {
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        }
    }

    public void onStart()
    {
        this.spiceManager.start(getActivity());
        super.onStart();
    }

    public void onStop()
    {
        this.spiceManager.shouldStop();
        super.onStop();
    }

    public void onPause()
    {
        super.onPause();
        this.mNewPassword.removeTextChangedListener(this.newPasswordWatcher);
        this.mNewPassword2.removeTextChangedListener(this.newPassword2Watcher);
    }

    public void onResume()
    {
        super.onResume();
        this.mNewPassword.addTextChangedListener(this.newPasswordWatcher);
        this.mNewPassword2.addTextChangedListener(this.newPassword2Watcher);
    }
}
