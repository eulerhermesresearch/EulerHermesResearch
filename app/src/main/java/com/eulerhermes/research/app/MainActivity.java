package com.eulerhermes.research.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.MenuItem.OnActionExpandListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.eulerhermes.research.BuildConfig;
import com.eulerhermes.research.R;
import com.eulerhermes.research.common.AnalyticsHelper;
import com.eulerhermes.research.core.CoreDevice;
import com.eulerhermes.research.core.CorePrefs;
import com.eulerhermes.research.core.CoreUtil;
import com.eulerhermes.research.core.IntentExtras;
import com.eulerhermes.research.fragment.HomeFragment;
import com.eulerhermes.research.fragment.IDialogListener;
import com.eulerhermes.research.fragment.KnowingEachOtherBetterFragmentDialog;
import com.eulerhermes.research.fragment.SearchFragment;
import com.eulerhermes.research.gcm.PubliFlashRegisterRequest;
import com.eulerhermes.research.model.Doc;
import com.eulerhermes.research.model.Docs;
import com.eulerhermes.research.model.PubliFlashRegisterResult;
import com.eulerhermes.research.network.rest.SearchRequest;
import com.eulerhermes.research.network.rest.rss.RssSpiceService;
import com.eulerhermes.research.provider.EHSearchContract;
import com.eulerhermes.research.util.RequestUtil;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.j256.ormlite.field.FieldType;
import com.octo.android.robospice.Jackson2GoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import fr.nicolaspomepuy.discreetapprate.AppRate;
import fr.nicolaspomepuy.discreetapprate.AppRate.OnShowListener;
import fr.nicolaspomepuy.discreetapprate.RetryPolicy;

import java.util.Iterator;
import java.util.List;

public class MainActivity extends BaseActivity implements IMainActivity, IDialogListener, SearchView.OnQueryTextListener
{
    private static final String                DISCLAIMER_URL         = BuildConfig.SERVER_1 + "/client/web/mentions.htm";
    private static final String                FRAGMENT_TAG           = "MainActivityFragment";
    private static final String                OUR_TEAM_URL           = BuildConfig.CLIENT + "/economic-research/about-economic-research/Pages/about.aspx";
    private static final String                SELECTED_INDEX         = "MainActivitySelectedIndex";
    private static final String                SENDER_ID              = "399688982206";
    private static final String                SOLUTIONS_URL          = BuildConfig.CLIENT + "/products-solutions/Pages/default.aspx";
    private static final String                TRADE_CREDIT_URL       = BuildConfig.CLIENT + "/group/who-we-are/Pages/default.aspx";
    private static final String[]              URLS                   = new String[]{TRADE_CREDIT_URL, SOLUTIONS_URL, OUR_TEAM_URL, DISCLAIMER_URL};
    private HomeFragment          homeFragment;
    private String                lastRequestCacheKey;
    private DrawerLayout          mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayoutAdapter   mNavAdapter;
    private SpiceManager          mRSSCacheSpiceManager  = new SpiceManager(RssSpiceService.class);
    private SpiceManager          mRestCacheSpiceManager = new SpiceManager(Jackson2GoogleHttpClientSpiceService.class);
    private MenuItem              mSearchItem;
    private SearchView            mSearchView;
    private SpiceManager          mSpiceManager          = new SpiceManager(Jackson2GoogleHttpClientSpiceService.class);
    private String                regid;
    private AlertDialog           termsDialog;

    private static class DrawerLayoutAdapter extends ArrayAdapter<String>
    {
        private int selectedPosition = 0;

        public DrawerLayoutAdapter(Context context, int textViewResourceId, String[] objects)
        {
            super(context, textViewResourceId, objects);
        }

        public DrawerLayoutAdapter(Context context, int textViewResourceId, List<String> objects)
        {
            super(context, textViewResourceId, objects);
        }

        public DrawerLayoutAdapter(Context context, int resource, int textViewResourceId, String[] objects)
        {
            super(context, resource, textViewResourceId, objects);
        }

        public DrawerLayoutAdapter(Context context, int resource, int textViewResourceId, List<String> objects)
        {
            super(context, resource, textViewResourceId, objects);
        }

