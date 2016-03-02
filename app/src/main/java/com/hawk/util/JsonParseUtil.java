
package com.hawk.util;

import com.hawk.middleware.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 对JSON格式数据分析
 */
public class JsonParseUtil {
	private static final int BAD_VALUE = -1;
	
	/*
	 * JSON格式为：{key:数字}
	 * */
	/**
	 * 解析int值
	 * @param object
	 * @param key
	 * @return
	 */
	public static int parseInteger(JSONObject object,String key){
		if(null == object || null == key || key.length() <= 0){
			return BAD_VALUE;
		}
		
		int result = BAD_VALUE;
		try {
			if(object.has(key)){
				result = object.getInt(key);
			}	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * 解析string值
	 */
	public static String parseString(JSONObject object,String key){
		if(null == object || null == key || key.length() <= 0){
			return null;
		}
		
		String result = null;
		try {
			if(object.has(key)){
				result = object.getString(key);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * 解析String列表值
	 */
	public static List<String> parseStringList(JSONObject object,String key){
		if(null == object || null == key || key.length() <= 0){
			return null;
		}
		
		List<String> result = null;
		try {
			if(object.has(key)){
				result = StringUtil.transformStringToList(object.getString(key));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	/*
	 * JSON格式为：{jsonData:{result:数字，message：字符,updateUrl:字符}}
	 * */
	/**
	 * 解析第一层孩子节点int值
	 */
	public static int parseChildInteger(JSONObject object,String parKey,String key){
		if(null == object || null == key || key.length() <= 0
				|| null == parKey || parKey.length() <= 0){
			return BAD_VALUE;
		}
		
		int result = BAD_VALUE;
		try {
			if(object.has(parKey)){
				JSONObject child = object.getJSONObject(parKey);
				if(null != child){
					if(child.has(key)){
						result = child.getInt(key);
					}
				}
			}	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * 解析第一层孩子节点int值
	 */
	public static long parseChildLong(JSONObject object,String parKey,String key){
		if(null == object || null == key || key.length() <= 0
				|| null == parKey || parKey.length() <= 0){
			return BAD_VALUE;
		}
		
		long result = BAD_VALUE;
		try {
			if(object.has(parKey)){
				JSONObject child = object.getJSONObject(parKey);
				if(null != child){
					if(child.has(key)){
						result = child.getInt(key);
					}
				}
			}	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * 解析第一层孩子节点string值
	 */
	public static String parseChildString(JSONObject object,String parKey,String key){
		if(null == object || null == key || key.length() <= 0
				|| null == parKey || parKey.length() <= 0){
			return null;
		}
		
		String result = null;
		try {
			if(object.has(parKey)){
				JSONObject child = object.getJSONObject(parKey);
				if(null != child){
					if(child.has(key)){
						result = child.getString(key);
					}
				}
			}	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return result;
	}

}
