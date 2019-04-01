package com.eulerhermes.research.fragment;

import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import com.yabeman.android.extended.adapter.AggregatedAdapter;
import com.yabeman.android.extended.items.Header;
import com.eulerhermes.research.R;
import com.eulerhermes.research.adapter.ContactUsAdapter;
import com.eulerhermes.research.adapter.ContactUsAdapter.ContactUsItem;
import com.eulerhermes.research.adapter.CountryDetailEHCAdapter;
import com.eulerhermes.research.adapter.CountryHeaderEHCAdapter;
import com.eulerhermes.research.adapter.holder.DocHolder;
import com.eulerhermes.research.app.IMainActivity;
import com.eulerhermes.research.core.CorePrefs;
import com.eulerhermes.research.core.CoreUtil;
import com.eulerhermes.research.model.CountryDetailEHC;
import com.eulerhermes.research.model.Doc;
import com.eulerhermes.research.network.rest.CountryDetailEHCRequest;
import com.eulerhermes.research.util.RequestUtil;
import com.octo.android.robospice.Jackson2GoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CountryEHCFragmentDialog extends EHGridFragmentDialog {
    private static final String KEY = "iso";
    private AggregatedAdapter adapter;
    private String iso;
    private String lastRequestCacheKey;
    private final SpiceManager spiceManager = new SpiceManager(Jackson2GoogleHttpClientSpiceService.class);

    private static class DocAdapter extends BaseAdapter {
        private List<Doc> items = new ArrayList();

        public void add(Doc doc) {
            this.items.add(doc);
        }

        public int getCount() {
            return this.items.size();
        }

        public Object getItem(int position) {
            return this.items.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doc_dialog, parent, false);
                convertView.setTag(new DocHolder((TextView) convertView.findViewById(R.id.title), (TextView) convertView.findViewById(R.id.desc), (TextView) convertView.findViewById(R.id.date)));
            }
            Doc doc = (Doc) getItem(position);
            DocHolder holder = (DocHolder) convertView.getTag();
            holder.title.setText(doc.getTitle());
            holder.desc.setText(doc.getDescription());
            if (doc.getPubDate() != null) {
                holder.date.setText(doc.getFormattedDateEHC());
            }
            float f = (!doc.isSecured() || CorePrefs.isLoggedIn()) ? 1.0f : 0.6f;
            convertView.setAlpha(f);
            return convertView;
        }
    }

    private static class HeaderAdapter extends BaseAdapter {
        private final Header header;

        public HeaderAdapter(Header header) {
            this.header = header;
        }

        public int getCount() {
            return 1;
        }

        public Object getItem(int position) {
            return Integer.valueOf(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false);
            }
            ((TextView) convertView).setText(this.header.name);
            return convertView;
        }
    }

    public final class CountryDetailRequestListener implements RequestListener<CountryDetailEHC> {
        public void onRequestFailure(SpiceException spiceException) {
            CountryEHCFragmentDialog.this.onError();
        }

        public void onRequestSuccess(CountryDetailEHC detail) {
            if (CountryEHCFragmentDialog.this.getActivity() != null) {
                CountryEHCFragmentDialog.this.getAdapter().clear();
                CountryEHCFragmentDialog.this.getAdapter().add(new CountryHeaderEHCAdapter(detail));
                if (detail.getLatestReports().size() > 0) {
                    CountryEHCFragmentDialog.this.getAdapter().add(new HeaderAdapter(new Header(CountryEHCFragmentDialog.this.getString(R.string.latest_publications))));
                    DocAdapter docAdapter = new DocAdapter();
                    Iterator it = detail.getLatestReports().iterator();
                    while (it.hasNext()) {
                        docAdapter.add((Doc) it.next());
                    }
                    CountryEHCFragmentDialog.this.getAdapter().add(docAdapter);
                }
                CountryEHCFragmentDialog.this.getAdapter().add(new HeaderAdapter(new Header(CountryEHCFragmentDialog.this.getString(R.string.complexity_relating_to))));
                CountryEHCFragmentDialog.this.getAdapter().add(new CountryDetailEHCAdapter(detail));
                CountryEHCFragmentDialog.this.getAdapter().add(new ContactUsAdapter());
                CountryEHCFragmentDialog.this.getAdapter().notifyDataSetChanged();
                CountryEHCFragmentDialog.this.onLoaded();
            }
        }
    }

    public static CountryEHCFragmentDialog newInstance(String iso) {
        CountryEHCFragmentDialog cfd = new CountryEHCFragmentDialog();
        Bundle args = new Bundle();
        args.putString("iso", iso);
        cfd.setArguments(args);
        return cfd;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(1);
        dialog.setContentView(getCustomLayout());
        dialog.getWindow().setLayout(getResources().getDimensionPixelSize(R.dimen.max_dialog_width), getResources().getDimensionPixelSize(R.dimen.max_dialog_height));
        return dialog;
    }

    private void updateDimensions() {
        getDialog().getWindow().setLayout(getResources().getDimensionPixelSize(R.dimen.max_dialog_width), getResources().getDimensionPixelSize(R.dimen.max_dialog_height));
    }

    public void onConfigurationChanged(Configuration myConfig) {
        super.onConfigurationChanged(myConfig);
        updateDimensions();
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d("CountryFragmentDialog", "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);
        this.adapter = new AggregatedAdapter() {
            public int getViewTypeCount() {
                return 5;
            }
        };
        setAdapter(this.adapter);
        if (getArguments() != null) {
            this.iso = getArguments().getString("iso");
        }
        performRequest();
    }

    protected void onGridItemClick(GridView gridView, View view, int position, long id) {
        Object item = this.adapter.getItem(position);
        if (item instanceof Doc) {
            CoreUtil.openDoc((Doc) item, getActivity());
        } else if (item instanceof ContactUsItem) {
            ((IMainActivity) getActivity()).openContactFragmentMoreAboutEH();
            dismiss();
        }
    }

    protected int getCustomLayout() {
        return R.layout.grid;
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

    protected void performRequest() {
        CountryDetailEHCRequest request = new CountryDetailEHCRequest(this.iso);
        this.lastRequestCacheKey = request.createCacheKey();
        onLoad();
        this.spiceManager.getFromCacheAndLoadFromNetworkIfExpired(request, this.lastRequestCacheKey, RequestUtil.getCacheExpiration(), new CountryDetailRequestListener());
    }

    protected AggregatedAdapter getAdapter() {
        return this.adapter;
    }

    public void refresh() {
        if (getAdapter() != null) {
            getAdapter().notifyDataSetChanged();
        }
    }
}
