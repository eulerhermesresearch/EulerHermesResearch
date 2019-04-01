package com.eulerhermes.research.fragment;

import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import com.yabeman.android.extended.items.BaseType;
import com.yabeman.android.extended.items.Header;
import com.yabeman.android.extended.items.TitleTextItem;
import com.eulerhermes.research.R;
import com.eulerhermes.research.adapter.CountryAdapter;
import com.eulerhermes.research.core.CoreUtil;
import com.eulerhermes.research.model.CountryDetail;
import com.eulerhermes.research.model.Doc;
import com.eulerhermes.research.network.rest.CountryDetailRequest;
import com.eulerhermes.research.util.RequestUtil;
import com.octo.android.robospice.Jackson2GoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;
import java.util.Iterator;

public class CountryFragmentDialog extends EHGridFragmentDialog {
    private static final String KEY = "iso";
    private CountryAdapter adapter;
    private String iso;
    private String lastRequestCacheKey;
    private final SpiceManager spiceManager = new SpiceManager(Jackson2GoogleHttpClientSpiceService.class);

    public final class CountryDetailRequestListener implements RequestListener<CountryDetail> {
        public void onRequestFailure(SpiceException spiceException) {
            CountryFragmentDialog.this.onError();
        }

        public void onRequestSuccess(CountryDetail detail) {
            if (CountryFragmentDialog.this.getActivity() != null) {
                int n;
                int i;
                CountryFragmentDialog.this.getAdapter().setCountryDetail(detail);
                if (detail.getLatestReports().size() > 0) {
                    CountryFragmentDialog.this.getAdapter().add(new Header(CountryFragmentDialog.this.getString(R.string.latest_reports)));
                    Iterator it = detail.getLatestReports().iterator();
                    while (it.hasNext()) {
                        CountryFragmentDialog.this.getAdapter().add((Doc) it.next());
                    }
                }
                CountryFragmentDialog.this.getAdapter().add(detail);
                if (detail.getStrengths().size() > 0) {
                    String strengths = "";
                    n = detail.getStrengths().size();
                    for (i = 0; i < n; i++) {
                        strengths = new StringBuilder(String.valueOf(strengths)).append("• ").append((String) detail.getStrengths().get(i)).toString();
                        if (i < n - 1) {
                            strengths = new StringBuilder(String.valueOf(strengths)).append("\n").toString();
                        }
                    }
                    CountryFragmentDialog.this.getAdapter().add(new TitleTextItem(CountryFragmentDialog.this.getString(R.string.strengths), strengths));
                }
                if (detail.getWeaknesses().size() > 0) {
                    String weaknesses = "";
                    n = detail.getWeaknesses().size();
                    for (i = 0; i < n; i++) {
                        weaknesses = new StringBuilder(String.valueOf(weaknesses)).append("• ").append((String) detail.getWeaknesses().get(i)).toString();
                        if (i < n - 1) {
                            weaknesses = new StringBuilder(String.valueOf(weaknesses)).append("\n").toString();
                        }
                    }
                    CountryFragmentDialog.this.getAdapter().add(new TitleTextItem(CountryFragmentDialog.this.getString(R.string.weaknesses), weaknesses));
                }
                CountryFragmentDialog.this.getAdapter().notifyDataSetChanged();
                CountryFragmentDialog.this.onLoaded();
            }
        }
    }

    public static CountryFragmentDialog newInstance(String iso) {
        CountryFragmentDialog cfd = new CountryFragmentDialog();
        Bundle args = new Bundle();
        args.putString("iso", iso);
        cfd.setArguments(args);
        return cfd;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(1);
        dialog.setContentView(getCustomLayout());
        dialog.getWindow().setLayout(getResources().getDimensionPixelSize(R.dimen.max_dialog_width),getResources().getDimensionPixelSize(R.dimen.max_dialog_height));
        return dialog;
    }

    private void updateDimensions() {
        getDialog().getWindow().setLayout(getResources().getDimensionPixelSize(R.dimen.max_dialog_width), getResources().getDimensionPixelSize(R.dimen.max_dialog_height));
    }

    public void onConfigurationChanged(Configuration myConfig) {
        super.onConfigurationChanged(myConfig);
        Log.d("CountryFragmentDialog", "onConfigurationChanged: " + myConfig.screenHeightDp);
        updateDimensions();
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.adapter = new CountryAdapter(5);
        setAdapter(this.adapter);
        ((StickyGridHeadersGridView) getGridView()).setAreHeadersSticky(false);
        if (getArguments() != null) {
            this.iso = getArguments().getString("iso");
        }
        performRequest();
    }

    protected void onGridItemClick(GridView gridView, View view, int position, long id) {
        BaseType item = (BaseType) this.adapter.getItem(position);
        if (item instanceof Doc) {
            CoreUtil.openDoc((Doc) item, getActivity());
        }
    }

    protected int getCustomLayout() {
        return R.layout.grid_sticky_headers;
    }

    protected int getColumnCount() {
        return 1;
    }

    public void onStart() {
        this.spiceManager.start(getActivity());
        super.onStart();
    }

    public void onStop() {
        this.spiceManager.shouldStop();
        super.onStop();
    }

    public void refresh() {
        if (this.adapter != null) {
            this.adapter.notifyDataSetChanged();
        }
    }

    protected void performRequest() {
        CountryDetailRequest request = new CountryDetailRequest(this.iso);
        this.lastRequestCacheKey = request.createCacheKey();
        onLoad();
        this.spiceManager.getFromCacheAndLoadFromNetworkIfExpired(request, this.lastRequestCacheKey, RequestUtil.getCacheExpiration(), new CountryDetailRequestListener());
    }

    protected CountryAdapter getAdapter() {
        return this.adapter;
    }
}
