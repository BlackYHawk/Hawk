package com.hawk.data.table;

import android.content.ContentValues;

import com.hawk.data.model.Twiter;
import com.hawk.middleware.sqlite.BaseTable;

public class TwiterTable extends BaseTable {
	private static TwiterTable instance = null;

	//表名称
	public static final String TABLE_NAME = "twiter";
	
	public static final String ID = "id";
    public static final String TITLE = "title";
	public static final String CONTENT = "content";
	public static final String TIME = "time";
	
	public static TwiterTable getInstance()
	{
		if(instance == null)
			instance = new TwiterTable();
		return instance;
	}
	
	private TwiterTable(){}
	
	@Override
	public <T> String generateSql(T arg0) {
		// TODO Auto-generated method stub
		if (null == arg0) {
			return null;
		}
		
		Twiter model = (Twiter)arg0;
		
		String sql = " INSERT INTO " + TABLE_NAME 
				+ "(" + ID + "," + TITLE + "," + CONTENT + "," + TIME + ")"
				+ " VALUES('" + model.id + "','"
                + model.title + "','"
				+ model.content + "','"
				+ model.time + "'"
				+ " )";
		
		return sql;
	}

	@Override
	public String getCreateTableSql() {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE IF NOT EXISTS" + " " + TABLE_NAME + " " + "("
				// 动态ID,非空
				+ ID + " " + "TEXT," + " "
                // 标题
                + TITLE + " " + "TEXT," + " "
				// 内容
				+ CONTENT + " " + "TEXT," + " "
				// 时间
				+ TIME + " " + "TEXT," + " "
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
