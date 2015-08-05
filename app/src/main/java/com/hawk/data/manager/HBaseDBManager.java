/**
 * file name:CardbooBaseDBManager.java
 * package name:com.kinghanhong.cardboo.database.manager 
 * project:Cardboo
 *
 * created by wjin at 2012-3-14
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
package com.hawk.data.manager;

import android.content.Context;

import com.hawk.data.HDBHelper;
import com.hawk.middleware.sqlite.BaseDbHelper;
import com.hawk.middleware.sqlite.BaseDbManager;

/**
 * @author hy
* 卡包项目db manager基类，主要是实现了获取db-helper
*/
public abstract class HBaseDBManager extends BaseDbManager {

	/**
	 * @param context
	 */
	public HBaseDBManager(Context context) {
		super(context);
	}

	/* (non-Javadoc)
	 * @see com.hawk.middleware.sqlite.BaseDbManager#getDbHelper()
	 */
	@Override
	protected BaseDbHelper getDbHelper() {
		if(null == context){
			return null;
		}
		return HDBHelper.getInstance(context);
	}
}