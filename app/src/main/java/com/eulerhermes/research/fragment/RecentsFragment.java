package com.eulerhermes.research.fragment;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import com.yabeman.android.extended.fragment.GridFragment;
import com.yabeman.android.extended.items.BaseType;
import com.yabeman.android.extended.util.AlertUtil;
import com.eulerhermes.research.R;
import com.eulerhermes.research.adapter.RecentsAdapter;
import com.eulerhermes.research.core.CoreUtil;
import com.eulerhermes.research.model.Doc;
import com.eulerhermes.research.model.Docs;
import com.eulerhermes.research.network.DatabaseSpiceService;
import com.eulerhermes.research.network.RecentsRequest;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RecentsFragment extends GridFragment implements IRefreshFragment {
    private RecentsAdapter adapter;
    private boolean[] checkedItems;
    private Docs docs;
    private String[] items;
    private OnMultiChoiceClickListener multiChoicelistener = new OnMultiChoiceClickListener() {
        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
            RecentsFragment.this.tmpCheckedItems[which] = isChecked;
        }
    };
    private OnClickListener positiveListener = new OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            RecentsFragment.this.checkedItems = RecentsFragment.this.tmpCheckedItems;
            RecentsFragment.this.filter();
        }
    };
    private final SpiceManager spiceManager = new SpiceManager(DatabaseSpiceService.class);
    private boolean[] tmpCheckedItems;

    public final class RecentsRequestListener implements RequestListener<Docs> {
        public void onRequestFailure(SpiceException spiceException) {
            Log.d("RecentsFragment.RecentsRequestListener", "onRequestFailure: ");
            RecentsFragment.this.onError();
        }

        public void onRequestSuccess(Docs docs) {
            RecentsFragment.this.docs = docs;
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
            RecentsFragment.this.items = new String[tags.size()];
            RecentsFragment.this.items = (String[]) tags.toArray(RecentsFragment.this.items);
            RecentsFragment.this.checkedItems = new boolean[RecentsFragment.this.items.length];
            Arrays.fill(RecentsFragment.this.checkedItems, false);
            List<BaseType> data = new ArrayList();
            data.addAll(docs);
            RecentsFragment.this.getAdapter().setData(data);
            RecentsFragment.this.getAdapter().notifyDataSetChanged();
            RecentsFragment.this.onLoaded();
            if (RecentsFragment.this.getActivity() != null) {
                RecentsFragment.this.getActivity().invalidateOptionsMenu();
            }
        }
    }

    public RecentsFragment() {
        setHasOptionsMenu(true);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.adapter = new RecentsAdapter();
        setAdapter(this.adapter);
        performRequest();
    }

    protected void onGridItemClick(GridView gridView, View view, int position, long id) {
        CoreUtil.openDoc((Doc) getAdapter().getItem(position), getActivity());
    }

    protected View getEmptyView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.view_recents_empty_list, null);
    }

    protected int getCustomLayout() {
        return R.layout.grid_sticky_headers;
    }

    protected int getColumnCount() {
        return getResources().getInteger(R.integer.grid_column_count);
    }

    public void onStart() {
        this.spiceManager.start(getActivity());
        super.onStart();
    }

    public void onStop() {
        this.spiceManager.shouldStop();
        super.onStop();
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (this.items != null && this.items.length > 0) {
            menu.add(0, R.id.menu_filter, 2, R.string.action_filter).setIcon(R.drawable.ic_action_labels).setShowAsAction(2);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_filter) {
            displayFilterDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    protected SpiceManager getSpiceManager() {
        return this.spiceManager;
    }

    private void performRequest() {
        RecentsRequest request = new RecentsRequest();
        onLoad();
        this.spiceManager.getFromCacheAndLoadFromNetworkIfExpired(request, "", 0, new RecentsRequestListener());
    }

    protected RecentsAdapter getAdapter() {
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
        getAdapter().notifyDataSetChanged();
    }
}
