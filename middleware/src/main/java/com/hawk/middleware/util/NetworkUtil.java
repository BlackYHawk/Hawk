
package com.hawk.middleware.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

/**
 * 网络工具类：
 * 1.当前是否有可用网络
 * 2.WIFI当前是否可用
 */
public class NetworkUtil {
	/**
	 * 函数说明：用于判断当前是否有可用网络.
	 * 
	 * 实现原理：
	 * 1. 获取Android的ConnectivityManager
	 * 2. 如果有active的NetworkInfo，判断是否可用    
	 * 
	 * 返回值：
	 *  如果网络可用，返回true; 如果网络不可用，返回false.
	 */
	public static boolean IsNetworkAvailable(Context context){
		ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(null == connMgr){
			return false;
		}
		
		/**
		 * getActiveNetworkInfo() 这个函数做了两件事：
		 * 1.判断当前Network是否Available
		 * 2.判断当前网络状态是否为CONNECTED
		 * 
		 * 注：如果是飞行模式，则返回的active network info为null
		 * 
		 * 对于WIFI，只有已经连上了WIFI网络，才算是active的Network
		 * 对于Mobile,只要打开了数据业务，网络状态就会置成CONNECTD,即使没有发生真正的数据传输。
		 * 
		 * 检测的网络类型有：
		 * mobile,WIFI,mobile_mms,mobile_supl,mobile_dun,mobile_hipri
		 * 
		 * ！！！注意：
		 * 如果WIFI和mobile同时打开，默认使用的active network是WIFI
		 * 但是有个例外，如果机子休眠时间长，WIFI会被自动关闭，这个时候默认使用的active network就是mobile了；当退出
		 * 休眠时,WIFI会被自动重连。
		 */
		NetworkInfo activeNet = connMgr.getActiveNetworkInfo();
		if(null != activeNet && activeNet.isAvailable()){
			return true;
		}
		
		return false;
	}
	
	/**
	 * 函数说明：用于判断网络是否WIFI.
	 * 
	 * 实现原理：
	 * 1. 获取Android的ConnectivityManager
	 * 2. 如果有active的NetworkInfo，判断是否WIFI    
	 * 
	 * 返回值：
	 *  如果网络可用，返回true; 如果网络不可用，返回false.
	 */
	public static boolean IsWIFINetworkAvailable(Context context){
		ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(null == connMgr){
			return false;
		}
		
		/**
		 * getActiveNetworkInfo() 这个函数做了两件事：
		 * 1.判断当前Network是否Available
		 * 2.判断当前网络状态是否为CONNECTED
		 * 
		 * 注：如果是飞行模式，则返回的active network info为null
		 * 
		 * 对于WIFI，只有已经连上了WIFI网络，才算是active的Network
		 * 对于Mobile,只要打开了数据业务，网络状态就会置成CONNECTD,即使没有发生真正的数据传输。
		 * 
		 * 检测的网络类型有：
		 * mobile,WIFI,mobile_mms,mobile_supl,mobile_dun,mobile_hipri
		 * 
		 * ！！！注意：
		 * 如果WIFI和mobile同时打开，默认使用的active network是WIFI
		 * 但是有个例外，如果机子休眠时间长，WIFI会被自动关闭，这个时候默认使用的active network就是mobile了；当退出
		 * 休眠时,WIFI会被自动重连。
		 */
		NetworkInfo activeNet = connMgr.getActiveNetworkInfo();
		if(null != activeNet && !activeNet.isAvailable()){
			return false;
		}
		
		//判断是否WIFI
		String type = activeNet.getTypeName();
		if(null != type && type.equals("WIFI")){
			return true;
		}
		
		return false;
	}
	
	/**
	 * 获取系统的网络设置intent,用于启动系统的网络设置界面
	 */
	@SuppressWarnings("unused")
	public static Intent getNetworkSettingIntent(){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		if(null == intent){
			return null;
		}
		
		//Settings.ACTION_WIRELESS_SETTINGS;
		
		//获取系统的网络设置组件
		ComponentName component = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
		if(null == component){
			return null;
		}
		
		intent.setComponent(component);
		
		return intent;
	}
	
	/**
	 * 获取当前网络类型
	 * @param context
	 * @return
	 */
	public static String getNetworkType(Context context){
		ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(null == connMgr){
			return null;
		}
		
		/**
		 * getActiveNetworkInfo() 这个函数做了两件事：
		 * 1.判断当前Network是否Available
		 * 2.判断当前网络状态是否为CONNECTED
		 * 
		 * 注：如果是飞行模式，则返回的active network info为null
		 * 
		 * 对于WIFI，只有已经连上了WIFI网络，才算是active的Network
		 * 对于Mobile,只要打开了数据业务，网络状态就会置成CONNECTD,即使没有发生真正的数据传输。
		 * 
		 * 检测的网络类型有：
		 * mobile,WIFI,mobile_mms,mobile_supl,mobile_dun,mobile_hipri
		 * 
		 * ！！！注意：
		 * 如果WIFI和mobile同时打开，默认使用的active network是WIFI
		 * 但是有个例外，如果机子休眠时间长，WIFI会被自动关闭，这个时候默认使用的active network就是mobile了；当退出
		 * 休眠时,WIFI会被自动重连。
		 */
		NetworkInfo activeNet = connMgr.getActiveNetworkInfo();
		if(null == activeNet){
			return null;
		}
		
		return activeNet.getTypeName();
	}
	
	/**
	 * 重新启动WIFI，主要为了解决某些型号的手机，进入休眠后，退出休眠,WIFI不可用的情况，即使状态显示WIFI可用，
	 * 但是，如果不手动重启一下WIFI，还是不能联网。
	 */
	public static boolean reconnectWIFI(Context context){
		if (null == context) {
			return false;
		}
		
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (null == wifiManager) {
			return false;
		}
		
		//注：貌似reconnect没有效果
//		boolean isSuccess = wifiManager.reconnect();
		
		//模拟手动重启WIFI流程，先关闭wifi，再打开wifi
		boolean isSuccess = wifiManager.setWifiEnabled(false);
		
		
		isSuccess = wifiManager.setWifiEnabled(true);
		
		return isSuccess;
	}
	
	/**
	 * 如果当前网络为WIFI，因为在某些机型上，WIFI进入休眠，再退出休眠时，虽然状态是好的，但是实际是连不上的，需要手动
	 * 重启WIFI才可。
	 * @param context
	 * @return
	 */
	public static boolean reconnectNetwork(Context context){
		//如果当前可用的是wifi，重启wifi
		if (IsWIFINetworkAvailable(context)) {
			return reconnectWIFI(context);
		}
		
		return false;
	}
}
