package com.eulerhermes.research.core;

import android.app.Application;

public abstract class BaseApplication extends Application {
    private static BaseApplication instance;

    protected abstract void initGlobals();

    protected abstract void initOthers();

    protected abstract void initPreferences();

    public void onCreate() {
        instance = this;
        initGlobals();
        initBugReport();
        initPreferences();
        initOthers();
        super.onCreate();
    }

    public static BaseApplication getInstance() {
        return instance;
    }

    private void initBugReport() {
    }
}
