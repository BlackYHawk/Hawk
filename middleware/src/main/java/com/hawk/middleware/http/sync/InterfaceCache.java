/**
 * file name:InterfaceCache.java
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

/**
 * @author houxin
 * 缓存接口，主要有三个：
 * 1.添加缓存
 * 2.获取缓存
 * 3.删除缓存
 */
public interface InterfaceCache {
	/**
	 * 添加缓存：网址，本地文件路径
	 */
	public boolean add(String url, String path);
	
	/**
	 * 获取缓存，输入网址，如果本地有文件存在，返回相应文件路径；否则，返回null
	 * @param url
	 * @return
	 */
	public String get(String url);
	
	/**
	 * 删除指定缓存。
	 * @param url
	 * @return
	 */
	public void del(String url);
}
