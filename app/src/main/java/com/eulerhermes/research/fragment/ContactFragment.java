package com.eulerhermes.research.fragment;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.eulerhermes.research.R;
import com.eulerhermes.research.core.CoreDevice;
import com.eulerhermes.research.core.CorePrefs;
import com.eulerhermes.research.fragment.contact.AboutPage;
import com.eulerhermes.research.fragment.contact.ChangePasswordPage;
import com.eulerhermes.research.fragment.contact.ContactWizardModel;
import com.eulerhermes.research.fragment.contact.FeedbackPage;
import com.eulerhermes.research.fragment.contact.PasswordPage;
import com.eulerhermes.research.fragment.contact.wizardpager.wizard.model.AbstractWizardModel;
import com.eulerhermes.research.fragment.contact.wizardpager.wizard.model.ModelCallbacks;
import com.eulerhermes.research.fragment.contact.wizardpager.wizard.model.Page;
import com.eulerhermes.research.fragment.contact.wizardpager.wizard.model.SingleFixedChoicePage;
import com.eulerhermes.research.fragment.contact.wizardpager.wizard.ui.PageFragmentCallbacks;
import com.eulerhermes.research.model.ChangePasswordResult;
import com.eulerhermes.research.model.InitPasswordResult;
import com.eulerhermes.research.model.SendFeedbackResult;
import com.eulerhermes.research.network.rest.ChangePasswordRequest;
import com.eulerhermes.research.network.rest.FeedbackRequest;
import com.eulerhermes.research.network.rest.ForgotPasswordRequest;
import com.octo.android.robospice.Jackson2GoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.List;

public class ContactFragment extends Fragment implements PageFragmentCallbacks, ModelCallbacks
{
    private       Dialog              dialog;
    private       Button              dialogButton;
    private       View                dialogContentView;
    private       TextView            dialogMessage;
    private       TextView            dialogTitle;
    private       ViewFlipper         dialogViewFlipper;
    private       List<Page>          mCurrentPageSequence;
    private       Button              mNextButton;
    private       ViewPager           mPager;
    private       MyPagerAdapter      mPagerAdapter;
    private       Button              mPrevButton;
    private       AbstractWizardModel mWizardModel;
    private final SpiceManager        spiceManager = new SpiceManager(Jackson2GoogleHttpClientSpiceService.class);

    public final class AboutRequestListener implements RequestListener<SendFeedbackResult>
    {
        public void onRequestFailure(SpiceException spiceException)
        {
            ContactFragment.this.displayErrorDialog(R.string.more_info_error_title, R.string.contact_error_message);
        }

        public void onRequestSuccess(SendFeedbackResult result)
        {
            if (result.isSuccess()) {
                ContactFragment.this.displaySuccessDialog(R.string.feedback_success_title, R.string.feedback_success_message);
            }
            else {
                ContactFragment.this.displayErrorDialog(R.string.more_info_error_title, R.string.contact_error_message);
            }
        }
    }

    public final class ChangePasswordRequestListener implements RequestListener<ChangePasswordResult>
    {
        public void onRequestFailure(SpiceException spiceException)
        {
            ContactFragment.this.displayErrorDialog(R.string.forgot_password_error_title, R.string.contact_error_message);
        }

        public void onRequestSuccess(ChangePasswordResult result)
        {
            if (result.isSuccess()) {
                ContactFragment.this.displaySuccessDialog(R.string.change_password_success_title, R.string.change_password_success_message);
            }
            else {
                ContactFragment.this.displayErrorDialog(R.string.change_password_error_title, R.string.change_password_error_message);
            }
        }
    }

    public final class FeedbackRequestListener implements RequestListener<SendFeedbackResult>
    {
        public void onRequestFailure(SpiceException spiceException)
        {
            ContactFragment.this.displayErrorDialog(R.string.feedback_error_title, R.string.contact_error_message);
        }

        public void onRequestSuccess(SendFeedbackResult result)
        {
            if (result.isSuccess()) {
                ContactFragment.this.displaySuccessDialog(R.string.feedback_success_title, R.string.feedback_success_message);
            }
            else {
                ContactFragment.this.displayErrorDialog(R.string.feedback_error_title, R.string.contact_error_message);
            }
        }
    }

