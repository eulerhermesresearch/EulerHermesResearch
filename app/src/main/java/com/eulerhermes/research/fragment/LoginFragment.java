package com.eulerhermes.research.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.yabeman.android.extended.util.AlertUtil;
import com.eulerhermes.research.R;
import com.eulerhermes.research.app.ContactActivity;
import com.eulerhermes.research.app.LinkedInLoginActivity;
import com.eulerhermes.research.app.LoginActivity;
import com.eulerhermes.research.app.MainActivity;
import com.eulerhermes.research.common.AnalyticsHelper;
import com.eulerhermes.research.core.CorePrefs;
import com.eulerhermes.research.core.IntentExtras;
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

public class LoginFragment extends Fragment
{
    private ProgressDialog dialog;
    private Button forgotPassword;
    private User linkedInUser;
    private Button linkedin;
    private EditText login;
    private EditText password;
    private final SpiceManager spiceManager = new SpiceManager(Jackson2GoogleHttpClientSpiceService.class);

    public final class LoginRequestListener implements RequestListener<AuthenticateResult> {
        public void onRequestFailure(SpiceException spiceException) {
            login.setEnabled(true);
            password.setEnabled(true);
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
            AlertUtil.showAlert(getActivity(), R.string.login_error_title, R.string.login_error_message, R.string.ok, null, R.string.assistance, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    showAssistance();
                }
            });
        }

        public void onRequestSuccess(AuthenticateResult result) {
            CorePrefs.saveUser(result.getUser());
            CorePrefs.incrementLoginCount();
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
            getActivity().finish();
            sendAnalytics(result.getUser().getUsername());
            String name = result.getUser().getFormattedName();
            Toast.makeText(getActivity(), getActivity().getString(R.string.login_successful, name), Toast.LENGTH_SHORT).show();
        }
    }

    public final class RegisterRequestListener implements RequestListener<SubscribeResult> {
        public void onRequestFailure(SpiceException spiceException) {
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
            login.setEnabled(true);
            password.setEnabled(true);
            linkedin.setEnabled(true);
            AlertUtil.showAlert(getActivity(), R.string.login_error_title, R.string.login_error_message, R.string.ok, null, R.string.assistance, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    showAssistance();
                }
            });
        }

        public void onRequestSuccess(SubscribeResult result) {
            linkedInUser.setId(result.getId());
            CorePrefs.saveUser(linkedInUser);
            CorePrefs.incrementLoginCount();
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
            getActivity().finish();
            sendAnalytics(linkedInUser.getUsername());
            String name = linkedInUser.getFormattedName();
            Toast.makeText(getActivity(), getActivity().getString(R.string.login_successful, name), Toast.LENGTH_SHORT).show();
        }
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.login = (EditText) view.findViewById(R.id.login);
        this.password = (EditText) view.findViewById(R.id.password);
        this.linkedin = (Button) view.findViewById(R.id.linkedin);
        this.forgotPassword = (Button) view.findViewById(R.id.forgot_password);
        if (CorePrefs.getUsername() != null) {
            this.login.setText(CorePrefs.getUsername());
        }
        this.password.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == 66) {
                    login();
                }
                return false;
            }
        });
        view.findViewById(R.id.signin).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                login();
            }
        });
        view.findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((LoginActivity) getActivity()).register();
            }
        });
        this.linkedin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getActivity().startActivityForResult(new Intent(getActivity(), LinkedInLoginActivity.class), 103);
            }
        });
        this.forgotPassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getActivity().finish();
                Intent intent = new Intent(getActivity(), ContactActivity.class);
                intent.putExtra(ContactActivity.DISPLAY_PASSWORD, true);
                getActivity().startActivity(intent);
            }
        });
    }

    public void refresh() {
        if (CorePrefs.getUsername() != null) {
            this.login.setText(CorePrefs.getUsername());
        }
    }

    private void login() {
        this.login.setEnabled(false);
        this.password.setEnabled(false);
        CorePrefs.saveUsername(this.login.getText().toString());
        LoginRequest request = new LoginRequest(this.login.getText().toString(), this.password.getText().toString());
        if (this.dialog == null) {
            this.dialog = ProgressDialog.show(getActivity(), null, getString(R.string.logging_in));
        }
        this.spiceManager.execute(request, null, -1, new LoginRequestListener());
    }

    private void showAssistance() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra(IntentExtras.MAIN_POSITION, 7);
        getActivity().startActivity(intent);
        getActivity().finish();
    }

    public void onResume() {
        getActivity().getActionBar().setTitle(getString(R.string.connect));
        this.spiceManager.start(getActivity());
        super.onResume();
    }

    public void onPause() {
        this.spiceManager.shouldStop();
        super.onPause();
    }

    protected SpiceManager getSpiceManager() {
        return this.spiceManager;
    }

    private void sendAnalytics(String login) {
        AnalyticsHelper.event("ui_action", "login_success");
    }

    public void onLinkedInLoginComplete(String firstName, String lastName, String email) {
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
        this.spiceManager.execute(new RegisterRequest(firstName, lastName, email, Boolean.valueOf(false)), null, RequestUtil.getCacheExpiration(), new RegisterRequestListener());
    }
}
