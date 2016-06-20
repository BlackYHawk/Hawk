package com.hawk.library.network.http;

import com.hawk.library.common.setting.Setting;
import com.hawk.library.network.task.TaskException;

import java.io.File;


public interface IHttpUtility {

	public <T> T doGet(HttpConfig config, Setting action, Params params, Class<T> responseCls) throws TaskException;

	public <T> T doPost(HttpConfig config, Setting action, Params params, Class<T> responseCls, Object requestObj) throws TaskException;

	public <T> T uploadFile(HttpConfig config, Setting action, Params params, File file, Params headers, Class<T> responseClass) throws TaskException;

}
