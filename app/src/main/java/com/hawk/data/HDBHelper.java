/**
 * file name:CardbooDBHelper.java
 * package name:com.kinghanhong.cardboo.database 
 * project:Cardboo
 *
 * created by wjin at 2012-7-31
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
package com.hawk.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.hawk.data.table.CommentTable;
import com.hawk.data.table.TwiterTable;
import com.hawk.data.table.UserTable;
import com.hawk.middleware.sqlite.BaseDbHelper;

/**
 * @author wjin
 * 
 */
public class HDBHelper extends BaseDbHelper {
	// 数据库名称
	private static final String H_DB_NAME = "hawk.db";

	// 数据库版本
	private static final int H_DB_VERSION = 1;

	// 实例
	private static HDBHelper mInstance = null;

	/**
	 * 获取实例
	 * 
	 * 注：context为application的context
	 */
	public static HDBHelper getInstance(Context context) {
		if (null == mInstance && null != context) {
			mInstance = new HDBHelper(context.getApplicationContext());
		}

		return mInstance;
	}

	/**
	 * 构造函数，创建数据库
	 * @param context
	 */
	private HDBHelper(Context context) {
		super(context, H_DB_NAME, null, H_DB_VERSION);
	}

	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public HDBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kinghanhong.middleware.sqlite.BaseDbHelper#deleteDb(android.database
	 * .sqlite.SQLiteDatabase)
	 */
	@Override
	public void deleteDb(SQLiteDatabase arg0) {
		if (null == arg0) {
			return;
		}

		String sql = "DROP TABLE IF EXISTS ";
		
		if(null != TwiterTable.getInstance()){
			arg0.execSQL(sql + TwiterTable.getInstance().getTableName());
		}
		if(null != CommentTable.getInstance()){
			arg0.execSQL(sql + CommentTable.getInstance().getTableName());
		}
		if(null != UserTable.getInstance()){
			arg0.execSQL(sql + UserTable.getInstance().getTableName());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
	 * .SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		if (null == db) {
			return;
		}

		if(null != TwiterTable.getInstance()){
			db.execSQL(TwiterTable.getInstance().getCreateTableSql());
		}
		if(null != CommentTable.getInstance()){
			db.execSQL(CommentTable.getInstance().getCreateTableSql());
		}
		if(null != UserTable.getInstance()){
			db.execSQL(UserTable.getInstance().getCreateTableSql());
		}
	}
}
