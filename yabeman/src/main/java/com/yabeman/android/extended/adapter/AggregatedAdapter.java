package com.yabeman.android.extended.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

public class AggregatedAdapter extends BaseAdapter
{
	private static class IndexTranslation
	{
		private ListAdapter	targetAdapter;
		private int			translatedIndex;
		private int			translatedViewType;

		private IndexTranslation(ListAdapter listAdapter, int translatedIndex, int translatedViewType)
		{
			targetAdapter = listAdapter;
			this.translatedIndex = translatedIndex;
			this.translatedViewType = translatedViewType;
		}
	}

	private List<BaseAdapter>			mAdapters;
	private boolean						mCachedAllItemsEnabled;
	private int							mCachedCount;
	private boolean						mCachedHasStableIds;
	private ArrayList<IndexTranslation>	mCachedTranslations;
	
	public AggregatedAdapter()
	{
		mCachedCount = 0;
		mCachedAllItemsEnabled = true;
		mCachedHasStableIds = true;
		mAdapters = new ArrayList<BaseAdapter>();
	}

	public AggregatedAdapter(BaseAdapter abaseadapter[])
	{
		mCachedCount = 0;
		mCachedAllItemsEnabled = true;
		mCachedHasStableIds = true;
		mAdapters = new ArrayList<BaseAdapter>();
		for (BaseAdapter baseAdapter : abaseadapter)
		{
			mAdapters.add(baseAdapter);
		}
		
		refreshCachedData();
	}

	public AggregatedAdapter(List<BaseAdapter> abaseadapter)
	{
		mCachedCount = 0;
		mCachedAllItemsEnabled = true;
		mCachedHasStableIds = true;
		mAdapters = abaseadapter;
		
		refreshCachedData();
	}
	
	public void setAdapters(List<BaseAdapter> abaseadapter)
	{
		mCachedCount = 0;
		mCachedAllItemsEnabled = true;
		mCachedHasStableIds = true;
		mAdapters = abaseadapter;
	}
	
	public void add(BaseAdapter abaseadapter)
	{
		if (mAdapters == null)
			mAdapters = new ArrayList<BaseAdapter>();
		
		mAdapters.add(abaseadapter);
	}
	
	public void clear()
	{
		mAdapters = new ArrayList<BaseAdapter>();
	}

	private void refreshCachedData()
	{
		int n = mAdapters.size();
		int translatedIndex = 0;
		int translatedViewType = 0;

		mCachedAllItemsEnabled = true;
		mCachedHasStableIds = true;
		mCachedTranslations = new ArrayList<IndexTranslation>(n * 3);
		
		@SuppressWarnings("rawtypes")
		HashMap<Class, Integer> map = new HashMap<Class, Integer>();

		for (int i = 0; i < n; i++)
		{
			BaseAdapter baseAdapter = mAdapters.get(i);
			
			if (map.containsKey(baseAdapter.getClass()) == false)
			{
				map.put(baseAdapter.getClass(), translatedViewType);
				translatedViewType += baseAdapter.getViewTypeCount();
			}
			
			int baseViewType = map.get(baseAdapter.getClass()); 

			mCachedAllItemsEnabled = mCachedAllItemsEnabled && baseAdapter.areAllItemsEnabled();
			mCachedHasStableIds = mCachedHasStableIds && baseAdapter.hasStableIds();

			int m = baseAdapter.getCount();
			for (int j = 0; j < m; j++)
			{
				IndexTranslation indextranslation = new IndexTranslation(baseAdapter, j, baseAdapter.getItemViewType(j) + baseViewType);
				mCachedTranslations.add(indextranslation);
			}

			translatedIndex += m;
		}

		mCachedCount = translatedIndex;
	}

	private IndexTranslation translate(int i)
	{
		return mCachedTranslations.get(i);
	}

	@Override
	public boolean areAllItemsEnabled()
	{
		return mCachedAllItemsEnabled;
	}

	public void dumpState()
	{
		Log.d("AggregattedAdapter", "****** AGGREGATED ADAPTER START ******");
		StringBuilder stringbuilder = new StringBuilder("Total items: ");
		stringbuilder.append(getCount());
		stringbuilder.append(" [ ");
		
		int i = mAdapters.size();
		for (int j = 0; j < i; j++)
		{
			stringbuilder.append(mAdapters.get(j).getCount());
			stringbuilder.append(" ");
		}

		stringbuilder.append("]");
		Log.d("AggregattedAdapter", stringbuilder.toString());
		StringBuilder stringbuilder1 = new StringBuilder("Index translation: ");
		for (int k = 0; k < getCount(); k++)
		{
			stringbuilder1.append(k);
			stringbuilder1.append(":");
			stringbuilder1.append(getItemViewType(k));
			stringbuilder1.append(" ");
		}

		Log.d("AggregattedAdapter", stringbuilder1.toString());
		Log.d("AggregattedAdapter", "****** AGGREGATED ADAPTER  END  ******");
	}

	public List<BaseAdapter> getAdapters()
	{
		return mAdapters;
	}

	@Override
	public int getCount()
	{
		return mCachedCount;
	}

	@Override
	public Object getItem(int i)
	{
		IndexTranslation indextranslation = translate(i);
		return indextranslation.targetAdapter.getItem(indextranslation.translatedIndex);
	}

	@Override
	public long getItemId(int i)
	{
		return i;
//		IndexTranslation indextranslation = translate(i);
//		return indextranslation.targetAdapter.getItemId(indextranslation.translatedIndex);
	}

	@Override
	public int getItemViewType(int i)
	{
		IndexTranslation indextranslation = translate(i);
		Log.d("AggregatedAdapter", "getItemViewType: " + i + " " + indextranslation.translatedViewType);
		return indextranslation.translatedViewType;
	}

	@Override
	public int getViewTypeCount()
	{
		int n = 0;
		
		List<Class<? extends BaseAdapter>> classes = new ArrayList<Class<? extends BaseAdapter>>();
		
		for (BaseAdapter adapter : mAdapters)
		{
			if (classes.indexOf(adapter.getClass()) == -1)
			{
				n += adapter.getViewTypeCount();
				classes.add(adapter.getClass());
			}
		}
		
		Log.d("AggregatedAdapter", "getViewTypeCount: " + n);
		
		return Math.max(n, 1);
	}

	@Override
	public View getView(int i, View view, ViewGroup viewgroup)
	{
		IndexTranslation indextranslation = translate(i);
		Log.d("AggregatedAdapter", "getView: " + i + " " + indextranslation.targetAdapter);
		return indextranslation.targetAdapter.getView(indextranslation.translatedIndex, view, viewgroup);
	}

	@Override
	public boolean hasStableIds()
	{
		return mCachedHasStableIds;
	}

	@Override
	public boolean isEmpty()
	{
		return mCachedCount == 0;
	}

	@Override
	public boolean isEnabled(int i)
	{
		IndexTranslation indextranslation = translate(i);
		return indextranslation.targetAdapter.isEnabled(indextranslation.translatedIndex);
	}

	@Override
	public void notifyDataSetChanged()
	{
		refreshCachedData();
		super.notifyDataSetChanged();
	}

	@Override
	public void notifyDataSetInvalidated()
	{
		super.notifyDataSetChanged();
	}
}
