package com.hawk.data.table;

import android.content.ContentValues;

import com.hawk.data.model.Comment;
import com.hawk.middleware.sqlite.BaseTable;

public class CommentTable extends BaseTable {
	private static CommentTable instance = null;

	//表名称
	public static final String TABLE_NAME = "comment";

	public static final String ID = "id";
    public static final String NICKNAME = "nickname";
	public static final String COMMENT = "comment";
	public static final String TIME = "time";

	public static CommentTable getInstance()
	{
		if(instance == null)
			instance = new CommentTable();
		return instance;
	}

	private CommentTable(){}
	
	@Override
	public <T> String generateSql(T arg0) {
		// TODO Auto-generated method stub
		if (null == arg0) {
			return null;
		}
		
		Comment model = (Comment)arg0;
		
		String sql = " INSERT INTO " + TABLE_NAME 
				+ "(" + ID + "," + NICKNAME + "," + COMMENT + "," + TIME + ")"
				+ " VALUES('" + model.id + "','"
				+ model.nickname + "','"
				+ model.comment + "','"
				+ model.time + "'"
				+ " )";
		
		return sql;
	}

	@Override
	public String getCreateTableSql() {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE IF NOT EXISTS" + " " + TABLE_NAME + " " + "("
				// 主键
				+ ID + " " + "TEXT," + " "
                // 标题
                + NICKNAME + " " + "TEXT," + " "
				// 内容
				+ COMMENT + " " + "TEXT," + " "
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
