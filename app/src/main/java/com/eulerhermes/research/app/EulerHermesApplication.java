package com.eulerhermes.research.app;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import androidx.multidex.MultiDex;
import com.crashlytics.android.Crashlytics;
import com.eulerhermes.research.BuildConfig;
import com.eulerhermes.research.common.AnalyticsHelper;
import com.eulerhermes.research.core.BaseApplication;
import com.eulerhermes.research.core.CoreDevice;
import io.fabric.sdk.android.Fabric;
import roboguice.util.temp.Ln;

public class EulerHermesApplication extends BaseApplication
{
    private static EulerHermesApplication instance;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static EulerHermesApplication getInstance()
    {
        return instance;
    }


    protected void initPreferences()
    {

    }

    protected void initGlobals()
    {
        instance = this;

        Ln.getConfig().setLoggingLevel(6);
        CoreDevice.initDeviceValues(this);

//        if (!BuildConfig.DEBUG)
            Fabric.with(this, new Crashlytics());
    }

    protected void initOthers()
    {
        AnalyticsHelper.event("AppLaunched");
    }

    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        Log.d("EulerHermesApplication", "onConfigurationChanged: ");
        CoreDevice.initDeviceValues(this, true);
    }
}
