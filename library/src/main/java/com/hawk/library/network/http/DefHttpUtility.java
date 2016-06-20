package com.hawk.library.network.http;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.hawk.library.common.setting.Setting;
import com.hawk.library.common.utils.Logger;
import com.hawk.library.common.utils.SystemUtils;
import com.hawk.library.network.biz.ABizLogic;
import com.hawk.library.network.task.TaskException;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DefHttpUtility implements IHttpUtility {

	private static final String TAG = DefHttpUtility.class.getSimpleName();

	@Override
	public <T> T doGet(HttpConfig config, Setting action, Params params, Class<T> responseCls) throws TaskException {
		// 是否有网络连接
		if (SystemUtils.getNetworkType() == SystemUtils.NetWorkType.none)
			throw new TaskException(TaskException.TaskError.noneNetwork.toString());

		String url = (config.baseUrl + action.getValue() + (params == null ? "" : "?" + ParamsUtil.encodeToURLParams(params))).replaceAll(" ", "");
		Logger.v(TAG, url);

		Request.Builder builder = new Request.Builder().url(url);
		configHttpHeader(builder, config);
		Request request = builder.build();

		return executeClient(request, responseCls);
	}

	@Override
	public <T> T doPost(HttpConfig config, Setting action, Params params, Class<T> responseCls, Object requestObj) throws TaskException {
		// 是否有网络连接
		if (SystemUtils.getNetworkType() == SystemUtils.NetWorkType.none)
			throw new TaskException(TaskException.TaskError.noneNetwork.toString());

		String url = (config.baseUrl + action.getValue() + (params == null ? "" : "?" + ParamsUtil.encodeToURLParams(params))).replaceAll(" ", "");
		Logger.v(TAG, url);


		Request.Builder builder = new Request.Builder().url(url);

		configHttpHeader(builder, config);

		if (requestObj != null) {
			String requestBodyStr = null;
			if (requestObj instanceof Params) {
				Params p = (Params) requestObj;
				requestBodyStr = ParamsUtil.encodeToURLParams(p);
			}
			else {
				requestBodyStr = JSON.toJSONString(requestObj);
			}

			RequestBody requestBody = RequestBody.create(null, requestBodyStr);

			builder.post(requestBody);
		}
		Request request = builder.build();

		return executeClient(request, responseCls);
	}

	public <T> T uploadFile(HttpConfig config, Setting action, Params params, File file, Params headers, Class<T> responseClazz) throws TaskException {
//		PostMethod postMethod = new PostMethod((config.baseUrl + action.getValue() + (params == null ? "" : "?"
//				+ ParamsUtil.encodeToURLParams(params))).replaceAll(" ", ""));
//
//		StringPart sp = new StringPart(" TEXT ", " testValue ");
//		FilePart fp = null;
//		try {
//			fp = new FilePart("file", file.getName(), file);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//
//		MultipartRequestEntity mrp = new MultipartRequestEntity(new Part[] { sp, fp }, postMethod.getParams());
//		postMethod.setRequestEntity(mrp);
//		postMethod.addRequestHeader("cookie", config.cookie);
//
//		if (headers != null)
//			for (String key : headers.getKeys())
//				postMethod.addRequestHeader(key, headers.getParameter(key));
//
//		// 执行postMethod
//		org.apache.commons.httpclient.HttpClient httpClient = new org.apache.commons.httpclient.HttpClient();
//		try {
//			httpClient.executeMethod(postMethod);
//			Logger.v(ABaseBizlogic.TAG, String.format("upload file's response body = %s", postMethod.getResponseBodyAsString()));
////			T result = new ObjectMapper().readValue(postMethod.getResponseBodyAsString(), responseClazz);
//			T result = JSON.parseObject(postMethod.getResponseBodyAsString(), responseClazz);
//			return result;
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		return null;
	}

	@SuppressWarnings("unchecked")
	private <T> T executeClient(Request request, Class<T> responseCls) throws TaskException {
		try {
			OkHttpClient httpClient = generateHttpClient();

			Response response = httpClient.newCall(request).execute();
			if (response.isSuccessful()) {
				String responseStr = response.message().toString();
				try {
					if (responseCls.getSimpleName().equals("String"))
						return (T) responseStr;
					
					return JSON.parseObject(responseStr, responseCls);
				} catch (Exception e) {
					e.printStackTrace();
					throw new TaskException(TaskException.TaskError.resultIllegal.toString());
				}
			} else {
				Logger.e(ABizLogic.TAG,
                        String.format("Access to the server error, statusCode = %d", response.code()));
				Logger.w(ABizLogic.TAG, response.message().toString());
				throw new TaskException(TaskException.TaskError.timeout.toString());
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			throw new TaskException(TaskException.TaskError.timeout.toString());
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
			throw new TaskException(TaskException.TaskError.timeout.toString());
		} catch (UnknownHostException e) {
			e.printStackTrace();
			throw new TaskException(TaskException.TaskError.timeout.toString());
		} catch (IOException e) {
			e.printStackTrace();
			throw new TaskException(TaskException.TaskError.timeout.toString());
		}
	}

	private void configHttpHeader(Request.Builder builder, HttpConfig config) {
		builder.header("Cookie", config.cookie);
		builder.header("Accept-Charset", "utf-8");

		if (!TextUtils.isEmpty(config.contentType))
			builder.header("Content-Type", config.contentType);
		else
			builder.header("Content-Type", "application/json");
	}

	private OkHttpClient generateHttpClient() {
		OkHttpClient client = new OkHttpClient();
		return client;
	}

}
