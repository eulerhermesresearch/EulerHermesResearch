package com.artifex.mupdfdemo;

import java.io.InputStream;
import java.util.concurrent.Executor;

import com.eulerhermes.research.BuildConfig;
import com.eulerhermes.research.common.AnalyticsHelper;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.eulerhermes.research.R;
import com.eulerhermes.research.core.CorePaths;
import com.eulerhermes.research.core.IntentExtras;
import com.eulerhermes.research.model.Doc;
import com.eulerhermes.research.network.DatabaseSpiceService;
import com.eulerhermes.research.network.FileSpiceService;
import com.eulerhermes.research.network.PdfDocRequest;
import com.eulerhermes.research.view.EmptyView;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.exception.RequestCancelledException;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.exception.CacheSavingException;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.listener.RequestProgress;
import com.octo.android.robospice.request.listener.RequestProgressListener;

class ThreadPerTaskExecutor implements Executor
{
	public void execute(Runnable r)
	{
		new Thread(r).start();
	}
}

public class MuPDFActivity extends Activity
{
	private static final String	LEGEND_PDF_FILE_NAME	= "risk_map_legend.pdf";

	private MuPDFCore			core;
	private String				mFileName;
	private int					mPage					= 0;
	private MuPDFReaderView		mDocView;
	private ViewGroup			mContainer;
	private ProgressBar			mProgress;
	private EmptyView			mEmptyView;

	private Doc					doc;
	private PdfDocRequest		request;
	private boolean				openRiskMapLegend		= false;

	protected boolean			loaded					= false;
	protected SpiceManager		fileSpiceManager		= new SpiceManager(FileSpiceService.class);
	protected SpiceManager		dbSpiceManager			= new SpiceManager(DatabaseSpiceService.class);

	private MuPDFCore openFile(String path)
	{
		int lastSlashPos = path.lastIndexOf('/');
		mFileName = new String(lastSlashPos == -1 ? path : path.substring(lastSlashPos + 1));

		try
		{
			core = new MuPDFCore(this, path);
			// New file: drop the old outline data
			OutlineActivityData.set(null);
		}
		catch (Exception e)
		{
			Log.d("MuPDFActivity", "openFile: " + e);
			onError();
			return null;
		}
		
		return core;
	}

