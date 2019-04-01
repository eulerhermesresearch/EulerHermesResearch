package com.eulerhermes.research.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewFlipper;
import androidx.fragment.app.DialogFragment;
import com.yabeman.android.extended.util.AlertUtil;
import com.eulerhermes.research.R;
import com.eulerhermes.research.core.CorePrefs;
import com.eulerhermes.research.model.AuthenticateResult;
import com.eulerhermes.research.model.User;
import com.eulerhermes.research.network.rest.KnowingEachOtherBetterRequest;
import com.octo.android.robospice.Jackson2GoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class KnowingEachOtherBetterFragmentDialog extends DialogFragment
{
    public static KnowingEachOtherBetterFragmentDialog newInstance()
    {
        return new KnowingEachOtherBetterFragmentDialog();
    }

    public KnowingEachOtherBetterFragmentDialog()
    {
        mSpiceManager = new SpiceManager(Jackson2GoogleHttpClientSpiceService.class);
    }

    private final SpiceManager	mSpiceManager;

    private EditText			mPhonePrefix;
    private EditText			mPhone;
    private Spinner				mEmployeesRange;
    private Spinner				mSalesRange;
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
        return inflater.inflate(R.layout.fragment_knowing_each_other_better, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        mPhonePrefix = (EditText) view.findViewById(R.id.phone_prefix);
        mPhone = (EditText) view.findViewById(R.id.phone);
        mEmployeesRange = (Spinner) view.findViewById(R.id.employees);
        mSalesRange = (Spinner) view.findViewById(R.id.sales);
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
        mPhonePrefix.setEnabled(false);
        mPhone.setEnabled(false);
        mEmployeesRange.setEnabled(false);
        mSalesRange.setEnabled(false);

        String employees = mEmployeesRange.getSelectedItem() != null ? mEmployeesRange.getSelectedItem().toString() : mEmployeesRange.getItemAtPosition(0).toString();
        String sales = mSalesRange.getSelectedItem() != null ? mSalesRange.getSelectedItem().toString() : mSalesRange.getItemAtPosition(0).toString();

        setCancelable(false);

        KnowingEachOtherBetterRequest request = new KnowingEachOtherBetterRequest(mPhonePrefix.getText().toString(), mPhone.getText().toString(), employees, sales);

        mSpiceManager.execute(request, null, DurationInMillis.ALWAYS_EXPIRED, new LoginRequestListener());
    }

    public final class LoginRequestListener implements RequestListener<AuthenticateResult>
    {

        @Override
        public void onRequestFailure(SpiceException spiceException)
        {
            mViewFlipper.setDisplayedChild(0);
            mPhonePrefix.setEnabled(true);
            mPhone.setEnabled(true);
            mEmployeesRange.setEnabled(true);
            mSalesRange.setEnabled(true);

            setCancelable(true);

            AlertUtil.showAlert(KnowingEachOtherBetterFragmentDialog.this.getActivity(), R.string.update_error_title, R.string.update_error_message,
                                R.string.ok);
        }

        @Override
        public void onRequestSuccess(final AuthenticateResult result)
        {
            User user = CorePrefs.getUser();
            user.setPhonePrefix(mPhonePrefix.getText().toString());
            user.setPhone(mPhone.getText().toString());
            user.setEmployeesRange(mEmployeesRange.getSelectedItem().toString());
            user.setSalesRange(mSalesRange.getSelectedItem().toString());

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