package com.eulerhermes.research.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import com.yabeman.android.extended.fragment.GridFragment.IEmptyView;
import com.eulerhermes.research.R;

public class EmptyView extends ScrollView implements IEmptyView
{
	private TextView	textView;
	private Button		button;

	public EmptyView(Context context)
	{
		super(context);

		init(context);
	}

	public EmptyView(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		init(context);
	}

	public EmptyView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);

		init(context);
	}

	private void init(Context context)
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_empty_error_list, this, true);

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
