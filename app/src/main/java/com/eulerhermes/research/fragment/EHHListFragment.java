package com.eulerhermes.research.fragment;

import android.view.View;
import android.view.View.OnClickListener;
import com.yabeman.android.extended.net.ConnectionState;
import com.eulerhermes.research.R;
import com.eulerhermes.research.view.HListEmptyView;

public abstract class EHHListFragment extends HListFragment {
    protected abstract void performRequest();

    protected View getEmptyView() {
        HListEmptyView view = new HListEmptyView(getActivity());
        view.setButtonListener(new OnClickListener() {
            public void onClick(View v) {
                EHHListFragment.this.onLoad();
                EHHListFragment.this.performRequest();
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
}
