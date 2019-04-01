/**
 *
 */

package com.eulerhermes.research.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.eulerhermes.research.R;

public class HomeDocCategory extends android.widget.LinearLayout
{
	private final View		topBorder;
	private final View		leftBorder;
	private final TextView	title;
	private final ImageView	image;

	public HomeDocCategory(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.item_home_doc_category, this, true);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HomeDocCategory, 0, 0);
		String titleStr = a.getString(R.styleable.HomeDocCategory_title);
		int color = a.getColor(R.styleable.HomeDocCategory_borderColor, context.getResources().getColor(android.R.color.white));
		Drawable drawable = a.getDrawable(R.styleable.HomeDocCategory_image);
		a.recycle();

		topBorder = view.findViewById(R.id.top_border);
		leftBorder = view.findViewById(R.id.left_border);
		title = ((TextView) view.findViewById(R.id.title));
		image = (ImageView) view.findViewById(R.id.image);
		
		topBorder.setBackgroundColor(color);
		leftBorder.setBackgroundColor(color);
		title.setText(titleStr);
		
		image.setImageDrawable(drawable);
	}
}