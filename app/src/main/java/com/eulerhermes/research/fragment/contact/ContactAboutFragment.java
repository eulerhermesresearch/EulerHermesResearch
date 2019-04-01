/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.eulerhermes.research.fragment.contact;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.eulerhermes.research.R;
import com.eulerhermes.research.core.CorePrefs;
import com.eulerhermes.research.fragment.contact.wizardpager.wizard.ui.PageFragmentCallbacks;
import com.eulerhermes.research.model.User;

public class ContactAboutFragment extends Fragment
{
    private static final String		ARG_KEY	= "key";

    private PageFragmentCallbacks	mCallbacks;
    private String					mKey;
    private AboutPage				mPage;
    private Spinner					mTopic;
    private TextView				mFirstName;
    private TextView				mLastName;
    private TextView				mEmail;
    private TextView				mFeedback;

    private TextWatcher firstnameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {}

        @Override
        public void afterTextChanged(Editable editable)
        {
            mPage.getData().putString(AboutPage.FIRST_NAME_DATA_KEY, (editable != null) ? editable.toString() : null);
            mPage.notifyDataChanged();
        }
    };

    private TextWatcher lastnameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {}

        @Override
        public void afterTextChanged(Editable editable)
        {
            mPage.getData().putString(AboutPage.LAST_NAME_DATA_KEY, (editable != null) ? editable.toString() : null);
            mPage.notifyDataChanged();
        }
    };

    private TextWatcher emailWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {}

        @Override
        public void afterTextChanged(Editable editable)
        {
            mPage.getData().putString(AboutPage.EMAIL_DATA_KEY, (editable != null) ? editable.toString() : null);
            mPage.notifyDataChanged();
        }
    };

    private TextWatcher feedbackWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {}

        @Override
        public void afterTextChanged(Editable editable)
        {
            mPage.getData().putString(AboutPage.FEEDBACK_DATA_KEY, (editable != null) ? editable.toString() : null);
            mPage.notifyDataChanged();
        }
    };

    public static ContactAboutFragment newInstance(String key)
    {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        ContactAboutFragment fragment = new ContactAboutFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ContactAboutFragment()
    {}

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = (AboutPage) mCallbacks.onGetPage(mKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_contact_about, container, false);
        ((TextView) rootView.findViewById(android.R.id.title)).setText(mPage.getTitle());

        return rootView;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        if (!(getParentFragment() instanceof PageFragmentCallbacks))
        {
            throw new ClassCastException("Parent fragment must implement PageFragmentCallbacks");
        }

        mCallbacks = (PageFragmentCallbacks) getParentFragment();
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        User user = CorePrefs.getUser();

        mTopic = ((Spinner) view.findViewById(R.id.topic));
        mTopic.setSelection(mPage.getData().getInt(AboutPage.TOPIC_DATA_KEY));

        mFirstName = ((TextView) view.findViewById(R.id.first_name));
        String firstname = mPage.getData().getString(AboutPage.FIRST_NAME_DATA_KEY);

        if (firstname != null && firstname.length() > 0)
        {
            mFirstName.setText(firstname);
        }
        else
        {
            mFirstName.setText(user.getForeName());
            mPage.getData().putString(AboutPage.FIRST_NAME_DATA_KEY, user.getForeName());
        }

        mLastName = ((TextView) view.findViewById(R.id.last_name));
        String lastname = mPage.getData().getString(AboutPage.LAST_NAME_DATA_KEY);

        if (lastname != null && lastname.length() > 0)
        {
            mLastName.setText(lastname);
        }
        else
        {
            mLastName.setText(user.getName());
            mPage.getData().putString(AboutPage.LAST_NAME_DATA_KEY, user.getName());
        }

        mEmail = ((TextView) view.findViewById(R.id.email));
        String email = mPage.getData().getString(AboutPage.EMAIL_DATA_KEY);

        if (email != null && email.length() > 0)
        {
            mEmail.setText(email);
        }
        else
        {
            mEmail.setText(user.getUsername());
            mPage.getData().putString(AboutPage.EMAIL_DATA_KEY, user.getUsername());
        }

        mFeedback = ((TextView) view.findViewById(R.id.feedback));
        mFeedback.setText(mPage.getData().getString(AboutPage.FEEDBACK_DATA_KEY));

        mTopic.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                mPage.getData().putInt(AboutPage.TOPIC_DATA_KEY, mTopic.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {

            }
        });
    }

    @Override
    public void setMenuVisibility(boolean menuVisible)
    {
        super.setMenuVisibility(menuVisible);

        // In a future update to the support library, this should override setUserVisibleHint
        // instead of setMenuVisibility.
        if (mFirstName != null)
        {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (!menuVisible)
            {
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mFirstName.removeTextChangedListener(firstnameWatcher);

        mLastName.removeTextChangedListener(lastnameWatcher);

        mEmail.removeTextChangedListener(emailWatcher);

        mFeedback.removeTextChangedListener(feedbackWatcher);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mFirstName.addTextChangedListener(firstnameWatcher);

        mLastName.addTextChangedListener(lastnameWatcher);

        mEmail.addTextChangedListener(emailWatcher);

        mFeedback.addTextChangedListener(feedbackWatcher);
    }
}
