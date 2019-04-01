package com.eulerhermes.research.fragment;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.ViewFlipper;
import androidx.fragment.app.Fragment;
import com.yabeman.android.extended.util.AlertUtil;
import com.eulerhermes.research.R;
import com.eulerhermes.research.core.CorePrefs;
import com.eulerhermes.research.model.SubscribeResult;
import com.eulerhermes.research.network.rest.RegisterRequest;
import com.eulerhermes.research.util.RequestUtil;
import com.octo.android.robospice.Jackson2GoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class RegisterFragment extends Fragment
{
    private ILoginDialog dialog;
    private ViewFlipper flipper;
    private EditText forename;
    private Switch hasAcceptedInformation;
    private EditText name;
    private final SpiceManager spiceManager = new SpiceManager(Jackson2GoogleHttpClientSpiceService.class);
    private EditText username;

    public final class RegisterRequestListener implements RequestListener<SubscribeResult> {
        public void onRequestFailure(SpiceException spiceException) {
            RegisterFragment.this.setProcessingState(false);
            RegisterFragment.this.dialog.onEndProcessing();
            AlertUtil.showAlert(RegisterFragment.this.getActivity(), R.string.login_error_title, R.string.register_error_message, R.string.ok);
        }

        public void onRequestSuccess(SubscribeResult result) {
            CorePrefs.saveUsername(RegisterFragment.this.username.getText().toString());
            CorePrefs.setHasRequestedId(RegisterFragment.this.username.getText().toString());
            RegisterFragment.this.dialog.onEndProcessing();
            AlertUtil.showAlert(RegisterFragment.this.getActivity(), (int) R.string.register_successful_title, (int) R.string.register_successful_message, (int) R.string.ok, new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int which) {
                    RegisterFragment.this.setProcessingState(false);
                    RegisterFragment.this.resetState();
                    RegisterFragment.this.dialog.onComplete();
                }
            });
        }
    }

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.name = (EditText) view.findViewById(R.id.last_name);
        this.forename = (EditText) view.findViewById(R.id.first_name);
        this.username = (EditText) view.findViewById(R.id.email);
        this.hasAcceptedInformation = (Switch) view.findViewById(R.id.has_accepted_info);
        this.flipper = (ViewFlipper) view.findViewById(R.id.view_flipper);
        this.username.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == 66) {
                    RegisterFragment.this.register();
                }
                return false;
            }
        });
        view.findViewById(R.id.register_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RegisterFragment.this.checkFields();
            }
        });
    }

    private void checkFields() {
        int errorId = 0;
        if (this.forename.getText().length() == 0) {
            errorId = R.string.register_error_forename;
        } else if (this.name.getText().length() == 0) {
            errorId = R.string.register_error_name;
        } else if (this.username.getText().length() == 0) {
            errorId = R.string.register_error_email;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(this.username.getText().toString()).matches()) {
            errorId = R.string.register_error_email;
        }
        if (errorId != 0) {
            AlertUtil.showAlert(getActivity(), R.string.register_error_title, errorId, R.string.ok);
        } else {
            register();
        }
    }

    private void register() {
        setProcessingState(true);
        this.dialog.onProcessing();
        this.spiceManager.execute(new RegisterRequest(this.name.getText().toString(), this.forename.getText().toString(), this.username.getText().toString(), Boolean.valueOf(this.hasAcceptedInformation.isChecked())), null, RequestUtil.getCacheExpiration(), new RegisterRequestListener());
    }

    private void setProcessingState(boolean processing) {
        boolean z;
        boolean z2 = false;
        this.flipper.setDisplayedChild(processing ? 1 : 0);
        EditText editText = this.name;
        if (processing) {
            z = false;
        } else {
            z = true;
        }
        editText.setEnabled(z);
        editText = this.forename;
        if (processing) {
            z = false;
        } else {
            z = true;
        }
        editText.setEnabled(z);
        editText = this.username;
        if (processing) {
            z = false;
        } else {
            z = true;
        }
        editText.setEnabled(z);
        Switch switchR = this.hasAcceptedInformation;
        if (!processing) {
            z2 = true;
        }
        switchR.setEnabled(z2);
    }

    private void resetState() {
        this.name.setText("");
        this.forename.setText("");
        this.username.setText("");
        this.hasAcceptedInformation.setChecked(true);
    }

    public void onResume() {
        getActivity().getActionBar().setTitle("Ask for a login ID");
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
}
