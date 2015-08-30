package com.hawk.data.table;

import android.content.ContentValues;

import com.hawk.data.model.User;
import com.hawk.middleware.sqlite.BaseTable;

public class UserTable extends BaseTable {
	private static UserTable instance = null;

	//表名称
	public static final String TABLE_NAME = "users";

    public static final String HEAD_PATH = "head_path";
	public static final String NICKNAME = "nickname";

	public static UserTable getInstance()
	{
		if(instance == null)
			instance = new UserTable();
		return instance;
	}

	private UserTable(){}
	
	@Override
	public <T> String generateSql(T arg0) {
		// TODO Auto-generated method stub
		if (null == arg0) {
			return null;
		}
		
		User model = (User)arg0;
		
		String sql = " INSERT INTO " + TABLE_NAME 
				+ "(" + HEAD_PATH + "," + NICKNAME + ")"
				+ " VALUES('" + model.headPath + "','"
				+ model.nickname + "'"
				+ " )";
		
		return sql;
	}

	@Override
	public String getCreateTableSql() {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE IF NOT EXISTS" + " " + TABLE_NAME + " " + "("
                // 标题
                + HEAD_PATH + " " + "TEXT," + " "
				// 内容
				+ NICKNAME + " " + "TEXT," + " "
				//设置主键
				+ " PRIMARY KEY ( " + NICKNAME + " )"
				+ ")";
		return sql;
	}

	@Override
	public <T> ContentValues getInsertValues(T arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return TABLE_NAME;
	}

}
