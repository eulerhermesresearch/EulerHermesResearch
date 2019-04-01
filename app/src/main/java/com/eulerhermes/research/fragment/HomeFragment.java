package com.eulerhermes.research.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.GridView;
import android.widget.ImageButton;
import com.yabeman.android.extended.items.BaseType;
import com.eulerhermes.research.R;
import com.eulerhermes.research.adapter.HomeAdapter;
import com.eulerhermes.research.app.ContactActivity;
import com.eulerhermes.research.app.LoginActivity;
import com.eulerhermes.research.app.ProfileActivity;
import com.eulerhermes.research.app.WebActivity;
import com.eulerhermes.research.core.CorePrefs;
import com.eulerhermes.research.core.CoreUtil;
import com.eulerhermes.research.model.Doc;
import com.eulerhermes.research.model.Docs;
import com.eulerhermes.research.model.HomeHorizontalListHeader;
import com.eulerhermes.research.network.rest.DocAndVideosRequest;
import com.google.android.gms.common.Scopes;
import com.octo.android.robospice.Jackson2GoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import java.util.ArrayList;
import java.util.List;
import org.brickred.socialauth.AuthProvider;

public class HomeFragment extends EHPagedGridFragment implements IRefreshFragment {
    public static final int LINKED_IN_LOGIN_CODE = 103;
    private HomeAdapter adapter;
    private Docs docs;
    private String[] items;
    private LoginFragmentDialog loginDialog;
    private int page = 1;
    private final SpiceManager spiceManager = new SpiceManager(Jackson2GoogleHttpClientSpiceService.class);

    public final class RecentsRequestListener implements RequestListener<Docs> {
        public void onRequestFailure(SpiceException spiceException) {
            HomeFragment.this.onError();
        }

        public void onRequestSuccess(Docs docs) {
            HomeFragment.this.docs = docs;
            List<BaseType> data = new ArrayList();
            data.addAll(docs);
            if (getAdapter().getCount() == 0) {
                data.add(0, new HomeHorizontalListHeader());
            }
            getAdapter().addAll(data);
            getAdapter().notifyDataSetChanged();
            onLoaded();
            endLoading();
            if (docs.size() == 0) {
                setNoMoreData(true);
            }
            if (getActivity() != null) {
                getActivity().invalidateOptionsMenu();
            }

            page += 1;
        }
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.adapter = new HomeAdapter(2);
        setAdapter(this.adapter);
        getGridView().setPadding(0, 0, 0, 0);
        getGridView().setVerticalSpacing(1);
        ImageButton contactButton = (ImageButton) view.findViewById(R.id.contact_button);
        ((ImageButton) view.findViewById(R.id.user_button)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (CorePrefs.isLoggedIn()) {
                    if (CoreUtil.isTablet()) {
                        ProfileFragment.newInstance().show(HomeFragment.this.getActivity().getSupportFragmentManager(), Scopes.PROFILE);
                    } else {
                        HomeFragment.this.startActivity(new Intent(HomeFragment.this.getActivity(), ProfileActivity.class));
                    }
                } else if (CoreUtil.isTablet()) {
                    HomeFragment.this.loginDialog = LoginFragmentDialog.newInstance();
                    HomeFragment.this.loginDialog.show(HomeFragment.this.getActivity().getSupportFragmentManager(), "login");
                } else {
                    HomeFragment.this.startActivity(new Intent(HomeFragment.this.getActivity(), LoginActivity.class));
                }
            }
        });
        contactButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                HomeFragment.this.startActivity(new Intent(HomeFragment.this.getActivity(), ContactActivity.class));
            }
        });
        onLoad();
        performRequest();
        if (!CorePrefs.hasShownMainCoachmark()) {
            final ViewStub coachmark1 = (ViewStub) view.findViewById(R.id.coachmark1);
            final ViewStub coachmark2 = (ViewStub) view.findViewById(R.id.coachmark2);
            View c1View = coachmark1.inflate();
            CorePrefs.setHasShownMainCoachmark();
            c1View.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    coachmark1.setVisibility(View.GONE);
                    final View c2View = coachmark2.inflate();
                    c2View.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            c2View.setVisibility(View.GONE);
                        }
                    });
                }
            });
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 103 && resultCode == -1) {
            String firstname = data.getStringExtra("firstName");
            String lastname = data.getStringExtra("lastName");
            String email = data.getStringExtra(AuthProvider.EMAIL);
            String accessToken = data.getStringExtra("accessToken");
            long expireDate = data.getExtras().getLong("expireDate");
            if (this.loginDialog != null) {
                this.loginDialog.onLinkedInLoginComplete(firstname, lastname, email);
            }
        }
    }

    protected void onGridItemClick(GridView gridView, View view, int position, long id) {
        Doc doc = (Doc) getAdapter().getItem(position);
        if (doc.isVideo()) {
            Intent intent = new Intent(getActivity(), WebActivity.class);
            intent.putExtra("url", doc.getLink());
            intent.putExtra("title", doc.getTitle());
            getActivity().startActivity(intent);
            return;
        }
        CoreUtil.openDoc(doc, getActivity());
    }

    protected View getEmptyView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.view_recents_empty_list, null);
    }

    protected int getCustomLayout() {
        return R.layout.fragment_home;
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
        DocAndVideosRequest request = new DocAndVideosRequest(this.page);
        this.spiceManager.getFromCacheAndLoadFromNetworkIfExpired(request, request.createCacheKey(), 3600000, new RecentsRequestListener());
    }

    protected HomeAdapter getAdapter() {
        return this.adapter;
    }

    public void refresh() {
        getAdapter().notifyDataSetChanged();
    }

    protected void loadMore() {
        startLoading();
        performRequest();
    }
}
