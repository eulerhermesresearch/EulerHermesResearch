/**
 *
 */

package com.eulerhermes.research.fragment;

import androidx.fragment.app.Fragment;
import it.sephiroth.android.library.widget.AdapterView;
import it.sephiroth.android.library.widget.HListView;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ListAdapter;
import com.eulerhermes.research.R;

public abstract class HListFragment extends Fragment
{
    public static interface IEmptyView
    {
        public void setText(String text);

        public void setText(int text);
    }

    private HListView	listView;
    private View		loadingView;
    private boolean		gridVisible;
    private int			errorText	= -1;
    private int			emptyText	= -1;

    public HListFragment()
    {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(getCustomLayout(), container, false);

        listView = (HListView) view.findViewById(android.R.id.list);

        View emptyView = getEmptyView();

        if (emptyView != null)
        {
            emptyView.setId(android.R.id.empty);

            ((ViewGroup) listView.getParent()).addView(emptyView);

            listView.setEmptyView(emptyView);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        if (listView == null)
            throw new IllegalStateException("GridFragment : There is no HListView with id \"list\" defined in the layout");

        listView.setDrawSelectorOnTop(true);

        ((View) listView.getParent()).setVisibility(View.GONE);
        loadingView = view.findViewById(android.R.id.progress);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long id)
            {
                onListItemClick((HListView) listView, view, position, id);
            }
        });
    }

    protected abstract void onListItemClick(HListView HListView, View view, int position, long id);

    @Override
    public void onConfigurationChanged(Configuration myConfig)
    {
        super.onConfigurationChanged(myConfig);

        if (getListView() != null)
        {
            int pos = getListView().getFirstVisiblePosition();
            getListView().setSelection(pos);
        }
    }

    protected HListView getListView()
    {
        if (listView == null && getView() != null)
            listView = (HListView) getView().findViewById(R.id.grid);

        return listView;
    }

    protected ListAdapter getAdapter()
    {
        if (getListView() != null && getListView().getAdapter() != null)
        {
            return getListView().getAdapter();
        }

        return null;
    }

    protected void setAdapter(ListAdapter adapter)
    {
        if (getListView() != null)
            getListView().setAdapter(adapter);
    }

    /**
     * Create, add and set the list empty view Override this method if you want
     * a custom empty view, or if you replaced listview by a HListView for
     * example
     */
    protected abstract View getEmptyView();

    /**
     * Override this method to define this activity's layout. Default layout is
     * the default ListFragment layout : a list, an empty TextView, a
     * ProgressBar
     *
     * @return the activity's layout. Example : <code>R.layout.main_activity</code>
     */
    protected abstract int getCustomLayout();

    protected void setGridVisible(boolean visible)
    {
        setGridVisibility(visible, true);
    }

    private void setGridVisibility(boolean visible, boolean animate)
    {
        if (gridVisible == visible)
        {
            return;
        }

        gridVisible = visible;

        View parent = (View) getListView().getParent();

        if (visible)
        {
            if (animate)
            {
                loadingView.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
                parent.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
            }
            else
            {
                loadingView.clearAnimation();
                parent.clearAnimation();
            }
            loadingView.setVisibility(View.GONE);

            if (parent != null)
                parent.setVisibility(View.VISIBLE);
        }
        else
        {
            if (animate)
            {
                loadingView.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
                parent.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
            }
            else
            {
                loadingView.clearAnimation();
                parent.clearAnimation();
            }
            loadingView.setVisibility(View.VISIBLE);

            if (parent != null)
                parent.setVisibility(View.GONE);
        }
    }

    protected void setEmptyText(int resId)
    {
        emptyText = resId;
        setText(emptyText);
    }

    protected void setErrorText(int resId)
    {
        errorText = resId;
    }

    private void setText(int resId)
    {
        if (resId != -1)
        {
            final View emptyView = getListView().getEmptyView();
            if (emptyView != null && emptyView instanceof IEmptyView)
            {
                ((IEmptyView) emptyView).setText(resId);
            }
        }
    }

    protected void onLoad()
    {
        setText(emptyText);
        setGridVisible(false);
    }

    protected void onLoaded()
    {
        setGridVisible(true);
    }

    protected void onError()
    {
        setText(errorText);
    }
}
