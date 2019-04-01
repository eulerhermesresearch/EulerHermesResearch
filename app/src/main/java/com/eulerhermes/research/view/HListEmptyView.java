package com.eulerhermes.research.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.eulerhermes.research.R;
import com.eulerhermes.research.fragment.HListFragment.IEmptyView;

public class HListEmptyView extends LinearLayout implements IEmptyView
{
	private TextView	textView;
	private Button		button;

	public HListEmptyView(Context context)
	{
		super(context);

		init(context);
	}

	public HListEmptyView(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		init(context);
	}

	public HListEmptyView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);

		init(context);
	}

	private void init(Context context)
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_hlist_empty_error_list, this, true);

		textView = (TextView) findViewById(R.id.textview);
		button = (Button) findViewById(R.id.button);
	}

	@Override
	public void setText(String text)
	{
		if (textView != null)
			textView.setText(text);
	}

	@Override
	public void setText(int text)
	{
		if (textView != null)
			textView.setText(text);
	}

	public void setButtonListener(OnClickListener listener)
	{
		if (button != null)
			button.setOnClickListener(listener);
	}
}
