/**
 * file name:BaseHttpImage.java
 * package name:com.jinhanhong.xcard.http
 * project: XCardNew
 *
 * created by houxin at 2012-3-15
 * 
 * Copyright @2012, KingHanHong Software, Co, Ltd. All Rights Reserved.
 *
 * Copyright Notice
 * KingHanHong copyrights this specification.No parts of this specification
 * may be reproduced in any form or means, without the prior written consent
 * of KingHanHong.
 *
 * Disclaimer
 * This specification is preliminary and is subject to change at any time
 * without notice. KingHanHong assumes no responsibility for any errors 
 * contained herein.
 */
package com.hawk.middleware.http.sync;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.os.Handler;

import com.hawk.middleware.threads.NetThreadPool;
import com.hawk.middleware.util.FileUtil;

/**
 * @author houxin
 * 获取图片的基类
 */
public abstract class BaseHttpImage {
	/**
	 * 内部类，用于实现call-back机制
	 */
	public abstract class CallBack{
		public abstract void downloadOk(String url,String path);
	}
	
	protected class QueueNode{
		public String mUrl = null;
		public List<CallBack> mCallBackList = null;
	}
	
	//消息队列
	protected List<QueueNode> mQueue = null;
	protected Context mContext = null;
	protected Handler mHandler = null;
	
	
	//获取cache interface实现类
	protected abstract InterfaceCache getCacheInterface();
	
	
	/**
	 * 从网络下载图片，保存在本地，返回本地路径
	 * 注：如果是rest风格接口，需要传入其他参数
	 * @param <T>
	 */
	protected abstract <T> String getFromNet(String url,T param);
	
	protected static boolean mIsDownloading = false;
	
	protected BaseHttpImage(Context context){
		if(null == context){
			return;
		}
		mContext = context;
		
		//初始化队列
		mQueue = new LinkedList<QueueNode>();
		mHandler = new Handler();
	}
	
	/**
	 * 获取图片，输入网址，返回bitmap
	 * @param <T>
	 * @param url
	 * @return
	 * 
	 * 逻辑如下：
	 * 1.从缓存中查找是否有
	 * 2.如果缓存中有，进入步骤5
	 * 3.如果缓存中没有，网络下载
	 * 4.网络下载完成后，保存至缓存
	 * 5.调用callback，通知调用者已完成
	 */
	public <T> void get(String url,CallBack callBack,T param){
		if(null == url || null == callBack){
			return;
		}
		
		//1.从缓存中查找是否有
		InterfaceCache cacheApi = getCacheInterface();
		if(null != cacheApi){
			//从缓存中查找
			String path = cacheApi.get(url);
			if(null != path && FileUtil.checkFilePathExists(path)){
				//缓存中有，调用callback，通知调用者已完成
				callBack.downloadOk(url, path);
				return;
			}
		}
		
		//2.添加或者更新队列节点
		boolean isExist = addOrUpdateQueue(url, callBack);
		if(isExist){
			return;
		}
		
		//3.网络下载
		requestCurNode();
//		download(url,param);
		
//		//3.缓存中没有，队列中也没有，网络下载
//		String path = getFromNet(url,param);
//		if(null == path){
//			return;
//		}
//		
//		//4.网络下载完成后，保存至缓存
//		if(null != cacheApi){
//			//如果缓存已存在，新的覆盖旧的；不存在，新建
//			cacheApi.add(url, path);
//		}
//		
//		//5.调用callback，通知调用者已完成
//		callBack.downloadOk(url, path);
	}
	
