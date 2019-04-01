package com.eulerhermes.research.util;

import java.util.Calendar;

public class RequestUtil {
    public static long getCacheExpiration() {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c2.set(11, 7);
        c2.set(12, 0);
        long t1 = c1.getTimeInMillis();
        long t2 = c2.getTimeInMillis();
        if (t2 > t1) {
            return t2 - t1;
        }
        if (t1 > t2) {
            return 86400000 + (t2 - t1);
        }
        return 86400000;
    }
}
