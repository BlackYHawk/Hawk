package com.hawk.middleware.http.sync;

import java.util.HashMap;
import java.util.TreeMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.json.JSONObject;


import android.content.Context;
import android.util.Log;

//网络访问抽象类
public class BaseHttp {

	protected HttpRestApi mRestApi = null;
	protected SyncHttpClient httpClient = null;
	protected HttpJSONParser jsonParser = null;
	protected Context context = null;
	
	public BaseHttp(Context context){
		this.context = context; 

		mRestApi = HttpRestApi.getInstance();
		httpClient = new SyncHttpClient(context.getApplicationContext());
		jsonParser = HttpJSONParser.getInstance();
	}
	
	//get方式访问网络
	public JSONObject get(String url,HashMap<String,String> headerParams){
		if(url == null || httpClient == null || jsonParser == null ){
			Log.e("+++","1");
			return null;
		}

		HttpResponse response = httpClient.get(url, headerParams);

		if(response == null){
			Log.e("+++","2");
			return null;
		}
		return jsonParser.parser(response);
	}
	//post方式访问网络
	public JSONObject post(String url,HttpEntity entity){
		if(url == null || httpClient == null || jsonParser == null ){
			return null;
		}
		Log.e("+++","after response");
		HttpResponse response = httpClient.post(url, entity);
		Log.e("+++","response");
		if(response == null){
			Log.e("+++","2");
			return null;
		}
		return jsonParser.parser(response);
	}

	
	//添加网络访问参数
	/**
	 * 创建Rest风格URL
	 * @param TreeMap<String, String> params
	 * 应用级参数
	 * @param url
	 * 链接地址
	 */
	protected String createUrl(String url, TreeMap<String, String> params, String format){
		if(null == mRestApi){
			return null;
		}
		
		if(params == null)
			params = new TreeMap<String, String>();
		
		params.put("format", format);
		
		return mRestApi.genUrl(url, params);
	}
}
