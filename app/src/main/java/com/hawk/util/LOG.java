package com.hawk.util;

import android.util.Log;

/**
 * Created by heyong on 15/5/17.
 */
public class LOG {

    public static final boolean LOG = true;

    public static void Debug(String tag, String str) {

        if(LOG)
            Log.d(tag, str);
    }

    public static void Error(String tag, String str) {

        if(LOG)
            Log.e(tag, str);
    }
}