        public DrawerLayoutAdapter(Context context, int resource, int textViewResourceId)
        {
            super(context, resource, textViewResourceId);
        }

        public DrawerLayoutAdapter(Context context, int textViewResourceId)
        {
            super(context, textViewResourceId);
        }

        public void setSelectedPosition(int position)
        {
            this.selectedPosition = position;
        }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drawer_layout, parent, false);
                convertView.setTag(new ViewHolder((ImageView) convertView.findViewById(R.id.icon), (TextView) convertView.findViewById(R.id.primary_text)));
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.text.setText((CharSequence) getItem(position));
            holder.image.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
            return convertView;
        }
    }

    public static class ViewHolder
    {
        public final ImageView image;
        public final TextView  text;

        public ViewHolder(ImageView image, TextView text)
        {
            this.image = image;
            this.text = text;
        }
    }

    public final class SearchListener implements RequestListener<Docs>
    {
        public void onRequestFailure(SpiceException spiceException)
        {
//            Log.d("CategoryFragment.CategoryListener", "onRequestFailure: ");
        }

        public void onRequestSuccess(Docs docs)
        {
            MatrixCursor cursor = new MatrixCursor(new String[]{FieldType.FOREIGN_ID_FIELD_SUFFIX, "category", "description", "docId", "iso", "name", "pudDate", "tags", "title", "isSecured"});
            Iterator     it     = docs.iterator();
            while (it.hasNext()) {
                Doc doc = (Doc) it.next();
                cursor.newRow().add(Integer.valueOf(docs.indexOf(doc))).add(doc.getCategory()).add(doc.getDescription()).add(doc.getDocId()).add(doc.getIso()).add(doc.getName()).add(doc.getPubDate()).add(doc.getTags()).add(doc.getTitle()).add(Integer.valueOf(doc.isSecured() ? 1 : 0));
            }
            MainActivity.this.mSearchView.getSuggestionsAdapter().changeCursor(cursor);
        }
    }

    public final class ServerRegisterListener implements RequestListener<PubliFlashRegisterResult>
    {
        public void onRequestFailure(SpiceException spiceException)
        {
//            Log.d("MainAc.RssRequestListener", "onRequestFailure: " + spiceException);
        }

        public void onRequestSuccess(PubliFlashRegisterResult result)
        {
//            Log.d("MainAc.ServerRegisterListener", "onRequestSuccess: " + result);
//            Log.d("MainAc.ServerRegisterListener", "onRequestSuccess: " + result.getD());
        }
    }

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(2);
        setContentView(R.layout.activity_main);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setLogo(R.drawable.ic_menu);
        getActionBar().setIcon(R.drawable.ic_menu);
        setTitle("");

        this.mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.mDrawerToggle = new ActionBarDrawerToggle(this, this.mDrawer, R.string.drawer_open, R.string.drawer_close)
        {
            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        this.mDrawer.setDrawerListener(this.mDrawerToggle);
        this.mDrawer.setScrimColor(-1157627904);
        this.mNavAdapter = new DrawerLayoutAdapter(getActionBar().getThemedContext(), (int) R.layout.item_drawer_layout, getResources().getStringArray(R.array.nav_drawer_labels));
        ListView navList = (ListView) findViewById(R.id.drawer);
        navList.setAdapter(this.mNavAdapter);
        navList.setSelector(R.drawable.nav_drawer_selector);
        navList.setOnItemClickListener(new OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id)
            {
                if (pos > 0) {
                    Intent intent = new Intent(MainActivity.this, WebActivity.class);
                    intent.putExtra("title", "");
                    intent.putExtra("url", MainActivity.URLS[pos - 1]);
                    MainActivity.this.startActivity(intent);
                }
                MainActivity.this.mDrawer.closeDrawer(Gravity.LEFT);
            }
        });
        int          finalWidth = Math.min(getResources().getDimensionPixelSize(R.dimen.max_drawer_layout_width), Math.min(CoreDevice.getDeviceWidth(), CoreDevice.getDeviceHeight()) - getResources().getDimensionPixelSize(R.dimen.dip_64));
        LayoutParams params     = ((View) navList.getParent()).getLayoutParams();
        params.width = finalWidth;
        ((View) navList.getParent()).setLayoutParams(params);
        if (savedInstanceState == null) {
            Log.d("MainActivity", "onCreate: noinstancestate");
            updateContent(0);
        }
        else {
            Log.d("MainActivity", "onCreate: has instance state");
            if (savedInstanceState.containsKey(IntentExtras.MAIN_POSITION)) {
                updateContent(savedInstanceState.getInt(IntentExtras.MAIN_POSITION));
            }
        }
