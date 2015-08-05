package com.hawk.middleware.util;

import java.util.Locale;
import java.util.UUID;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;

//获得手机信息
public class SystemUtil {
	public static final String HS_OS_ANDROID = "Android";
	
	/**
	 * 获取当前手机语言设置类别，调用android的local类实现
	 */
	public static final String getLanguage(){
		return Locale.getDefault().getLanguage();
	}
	
	
	/**
	 * 获取当前手机语言设置类别，调用android的local类实现
	 * 中文简体与繁体是通过countryCode来区分
	 */
	public static final String getCountryCode(){
		return Locale.getDefault().getCountry();
	}
	
	/**
	 * 获取手机品牌
	 */
	public static String getBrand(){
		return android.os.Build.BRAND;
	}
	
	/**
	 * 获取手机型号
	 */
	public static String getModel(){
		return android.os.Build.MODEL;
	}
	
	/**
	 * 获取操作系统型号
	 */
	public static String getOS(){
		return HS_OS_ANDROID;
	}
	
	/**
	 * 获取操作系统版本
	 */
	public static String getOSVersion(){
		return android.os.Build.VERSION.RELEASE;
	}
	
	/**
	 * 获取手机imei号码
	 */
	public static String getIMEI(Context context){
		if(null == context){
			return null;
		}
		
		//获取电话服务
		TelephonyManager telMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if(null == telMgr){
			return null;
		}
		
		return telMgr.getDeviceId();
	}
	
	/**
	 * 获取当前系统的SDK版本
	 */
	public static int getSdkLevel(){
		return android.os.Build.VERSION.SDK_INT;
	}
	
	
	/**
	 * 获取App唯一标识
	 * @return
	 */
	public static String getAppId() {
		
		return UUID.randomUUID().toString();
		
	}
	
	/**
	 * 获取系统软件包版本号
	 */
	public static PackageInfo getPackageInfo(Context context){
		if(null == context){
			return null;
		}
		
		PackageManager pkgManager = context.getPackageManager();
		if(null == pkgManager){
			return null;
		}

		try {
			return pkgManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	
	/**
	 * 获取archiveFilePath该文件的版本号
	 * @param context
	 * @param archiveFilePath 为apk的路径
	 * @return 版本号
	 */
	public static PackageInfo getApkInfo(Context context, String archiveFilePath){
		if(null == context){
			return null;
		}
		
		PackageManager pkgManager = context.getPackageManager();
		if(null == pkgManager){
			return null;
		}
		
		try{
			return pkgManager.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);
			
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取当前系统时间，单位：毫秒
	 */
	public static long getCurTime(){
		return System.currentTimeMillis();
	}
	
	/**
	 * 获取手机服务商信息 
	 */
    public static String getProvidersName(Context context) {  
        String ProvidersName = null;  
        TelephonyManager telMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        // 返回唯一的用户ID;就是这张卡的编号神马的  
        String IMSI = telMgr.getSubscriberId();  
        // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。  
        if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {  
            ProvidersName = "中国移动";  
        } else if (IMSI.startsWith("46001")) {  
            ProvidersName = "中国联通";  
        } else if (IMSI.startsWith("46003")) {  
            ProvidersName = "中国电信";  
        }  
        return ProvidersName;  
    }
}
