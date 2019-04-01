package com.eulerhermes.research.view;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.ViewFlipper;
import com.yabeman.android.extended.util.AlertUtil;
import com.eulerhermes.research.R;
import com.eulerhermes.research.core.CorePrefs;
import com.eulerhermes.research.fragment.ILoginDialog;
import com.eulerhermes.research.model.SubscribeResult;
import com.eulerhermes.research.network.rest.RegisterRequest;
import com.eulerhermes.research.util.RequestUtil;
import com.octo.android.robospice.Jackson2GoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class RegisterView extends LinearLayout
{
	private EditText name;
	private EditText forename;
	private EditText username;
	private Switch hasAcceptedInformation;
	private ViewFlipper flipper;
	
	private ILoginDialog dialog;
	
	private final SpiceManager	spiceManager;

	public RegisterView(Context context, ILoginDialog dialog)
	{
		super(context);
		
		this.dialog = dialog;
		spiceManager = new SpiceManager(Jackson2GoogleHttpClientSpiceService.class);

		init(context);
	}

	public RegisterView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		spiceManager = new SpiceManager(Jackson2GoogleHttpClientSpiceService.class);

		init(context);
	}

	public RegisterView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		
		spiceManager = new SpiceManager(Jackson2GoogleHttpClientSpiceService.class);

		init(context);
	}

	private void init(Context context)
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_register, this, true);

		name = (EditText) findViewById(R.id.last_name);
		forename = (EditText) findViewById(R.id.first_name);
		username = (EditText) findViewById(R.id.email);
		hasAcceptedInformation = (Switch) findViewById(R.id.has_accepted_info);
		flipper = (ViewFlipper) findViewById(R.id.view_flipper);
		
		username.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				if (keyCode == KeyEvent.KEYCODE_ENTER)
				{
					register();
				}
				return false;
			}
		});
		
		findViewById(R.id.register_button).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v)
			{
				checkFields();
			}
		});
	}
	
	private void checkFields()
	{
		int errorId = 0;
		
		if (forename.getText().length() == 0)
		{
			errorId = R.string.register_error_forename;
		}
		else if (name.getText().length() == 0)
		{
			errorId = R.string.register_error_name;
		}
		else if (username.getText().length() == 0)
		{
			errorId = R.string.register_error_email;
		}
		else if (!Patterns.EMAIL_ADDRESS.matcher(username.getText().toString()).matches())
		{
			errorId = R.string.register_error_email;
		}
		
		if (errorId != 0)
		{
			AlertUtil.showAlert(getContext(), R.string.register_error_title, errorId, R.string.ok);
		}
		else
		{
			register();
		}
	}
	
	private void register()
	{
		setProcessingState(true);
		
		dialog.onProcessing();
		
		RegisterRequest request = new RegisterRequest(name.getText().toString(), forename.getText().toString(), username.getText().toString(), hasAcceptedInformation.isChecked());

		spiceManager.execute(request, null, RequestUtil.getCacheExpiration(), new RegisterRequestListener());
	}
	
	public final class RegisterRequestListener implements RequestListener<SubscribeResult>
	{

		@Override
		public void onRequestFailure(SpiceException spiceException)
		{
			Log.d("RegisterView.RegisterRequestListener", "onRequestFailure: " + spiceException);
			
			setProcessingState(false);
			
			dialog.onEndProcessing();
			AlertUtil.showAlert(RegisterView.this.getContext(), R.string.login_error_title, R.string.register_error_message, R.string.ok);
		}

		@Override
		public void onRequestSuccess(final SubscribeResult result)
		{
			CorePrefs.saveUsername(username.getText().toString());
			CorePrefs.setHasRequestedId(username.getText().toString());
			
			dialog.onEndProcessing();
			
			AlertUtil.showAlert(RegisterView.this.getContext(), R.string.register_successful_title, R.string.register_successful_message, R.string.ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialogInterface, int which)
				{
					setProcessingState(false);
					resetState();
					dialog.onComplete();					
				}
			});
		}
	}
	
	private void setProcessingState(boolean processing)
	{
		flipper.setDisplayedChild(processing ? 1 : 0);
		name.setEnabled(!processing);
		forename.setEnabled(!processing);
		username.setEnabled(!processing);
		hasAcceptedInformation.setEnabled(!processing);
	}
	
	private void resetState()
	{
		name.setText("");
		forename.setText("");
		username.setText("");
		hasAcceptedInformation.setChecked(true);
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