//        registerGCM();
        initRatingBar(false);
    }

    private void initRatingBar(boolean force)
    {
        AppRate appRate = AppRate.with(this).text(R.string.rate_app).retryPolicy(RetryPolicy.EXPONENTIAL).initialLaunchCount(5).listener(new OnShowListener()
        {
            @Override
            public void onRateAppShowing()
            {

            }

            @Override
            public void onRateAppDismissed()
            {
                showKnowingEachOtherBetter();
            }

            @Override
            public void onRateAppClicked()
            {
                showKnowingEachOtherBetter();
            }
        });
        if (force) {
            appRate.forceShow();
        }
        else {
            appRate.checkAndShow();
        }
    }

    private void showKnowingEachOtherBetter()
    {
        Log.d("MainActivity", "showKnowingEachOtherBetter: ");
        if (CorePrefs.isLoggedIn()) {
            KnowingEachOtherBetterFragmentDialog.newInstance().show(getSupportFragmentManager(), "knowingEachOtherBetterFragmentDialog");
        }
    }

    public void openFragment(int fragmentIndex)
    {
        updateContent(fragmentIndex);
    }

    public void openContactFragmentMoreAboutEH()
    {
        updateContent(8, true, false);
    }

    public void openContactFragmentChangePassword()
    {
        updateContent(8, false, true);
    }

    private void updateContent(int selection)
    {
        updateContent(selection, false, false);
    }

    private void updateContent(int selection, boolean moreAboutEH, boolean changePassword)
    {
        this.homeFragment = HomeFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.main, this.homeFragment).commit();
        sendGoogleAnalytics();
    }

    private void sendGoogleAnalytics()
    {
        AnalyticsHelper.view(this, "Home");
    }

    private void displaySearchResults(String query)
    {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        if (fragment == null || !(fragment instanceof SearchFragment)) {
            SearchFragment searchFragment = SearchFragment.newInstance();
            searchFragment.setQuery(query);
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.main, searchFragment, FRAGMENT_TAG);
            tx.commitAllowingStateLoss();
            return;
        }
        ((SearchFragment) fragment).setQuery(query);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        Log.d("MainActivity", "onCreateOptionsMenu: ");
        getMenuInflater().inflate(R.menu.main_activity, menu);
        this.mSearchItem = menu.findItem(R.id.action_search);
        this.mSearchView = (SearchView) this.mSearchItem.getActionView();

        if (mSearchView != null) {
            this.mSearchView.setImeOptions(3);
            this.mSearchItem.setOnActionExpandListener(new OnActionExpandListener()
            {
                public boolean onMenuItemActionExpand(MenuItem item)
                {
                    return true;
                }

                public boolean onMenuItemActionCollapse(MenuItem item)
                {
                    return true;
                }
            });
            this.mSearchView.setOnQueryTextListener(this);
            this.mSearchView.setSuggestionsAdapter(new ResourceCursorAdapter(getActionBar().getThemedContext(), android.R.layout.simple_dropdown_item_1line, null, 0)
            {
                public void bindView(View view, Context arg1, Cursor cursor)
                {
                    ((TextView) view.findViewById(android.R.id.text1)).setText(cursor.getString(8));
                }
            });
            this.mSearchView.setOnSuggestionListener(new SearchView.OnSuggestionListener()
            {
                public boolean onSuggestionSelect(int position)
                {
                    return false;
                }

                public boolean onSuggestionClick(int position)
                {
                    boolean z   = true;
                    Cursor  c   = (Cursor) MainActivity.this.mSearchView.getSuggestionsAdapter().getItem(position);
                    Doc     doc = new Doc();
                    doc.setCategory(c.getString(1));
                    doc.setDescription(c.getString(2));
                    doc.setDocId(c.getString(3));
                    doc.setIso(c.getString(4));
                    doc.setName(c.getString(5));
                    doc.setPubDate(c.getString(6));
                    doc.setTags(c.getString(7));
                    doc.setTitle(c.getString(8));
                    if (c.getInt(9) != 1) {
                        z = false;
                    }
                    doc.setSecured(z);
                    CoreUtil.openDoc(doc, MainActivity.this);
                    return false;
                }
            });
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() != android.R.id.home) {
            if (item.getItemId() == R.id.menu_clear_cache) {
                this.mRestCacheSpiceManager.removeAllDataFromCache();
                this.mRSSCacheSpiceManager.removeAllDataFromCache();
            }
            else if (item.getItemId() == R.id.menu_display_rate) {
                item.setChecked(!item.isChecked());
                if (item.isChecked()) {
                    initRatingBar(true);
                }
            }
            return super.onOptionsItemSelected(item);
        }
        else if (this.mDrawer.isDrawerOpen(Gravity.LEFT)) {
            this.mDrawer.closeDrawer(Gravity.LEFT);
            return true;
        }
        else {
            this.mDrawer.openDrawer(Gravity.LEFT);
            return true;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.homeFragment != null) {
            this.homeFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        this.mDrawerToggle.onConfigurationChanged(newConfig);
        if (this.termsDialog != null) {
            this.termsDialog.getWindow().setLayout(getResources().getDimensionPixelSize(R.dimen.max_dialog_width), getResources().getDimensionPixelSize(R.dimen.max_dialog_height));
        }
    }

    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        this.mDrawerToggle.syncState();
    }

    protected void onResume()
    {
        super.onResume();
        checkPlayServices();
    }

    protected void onStart()
    {
        this.mSpiceManager.start(this);
        this.mRestCacheSpiceManager.start(this);
        this.mRSSCacheSpiceManager.start(this);
        super.onStart();
    }

    protected void onStop()
    {
        this.mSpiceManager.shouldStop();
        this.mRestCacheSpiceManager.shouldStop();
        this.mRSSCacheSpiceManager.shouldStop();
        super.onStop();
    }

    protected void onDestroy()
    {
        super.onDestroy();
        this.mDrawer = null;
        this.mDrawerToggle = null;
    }

    protected void onNewIntent(Intent intent)
    {
        if ("android.intent.action.SEARCH".equals(intent.getAction())) {
            displaySearchResults(intent.getStringExtra("query"));
            return;
        }
        int position = intent.getIntExtra(IntentExtras.MAIN_POSITION, -1);
        if (position != -1) {
            updateContent(position);
        }
    }

    private boolean checkPlayServices()
    {
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) != 0) {
            return false;
        }
        return true;
    }

    private void sendRegistrationIdToBackend()
    {
        this.mSpiceManager.execute(new PubliFlashRegisterRequest(CorePrefs.getGCMRegistrationId()), null, -1, new ServerRegisterListener());
    }

    public void onDialogActionComplete(int dialog)
    {
    }

    public boolean onQueryTextChange(String newText)
    {
        Log.d(EHSearchContract.PATH_SEARCHS, "query " + newText);
        if (newText.length() > 2) {
            performRequest(newText);
        }
        else {
            this.mSearchView.getSuggestionsAdapter().swapCursor(null);
        }
        return true;
    }

    public boolean onQueryTextSubmit(String query)
    {
        this.mSearchView.setQuery(query, false);
        return true;
    }

    private void performRequest(String query)
    {
        Log.d("mainactivity", "performRequest " + query);
        SearchRequest request = new SearchRequest(query);
        this.lastRequestCacheKey = request.createCacheKey();
        sendAnalytics(query);
        this.mSpiceManager.execute(request, this.lastRequestCacheKey, RequestUtil.getCacheExpiration(), new SearchListener());
    }

    private void sendAnalytics(String query)
    {
        AnalyticsHelper.event("ui_action", "search_submited");
    }
}
