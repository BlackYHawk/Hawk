package com.hawk.middleware.sqlite;

import android.content.ContentValues;

/**
 * sqlite table类的基类，封装：
 * 1.表的创建
 * 2.表的删除
 */
public abstract class BaseTable {

	public abstract String getCreateTableSql();
	
	public abstract String getTableName();
	
	public abstract <T> ContentValues getInsertValues(T model);
	
	public abstract <T> String generateSql(T model);
	
	public String isNull(String value)
	{
		return value == null ? "":value;
	}
	
}
