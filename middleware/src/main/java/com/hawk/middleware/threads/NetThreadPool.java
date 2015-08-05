/**
 * file name:KhhThreadPool.java
 * package name:com.kinghanhong.middleware.threads 
 * project: LoveCard
 *
 * created by HouXin at 2011-12-30
 * 
 * Copyright @2011, JinHanHong Software, Co, Ltd. All Rights Reserved.
 *
 * Copyright Notice
 * JinHanHong copyrights this specification.No parts of this specification
 * may be reproduced in any form or means, without the prior written consent
 * of JingHanHong.
 *
 * Disclaimer
 * This specification is preliminary and is subject to change at any time
 * without notice. JinHanHong assumes no responsibility for any errors 
 * contained herein.
 */
package com.hawk.middleware.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author HouXin
 * 线程池，使用java的executor service实现的线程池机制，
 * 
 * 实现原理：
 * 1.使用newFixedThreadPool()机制，建立固定线程数量的线程池的executor service
 * 2.在创建线程执行时，使用ExecutorService.submit(Runnable task)，将任务放入线程池中执行
 * 
 * executor service有三种类型的线程池：
 * 1.newCachedThreadPool, 缓存型池子，先查看池中有没有以前建立的线程，如果有，就reuse.如果没有，就建一个新的线程加入池中
 * 2.newFixedThreadPool,任意时间点，最多只能有固定数目的活动线程存在，此时如果有新的线程要建立，只能放在另外的队列中等待，
 * 直到当前的线程中某个线程终止直接被移出池子
 * 3.ScheduledThreadPool,调度型线程池，这个池子里的线程可以按schedule依次delay执行，或周期执行
 * 4.SingleThreadExecutor,单例线程，任意时间池中只能有一个线程
 * 
 */
public class NetThreadPool {
	private static NetThreadPool mInstance = null;
	
	private ExecutorService mExecutor = null;
	private final int KHH_THREAD_POOL_MAX_RUN_SIZE = 4;
	
	/**
	 * 获取实例
	 */
	public static NetThreadPool getInstance(){
		if(null == mInstance){
			mInstance = new NetThreadPool();
		}
		
		return mInstance;
	}
	
	/**
	 * 构造函数:
	 * 1.创建线程池
	 */
	private NetThreadPool(){
		mExecutor = Executors.newFixedThreadPool(KHH_THREAD_POOL_MAX_RUN_SIZE);
	}
	
	/**
	 * 在线程中执行
	 */
	public void runInThread(Runnable runnable){
		if(null == mExecutor || null == runnable){
			return;
		}
		
		try {
			mExecutor.submit(runnable);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
