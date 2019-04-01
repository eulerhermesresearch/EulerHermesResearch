package com.eulerhermes.research.view;

import org.apache.commons.lang3.StringUtils;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.eulerhermes.research.R;
import com.eulerhermes.research.core.CorePrefs;
import com.eulerhermes.research.fragment.ILoginDialog;
import com.eulerhermes.research.model.User;

public class ProfileView extends LinearLayout
{
	private ILoginDialog dialog;
	
	public ProfileView(Context context, ILoginDialog dialog)
	{
		super(context);
		
		this.dialog = dialog;
		
		init(context);
	}

	public ProfileView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		init(context);
	}

	public ProfileView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		
		init(context);
	}

	private void init(Context context)
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_profile, this, true);

		final TextView firstName = (TextView) findViewById(R.id.first_name);
		final TextView lastName = (TextView) findViewById(R.id.last_name);
		final TextView email = (TextView) findViewById(R.id.email);
		
		User user = CorePrefs.getUser();
		
		firstName.setText(StringUtils.capitalize(user.getForeName()));
		lastName.setText(user.getName().toUpperCase());
		email.setText(user.getUsername());
		
		findViewById(R.id.logout).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v)
			{
				CorePrefs.destroyUser();
				dialog.onComplete();
				Toast.makeText(getContext(), R.string.logged_out, Toast.LENGTH_SHORT).show();
			}
		});
	}
}
