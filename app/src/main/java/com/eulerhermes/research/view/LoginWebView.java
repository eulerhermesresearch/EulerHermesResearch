package com.eulerhermes.research.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * @author AmalChandran(Novasys)
 * **/
public class LoginWebView extends WebView
{

	public LoginWebView(Context context)
	{
		this(context, null);
	}

	public LoginWebView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public LoginWebView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	/* @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	      your code here 
	 	Log.e("onKeyDown", "KeyCode: "+keyCode);
	     return super.onKeyDown(keyCode, event);
	 }*/
	
	// Note this!
	@Override
	public boolean onCheckIsTextEditor()
	{
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		switch (ev.getAction())
		{
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_UP:
				if (!hasFocus())
				{
					requestFocus();
				}
				break;
		}

		return super.onTouchEvent(ev);
	}
}