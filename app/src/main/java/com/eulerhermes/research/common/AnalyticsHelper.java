package com.eulerhermes.research.common;

import android.app.Activity;
import android.os.Bundle;
import com.eulerhermes.research.app.EulerHermesApplication;
import com.google.firebase.analytics.FirebaseAnalytics;

public class AnalyticsHelper
{
    private static FirebaseAnalytics mFirebaseAnalytics;

    private static FirebaseAnalytics getAnalytics()
    {
        if (mFirebaseAnalytics == null)
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(EulerHermesApplication.getInstance());

        return mFirebaseAnalytics;
    }

    public static void event(String event)
    {
        AnalyticsHelper.event(event, null, null);
    }

    public static void event(String event, String action)
    {
        AnalyticsHelper.event(event, action, null);
    }

    public static void event(String event, String action, String label)
    {
        Bundle bundle = new Bundle();

        if (action != null)
            bundle.putString("action", action);

        if (label != null)
            bundle.putString("label", label);

        getAnalytics().logEvent(event, bundle);
    }

    public static void view(Activity activity, String view)
    {
        mFirebaseAnalytics.setCurrentScreen(activity, view, null /* class override */);
    }
}


