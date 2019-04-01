package com.eulerhermes.research.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ViewFlipper;
import androidx.fragment.app.DialogFragment;
import com.yabeman.android.extended.util.AlertUtil;
import com.eulerhermes.research.R;
import com.eulerhermes.research.core.CorePrefs;
import com.eulerhermes.research.model.AuthenticateResult;
import com.eulerhermes.research.model.User;
import com.eulerhermes.research.network.rest.UpdateSmallRequest;
import com.octo.android.robospice.Jackson2GoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class FinalStepFragmentDialog extends DialogFragment
{
    public static FinalStepFragmentDialog newInstance()
    {
        return new FinalStepFragmentDialog();
    }

    public FinalStepFragmentDialog()
    {
        mSpiceManager = new SpiceManager(Jackson2GoogleHttpClientSpiceService.class);
    }

    private final SpiceManager	mSpiceManager;

    private EditText			mCompany;
    private EditText			mSector;
    private EditText			mJob;
    private EditText			mDepartment;
    private Switch				mEHCustomer;
    private EditText			mCountry;
    private ViewFlipper			mViewFlipper;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        setStyle(STYLE_NORMAL, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_final_step, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        mCompany = (EditText) view.findViewById(R.id.company);
        mSector = (EditText) view.findViewById(R.id.sector);
        mJob = (EditText) view.findViewById(R.id.job);
        mDepartment = (EditText) view.findViewById(R.id.department);
        mEHCustomer = (Switch) view.findViewById(R.id.eh_customer);
        mCountry = (EditText) view.findViewById(R.id.country);
        mViewFlipper = (ViewFlipper) view.findViewById(R.id.view_flipper);

        final Button cancelButton = (Button) view.findViewById(R.id.cancel);
        final Button sendButton = (Button) view.findViewById(R.id.send);

        cancelButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v)
            {
                dismiss();
            }
        });

        sendButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v)
            {
                performRequest();
            }
        });
    }

    private void performRequest()
    {
        mViewFlipper.setDisplayedChild(1);
        mCompany.setEnabled(false);
        mSector.setEnabled(false);
        mJob.setEnabled(false);
        mDepartment.setEnabled(false);
        mEHCustomer.setEnabled(false);
        mCountry.setEnabled(false);

        setCancelable(false);

        UpdateSmallRequest request = new UpdateSmallRequest(mCompany.getText().toString(), mSector.getText().toString(), mJob.getText().toString(),
                                                            mDepartment.getText().toString(), mEHCustomer.isChecked(), mCountry.getText().toString());

        mSpiceManager.execute(request, null, DurationInMillis.ALWAYS_EXPIRED, new LoginRequestListener());
    }

    public final class LoginRequestListener implements RequestListener<AuthenticateResult>
    {

        @Override
        public void onRequestFailure(SpiceException spiceException)
        {
            mViewFlipper.setDisplayedChild(0);
            mCompany.setEnabled(true);
            mSector.setEnabled(true);
            mJob.setEnabled(true);
            mDepartment.setEnabled(true);
            mEHCustomer.setEnabled(true);
            mCountry.setEnabled(true);

            setCancelable(true);

            AlertUtil.showAlert(FinalStepFragmentDialog.this.getActivity(), R.string.update_error_title, R.string.update_error_message, R.string.ok);
        }

        @Override
        public void onRequestSuccess(final AuthenticateResult result)
        {
            User user = CorePrefs.getUser();
            user.setCompany(mCompany.getText().toString());
            user.setSector(mSector.getText().toString());
            user.setJobTitle(mJob.getText().toString());
            user.setDepartment(mDepartment.getText().toString());
            user.setIsCustomerOfEH(mEHCustomer.isChecked());
            user.setCountry(mCountry.getText().toString());

            CorePrefs.saveUser(user);

            Toast.makeText(getActivity(), getString(R.string.small_update_success), Toast.LENGTH_SHORT).show();
            dismiss();
        }
    }

    @Override
    public void onStart()
    {
        mSpiceManager.start(getActivity());
        super.onStart();
    }

    @Override
    public void onStop()
    {
        mSpiceManager.shouldStop();
        super.onStop();
    }

}