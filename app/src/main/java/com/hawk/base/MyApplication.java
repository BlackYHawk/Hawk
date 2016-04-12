package com.hawk.base;

import com.baidu.mapapi.SDKInitializer;

public class MyApplication extends GlobalContext {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		//baidu map
		SDKInitializer.initialize(this);
	}

}
