package com.eulerhermes.research.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.*;
import androidx.fragment.app.DialogFragment;
import com.yabeman.android.extended.util.AlertUtil;
import com.eulerhermes.research.R;
import com.eulerhermes.research.app.ContactActivity;
import com.eulerhermes.research.app.LinkedInLoginActivity;
import com.eulerhermes.research.common.AnalyticsHelper;
import com.eulerhermes.research.core.CorePrefs;
import com.eulerhermes.research.model.AuthenticateResult;
import com.eulerhermes.research.model.SubscribeResult;
import com.eulerhermes.research.model.User;
import com.eulerhermes.research.network.rest.LoginRequest;
import com.eulerhermes.research.network.rest.RegisterRequest;
import com.eulerhermes.research.util.RequestUtil;
import com.octo.android.robospice.Jackson2GoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class LoginFragmentDialog extends DialogFragment
{
    private       ProgressDialog dialog;
    private       ViewFlipper    flipper;
    private       EditText       forename;
    private       Button         forgotPassword;
    private       Switch         hasAcceptedInformation;
    private       User           linkedInUser;
    private       Button         linkedin;
    private       EditText       login;
    private       EditText       name;
    private       EditText       password;
    private final SpiceManager   spiceManager = new SpiceManager(Jackson2GoogleHttpClientSpiceService.class);
    private       EditText       username;

    public final class LoginRequestListener implements RequestListener<AuthenticateResult>
    {
        public void onRequestFailure(SpiceException spiceException)
        {
            login.setEnabled(true);
            password.setEnabled(true);
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
            AlertUtil.showAlert(getActivity(), R.string.login_error_title, R.string.login_error_message, R.string.ok, null, R.string.assistance, new OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    showAssistance();
                }
            });
        }

        public void onRequestSuccess(AuthenticateResult result)
        {
            CorePrefs.saveUser(result.getUser());
            CorePrefs.incrementLoginCount();
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
            sendAnalytics(result.getUser().getUsername());
            String name = result.getUser().getFormattedName();
            Toast.makeText(getActivity(), getActivity().getString(R.string.login_successful, name), Toast.LENGTH_SHORT).show();
            dismiss();
        }
    }

    public final class RegisterRequestListener2 implements RequestListener<SubscribeResult>
    {
        public void onRequestFailure(SpiceException spiceException)
        {
            setProcessingState(false);
            AlertUtil.showAlert(getActivity(), R.string.login_error_title, R.string.register_error_message, R.string.ok);
        }

        public void onRequestSuccess(SubscribeResult result)
        {
            CorePrefs.saveUsername(username.getText().toString());
            CorePrefs.setHasRequestedId(username.getText().toString());
            AlertUtil.showAlert(getActivity(), (int) R.string.register_successful_title, (int) R.string.register_successful_message, (int) R.string.ok, new OnClickListener()
            {
                public void onClick(DialogInterface dialogInterface, int which)
                {
                    setProcessingState(false);
                    resetState();
                    dismiss();
                }
            });
        }
    }

    public final class RegisterRequestListener implements RequestListener<SubscribeResult>
    {
        public void onRequestFailure(SpiceException spiceException)
        {
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
            login.setEnabled(true);
            password.setEnabled(true);
            linkedin.setEnabled(true);
            AlertUtil.showAlert(getActivity(), R.string.login_error_title, R.string.login_error_message, R.string.ok, null, R.string.assistance, new OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    showAssistance();
                }
            });
        }

        public void onRequestSuccess(SubscribeResult result)
        {
            linkedInUser.setId(result.getId());
            CorePrefs.saveUser(linkedInUser);
            CorePrefs.incrementLoginCount();
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
            sendAnalytics(linkedInUser.getUsername());
            String name = linkedInUser.getFormattedName();
            Toast.makeText(getActivity(), getActivity().getString(R.string.login_successful, name), Toast.LENGTH_SHORT).show();
            dismiss();
        }
    }

    public static LoginFragmentDialog newInstance()
    {
        return new LoginFragmentDialog();
    }

    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        setStyle(STYLE_NORMAL, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setLayout(getMaxDialogWidth(), getMaxDialogHeight());
        return dialog;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getDialog().getWindow().setLayout(getMaxDialogWidth(), getMaxDialogHeight());
        return inflater.inflate(R.layout.dialog_login, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().setLayout(getMaxDialogWidth(), getMaxDialogHeight());
        final ViewSwitcher viewSwitcher = (ViewSwitcher) view.findViewById(R.id.view_switcher);
        viewSwitcher.setInAnimation(getActivity(), R.anim.slide_in_right);
        viewSwitcher.setOutAnimation(getActivity(), R.anim.slide_out_left);
        final TextView title = (TextView) view.findViewById(R.id.title);
        ((Button) view.findViewById(R.id.register)).setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                viewSwitcher.showNext();
                title.setText("Register");
            }
        });
        ((Button) view.findViewById(R.id.cancel_button)).setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                viewSwitcher.showPrevious();
                title.setText("Connect");
            }
        });
        this.login = (EditText) view.findViewById(R.id.login);
        this.password = (EditText) view.findViewById(R.id.password);
        this.linkedin = (Button) view.findViewById(R.id.linkedin);
        this.forgotPassword = (Button) view.findViewById(R.id.forgot_password);
        if (CorePrefs.getUsername() != null) {
            this.login.setText(CorePrefs.getUsername());
        }
        this.password.setOnKeyListener(new OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (keyCode == 66) {
                    login();
                }
                return false;
            }
        });
        view.findViewById(R.id.signin).setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                login();
            }
        });
        this.linkedin.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                getActivity().startActivityForResult(new Intent(getActivity(), LinkedInLoginActivity.class), 103);
            }
        });
        this.forgotPassword.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), ContactActivity.class);
                intent.putExtra(ContactActivity.DISPLAY_PASSWORD, true);
                getActivity().startActivity(intent);
                dismiss();
            }
        });
        this.name = (EditText) view.findViewById(R.id.last_name);
        this.forename = (EditText) view.findViewById(R.id.first_name);
        this.username = (EditText) view.findViewById(R.id.email);
        this.hasAcceptedInformation = (Switch) view.findViewById(R.id.has_accepted_info);
        this.flipper = (ViewFlipper) view.findViewById(R.id.view_flipper);
        this.username.setOnKeyListener(new OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (keyCode == 66) {
                    register();
                }
                return false;
            }
        });
        view.findViewById(R.id.register_button).setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                checkFields();
            }
        });
    }

    protected int getMaxDialogWidth()
    {
        return getResources().getDimensionPixelSize(R.dimen.login_dialog_width);
    }

    protected int getMaxDialogHeight()
    {
        return -2;
    }

    private void login()
    {
        this.login.setEnabled(false);
        this.password.setEnabled(false);
        CorePrefs.saveUsername(this.login.getText().toString());
        LoginRequest request = new LoginRequest(this.login.getText().toString(), this.password.getText().toString());
        if (this.dialog == null) {
            this.dialog = ProgressDialog.show(getActivity(), null, getString(R.string.logging_in));
        }
        this.spiceManager.execute(request, null, -1, new LoginRequestListener());
    }

    private void showAssistance()
    {
        getActivity().startActivity(new Intent(getActivity(), ContactActivity.class));
        dismiss();
    }

    public void onResume()
    {
        getActivity().getActionBar().setTitle(getString(R.string.connect));
        this.spiceManager.start(getActivity());
        super.onResume();
    }

    public void onPause()
    {
        this.spiceManager.shouldStop();
        super.onPause();
    }

    protected SpiceManager getSpiceManager()
    {
        return this.spiceManager;
    }

    private void sendAnalytics(String login)
    {
        AnalyticsHelper.event("ui_actior", "login_success");
    }

    public void onLinkedInLoginComplete(String firstName, String lastName, String email)
    {
        if (this.dialog == null) {
            this.dialog = ProgressDialog.show(getActivity(), null, getString(R.string.logging_in));
        }
        this.login.setEnabled(false);
        this.password.setEnabled(false);
        this.linkedin.setEnabled(false);
        this.linkedInUser = new User();
        this.linkedInUser.setName(lastName);
        this.linkedInUser.setForeName(firstName);
        this.linkedInUser.setUsername(email);
        CorePrefs.saveUsername(email);
        this.spiceManager.execute(new RegisterRequest(firstName, lastName, email, false), null, RequestUtil.getCacheExpiration(), new RegisterRequestListener());
    }

    private void checkFields()
    {
        int errorId = 0;
        if (this.forename.getText().length() == 0) {
            errorId = R.string.register_error_forename;
        }
        else if (this.name.getText().length() == 0) {
            errorId = R.string.register_error_name;
        }
        else if (this.username.getText().length() == 0) {
            errorId = R.string.register_error_email;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(this.username.getText().toString()).matches()) {
            errorId = R.string.register_error_email;
        }
        if (errorId != 0) {
            AlertUtil.showAlert(getActivity(), R.string.register_error_title, errorId, R.string.ok);
        }
        else {
            register();
        }
    }

    private void register()
    {
        setProcessingState(true);
        this.spiceManager.execute(new RegisterRequest(this.name.getText().toString(), this.forename.getText().toString(), this.username.getText().toString(), hasAcceptedInformation.isChecked()), null, RequestUtil.getCacheExpiration(), new RegisterRequestListener2());
    }

    private void setProcessingState(boolean processing)
    {
        boolean z;
        boolean z2 = false;
        this.flipper.setDisplayedChild(processing ? 1 : 0);
        EditText editText = this.name;
        if (processing) {
            z = false;
        }
        else {
            z = true;
        }
        editText.setEnabled(z);
        editText = this.forename;
        if (processing) {
            z = false;
        }
        else {
            z = true;
        }
        editText.setEnabled(z);
        editText = this.username;
        if (processing) {
            z = false;
        }
        else {
            z = true;
        }
        editText.setEnabled(z);
        Switch switchR = this.hasAcceptedInformation;
        if (!processing) {
            z2 = true;
        }
        switchR.setEnabled(z2);
    }

    private void resetState()
    {
        this.name.setText("");
        this.forename.setText("");
        this.username.setText("");
        this.hasAcceptedInformation.setChecked(true);
    }
}
