package com.hawk.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceUtil {
	
	private static final String PREFERENCE_NAME = "hawk";      //首选项名
	private static final String SUDOKU_ON = "sudoku_on";
	private static final String SUDOKU_PASSWD = "activity_sudoku";

	public static boolean get_sudoku_on(Context context)
	{
		if(context == null){
			return false;
		}
		SharedPreferences pref = context.getSharedPreferences(PREFERENCE_NAME, 0);
		
		if(pref == null)
			return false;
		return pref.getBoolean(SUDOKU_ON, false);
	}
	
	public static boolean set_sudoku_on(Context context, boolean on)
	{
		if(context == null){
			return false;
		}
		SharedPreferences pref = context.getSharedPreferences(PREFERENCE_NAME, 0);
		
		Editor editor = pref.edit();
		
		if(editor == null)
			return false;
		editor.putBoolean(SUDOKU_ON, on);
		
		return editor.commit();
	}
	
	public static String get_sudoku_passwd(Context context)
	{
		if(context == null){
			return null;
		}
		SharedPreferences pref = context.getSharedPreferences(PREFERENCE_NAME, 0);
		
		if(pref == null)
			return null;
		return pref.getString(SUDOKU_PASSWD, null);
	}
	
	public static boolean set_sudoku_passwd(Context context, String passwd)
	{
		if(context == null){
			return false;
		}
		SharedPreferences pref = context.getSharedPreferences(PREFERENCE_NAME, 0);
		
		Editor editor = pref.edit();
		
		if(editor == null)
			return false;
		editor.putString(SUDOKU_PASSWD, passwd);
		
		return editor.commit();
	}
	
}
