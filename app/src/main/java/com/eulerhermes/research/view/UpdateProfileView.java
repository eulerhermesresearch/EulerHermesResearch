package com.eulerhermes.research.view;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ViewFlipper;
import com.yabeman.android.extended.util.AlertUtil;
import com.eulerhermes.research.R;
import com.eulerhermes.research.core.CorePrefs;
import com.eulerhermes.research.fragment.ILoginDialog;
import com.eulerhermes.research.model.AuthenticateResult;
import com.eulerhermes.research.model.User;
import com.eulerhermes.research.network.rest.UpdateRequest;
import com.eulerhermes.research.util.RequestUtil;
import com.octo.android.robospice.Jackson2GoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class UpdateProfileView extends LinearLayout
{
	private ILoginDialog		dialog;

	private EditText			firstName;
	private EditText			lastName;
	private EditText			email;
	private EditText			company;
	private EditText			sector;
	private EditText			job;
	private EditText			department;
	private Switch				ehCustomer;
	private Switch				receiveInfo;
	private EditText			country;
	private EditText			phonePrefix;
	private EditText			phone;
	private Spinner				employees;
	private Spinner				sales;
	
	private ViewFlipper flipper;

	private final SpiceManager	spiceManager;
	private User tmpUser;

	public UpdateProfileView(Context context, ILoginDialog dialog)
	{
		super(context);

		this.dialog = dialog;
		spiceManager = new SpiceManager(Jackson2GoogleHttpClientSpiceService.class);

		init(context);
	}

	public UpdateProfileView(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		spiceManager = new SpiceManager(Jackson2GoogleHttpClientSpiceService.class);

		init(context);
	}

	public UpdateProfileView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);

		spiceManager = new SpiceManager(Jackson2GoogleHttpClientSpiceService.class);

		init(context);
	}

	private void init(Context context)
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_update_profile, this, true);

		firstName = (EditText) findViewById(R.id.update_first_name);
		lastName = (EditText) findViewById(R.id.update_last_name);
		email = (EditText) findViewById(R.id.update_email);
		company = (EditText) findViewById(R.id.company);
		job = (EditText) findViewById(R.id.job);
		sector = (EditText) findViewById(R.id.sector);
		department = (EditText) findViewById(R.id.department);
		country = (EditText) findViewById(R.id.country);
		phonePrefix = (EditText) findViewById(R.id.phone_prefix);
		phone = (EditText) findViewById(R.id.phone);
		ehCustomer = (Switch) findViewById(R.id.eh_customer);
		receiveInfo = (Switch) findViewById(R.id.receive_info);
		employees = (Spinner) findViewById(R.id.employees);
		sales = (Spinner) findViewById(R.id.sales);
		flipper = (ViewFlipper) findViewById(R.id.signin_flipper);

		User user = CorePrefs.getUser();

		firstName.setText(StringUtils.capitalize(user.getForeName()));
		lastName.setText(user.getName().toUpperCase());
		email.setText(user.getUsername());
		company.setText(user.getCompany());
		job.setText(user.getJobTitle());
		sector.setText(user.getSector());
		department.setText(user.getDepartment());
		country.setText(user.getCountry());
		phonePrefix.setText(user.getPhonePrefix());
		phone.setText(user.getPhone());
		ehCustomer.setChecked(user.getIsCustomerOfEH());
		receiveInfo.setChecked(user.getHasAcceptedInformation());
		
		
		String[] employeesRange = getResources().getStringArray(R.array.employees_range);
		employees.setSelection(ArrayUtils.indexOf(employeesRange, user.getEmployeesRange()));
		
		String[] salesRange = getResources().getStringArray(R.array.sales_range);
		sales.setSelection(ArrayUtils.indexOf(salesRange, user.getSalesRange()));
		
		Log.d("UpdateProfileView", "init: " + user.getEmployeesRange());
		Log.d("UpdateProfileView", "init: " + ArrayUtils.indexOf(employeesRange, user.getEmployeesRange()));
		Log.d("UpdateProfileView", "init: " + user.getSalesRange());
		Log.d("UpdateProfileView", "init: " + ArrayUtils.indexOf(salesRange, user.getSalesRange()));
		
		phone.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				if (keyCode == KeyEvent.KEYCODE_ENTER)
				{
					update();
				}
				
				return false;
			}
		});

		findViewById(R.id.update).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				update();
			}
		});
	}

	private void update()
	{
		flipper.setDisplayedChild(1);
		setFormEnabled(false);
		
		tmpUser = CorePrefs.getUser();
		tmpUser.setCompany(company.getText().toString());
		tmpUser.setCountry(country.getText().toString());
		tmpUser.setDepartment(department.getText().toString());
		tmpUser.setEmployeesRange((String) employees.getSelectedItem());
		tmpUser.setForeName(firstName.getText().toString());
		tmpUser.setHasAcceptedInformation(receiveInfo.isChecked());
		tmpUser.setIsCustomerOfEH(ehCustomer.isChecked());
		tmpUser.setJobTitle(job.getText().toString());
		tmpUser.setName(lastName.getText().toString());
		tmpUser.setPhone(phone.getText().toString());
		tmpUser.setPhonePrefix(phonePrefix.getText().toString());
		tmpUser.setSalesRange((String) sales.getSelectedItem());
		tmpUser.setSector(sector.getText().toString());
		
		Log.d("UpdateProfileView", "update: " + employees.getSelectedItem());
		Log.d("UpdateProfileView", "update: " + sales.getSelectedItem());
		
		UpdateRequest request = new UpdateRequest(tmpUser);

		dialog.onProcessing();
		spiceManager.execute(request, null, RequestUtil.getCacheExpiration(), new LoginRequestListener());
	}
	
	private void setFormEnabled(boolean enabled)
	{
		firstName.setEnabled(enabled);
		lastName.setEnabled(enabled);
		email.setEnabled(enabled);
		company.setEnabled(enabled);
		job.setEnabled(enabled);
		sector.setEnabled(enabled);
		department.setEnabled(enabled);
		country.setEnabled(enabled);
		phonePrefix.setEnabled(enabled);
		phone.setEnabled(enabled);
		ehCustomer.setEnabled(enabled);
		receiveInfo.setEnabled(enabled);
	}

	public final class LoginRequestListener implements RequestListener<AuthenticateResult>
	{

		@Override
		public void onRequestFailure(SpiceException spiceException)
		{
			Log.d("LoginFragmentDialog.LoginRequestListener", "onRequestFailure: " + spiceException);

			flipper.setDisplayedChild(0);
			setFormEnabled(true);

			dialog.onEndProcessing();
			AlertUtil.showAlert(UpdateProfileView.this.getContext(), R.string.update_error_title, R.string.update_error_message, R.string.ok);
		}

		@Override
		public void onRequestSuccess(final AuthenticateResult result)
		{
			Log.d("LoginView.LoginRequestListener", "onRequestSuccess: " + result);
			CorePrefs.saveUser(tmpUser);
			dialog.onEndProcessing();
			dialog.onComplete();
			
			Toast.makeText(getContext(), R.string.profile_updated, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onAttachedToWindow()
	{
		spiceManager.start(getContext());
		super.onAttachedToWindow();
	}

	@Override
	protected void onDetachedFromWindow()
	{
		spiceManager.shouldStop();
		super.onDetachedFromWindow();
	}

	protected SpiceManager getSpiceManager()
	{
		return spiceManager;
	}
}
