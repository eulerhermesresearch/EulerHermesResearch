/**
 *
 */

package com.eulerhermes.research.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import androidx.fragment.app.Fragment;
import com.yabeman.android.extended.net.ConnectionState;
import com.eulerhermes.research.R;
import com.eulerhermes.research.view.HListEmptyView;

public abstract class RequestFragment extends Fragment
{
    private View mProgress;
    private HListEmptyView mEmptyView;

    public RequestFragment()
    {
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        mProgress = view.findViewById(android.R.id.progress);
        mEmptyView = (HListEmptyView) view.findViewById(android.R.id.empty);

        mEmptyView.setButtonListener(new OnClickListener() {

            @Override
            public void onClick(View v)
            {
                performRequest();
            }
        });
    }

    protected void performRequest()
    {
        mProgress.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
    }

    protected void onLoaded()
    {
        mProgress.setVisibility(View.GONE);
    }

    protected void onError()
    {
        if (getActivity() != null)
        {
            if (ConnectionState.getInstance(getActivity()).isOnline())
            {
                mEmptyView.setText(R.string.request_error);
            }
            else
            {
                mEmptyView.setText(R.string.internet_error);
            }

            mProgress.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }
}