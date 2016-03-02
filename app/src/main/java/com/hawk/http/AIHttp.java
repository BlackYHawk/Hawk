package com.hawk.http;

import android.content.Context;
import android.util.Log;

import com.hawk.middleware.http.sync.BaseHttp;
import com.hawk.util.JsonParseUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AIHttp extends BaseHttp {
	private static final String GET_URI = "http://www.tuling123.com/openapi/api?key=bea5018c82cf65ddb3bd01c0b4783d96&info=";
	
	public AIHttp(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public Map<String,String> getAIResult(String request)
	{
		Map<String,String> param = new HashMap<String,String>(2);
		
		String url = GET_URI + request;
		
		JSONObject object = this.get(url, null);
		
		if(object == null)
		{
			Log.d("AI", "JSONObject false");
		}
		
		String code = JsonParseUtil.parseString(object, "code");
		String result = JsonParseUtil.parseString(object, "text");
		
		if(code != null && result != null)
		{
			param.put("code", code);
			param.put("text", result);
			
			return param;
		}
		
		return null;
	}
	
}
