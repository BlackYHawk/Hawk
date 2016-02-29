package com.hawk.base;

import android.app.Application;

public class GlobalContext extends Application {

	private static GlobalContext _context;


	@Override
	public void onCreate() {
		super.onCreate();
		
		_context = this;
	}

	public static GlobalContext getInstance() {
		return _context;
	}

}