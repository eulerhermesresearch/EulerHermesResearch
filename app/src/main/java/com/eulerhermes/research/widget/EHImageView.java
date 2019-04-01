package com.eulerhermes.research.widget;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;

public class EHImageView extends AppCompatImageView
{
	private Paint	maskPaint;
	private Path	path;

	public EHImageView(Context context)
	{
		super(context);
		init();
	}

	public EHImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public EHImageView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init();
	}

	private void init()
	{
		setWillNotDraw(false);
		maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		maskPaint.setStyle(Paint.Style.FILL);
		maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
		maskPaint.setColor(Color.TRANSPARENT);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);

		createMask(w, h);
	}

	private void createMask(int w, int h)
	{
		int sw = Math.round((float) (w / 6));

		path = new Path();
		path.moveTo(0, 0);
		path.lineTo(0, h);
		path.lineTo(w - sw, h);
		path.lineTo(w, h - sw);
		path.lineTo(w, 0);
		path.lineTo(0, 0);
		path.close();
//		path.setFillType(FillType.WINDING);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		canvas.save();
		super.onDraw(canvas);

		if (path != null)
			canvas.drawPath(path, maskPaint);
		
		canvas.restore();
	}
}
