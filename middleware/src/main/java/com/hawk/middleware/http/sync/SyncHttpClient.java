package com.hawk.middleware.http.sync;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import android.content.Context;

import com.hawk.middleware.util.NetworkUtil;
import com.hawk.middleware.util.SystemUtil;

public class SyncHttpClient {
	private DefaultHttpClient httpClient = null;
	private static String appUserAgent;
	private Context context;

	private static final int RETRY_TIME = 3;
	private static final int HTTP_CONNECT_TIMEOUT = 30*1000;
	private static final int HTTP_SOCKET_TIMEOUT = 60*1000;
	private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;
	
	@SuppressWarnings("unused")
	public SyncHttpClient(Context context){
		if(context == null){
			return;
		}
		this.context = context;
		BasicHttpParams httpParams = new BasicHttpParams();
		if(httpParams == null){
			return;
		}
		
		HttpConnectionParams.setConnectionTimeout(httpParams, HTTP_CONNECT_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, HTTP_SOCKET_TIMEOUT);
		HttpConnectionParams.setTcpNoDelay(httpParams, true);
		HttpConnectionParams.setSocketBufferSize(httpParams, DEFAULT_SOCKET_BUFFER_SIZE);
		
		HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
		HttpProtocolParams.setHttpElementCharset(httpParams, HTTP.UTF_8);
		
		SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(httpParams, schemeRegistry);
        
        httpClient = new DefaultHttpClient(cm, httpParams);

	}
	/*Get方法*/
	@SuppressWarnings("unused")
	public HttpResponse get(String url,HashMap<String,String> headers){
		int time = 0;
		
		if(httpClient == null){
			return null;
		}
		
		HttpGet get = new HttpGet(url);
		if(get == null){
			return null;
		}
		
		if(headers != null ){
			for(Entry<String,String> entry:headers.entrySet()){
				if(entry == null){
					continue;
				}
				get.addHeader(entry.getKey(), entry.getValue());
			}
		}
		
		do{
			try{
				return httpClient.execute(get);
			}catch(ClientProtocolException ex){
				ex.printStackTrace();
				get.abort();
				NetworkUtil.reconnectNetwork(context);		
				break;
			}catch(IOException ex){
				time++;
				if(time < RETRY_TIME)
				{
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {} 
					continue;
				}
				get.abort();
				ex.printStackTrace();
			}
		}while(time < RETRY_TIME);

		httpClient = null;
		return null;
	}
	/*用户凭证方式Get方法*/
	@SuppressWarnings("unused")
	public HttpResponse get(String url,String username,String password,HashMap<String,String> headers){
		int time = 0;
		
		if(httpClient == null){
			return null;
		}
		HttpGet get=new HttpGet(url);
		if(get == null ){
			return null;
		}
		
		Credentials cred=new UsernamePasswordCredentials(username,password);
		httpClient.getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY_HOST,AuthScope.ANY_PORT), cred);

		if(headers != null ){
			for(Entry<String,String> entry:headers.entrySet()){
				if(entry == null){
					continue;
				}
				get.addHeader(entry.getKey(), entry.getValue());
			}
		}

		do{
			try{
				return httpClient.execute(get);
			}catch(UnknownHostException ex){
				ex.printStackTrace();
				get.abort();
				NetworkUtil.reconnectNetwork(context);
				break;
			}catch(IOException ex){
				time++;
				if(time < RETRY_TIME)
				{
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {} 
					continue;
				}
				get.abort();
				ex.printStackTrace();
			}
		}while(time < RETRY_TIME);
		httpClient = null;
		return null;
	}
	/*Post方法*/
	@SuppressWarnings("unused")
	public HttpResponse post(String url,HttpEntity entity){
		int time = 0;
		
		if(httpClient == null){
			return null;
		}
		HttpPost post = new HttpPost(url);
		if(post == null){
			return null;
		}

		if(entity != null){
			post.setEntity(entity);
		}
		
		do{
			try{
				return httpClient.execute(post);
			}catch(UnknownHostException ex){
				ex.printStackTrace();
				post.abort();
				NetworkUtil.reconnectNetwork(context);		
				break;
			}catch(IOException ex){
				time++;
				if(time < RETRY_TIME)
				{
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {} 
					continue;
				}
				post.abort();
				ex.printStackTrace();
			}
		}while(time < RETRY_TIME);
		httpClient = null;
		return null;
	}
	/*用户凭证方式Post方法*/
	@SuppressWarnings("unused")
	public HttpResponse post(String url,String username,String password,HttpEntity entity){
		int time = 0;
		
		if(httpClient == null){
			return null;
		}
		HttpPost post = new HttpPost(url);
		if(post == null){
			return null;
		}
		
		Credentials cred=new UsernamePasswordCredentials(username,password);
		httpClient.getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY_HOST,AuthScope.ANY_PORT), cred);
		httpClient.addRequestInterceptor(preemptiveAuth,0);
		
		if(entity != null){
			post.setEntity(entity);
		}
		
		do{
			try{
				return httpClient.execute(post);
			}catch(UnknownHostException ex){
				ex.printStackTrace();
				post.abort();
				NetworkUtil.reconnectNetwork(context);
			}catch(IOException ex){
				time++;
				if(time < RETRY_TIME)
				{
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {} 
					continue;
				}
				post.abort();
				ex.printStackTrace();
			}
		}while(time < RETRY_TIME);
		httpClient = null;
		return null;
	}
	
	private String getUserAgent() {
		if(appUserAgent == null || appUserAgent == "") {
			StringBuilder ua = new StringBuilder("HAWK.NET");
			ua.append('/'+SystemUtil.getPackageInfo(context).versionName+'_'+SystemUtil.getPackageInfo(context).versionCode);//App版本
			ua.append("/"+SystemUtil.getOS());//手机系统平台
			ua.append("/"+SystemUtil.getOSVersion());//手机系统版本
			ua.append("/"+SystemUtil.getModel()); //手机型号
			ua.append("/"+SystemUtil.getAppId());//客户端唯一标识
			appUserAgent = ua.toString();
		}
		return appUserAgent;
	}
	
	//网络访问拦截器
	private HttpRequestInterceptor preemptiveAuth = new HttpRequestInterceptor(){

		@Override
		public void process(HttpRequest request, HttpContext context)
				throws HttpException, IOException {
			// TODO Auto-generated method stub
			AuthState authState = (AuthState) context
					.getAttribute(ClientContext.TARGET_AUTH_STATE);
			CredentialsProvider credsProvider = (CredentialsProvider) context
					.getAttribute(ClientContext.CREDS_PROVIDER);
			HttpHost targetHost = (HttpHost) context
					.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
			if (authState.getAuthScheme() == null) {
				AuthScope authScope = new AuthScope(targetHost.getHostName(),
						targetHost.getPort());
				Credentials creds = credsProvider.getCredentials(authScope);
				if (creds != null) {
					authState.setAuthScheme(new BasicScheme());
					authState.setCredentials(creds);
				}
			}
		}
		
	};

}
