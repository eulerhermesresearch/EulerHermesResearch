package com.eulerhermes.research.core;

import android.content.Context;
import android.util.DisplayMetrics;

public class CoreDevice {
    private static int deviceDPI;
    private static float deviceDensity;
    private static int deviceHeight;
    private static int deviceWidth;
    private static float deviceXdpi;
    private static float deviceYdpi;
    private static int orientation;
    private static boolean valuesInitialized = false;

    public static void initDeviceValues(Context context) {
        initDeviceValues(context, false);
    }

    public static void initDeviceValues(Context context, boolean reset) {
        if (reset || !valuesInitialized) {
            DisplayMetrics displaymetrics = context.getResources().getDisplayMetrics();
            deviceWidth = displaymetrics.widthPixels;
            deviceHeight = displaymetrics.heightPixels;
            deviceDPI = displaymetrics.densityDpi;
            deviceDensity = displaymetrics.density;
            deviceXdpi = displaymetrics.xdpi;
            deviceYdpi = displaymetrics.ydpi;
            orientation = context.getResources().getConfiguration().orientation;
            valuesInitialized = true;
        }
    }

    public static int getDeviceWidth() {
        return deviceWidth;
    }

    public static int getDeviceHeight() {
        return deviceHeight;
    }

    public static int getDeviceDPI() {
        return deviceDPI;
    }

    public static float getDeviceDensity() {
        return deviceDensity;
    }

    public static float getDeviceXdpi() {
        return deviceXdpi;
    }

    public static float getDeviceYdpi() {
        return deviceYdpi;
    }

    public static boolean isPortraitMode() {
        return orientation == 1;
    }

    public static boolean isLandscapeMode() {
        return orientation == 2;
    }
}