	/**
	 * 添加或者更新队列节点，
	 * @param url
	 * @param callBack
	 * @return 如果节点已存在，返回true;
	 * 		   如果节点不存在，返回false
	 */
	protected boolean addOrUpdateQueue(String url, CallBack callBack) {
		if (null == mQueue || null == url || url.trim().length() <= 0 || null == callBack) {
			// 节点不存在
			return false;
		}

		for (QueueNode node : mQueue) {
			if (node.mUrl.equalsIgnoreCase(url)) {
				// 更新节点
				updateQueueNode(node, callBack);
				// 节点已存在
				return true;
			}
		}

		// 添加节点
		addQueueNode(url, callBack);

		// 节点不存在
		return false;
	}
	
	/**
	 * 添加节点
	 * @param url
	 * @param callBack
	 * @return
	 */
	protected boolean addQueueNode(String url, CallBack callBack) {
		if (null == mQueue || null == url || url.trim().length() <= 0 || null == callBack) {
			return false;
		}

		QueueNode node = new QueueNode();
		if (null == node) {
			return false;
		}

		node.mUrl = url;

		node.mCallBackList = new ArrayList<CallBack>();
		if (null == node.mCallBackList) {
			return false;
		}
		node.mCallBackList.add(callBack);

		mQueue.add(node);

		return true;
	}
	
	/**
	 * 更新节点
	 */
	protected boolean updateQueueNode(QueueNode node, CallBack callBack) {
		if (null == node || null == callBack) {
			return false;
		}

		if (null == node.mCallBackList) {
			node.mCallBackList = new ArrayList<CallBack>();
			if (null == node.mCallBackList) {
				return false;
			}
		}

		return node.mCallBackList.add(callBack);
	}
	
	protected <T> boolean download(final String url, final T param) {
		if (null == mContext || null == url || url.trim().length() <= 0) {
			return false;
		}

		NetThreadPool threadPool = NetThreadPool.getInstance();

		if (null == threadPool) {
			return false;
		}

		threadPool.runInThread(new Runnable() {

			@Override
			public void run() {
				doDownload(url, param);

				mIsDownloading = false;
			}
		});
		return true;
	}
	
	protected synchronized <T> void doDownload(final String url, T param) {
		if (null == url) {
			return;
		}

		/**
		 * 1.网络下载
		 * 
		 * 注：可以下载失败，下载失败的话，要从队列中移除当前的node,并请求下一个node。
		 */
		final String path = getFromNet(url, param);

		// 2.下载完成后执行动作
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				afterDoDownload(url, path);
			}
		});
	}
	
	protected void afterDoDownload(String url, String path) {
		if (null == url || null == path || path.trim().length() <= 0) {
			// 下载失败，请求下一个node
			doNextRequest();
			return;
		}

		// 1.网络下载完成后，保存至缓存
		InterfaceCache cacheApi = getCacheInterface();
		if (null != cacheApi) {
			// 如果缓存已存在，新的覆盖旧的；不存在，新建
			cacheApi.add(url, path);
		}

		// 2.调用callback，通知调用者已完成
		if (null != mQueue) {
			for (QueueNode node : mQueue) {
				if (node.mUrl.equalsIgnoreCase(url)) {
					if (null != node.mCallBackList) {
						for (CallBack callBack : node.mCallBackList) {
							callBack.downloadOk(url, path);
						}
					}
				}
			}
		}

		// 3.请求下一个节点
		doNextRequest();
	}
	
	/**
	 * 请求下个节点
	 */
	private void doNextRequest() {

		if (null == mQueue || mQueue.size() <= 0) {
			return;
		}
		// 1.移除当前的node
		mQueue.remove(0);

		// 2.请求当前节点
		requestCurNode();
	}
	
	/**
	 * 请求当前节点
	 * 开始下载当前节点的数据
	 */
	private void requestCurNode() {
		if (null == mQueue || mQueue.size() <= 0 || mIsDownloading) {
			return;
		}

		// 取第一个节点
		QueueNode node = mQueue.get(0);
		if (null == node || null == node.mUrl) {
			// 请求下一个节点
			doNextRequest();
			return;
		}
		mIsDownloading = true;

		// 下载当前节点
		download(node.mUrl, null);
	}
}
