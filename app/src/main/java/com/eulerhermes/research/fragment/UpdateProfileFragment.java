package com.eulerhermes.research.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ViewFlipper;
import androidx.fragment.app.DialogFragment;
import com.yabeman.android.extended.util.AlertUtil;
import com.eulerhermes.research.R;
import com.eulerhermes.research.core.CorePrefs;
import com.eulerhermes.research.core.CoreUtil;
import com.eulerhermes.research.model.AuthenticateResult;
import com.eulerhermes.research.model.User;
import com.eulerhermes.research.network.rest.UpdateRequest;
import com.eulerhermes.research.util.RequestUtil;
import com.octo.android.robospice.Jackson2GoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class UpdateProfileFragment extends DialogFragment
{
    private EditText company;
    private EditText country;
    private EditText department;
    private Switch ehCustomer;
    private EditText email;
    private Spinner employees;
    private EditText firstName;
    private ViewFlipper flipper;
    private EditText job;
    private EditText lastName;
    private EditText phone;
    private EditText phonePrefix;
    private Switch receiveInfo;
    private Spinner sales;
    private EditText sector;
    private final SpiceManager spiceManager = new SpiceManager(Jackson2GoogleHttpClientSpiceService.class);
    private User tmpUser;

    public final class LoginRequestListener implements RequestListener<AuthenticateResult> {
        public void onRequestFailure(SpiceException spiceException) {
            UpdateProfileFragment.this.flipper.setDisplayedChild(0);
            UpdateProfileFragment.this.setFormEnabled(true);
            AlertUtil.showAlert(UpdateProfileFragment.this.getActivity(), R.string.update_error_title, R.string.update_error_message, R.string.ok);
        }

        public void onRequestSuccess(AuthenticateResult result) {
            CorePrefs.saveUser(UpdateProfileFragment.this.tmpUser);
            Toast.makeText(UpdateProfileFragment.this.getActivity(), R.string.profile_updated, Toast.LENGTH_SHORT).show();
            if (CoreUtil.isTablet()) {
                UpdateProfileFragment.this.dismiss();
            } else {
                UpdateProfileFragment.this.getActivity().finish();
            }
        }
    }

    public static UpdateProfileFragment newInstance() {
        return new UpdateProfileFragment();
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setStyle(STYLE_NORMAL, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setLayout(getMaxDialogWidth(), getMaxDialogHeight());
        return dialog;
    }

    protected int getMaxDialogWidth() {
        return getResources().getDimensionPixelSize(R.dimen.login_dialog_width);
    }

    protected int getMaxDialogHeight() {
        return -2;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_update_profile, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(getMaxDialogWidth(), getMaxDialogHeight());
        }
        if (!CoreUtil.isTablet()) {
            view.findViewById(R.id.title).setVisibility(View.GONE);
        }
        this.firstName = (EditText) view.findViewById(R.id.update_first_name);
        this.lastName = (EditText) view.findViewById(R.id.update_last_name);
        this.email = (EditText) view.findViewById(R.id.update_email);
        this.company = (EditText) view.findViewById(R.id.company);
        this.job = (EditText) view.findViewById(R.id.job);
        this.sector = (EditText) view.findViewById(R.id.sector);
        this.department = (EditText) view.findViewById(R.id.department);
        this.country = (EditText) view.findViewById(R.id.country);
        this.phonePrefix = (EditText) view.findViewById(R.id.phone_prefix);
        this.phone = (EditText) view.findViewById(R.id.phone);
        this.ehCustomer = (Switch) view.findViewById(R.id.eh_customer);
        this.receiveInfo = (Switch) view.findViewById(R.id.receive_info);
        this.employees = (Spinner) view.findViewById(R.id.employees);
        this.sales = (Spinner) view.findViewById(R.id.sales);
        this.flipper = (ViewFlipper) view.findViewById(R.id.signin_flipper);
        User user = CorePrefs.getUser();
        this.firstName.setText(StringUtils.capitalize(user.getForeName()));
        this.lastName.setText(user.getName().toUpperCase());
        this.email.setText(user.getUsername());
        this.company.setText(user.getCompany());
        this.job.setText(user.getJobTitle());
        this.sector.setText(user.getSector());
        this.department.setText(user.getDepartment());
        this.country.setText(user.getCountry());
        this.phonePrefix.setText(user.getPhonePrefix());
        this.phone.setText(user.getPhone());
        this.ehCustomer.setChecked(user.getIsCustomerOfEH());
        this.receiveInfo.setChecked(user.getHasAcceptedInformation());
        Object[] employeesRange = getResources().getStringArray(R.array.employees_range);
        this.employees.setSelection(ArrayUtils.indexOf(employeesRange, user.getEmployeesRange()));
        Object[] salesRange = getResources().getStringArray(R.array.sales_range);
        this.sales.setSelection(ArrayUtils.indexOf(salesRange, user.getSalesRange()));
        Log.d("UpdateProfileView", "init: " + user.getEmployeesRange());
        Log.d("UpdateProfileView", "init: " + ArrayUtils.indexOf(employeesRange, user.getEmployeesRange()));
        Log.d("UpdateProfileView", "init: " + user.getSalesRange());
        Log.d("UpdateProfileView", "init: " + ArrayUtils.indexOf(salesRange, user.getSalesRange()));
        this.phone.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == 66) {
                    UpdateProfileFragment.this.update();
                }
                return false;
            }
        });
        view.findViewById(R.id.update).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                UpdateProfileFragment.this.update();
            }
        });
    }

    private void update() {
        this.flipper.setDisplayedChild(1);
        setFormEnabled(false);
        this.tmpUser = CorePrefs.getUser();
        this.tmpUser.setCompany(this.company.getText().toString());
        this.tmpUser.setCountry(this.country.getText().toString());
        this.tmpUser.setDepartment(this.department.getText().toString());
        this.tmpUser.setEmployeesRange((String) this.employees.getSelectedItem());
        this.tmpUser.setForeName(this.firstName.getText().toString());
        this.tmpUser.setHasAcceptedInformation(this.receiveInfo.isChecked());
        this.tmpUser.setIsCustomerOfEH(this.ehCustomer.isChecked());
        this.tmpUser.setJobTitle(this.job.getText().toString());
        this.tmpUser.setName(this.lastName.getText().toString());
        this.tmpUser.setPhone(this.phone.getText().toString());
        this.tmpUser.setPhonePrefix(this.phonePrefix.getText().toString());
        this.tmpUser.setSalesRange((String) this.sales.getSelectedItem());
        this.tmpUser.setSector(this.sector.getText().toString());
        Log.d("UpdateProfileView", "update: " + this.employees.getSelectedItem());
        Log.d("UpdateProfileView", "update: " + this.sales.getSelectedItem());
        this.spiceManager.execute(new UpdateRequest(this.tmpUser), null, RequestUtil.getCacheExpiration(), new LoginRequestListener());
    }

    private void setFormEnabled(boolean enabled) {
        this.firstName.setEnabled(enabled);
        this.lastName.setEnabled(enabled);
        this.email.setEnabled(enabled);
        this.company.setEnabled(enabled);
        this.job.setEnabled(enabled);
        this.sector.setEnabled(enabled);
        this.department.setEnabled(enabled);
        this.country.setEnabled(enabled);
        this.phonePrefix.setEnabled(enabled);
        this.phone.setEnabled(enabled);
        this.ehCustomer.setEnabled(enabled);
        this.receiveInfo.setEnabled(enabled);
    }

    public void onStart() {
        this.spiceManager.start(getActivity());
        super.onStart();
    }

    public void onStop() {
        this.spiceManager.shouldStop();
        super.onStop();
    }

    protected SpiceManager getSpiceManager() {
        return this.spiceManager;
    }
}
