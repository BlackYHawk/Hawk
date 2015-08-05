package com.hawk.application;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class AppContext extends Application {

    private RefWatcher mRefWatcher;

    public static RefWatcher getRefWatcher(Context context) {

        AppContext appContext = (AppContext)context.getApplicationContext();

        return appContext.mRefWatcher;

    }

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

        mRefWatcher = LeakCanary.install(this);
	}
	

}
