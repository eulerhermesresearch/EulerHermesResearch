package com.eulerhermes.research.fragment;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import com.yabeman.android.extended.items.BaseType;
import com.yabeman.android.extended.util.AlertUtil;
import com.eulerhermes.research.R;
import com.eulerhermes.research.adapter.HomeAdapter;
import com.eulerhermes.research.core.CoreUtil;
import com.eulerhermes.research.model.Doc;
import com.eulerhermes.research.model.Docs;
import com.eulerhermes.research.network.rest.InfographicsRequest;
import com.eulerhermes.research.util.RequestUtil;
import com.octo.android.robospice.Jackson2GoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class InfographicsFragment extends EHPagedGridFragment {
    private static final String EXTRA_TYPE = "type";
    public static final String TYPE_INFOGRAPHICS = "infographic";
    public static final String TYPE_REPORTS = "report";
    private HomeAdapter adapter;
    private boolean[] checkedItems;
    private Docs docs;
    private String[] items;
    private String lastRequestCacheKey;
    private OnMultiChoiceClickListener multiChoicelistener = new OnMultiChoiceClickListener() {
        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
            InfographicsFragment.this.tmpCheckedItems[which] = isChecked;
        }
    };
    private int page = 1;
    private OnClickListener positiveListener = new OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            InfographicsFragment.this.checkedItems = InfographicsFragment.this.tmpCheckedItems;
            InfographicsFragment.this.filter();
        }
    };
    private final SpiceManager spiceManager = new SpiceManager(Jackson2GoogleHttpClientSpiceService.class);
    private boolean[] tmpCheckedItems;
    private String type;

    public final class CategoryListener implements RequestListener<Docs> {
        public void onRequestFailure(SpiceException spiceException) {
            InfographicsFragment.this.onError();
        }

        public void onRequestSuccess(Docs docs) {
            InfographicsFragment.this.docs = docs;
            List<String> tags = new ArrayList();
            int n = docs.size();
            for (int i = 0; i < n; i++) {
                for (String tag : ((Doc) docs.get(i)).getTagArray()) {
                    if (!tags.contains(tag)) {
                        tags.add(tag);
                    }
                }
            }
            Collections.sort(tags);
            InfographicsFragment.this.items = new String[tags.size()];
            InfographicsFragment.this.items = (String[]) tags.toArray(InfographicsFragment.this.items);
            InfographicsFragment.this.checkedItems = new boolean[InfographicsFragment.this.items.length];
            Arrays.fill(InfographicsFragment.this.checkedItems, false);
            List<BaseType> data = new ArrayList();
            data.addAll(docs);
            InfographicsFragment.this.getAdapter().addAll(data);
            InfographicsFragment.this.getAdapter().notifyDataSetChanged();
            InfographicsFragment.this.onLoaded();
            if (InfographicsFragment.this.getActivity() != null) {
                InfographicsFragment.this.getActivity().invalidateOptionsMenu();
            }
            InfographicsFragment infographicsFragment = InfographicsFragment.this;
            infographicsFragment.page = infographicsFragment.page + 1;
            if (docs.size() == 0) {
                InfographicsFragment.this.setNoMoreData(true);
            }
            InfographicsFragment.this.endLoading();
        }
    }

    public static InfographicsFragment newInstance(String type) {
        InfographicsFragment fragment = new InfographicsFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    public InfographicsFragment() {
        setHasOptionsMenu(true);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d("CategoryFragment", "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);
        this.type = getArguments().getString(EXTRA_TYPE);
        this.adapter = new HomeAdapter(2);
        setAdapter(this.adapter);
        onLoad();
        performRequest();
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (this.items != null && this.items.length > 0) {
            menu.add(0, R.id.menu_filter, 1, R.string.action_filter).setIcon(R.drawable.ic_filter_outline).setShowAsAction(2);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_filter) {
            displayFilterDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onConfigurationChanged(Configuration myConfig) {
        super.onConfigurationChanged(myConfig);
        Log.d("CategoryFragment", "onConfigurationChanged: " + getColumnCount());
        getGridView().setNumColumns(getColumnCount());
    }

    protected void onGridItemClick(GridView gridView, View view, int position, long id) {
        CoreUtil.openDoc((Doc) getAdapter().getItem(position), getActivity());
    }

    protected int getCustomLayout() {
        return R.layout.fragment_infographics_reports;
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

    protected SpiceManager getSpiceManager() {
        return this.spiceManager;
    }

    protected void performRequest() {
        Log.d("tt", "page " + this.page);
        InfographicsRequest request = new InfographicsRequest(this.page, this.type);
        this.lastRequestCacheKey = request.createCacheKey();
        this.spiceManager.getFromCacheAndLoadFromNetworkIfExpired(request, this.lastRequestCacheKey, RequestUtil.getCacheExpiration(), new CategoryListener());
    }

    protected HomeAdapter getAdapter() {
        return this.adapter;
    }

    private void displayFilterDialog() {
        this.tmpCheckedItems = Arrays.copyOf(this.checkedItems, this.checkedItems.length);
        AlertUtil.showAlert(getActivity(), R.string.action_filter, this.items, this.tmpCheckedItems, this.multiChoicelistener, R.string.ok, this.positiveListener, R.string.cancel, null);
    }

    private void filter() {
        List<BaseType> data = new ArrayList();
        boolean allFalse = true;
        Iterator it = this.docs.iterator();
        while (it.hasNext()) {
            Doc doc = (Doc) it.next();
            int n = this.items.length;
            for (int i = 0; i < n; i++) {
                if (this.checkedItems[i]) {
                    allFalse = false;
                    if (doc.containsTag(this.items[i])) {
                        data.add(doc);
                        break;
                    }
                }
            }
        }
        if (allFalse) {
            data.addAll(this.docs);
        }
        getAdapter().setData(data);
        getAdapter().notifyDataSetChanged();
    }

    public void refresh() {
        if (this.adapter != null) {
            this.adapter.notifyDataSetChanged();
        }
    }

    protected void loadMore() {
        startLoading();
        performRequest();
    }
}
