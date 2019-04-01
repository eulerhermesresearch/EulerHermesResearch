/**
 *
 */

package com.eulerhermes.research.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.yabeman.android.extended.items.BaseType;
import com.yabeman.android.extended.util.AlertUtil;
import com.eulerhermes.research.R;
import com.eulerhermes.research.adapter.CategoryAdapter;
import com.eulerhermes.research.core.CoreUtil;
import com.eulerhermes.research.model.Doc;
import com.eulerhermes.research.model.Docs;
import com.eulerhermes.research.network.rest.CategoryDocsRequest;
import com.eulerhermes.research.util.RequestUtil;
import com.octo.android.robospice.Jackson2GoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

public class CategoryFragment extends EHGridFragment implements OnScrollListener
{
    private int[]						categories_images		= new int[] {
            R.drawable.doc_category_1_last,
            R.drawable.doc_category_2_last,
            R.drawable.doc_category_3_last,
            R.drawable.doc_category_4_last,
            R.drawable.doc_category_5_last				};

    private int[]						categories_descriptions	= new int[] {
            R.string.doc_category_1,
            R.string.doc_category_2,
            R.string.doc_category_3,
            R.string.doc_category_4,
            R.string.doc_category_5					};

    private final SpiceManager			spiceManager;
    private String						lastRequestCacheKey;
    private int							category;
    private CategoryAdapter				adapter;
    private Docs						docs;

    private View						mHeader;

    private String[]					items;
    private boolean[]					checkedItems;
    private boolean[]					tmpCheckedItems;

    private OnClickListener				positiveListener		= new OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            checkedItems = tmpCheckedItems;
            filter();
        }
    };

    private OnMultiChoiceClickListener	multiChoicelistener		= new OnMultiChoiceClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which, boolean isChecked)
        {
            tmpCheckedItems[which] = isChecked;
        }
    };

    public CategoryFragment()
    {
        spiceManager = new SpiceManager(Jackson2GoogleHttpClientSpiceService.class);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        Log.d("CategoryFragment", "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);

        adapter = new CategoryAdapter();
        setAdapter(adapter);

        category = getActivity().getIntent().getIntExtra("category", 0);

        mHeader = view.findViewById(R.id.header);

        final ImageView image = (ImageView) view.findViewById(R.id.image);
        final TextView desc = (TextView) view.findViewById(R.id.desc);
        final View border = view.findViewById(R.id.border);

        // category = [1 - 6]
        image.setImageResource(categories_images[category - 1]);

        desc.setText(categories_descriptions[category - 1]);

        border.setBackgroundResource(CoreUtil.getColorForCategory(category));

        getGridView().setOnScrollListener(this);

        ((StickyGridHeadersGridView) getGridView()).setAreHeadersSticky(false);

        performRequest();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        if (items != null && items.length > 0)
        {
            menu.add(Menu.NONE, R.id.menu_filter, 1, R.string.action_filter).setIcon(R.drawable.ic_action_labels)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.menu_filter)
        {
            displayFilterDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration myConfig)
    {
        super.onConfigurationChanged(myConfig);

        Log.d("CategoryFragment", "onConfigurationChanged: " + getColumnCount());
        getGridView().setNumColumns(getColumnCount());
    }

    @Override
    protected void onGridItemClick(GridView gridView, View view, int position, long id)
    {
        final Doc doc = (Doc) getAdapter().getItem(position);

        CoreUtil.openDoc(doc, getActivity());
    }

    @Override
    protected int getCustomLayout()
    {
        return R.layout.fragment_category;
    }

    @Override
    protected int getColumnCount()
    {
        Log.d("CategoryFragment", "getColumnCount: " + getResources().getInteger(R.integer.grid_column_count));
        return getResources().getInteger(R.integer.grid_column_count);
    }

    @Override
    public void onStart()
    {
        spiceManager.start(getActivity());
        super.onStart();
    }

    @Override
    public void onStop()
    {
        spiceManager.shouldStop();
        super.onStop();
    }

    protected SpiceManager getSpiceManager()
    {
        return spiceManager;
    }

    @Override
    protected void performRequest()
    {
        CategoryDocsRequest request = new CategoryDocsRequest(category);
        lastRequestCacheKey = request.createCacheKey();

        onLoad();
        spiceManager.getFromCacheAndLoadFromNetworkIfExpired(request, lastRequestCacheKey, RequestUtil.getCacheExpiration(), new CategoryListener());
    }

    @Override
    protected CategoryAdapter getAdapter()
    {
        return adapter;
    }

    private void displayFilterDialog()
    {
        tmpCheckedItems = Arrays.copyOf(checkedItems, checkedItems.length);

        AlertUtil.showAlert(getActivity(), R.string.action_filter, items, tmpCheckedItems, multiChoicelistener, R.string.ok, positiveListener,
                            R.string.cancel, null);
    }

    private void filter()
    {
        List<BaseType> data = new ArrayList<BaseType>();

        boolean allFalse = true;
        for (Doc doc : docs)
        {
            for (int i = 0, n = items.length; i < n; i++)
            {
                if (checkedItems[i] == true)
                {
                    allFalse = false;

                    if (doc.containsTag(items[i]))
                    {
                        data.add(doc);
                        break;
                    }
                }
            }
        }

        // If no filter selected
        // Then display all docs
        // (previous loop does nothing)
        if (allFalse == true)
        {
            data.addAll(docs);
        }

        getAdapter().setData(data);
        getAdapter().notifyDataSetChanged();
    }

    public final class CategoryListener implements RequestListener<Docs>
    {

        @Override
        public void onRequestFailure(SpiceException spiceException)
        {
            Log.d("CategoryFragment.CategoryListener", "onRequestFailure: ");
            onError();
        }

        @Override
        public void onRequestSuccess(final Docs docs)
        {
            // Reorder by date ASC
            Collections.reverse(docs);

            CategoryFragment.this.docs = docs;

            // Prepare filter arrays
            List<String> tags = new ArrayList<String>();

            String tag = null;
            for (int i = 0, n = docs.size(); i < n; i++)
            {
                String[] docTags = docs.get(i).getTagArray();

                for (int j = 0, m = docTags.length; j < m; j++)
                {
                    tag = docTags[j];

                    if (!tags.contains(tag))
                        tags.add(tag);
                }
            }

            Collections.sort(tags);

            items = new String[tags.size()];
            items = tags.toArray(items);
            checkedItems = new boolean[items.length];

            Arrays.fill(checkedItems, false);

            // Fill GridView
            List<BaseType> data = new ArrayList<BaseType>();
            data.addAll(docs);

            getAdapter().setData(data);
            getAdapter().notifyDataSetChanged();

            onLoaded();

            if (getActivity() != null)
                getActivity().invalidateOptionsMenu();
        }
    }

    // ___ Scroll Effect ___
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
        int scrollY = getScrollY();

        mHeader.setTranslationY(Math.max((int) -scrollY, -mHeader.getHeight()));
    }

    public int getScrollY()
    {
        if (getGridView() == null)
            return 0;

        View c = getGridView().getChildAt(1);
        if (c == null)
        {
            return 0;
        }

        int firstVisiblePosition = getGridView().getFirstVisiblePosition();
        int top = c.getTop();

        return -top + firstVisiblePosition * c.getHeight() + getGridView().getPaddingTop();
    }

    public static float clamp(float value, float max, float min)
    {
        return Math.max(Math.min(value, min), max);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    public void refresh()
    {
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }
}
