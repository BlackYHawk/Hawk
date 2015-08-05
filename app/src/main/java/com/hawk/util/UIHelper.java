package com.hawk.util;

import android.content.Context;
import android.widget.Toast;

public class UIHelper {
	
	/**
	 * 显示提示
	 * 
	 * @param content
	 */
	public static void showToast(final Context context, String content)
	{
		Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
	}

}
