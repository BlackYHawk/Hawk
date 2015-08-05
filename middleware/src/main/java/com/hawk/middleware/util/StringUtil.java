
package com.hawk.middleware.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.json.JSONObject;

import android.content.Context;

/**
 *字符工具类
 */
public class StringUtil {
    public static final String STRING_SEMICOLON = ";";
    public static final String STRING_SHARP = "#";
    public static final String STRING_SPACE = " ";
    public static final String STRING_HYPHEN = "-";
    public static final String STRING_UNDERLINE = "_";
    public static final String STRING_SEPARATOR = "|";
    public static final String STRING_COMMA = ",";
    
    //拆分String的正则表达式
    public static final String STRING_SEPARATOR_SPLIT_REG = "\\|";
    
    private final static String DATE_FORMAT = "yyyy-MM-dd";
    
    
	/**
	 * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}
	
	public static String getUUID() { 
		
        String s = UUID.randomUUID().toString(); 
        //去掉“-”符号 
        
        return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24); 
    }
    
	/**
	 * 字符串转整数
	 * 
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static int toInt(String str, int defValue) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
		}
		return defValue;
	}
	
	/**
	 * 对象转整数
	 * 
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static int toInt(Object obj) {
		if (obj == null)
			return 0;
		return toInt(obj.toString(), 0);
	}
	
	/**  
	 * 反格式化byte  
	 *   
	 * @param s  
	 * @return  
	 */  
	public static byte[] hex2byte(String s) {   
	    byte[] src = s.toLowerCase().getBytes();   
	    byte[] ret = new byte[src.length / 2];   
	    for (int i = 0; i < src.length; i += 2) {   
	        byte hi = src[i];   
	        byte low = src[i + 1];   
	        hi = (byte) ((hi >= 'a' && hi <= 'f') ? 0x0a + (hi - 'a')   
	                : hi - '0');   
	        low = (byte) ((low >= 'a' && low <= 'f') ? 0x0a + (low - 'a')   
	                : low - '0');   
	        ret[i / 2] = (byte) (hi << 4 | low);   
	    }   
	    return ret;   
	}
	
	 /**
	  * 将List转成String,使用"|"做分隔符
	  */
	 public static String transformListToString(List<String> list){
		  if(null == list){
		   return null;
		  }
		  
		  StringBuffer result = new StringBuffer();
		  if(null == result){
		   return null;
		  }
		  
		  boolean isFirst = true;
		  for(String str : list){
		   if(!isFirst){
		    result.append(STRING_SEPARATOR);
		   }
		   result.append(str);
		   isFirst = false;
		  }
		  
		  return result.toString();
	  
	 }
	 
	 /**
	  * 将List转成String,使用","做分隔符
	  */
	 public static String transformListToStringEX(List<String> list){
		  if(null == list){
		   return null;
		  }
		  
		  StringBuffer result = new StringBuffer();
		  if(null == result){
		   return null;
		  }
		  
		  boolean isFirst = true;
		  for(String str : list){
		   if(!isFirst){
		    result.append(STRING_COMMA);
		   }
		   result.append(str);
		   isFirst = false;
		  }
		  
		  return result.toString();
	  
	 }
	 
	 /**
	  * 将使用"|"做分隔符的String转成list
	  */
	 public static List<String> transformStringToList(String str){
		 if(null == str || str.length() <= 0){
			 return null;
		 }
  
		 String items[] = str.split(STRING_SEPARATOR_SPLIT_REG);
		 if(null == items){
			 return null;
		 }
  
		 List<String> list = new ArrayList<String>();
		 if(null == list){
			 return null;
		 }
  
		 for(String item:items){
			 list.add(item);
		 }
  
		 return list;
	  
	 }
	 
	 /**
	  * 将字符串转成Date,字符串格式"yyyy-MM-dd"
	  */
	 public static Date strToDate(String str){
		  if(null == str){
		   return null;
		  }
		  str.trim();
		  
		  SimpleDateFormat dateFromat = new SimpleDateFormat(DATE_FORMAT);
		  if(null == dateFromat){
		   return null;
		  }
		  
		  Date date = null;
		  try{
		   date = dateFromat.parse(str);
		  }catch (Exception e) {
		   e.printStackTrace();
		  }
		  
		  return date;
	 }
	 
	 /**
	  * 将Date转成字符串,字符串格式"yyyy-MM-dd"
	  */
	 public static String dateToStr(Date date){
		  if(null == date){
		   return null;
		  }
		  
		  SimpleDateFormat dateFromat = new SimpleDateFormat(DATE_FORMAT);
		  if(null == dateFromat){
		   return null;
		  }
		  
		  String str = null;
		  try{
		   str = dateFromat.format(date);
		  }catch (Exception e) {
		   e.printStackTrace();
		  }
		  
		  return str;
	 }
	 
	 /**
	  * 通过资源获取字符串
	  * @param context
	  * @param resId
	  * @return
	  */
	 public static String getResourcesString(Context context,int resId)
	 {
		  if (null == context || 0>=resId) {
		   return null;
		  }
		  return context.getString(resId);
	 }
	 
		/**
		 * 流转字符串方法
		 * 
		 * @param is
		 * @return
		 */
		public static String convertStreamToString(InputStream is) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return sb.toString();
		}
		
		/**
		 * 字符串转json对象
		 * @param str
		 * @param split
		 * @return
		 */
		public static JSONObject string2JSON(String str, String split) {
			JSONObject json = new JSONObject();
			try {
				String[] arrStr = str.split(split);
				for (int i = 0; i < arrStr.length; i++) {
					String[] arrKeyValue = arrStr[i].split("=");
					json.put(arrKeyValue[0],
							arrStr[i].substring(arrKeyValue[0].length() + 1));
				}
			}

			catch (Exception e) {
				e.printStackTrace();
			}

			return json;
		}
		
}