    public final class PasswordRequestListener implements RequestListener<InitPasswordResult>
    {
        public void onRequestFailure(SpiceException spiceException)
        {
            ContactFragment.this.displayErrorDialog(R.string.forgot_password_error_title, R.string.contact_error_message);
        }

        public void onRequestSuccess(InitPasswordResult result)
        {
            if (result.isSuccess()) {
                ContactFragment.this.displaySuccessDialog(R.string.forgot_password_success_title, R.string.forgot_password_success_message);
            }
            else {
                ContactFragment.this.displayErrorDialog(R.string.forgot_password_error_title, R.string.contact_error_message);
            }
        }
    }

    public class MyPagerAdapter extends FragmentStatePagerAdapter
    {
        private int      mCutOffPage;
        private Fragment mPrimaryItem;

        public MyPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        public Fragment getItem(int i)
        {
            return ((Page) ContactFragment.this.mCurrentPageSequence.get(i)).createFragment();
        }

        public int getItemPosition(Object object)
        {
            if (object == this.mPrimaryItem) {
                return -1;
            }
            return -2;
        }

        public void setPrimaryItem(ViewGroup container, int position, Object object)
        {
            super.setPrimaryItem(container, position, object);
            this.mPrimaryItem = (Fragment) object;
        }

        public int getCount()
        {
            if (ContactFragment.this.mCurrentPageSequence == null) {
                return 0;
            }
            return Math.min(this.mCutOffPage + 1, ContactFragment.this.mCurrentPageSequence.size());
        }

        public void setCutOffPage(int cutOffPage)
        {
            if (cutOffPage < 0) {
                cutOffPage = Integer.MAX_VALUE;
            }
            this.mCutOffPage = cutOffPage;
        }

        public int getCutOffPage()
        {
            return this.mCutOffPage;
        }
    }