	private MuPDFCore openBuffer(byte buffer[])
	{
		try
		{
			core = new MuPDFCore(this, buffer);
			// New file: drop the old outline data
			OutlineActivityData.set(null);
		}
		catch (Exception e)
		{
			Log.d("MuPDFActivity", "openBuffer: " + e);
			onError();
			return null;
		}
		return core;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		Log.d("MuPDFActivity", "onCreate: ");
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		setContentView(R.layout.activity_mupdf);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setLogo(R.drawable.ic_arrow_left);

		doc = getIntent().getParcelableExtra(IntentExtras.PDF_DOC);
		openRiskMapLegend = getIntent().getBooleanExtra(IntentExtras.PDF_LEGEND, false);

		mContainer = (ViewGroup) findViewById(R.id.pdf_container);
		mProgress = (ProgressBar) findViewById(android.R.id.progress);
		mEmptyView = (EmptyView) findViewById(android.R.id.empty);

		mEmptyView.setText(R.string.cannot_open_document);
		mEmptyView.setButtonListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				loadPdf();
			}
		});
		
		Log.d("MuPDFActivity", "onCreate: " + core);
		setupShare();
		
		if (doc != null)
		{
			sendAnalytics(doc.getTitle());
		}

		if (core == null)
		{
			Log.d("MuPDFActivity", "onCreate: core = null");
			core = (MuPDFCore) getLastNonConfigurationInstance();

			if (savedInstanceState != null && savedInstanceState.containsKey("FileName"))
			{
				mFileName = savedInstanceState.getString("FileName");
				mPage = savedInstanceState.getInt("page");
				Log.d("MuPDFActivity", "onCreate: savedInstance page = " + mPage);
				createUI();
			}
		}

		if (core == null)
		{
			Log.d("MuPDFActivity", "onCreate: core = null2");
			loadPdf();
		}
	}
	
	private void sendAnalytics(String doc)
	{
		AnalyticsHelper.event("ui_action", "PDF_opened", doc);
	}

	private void loadPdf()
	{
		mProgress.setVisibility(View.VISIBLE);
		mContainer.setVisibility(View.GONE);
		mEmptyView.setVisibility(View.GONE);

		if (doc != null)
		{
			setTitle(doc.getTitle());

			performRequest();
		}
		else if (openRiskMapLegend)
		{
			setTitle(R.string.risk_map_legend);

			displayPdf();
		}
	}

	private void onError()
	{
		mProgress.setVisibility(View.GONE);
		mContainer.setVisibility(View.GONE);
		mEmptyView.setVisibility(View.VISIBLE);
	}

	private void performRequest()
	{
		request = new PdfDocRequest(doc);
		fileSpiceManager.execute(request, doc.getName(), DurationInMillis.ALWAYS_RETURNED, new ImageRequestListener());
	}

	private void displayPdf()
	{
		if (!openRiskMapLegend)
		{
			core = openFile(Uri.decode(request.getCacheFile().getPath()));
		}
		else
		{
			try
			{
				InputStream is = getResources().getAssets().open(LEGEND_PDF_FILE_NAME);
				int len = is.available();
				byte[] buffer = new byte[len];
				is.read(buffer, 0, len);
				is.close();
				openBuffer(buffer);
			}
			catch (OutOfMemoryError e)
			{
				Log.d("MuPDFActivity", "displayPdf: " + e);
				onError();
			}
			catch (Exception e)
			{
				Log.d("MuPDFActivity", "displayPdf: " + e);
				onError();
			}
		}

		createUI();
	}

	private void saveToRecents()
	{
		try
		{
			dbSpiceManager.putDataInCache(doc.getName(), doc);
		}
		catch (CacheSavingException e)
		{
			e.printStackTrace();
		}
		catch (CacheCreationException e)
		{
			e.printStackTrace();
		}
	}

	private void hideProgressBar()
	{
		if (mProgress != null)
		{
			mProgress.setVisibility(View.GONE);
		}
	}

	private class ImageRequestListener implements RequestListener<InputStream>, RequestProgressListener
	{

		@Override
		public void onRequestFailure(SpiceException arg0)
		{
			if (!(arg0 instanceof RequestCancelledException))
			{
				onError();
			}
		}

		@Override
		public void onRequestSuccess(InputStream inputStream)
		{
			displayPdf();
			saveToRecents();
		}

		@Override
		public void onRequestProgressUpdate(RequestProgress progress)
		{

		}
	}

	public void createUI()
	{
		if (core == null)
			return;

		mDocView = new MuPDFReaderView(this);
		mDocView.setAdapter(new MuPDFPageAdapter(this, core));
		mDocView.setDisplayedViewIndex(mPage);
		mContainer.addView(mDocView);

		mContainer.setVisibility(View.VISIBLE);
		hideProgressBar();

	}

	public Object onRetainNonConfigurationInstance()
	{
		MuPDFCore mycore = core;
		core = null;
		return mycore;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);

		if (mFileName != null && mDocView != null)
		{
			outState.putString("FileName", mFileName);
			outState.putInt("page", mDocView.getDisplayedViewIndex());

			// Store current page in the prefs against the file name,
			// so that we can pick it up each time the file is loaded
			// Other info is needed only for screen-orientation change,
			// so it can go in the bundle
			SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
			SharedPreferences.Editor edit = prefs.edit();
			edit.putInt("page" + mFileName, mDocView.getDisplayedViewIndex());
			edit.commit();
		}

	}

	@Override
	protected void onPause()
	{
		super.onPause();

		if (mFileName != null && mDocView != null)
		{
			SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
			SharedPreferences.Editor edit = prefs.edit();
			edit.putInt("page" + mFileName, mDocView.getDisplayedViewIndex());
			edit.commit();
		}
	}

	public void onDestroy()
	{
		if (core != null)
			core.onDestroy();

		core = null;

		doc = null;
		request = null;
		fileSpiceManager = null;
		dbSpiceManager = null;

		super.onDestroy();
	}

	@Override
	protected void onStart()
	{
		if (core != null)
		{
			core.startAlerts();
		}

		fileSpiceManager.start(this);
		dbSpiceManager.start(this);
		
		super.onStart();
	}

	@Override
	protected void onStop()
	{
		if (core != null)
		{
			core.stopAlerts();
		}
		
		fileSpiceManager.shouldStop();
		dbSpiceManager.shouldStop();

		super.onStop();
	}
	
	// ___ Share feature ___

		private SocialAuthAdapter	adapter;
		private ProgressDialog		dialog;

		@Override
		public boolean onCreateOptionsMenu(Menu menu)
		{
			getMenuInflater().inflate(R.menu.share, menu);

			return true;
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item)
		{
			if (item.getItemId() == R.id.menu_share_twitter)
			{
				dialog = ProgressDialog.show(MuPDFActivity.this, "", "Authenticating...", true, false);

				adapter.authorize(MuPDFActivity.this, Provider.TWITTER);

				return true;
			}
			else if (item.getItemId() == R.id.menu_share_linkedin)
			{
				dialog = ProgressDialog.show(MuPDFActivity.this, "",
						getString(R.string.authenticating), true, false);

				adapter.authorize(MuPDFActivity.this, Provider.LINKEDIN);

				return true;
			}
			else if (item.getItemId() == android.R.id.home)
			{
				finish();
				return true;
			}
			return super.onOptionsItemSelected(item);
		}

		private void setupShare()
		{
			// Add Bar to library
			adapter = new SocialAuthAdapter(new ResponseListener());

			// Please note : Update status functionality is only supported by
			// Facebook, Twitter, Linkedin, MySpace, Yahoo and Yammer.

			// Add providers
			adapter.addProvider(Provider.TWITTER, R.drawable.ic_twitter);
			adapter.addProvider(Provider.LINKEDIN, R.drawable.ic_linkedin);

			// For twitter use add callback method. Put your own callback url here.
			adapter.addCallBack(Provider.TWITTER,
								BuildConfig.CLIENT + "/subscription/Pages/euler-hermes-on-twitter.aspx");

			// Add keys and Secrets
			try
			{
				adapter.addConfig(Provider.TWITTER, "r2U6zsTalmYnTAuSuMb2VYPNi",
						"p8kgDaXhtMOC6XlCEcqpqZbEcYLONicYQS8OiuZWGdpLwHea6i", null);
				adapter.addConfig(Provider.LINKEDIN, "77577nsgex4w58", "QiSTmQXdtHX28su5", null);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		private final class ResponseListener implements DialogListener
		{
			@Override
			public void onComplete(Bundle values)
			{
				// Get name of provider after authentication
				final String providerName = values.getString(SocialAuthAdapter.PROVIDER);
				Log.d("Share-Bar", "Provider Name = " + providerName);

				/*Toast.makeText(MuPDFActivity.this, providerName + " connected", Toast.LENGTH_SHORT)
						.show();*/

				if (dialog != null)
					dialog.setMessage(getString(R.string.publishing));

				String shareMessage = doc.getTitle() + " " + CorePaths.DOC_BASE_URL + doc.getName();

				adapter.updateStatus(shareMessage, new MessageListener(), false);
			}

			@Override
			public void onError(SocialAuthError error)
			{
				error.printStackTrace();

				new Handler(Looper.getMainLooper()).post(new Runnable() {

					@Override
					public void run()
					{
						if (dialog != null)
							dialog.dismiss();

						Toast.makeText(MuPDFActivity.this, getString(R.string.message_not_posted),
								Toast.LENGTH_SHORT).show();
					}
				});
			}

			@Override
			public void onCancel()
			{
				if (dialog != null)
					dialog.dismiss();
			}

			@Override
			public void onBack()
			{
				if (dialog != null)
					dialog.dismiss();
			}
		}

		// To get status of message after authentication
		private final class MessageListener implements SocialAuthListener<Integer>
		{
			@Override
			public void onExecute(String provider, Integer t)
			{
				if (dialog != null)
					dialog.dismiss();

				Integer status = t;
				if (status.intValue() == 200 || status.intValue() == 201 || status.intValue() == 204)
				{
					Toast.makeText(MuPDFActivity.this, getString(R.string.message_posted_on, provider),
							Toast.LENGTH_LONG).show();
				}
				else
				{
					Toast.makeText(MuPDFActivity.this,
							getString(R.string.message_not_posted_on, provider), Toast.LENGTH_LONG)
							.show();
				}
			}

			@Override
			public void onError(SocialAuthError e)
			{
				new Handler(Looper.getMainLooper()).post(new Runnable() {

					@Override
					public void run()
					{
						if (dialog != null)
							dialog.dismiss();

						Toast.makeText(MuPDFActivity.this, getString(R.string.error_while_publishing),
								Toast.LENGTH_LONG).show();
					}
				});

				Log.d("MuPDFActivity.MessageListener", "onError: " + e.getMessage());
			}
		}
}