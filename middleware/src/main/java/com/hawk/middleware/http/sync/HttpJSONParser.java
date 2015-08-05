package com.hawk.middleware.http.sync;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.json.JSONObject;

import android.util.Log;

/*后台数据的JSON格式解析*/
public class HttpJSONParser {
	private static HttpJSONParser instance = null;
	
	public static HttpJSONParser getInstance(){
		if(instance == null){
			instance = new HttpJSONParser();
		}
		return instance;
	}
	
	private HttpJSONParser(){
		
	}
	//解析服务器返回数据
	public JSONObject parser(HttpResponse response){
		return parser(response,"UTF-8");
	}
	
	@SuppressWarnings("unused")
	public JSONObject parser(HttpResponse response,String encoding){
		if(response == null){
			Log.e("++++","response==null");
			return null;
		}
		Log.e("++++","response!=null");
		int statusCode = response.getStatusLine().getStatusCode();
		if(statusCode < 200 || statusCode >= 300){
			Log.e("++++","status code"+statusCode);
			return null;
		}
		
		HttpEntity entity = response.getEntity();
		if(entity == null){
			Log.e("++++","entity");
			return null;
		}
		
		InputStream inputStream;
		try{
			inputStream = entity.getContent();
			if(inputStream == null){
				Log.e("++++","content");
				return null;
			}
			
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			if(inputStreamReader == null){
				Log.e("++++","input stream");
				inputStream.close();
				return null;
			}
			
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			if(bufferedReader == null){
				Log.e("++++","buffer");
				inputStream.close();
				return null;
			}
			
			StringBuilder builder = new StringBuilder();
			if(builder == null){
				inputStream.close();
				return null;
			}
			
			String string = null;
			while( (string=bufferedReader.readLine()) !=null){
				builder.append(string);
			}
			inputStream.close();
			
			String jsonStr = builder.toString();
			Log.e("builder",jsonStr);
			if(jsonStr == null || jsonStr.length() <= 0){
				return null;
			}

			String lastData = new String(jsonStr.getBytes(),encoding);

			return new JSONObject(lastData); 
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}

}
