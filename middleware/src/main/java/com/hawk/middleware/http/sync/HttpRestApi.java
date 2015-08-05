
package com.hawk.middleware.http.sync;

import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * HTTP Rest风格API
 */
public class HttpRestApi {
	private static HttpRestApi mInstance = null;
	
	/**
	 * 获取实例
	 */
	public static HttpRestApi getInstance(){
		if(null == mInstance){
			mInstance = new HttpRestApi();
		}
		return mInstance;
	}
	
	protected HttpRestApi(){}
	
	
	/**
	 * 使用REST风格签名规则，生成REST风格URL
	 * 
	 * 参数：
	 * 1.uri			 ------   URL前半截，后面跟上参数，组装成REST风格URI
	 * 1.signParamName   ------   在URL参数中用来指定签名的参数名，如"sign"
	 * 2.secretKey	     ------   私钥，用于生成签名
	 * 3.params			 ------   参数，用于生成签名
	 */
	@SuppressWarnings("unused")
	public String genUrl(String uri,TreeMap<String, String> params){
		if(null == uri || null == params){
			return null;
		}
/*		
		//生成签名
		String sign = sign(secretKey, params);
		if(null == sign){
			return null;
		}
		
		params.put(signParamName, sign);
		*/
		StringBuffer stringBuffer = new StringBuffer();
		if(null == stringBuffer){
			return null;
		}
		
		stringBuffer.append(uri);
		if(!uri.endsWith("?")){
			stringBuffer.append('?');
		}
		
		for(Entry<String, String> entry:params.entrySet()){
			if(null != entry.getKey()){
				stringBuffer.append(entry.getKey());
				stringBuffer.append('=');
			}
			
			if(null != entry.getValue()){
				stringBuffer.append(entry.getValue());
			}
			
			stringBuffer.append('&');
		}
		
		//移除最后一个'&'
		stringBuffer.deleteCharAt(stringBuffer.length() - 1);
		
		
		return stringBuffer.toString();
	}
	
	/**
	 * 生成签名,算法:
	 * 1.私钥参数名1参数1参数名2参数2...参数名N参数N私钥, 组装成一个长的字符串
	 * 2.对组装出来的字符串进行MD5编码，生成32位的字符串
	 */
/*	@SuppressWarnings("unused")
	private String sign(String secretKey,TreeMap<String, String> params){
		if(null == secretKey || null == params){
			return null;
		}
		
		MD5Coder md5Coder = MD5Coder.getInstance();
		if(null == md5Coder){
			return null;
		}
		
		StringBuffer stringBuffer = new StringBuffer();
		if(null == stringBuffer){
			return null;
		}
		
		stringBuffer.append(secretKey);
		
		for(Entry<String, String> entry:params.entrySet()){
			if(null != entry.getKey()){
				stringBuffer.append(entry.getKey());
			}
			
			if(null != entry.getValue()){
				stringBuffer.append(entry.getValue());
			}
		}
		
		stringBuffer.append(secretKey);
		
		return md5Coder.encode(stringBuffer.toString().getBytes());
	}*/
}
