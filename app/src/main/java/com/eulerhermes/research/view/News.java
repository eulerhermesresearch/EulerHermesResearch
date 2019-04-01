package com.eulerhermes.research.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import com.eulerhermes.research.R;
import com.eulerhermes.research.app.WebActivity;
import com.eulerhermes.research.common.AnalyticsHelper;
import com.eulerhermes.research.core.CoreUtil;
import com.eulerhermes.research.core.IntentExtras;
import com.eulerhermes.research.fragment.CountryFragmentDialog;
import com.eulerhermes.research.model.Doc;
import com.eulerhermes.research.network.rest.rss.RssCategory;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Category;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Item;

public class News extends LinearLayout
{
	private final Item	item;
	private ImageView	image;
	private TextView	country;
	private TextView	title;
	private TextView	category;
	private TextView	date;
	private TextView	description;
	private Button		button;

	public News(Context context, Item item)
	{
		super(context);

		this.item = item;

		init(context);
	}

	public News(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		this.item = null;

		init(context);
	}

	public News(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);

		this.item = null;

		init(context);
	}

	private void init(Context context)
	{
		setOrientation(LinearLayout.VERTICAL);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_news, this, true);

		image = (ImageView) findViewById(R.id.image);
		country = (TextView) findViewById(R.id.country);
		title = (TextView) findViewById(R.id.title);
		category = (TextView) findViewById(R.id.category);
		date = (TextView) findViewById(R.id.date);
		description = (TextView) findViewById(R.id.description);
		button = ((Button) findViewById(R.id.button));
		
		findViewById(R.id.scrollview).setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) 
            {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().getParent().getParent().getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

		if (item != null)
			setData(item);
	}

	public void setData(final Item news)
	{
		image.setImageResource(CoreUtil.getCountryFlagForIso(news.getSource().getValue()));
		country.setText(CoreUtil.getCountryNameForIso(news.getSource().getValue()));
		title.setText(news.getTitle());
		category.setText(news.getAuthor());
		date.setText(CoreUtil.getFormattedDateFromItem(news));
		description.setText(news.getDescription().getValue());
		
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				sendAnalytics(news.getSource().getValue());
				
				Category category = (Category) news.getCategories().get(0); 
				
				if (RssCategory.isApp(category))
				{
					CountryFragmentDialog dialog = CountryFragmentDialog.newInstance(news.getSource().getValue());
					dialog.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "country");
				}
				else if (RssCategory.isPdf(category))
				{
					CoreUtil.openDoc(Doc.fromItem(item), getContext());
				}
				else if (RssCategory.isWeb(category))
				{
					Intent intent = new Intent(getContext(), WebActivity.class);
					intent.putExtra(IntentExtras.TITLE, item.getLink());
					intent.putExtra(IntentExtras.WEB_URL, item.getLink());
					getContext().startActivity(intent);
				}
			}
		});
	}
	
	private void sendAnalytics(String country)
	{
		AnalyticsHelper.event("ui_action", "news_viewed", country);
	}

	@Override
	protected void onAttachedToWindow()
	{
		super.onAttachedToWindow();
	}

}
