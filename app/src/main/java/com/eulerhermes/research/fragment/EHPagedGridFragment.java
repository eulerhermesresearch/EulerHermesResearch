package com.eulerhermes.research.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import com.yabeman.android.extended.adapter.MultiTypeAdapter;
import com.yabeman.android.extended.items.BaseType;
import com.yabeman.android.extended.net.ConnectionState;
import com.eulerhermes.research.R;
import com.eulerhermes.research.model.LoadingItem;
import com.eulerhermes.research.view.EmptyView;

public abstract class EHPagedGridFragment extends EHGridFragment implements OnScrollListener {
    private boolean isError;
    private boolean loading = true;
    private LoadingItem loadingObject = new LoadingItem();
    private boolean noMoreData;
    private boolean userScroll;

    protected abstract MultiTypeAdapter<BaseType> getAdapter();

    protected abstract void loadMore();

    protected abstract void performRequest();

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getGridView().setOnScrollListener(this);
    }

    protected View getEmptyView() {
        EmptyView view = new EmptyView(getActivity());
        view.setButtonListener(new OnClickListener() {
            public void onClick(View v) {
                EHPagedGridFragment.this.onLoad();
                EHPagedGridFragment.this.performRequest();
            }
        });
        return view;
    }

    protected void onError() {
        if (getActivity() != null) {
            if (ConnectionState.getInstance(getActivity()).isOnline().booleanValue()) {
                setErrorText(R.string.request_error);
            } else {
                setErrorText(R.string.internet_error);
            }
            onLoaded();
            super.onError();
        }
    }

    protected void startLoading() {
        this.loading = true;
        this.isError = false;
        getAdapter().add(this.loadingObject);
        getAdapter().notifyDataSetChanged();
    }

    protected void endLoading() {
        this.loading = false;
        if (getAdapter().getItemPosition(this.loadingObject) >= 0) {
            getAdapter().remove(this.loadingObject);
        }
        getAdapter().notifyDataSetChanged();
    }

    protected void setNoMoreData(boolean noMoreData) {
        this.noMoreData = noMoreData;
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (this.userScroll && !this.loading && !this.noMoreData && !this.isError) {
            if (firstVisibleItem + visibleItemCount >= totalItemCount + -5) {
                loadMore();
            }
        }
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.userScroll = scrollState != 0;
    }
}
