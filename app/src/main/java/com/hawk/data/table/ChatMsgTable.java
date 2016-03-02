package com.hawk.data.table;

import android.content.ContentValues;

import com.hawk.data.model.ChatMsg;
import com.hawk.middleware.sqlite.BaseTable;

public class ChatMsgTable extends BaseTable {
	private static ChatMsgTable instance = null;

	//表名称
	public static final String TABLE_NAME = "chatMsg";
	
	public static final String ID = "id";
	public static final String CONTENT = "text";
	public static final String TIME = "date";
	public static final String TYPE = "type";
	
	public static ChatMsgTable getInstance()
	{
		if(instance == null)
			instance = new ChatMsgTable();
		return instance;
	}
	
	private ChatMsgTable(){}
	
	@Override
	public <T> String generateSql(T arg0) {
		// TODO Auto-generated method stub
		if (null == arg0) {
			return null;
		}
		
		ChatMsg model = (ChatMsg)arg0;
		
		String sql = " INSERT INTO " + TABLE_NAME
				+ "("+CONTENT+","+TIME+","+TYPE+")"
				+ " VALUES( " 
				+ "'" + model.text + "'" + ", "
				+ "'" + model.date + "'" + ", "
				+ (model.isComMsg?0:1)
				+ " );";
		
		return sql;
	}

	@Override
	public String getCreateTableSql() {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE IF NOT EXISTS" + " " + TABLE_NAME + " " + "("
				// 动态ID,非空
				+ ID + " " + "INTEGER," + " "
				// 内容
				+ CONTENT + " " + "TEXT," + " "
				// 时间
				+ TIME + " " + "TEXT," + " "
				// 标志
				+ TYPE + " " + "INTEGER," + " "
				//设置主键
				+ " PRIMARY KEY ( " + ID + " )"
				// 结束
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