    public static ContactFragment newInstance(boolean openMoreAboutEH, boolean openChangePassword)
    {
        ContactFragment fragment = new ContactFragment();
        Bundle          args     = new Bundle();
        args.putBoolean("openMoreAboutEH", openMoreAboutEH);
        args.putBoolean("openChangePassword", openChangePassword);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        this.mWizardModel = new ContactWizardModel(getActivity());
        if (savedInstanceState != null) {
            this.mWizardModel.load(savedInstanceState.getBundle("model"));
        }
        this.mWizardModel.registerListener(this);
        this.mPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        this.mPager = (ViewPager) view.findViewById(R.id.pager);
        this.mPager.setAdapter(this.mPagerAdapter);
        refreshViewPagerWidth();
        this.mNextButton = (Button) view.findViewById(R.id.next_button);
        this.mPrevButton = (Button) view.findViewById(R.id.prev_button);
        this.mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
        {
            public void onPageSelected(int position)
            {
                ContactFragment.this.updateBottomBar();
            }
        });
        this.mNextButton.setOnClickListener(new OnClickListener()
        {
            public void onClick(View view)
            {
                if (ContactFragment.this.mPager.getCurrentItem() <= 0 || ContactFragment.this.mPager.getCurrentItem() != ContactFragment.this.mCurrentPageSequence.size() - 1) {
                    ContactFragment.this.mPager.setCurrentItem(ContactFragment.this.mPager.getCurrentItem() + 1);
                }
                else {
                    ContactFragment.this.handleRequest();
                }
            }
        });
        this.mPrevButton.setOnClickListener(new OnClickListener()
        {
            public void onClick(View view)
            {
                ContactFragment.this.mPager.setCurrentItem(ContactFragment.this.mPager.getCurrentItem() - 1);
            }
        });
        if (getArguments() != null && getArguments().getBoolean("openMoreAboutEH")) {
            ((SingleFixedChoicePage) this.mWizardModel.findByKey(getString(R.string.contact_section_title))).setValue(getString(R.string.contact_section_more_about_eh));
            onPageTreeChanged();
            updateBottomBar();
            this.mPager.setCurrentItem(this.mPager.getCurrentItem() + 1);
        }
        else if (getArguments() == null || !getArguments().getBoolean("openChangePassword")) {
            onPageTreeChanged();
            updateBottomBar();
        }
        else {
            ((SingleFixedChoicePage) this.mWizardModel.findByKey(getString(R.string.contact_section_title))).setValue(getString(R.string.contact_section_change_password));
            onPageTreeChanged();
            updateBottomBar();
            this.mPager.setCurrentItem(this.mPager.getCurrentItem() + 1);
        }
    }

    private void refreshViewPagerWidth()
    {
        LayoutParams params = this.mPager.getLayoutParams();
        params.width = Math.min(CoreDevice.getDeviceWidth(), getResources().getDimensionPixelSize(R.dimen.wide_layout_max_width));
        this.mPager.setLayoutParams(params);
    }

    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        refreshViewPagerWidth();
    }

    public void onPageTreeChanged()
    {
        this.mCurrentPageSequence = this.mWizardModel.getCurrentPageSequence();
        recalculateCutOffPage();
        this.mPagerAdapter.notifyDataSetChanged();
        updateBottomBar();
    }

    private void updateBottomBar()
    {
        boolean z        = true;
        int     i        = 0;
        int     position = this.mPager.getCurrentItem();
        if (position <= 0 || position != this.mCurrentPageSequence.size() - 1) {
            this.mNextButton.setText(R.string.next);
            this.mNextButton.setBackgroundResource(R.drawable.list_selector_holo_light);
            TypedValue v = new TypedValue();
            getActivity().getTheme().resolveAttribute(16842817, v, true);
            this.mNextButton.setTextAppearance(getActivity(), v.resourceId);
            Button button = this.mNextButton;
            if (position == this.mPagerAdapter.getCutOffPage()) {
                z = false;
            }
            button.setEnabled(z);
        }
        else {
            this.mNextButton.setText(R.string.send);
            this.mNextButton.setBackgroundResource(R.drawable.main_blue_button_selector);
            this.mNextButton.setTextAppearance(getActivity(), R.style.TextAppearanceFinish);
            this.mNextButton.setTextColor(getResources().getColorStateList(R.drawable.main_blue_button_text_selector));
            this.mNextButton.setEnabled(((Page) this.mCurrentPageSequence.get(position)).isCompleted());
        }
        Button button2 = this.mPrevButton;
        if (position <= 0) {
            i = 4;
        }
        button2.setVisibility(i);
    }

    public void onDestroy()
    {
        super.onDestroy();
        this.mWizardModel.unregisterListener(this);
    }

    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putBundle("model", this.mWizardModel.save());
    }

    public void onPageDataChanged(Page page)
    {
        if (page.isRequired() && recalculateCutOffPage()) {
            this.mPagerAdapter.notifyDataSetChanged();
            updateBottomBar();
        }
    }

    public Page onGetPage(String key)
    {
        return this.mWizardModel.findByKey(key);
    }

    private boolean recalculateCutOffPage()
    {
        int cutOffPage = this.mCurrentPageSequence.size() + 1;
        for (int i = 0; i < this.mCurrentPageSequence.size(); i++) {
            Page page = (Page) this.mCurrentPageSequence.get(i);
            if (page.isRequired() && !page.isCompleted()) {
                cutOffPage = i;
                break;
            }
        }
        if (this.mPagerAdapter.getCutOffPage() == cutOffPage) {
            return false;
        }
        this.mPagerAdapter.setCutOffPage(cutOffPage);
        return true;
    }

    private void handleRequest()
    {
        Page page = (Page) this.mCurrentPageSequence.get(this.mCurrentPageSequence.size() - 1);
        displayLoadingDialog();
        if (page instanceof PasswordPage) {
            sendPasswordRequest((PasswordPage) page);
        }
        else if (page instanceof ChangePasswordPage) {
            sendChangePasswordRequest((ChangePasswordPage) page);
        }
        else if (page instanceof AboutPage) {
            sendAboutRequest((AboutPage) page);
        }
        else if (page instanceof FeedbackPage) {
            sendFeedbackRequest((FeedbackPage) page);
        }
    }

    private void createDialogContentView()
    {
        this.dialogContentView = LayoutInflater.from(getActivity()).inflate(R.layout.view_alert_dialog, null, false);
        this.dialogViewFlipper = (ViewFlipper) this.dialogContentView.findViewById(R.id.view_flipper);
        this.dialogTitle = (TextView) this.dialogContentView.findViewById(R.id.title);
        this.dialogMessage = (TextView) this.dialogContentView.findViewById(R.id.message);
        this.dialogButton = (Button) this.dialogContentView.findViewById(R.id.button);
    }

    private void displayLoadingDialog()
    {
        if (this.dialog == null) {
            createDialogContentView();
        }
        this.dialogViewFlipper.setDisplayedChild(0);
        this.dialogMessage.setVisibility(View.GONE);
        this.dialog = new Builder(getActivity()).setView(this.dialogContentView).create();
        this.dialog.setCancelable(false);
        this.dialog.show();
    }

    private void displayErrorDialog(int title, int message)
    {
        this.dialogMessage.setVisibility(View.VISIBLE);
        this.dialogTitle.setText(title);
        this.dialogMessage.setText(message);
        this.dialogViewFlipper.setDisplayedChild(1);
        this.dialog.setCancelable(true);
        this.dialogButton.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                ContactFragment.this.dialog.dismiss();
                ContactFragment.this.dialog = null;
            }
        });
    }

    private void displaySuccessDialog(int title, int message)
    {
        this.dialogMessage.setVisibility(View.VISIBLE);
        this.dialogTitle.setText(title);
        this.dialogMessage.setText(message);
        this.dialogViewFlipper.setDisplayedChild(1);
        this.dialog.setCancelable(true);
        this.dialogButton.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                ContactFragment.this.dialog.dismiss();
                ContactFragment.this.mPager.setCurrentItem(0);
                ContactFragment.this.updateBottomBar();
                ContactFragment.this.dialog = null;
            }
        });
    }

    private void sendPasswordRequest(PasswordPage page)
    {
        Log.d("ContactFragment", "sendPasswordRequest: ");
        this.spiceManager.execute(new ForgotPasswordRequest(page.getData().getString(PasswordPage.LAST_NAME_DATA_KEY), page.getData().getString(PasswordPage.FIRST_NAME_DATA_KEY), page.getData().getString(PasswordPage.EMAIL_DATA_KEY)), null, -1, new PasswordRequestListener());
    }

    private void sendChangePasswordRequest(ChangePasswordPage page)
    {
        this.spiceManager.execute(new ChangePasswordRequest(CorePrefs.getUser().getUsername(), page.getData().getString(ChangePasswordPage.OLD_PASSWORD_DATA_KEY), page.getData().getString(ChangePasswordPage.NEW_PASSWORD_DATA_KEY)), null, -1, new ChangePasswordRequestListener());
    }

    private void sendAboutRequest(AboutPage page)
    {
        Log.d("ContactFragment", "sendAboutRequest: ");
        this.spiceManager.execute(new FeedbackRequest(page.getData().getString(AboutPage.LAST_NAME_DATA_KEY), page.getData().getString(AboutPage.FIRST_NAME_DATA_KEY), page.getData().getString(AboutPage.EMAIL_DATA_KEY), page.getData().getString(AboutPage.FEEDBACK_DATA_KEY)), null, -1, new AboutRequestListener());
    }

    private void sendFeedbackRequest(FeedbackPage page)
    {
        Log.d("ContactFragment", "sendFeedbackRequest: ");
        this.spiceManager.execute(new FeedbackRequest(page.getData().getString(FeedbackPage.LAST_NAME_DATA_KEY), page.getData().getString(FeedbackPage.FIRST_NAME_DATA_KEY), page.getData().getString(FeedbackPage.EMAIL_DATA_KEY), page.getData().getString(FeedbackPage.FEEDBACK_DATA_KEY)), null, -1, new FeedbackRequestListener());
    }

    public void onStart()
    {
        this.spiceManager.start(getActivity());
        super.onStart();
    }

    public void onStop()
    {
        this.spiceManager.shouldStop();
        super.onStop();
    }
}
