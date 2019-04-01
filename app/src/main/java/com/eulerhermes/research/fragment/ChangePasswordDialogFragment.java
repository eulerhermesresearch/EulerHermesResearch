/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.eulerhermes.research.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;
import androidx.fragment.app.DialogFragment;
import com.eulerhermes.research.R;
import com.eulerhermes.research.core.CoreDevice;
import com.eulerhermes.research.core.CorePrefs;
import com.eulerhermes.research.model.ChangePasswordResult;
import com.eulerhermes.research.network.rest.ChangePasswordRequest;
import com.octo.android.robospice.Jackson2GoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class ChangePasswordDialogFragment extends DialogFragment
{
    private TextView			mOldPassword;
    private TextView			mNewPassword;
    private TextView			mNewPassword2;
    private Button				mButton;

    private View				dialogContentView;
    private ViewFlipper			dialogViewFlipper;
    private TextView			dialogTitle;
    private TextView			dialogMessage;
    private Button				dialogButton;

    private final SpiceManager	spiceManager;

    private TextWatcher			newPasswordWatcher	= new TextWatcher() {
        @Override
        public void beforeTextChanged(
                CharSequence charSequence, int i,
                int i1, int i2)
        {}

        @Override
        public void onTextChanged(
                CharSequence charSequence, int i,
                int i1, int i2)
        {}

        @Override
        public void afterTextChanged(
                Editable editable)
        {
            checkPassword();
        }
    };

    private TextWatcher			newPassword2Watcher	= new TextWatcher() {
        @Override
        public void beforeTextChanged(
                CharSequence charSequence, int i,
                int i1, int i2)
        {}

        @Override
        public void onTextChanged(
                CharSequence charSequence, int i,
                int i1, int i2)
        {}

        @Override
        public void afterTextChanged(
                Editable editable)
        {
            checkPassword();
        }
    };

    public ChangePasswordDialogFragment()
    {
        spiceManager = new SpiceManager(Jackson2GoogleHttpClientSpiceService.class);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        setStyle(STYLE_NORMAL, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setLayout(getMaxDialogWidth(), getMaxDialogHeight());
        return dialog;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        // defineHeight();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        // defineHeight();
    }

    protected int getMaxDialogWidth()
    {
        return getResources().getDimensionPixelSize(R.dimen.login_dialog_width);
    }

    protected int getMaxDialogHeight()
    {
        int maxHeight = getResources().getDimensionPixelSize(R.dimen.dialog_max_height);

        if (CoreDevice.isPortraitMode())
        {
            int height = (int) (CoreDevice.getDeviceHeight() * 3 / 4 - 96 * CoreDevice
                    .getDeviceDensity());
            return Math.min(maxHeight, height);
        }
        else
        {
            // Device Height - 96 dpi
            int height = (int) (CoreDevice.getDeviceHeight() - 96 * CoreDevice.getDeviceDensity());
            return Math.min(maxHeight, height);
        }
    }

    private void checkPassword()
    {
        boolean isOK = false;

        if (mNewPassword2.getText().length() > 0)
        {
            isOK = mNewPassword.getText().toString().equals(mNewPassword2.getText().toString());
            int resId = isOK ? R.drawable.ic_done_green_24dp : R.drawable.ic_clear_red_24dp;
            mNewPassword2.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, resId, 0);
        }

        mButton.setEnabled(isOK);
    }

    public static ChangePasswordDialogFragment newInstance()
    {
        return new ChangePasswordDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_contact_change_password, container,
                                         false);

        mOldPassword = ((TextView) rootView.findViewById(R.id.old_password));
		/*String oldPassword = mPage.getData().getString(ChangePasswordPage.OLD_PASSWORD_DATA_KEY);

		if (oldPassword != null && oldPassword.length() > 0)
		{
			mOldPassword.setText(oldPassword);
		}*/

        mNewPassword = ((TextView) rootView.findViewById(R.id.new_password));
		/*String newPassword = mPage.getData().getString(ChangePasswordPage.NEW_PASSWORD_DATA_KEY);

		if (newPassword != null && newPassword.length() > 0)
		{
			mNewPassword.setText(newPassword);
		}
		*/

        mNewPassword2 = ((TextView) rootView.findViewById(R.id.new_password2));
		/*String newPassword2 = mPage.getData().getString(ChangePasswordPage.NEW_PASSWORD2_DATA_KEY);

		if (newPassword2 != null && newPassword2.length() > 0)
		{
			mNewPassword2.setText(newPassword2);
		}*/
        mButton = (Button) rootView.findViewById(R.id.button);
        mButton.setEnabled(false);
        mButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                sendRequest();
            }
        });

        return rootView;
    }

    private void sendRequest()
    {
        displayLoadingDialog();

        ChangePasswordRequest request = new ChangePasswordRequest(
                CorePrefs.getUser().getUsername(), mOldPassword.getText().toString(), mNewPassword
                .getText().toString());

        spiceManager.execute(request, null, DurationInMillis.ALWAYS_EXPIRED,
                             new ChangePasswordRequestListener());
    }

    public final class ChangePasswordRequestListener implements
            RequestListener<ChangePasswordResult>
    {

        @Override
        public void onRequestFailure(SpiceException spiceException)
        {
            // Log.d("ContactFragment.PasswordRequestListener", "onRequestFailure: ");
            displayErrorDialog(R.string.forgot_password_error_title, R.string.contact_error_message);
        }

        @Override
        public void onRequestSuccess(final ChangePasswordResult result)
        {
            if (result.isSuccess())
            {
                displaySuccessDialog(R.string.change_password_success_title,
                                     R.string.change_password_success_message);
            }
            else
            {
                displayErrorDialog(R.string.change_password_error_title,
                                   R.string.change_password_error_message);
            }
        }
    }

    private Dialog	dialog;

    private void createDialogContentView()
    {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        dialogContentView = inflater.inflate(R.layout.view_alert_dialog, null, false);

        dialogViewFlipper = (ViewFlipper) dialogContentView.findViewById(R.id.view_flipper);
        dialogTitle = (TextView) dialogContentView.findViewById(R.id.title);
        dialogMessage = (TextView) dialogContentView.findViewById(R.id.message);
        dialogButton = (Button) dialogContentView.findViewById(R.id.button);
    }

    private void displayLoadingDialog()
    {
        if (dialog == null)
            createDialogContentView();

        dialogViewFlipper.setDisplayedChild(0);
        dialogMessage.setVisibility(View.GONE);

        dialog = new AlertDialog.Builder(getActivity()).setView(dialogContentView).create();
        dialog.setCancelable(false);
        dialog.show();
    }

    private void displayErrorDialog(int title, int message)
    {
        dialogMessage.setVisibility(View.VISIBLE);
        dialogTitle.setText(title);
        dialogMessage.setText(message);
        dialogViewFlipper.setDisplayedChild(1);
        dialog.setCancelable(true);

        dialogButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                dialog = null;
            }
        });
    }

    private void displaySuccessDialog(int title, int message)
    {
        dialogMessage.setVisibility(View.VISIBLE);
        dialogTitle.setText(title);
        dialogMessage.setText(message);
        dialogViewFlipper.setDisplayedChild(1);
        dialog.setCancelable(true);

        dialogButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                dialog = null;
                dismiss();
            }
        });
    }

    @Override
    public void setMenuVisibility(boolean menuVisible)
    {
        super.setMenuVisibility(menuVisible);

        // In a future update to the support library, this should override setUserVisibleHint
        // instead of setMenuVisibility.
        if (mOldPassword != null)
        {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            if (!menuVisible)
            {
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        }
    }

    @Override
    public void onStart()
    {
        spiceManager.start(getActivity());
        super.onStart();
    }

    @Override
    public void onStop()
    {
        spiceManager.shouldStop();
        super.onStop();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        mNewPassword.removeTextChangedListener(newPasswordWatcher);

        mNewPassword2.removeTextChangedListener(newPassword2Watcher);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        mNewPassword.addTextChangedListener(newPasswordWatcher);

        mNewPassword2.addTextChangedListener(newPassword2Watcher);
    }
}
