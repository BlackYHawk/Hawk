/**
 * file name:CommonUtil.java
 * package name:com.kinghanhong.middleware.util 
 * project: LoveCard
 *
 * created by HouXin at 2012-1-4
 * 
 * Copyright @2011, JinHanHong Software, Co, Ltd. All Rights Reserved.
 *
 * Copyright Notice
 * JinHanHong copyrights this specification.No parts of this specification
 * may be reproduced in any form or means, without the prior written consent
 * of JingHanHong.
 *
 * Disclaimer
 * This specification is preliminary and is subject to change at any time
 * without notice. JinHanHong assumes no responsibility for any errors 
 * contained herein.
 */
package com.hawk.middleware.util;

import java.text.SimpleDateFormat;

import android.content.Context;
import android.util.TypedValue;

/**
 * @author HouXin
 * 常用工具类
 */
public class CommonUtil {
	
	public static final int KHH_DOWNLOAD_FILE_BUF_MAX_LEN = 2048; //2K
	
	/**
	 * 获取4位随机数
	 */
	public static int getRandom(){
		return (int) (Math.random() * 9000 + 1000);
	}

	/**
	 * 获取系统日期
	 * @param formate String 日期格式(e.g. yyyy-MM-dd)
	 * @return 系统日期 String
	 */
	public static String getSysDate(String formate) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat(formate);
		return sDateFormat.format(new java.util.Date());
	}
	
	/**  
	 * 根据手机的分辨率从 dip 的单位 转成为 px(像素)  
	 */ 
	public static int dip2px(Context mContext, float dipValue){
		if (null == mContext) {
			return -1;
	    }
	    
	    float fPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, mContext.getResources().getDisplayMetrics());
	    
	    return Math.round(fPx);
	}
	
	/**
     * px 转成 sp, sp和dip是一样的
     */
    public static int px2dip(Context context, float pxValue){
 		final float scale = context.getResources().getDisplayMetrics().density;
 		return (int)(pxValue / scale + 0.5f);
 	}
}

