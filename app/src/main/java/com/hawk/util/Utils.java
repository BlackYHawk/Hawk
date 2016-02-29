package com.hawk.util;

import android.util.TypedValue;

import com.hawk.base.GlobalContext;

public class Utils {

	public static int dip2px(int dipValue) {
		float reSize = GlobalContext.getInstance().getResources().getDisplayMetrics().density;
		return (int) ((dipValue * reSize) + 0.5);
	}

	public static int px2dip(int pxValue) {
		float reSize = GlobalContext.getInstance().getResources().getDisplayMetrics().density;
		return (int) ((pxValue / reSize) + 0.5);
	}

	public static float sp2px(int spValue) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, GlobalContext.getInstance().getResources().getDisplayMetrics());
	}
	
}
