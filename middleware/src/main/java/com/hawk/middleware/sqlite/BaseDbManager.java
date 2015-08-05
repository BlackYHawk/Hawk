package com.hawk.middleware.sqlite;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public abstract class BaseDbManager {
	protected Context context = null;
	protected BaseDbHelper mDbHelper = null;
	
	protected abstract BaseDbHelper getDbHelper();
	
	protected abstract class QueryHandler{
		
		public abstract <T> T handle(Cursor cursor);
		
	}
	
	public BaseDbManager(Context context)
	{
		this.context = context.getApplicationContext();
		mDbHelper = getDbHelper();
	}

	public void openDb()
	{
		if(mDbHelper != null)
			mDbHelper.getReadableDatabase();
	}
	
	public void closeDb()
	{
		if(mDbHelper != null)
			mDbHelper.close();
	}
	
	public <T> long insert(BaseTable table, T model)
	{
		if(table == null || model == null)
			return mDbHelper.HY_INT_VALUE;
		
		ContentValues cv = table.getInsertValues(model);
		if(cv == null)
			return mDbHelper.HY_INT_VALUE;
		
		return insert(table, cv);
	}
	
	protected long insert(BaseTable table, ContentValues cv)
	{
		if(mDbHelper == null || cv == null || table.getTableName() == null)
			return mDbHelper.HY_INT_VALUE;
		
		long rowId = mDbHelper.insert(table.getTableName(), null, cv);
		if(rowId == -1)
			return mDbHelper.HY_INT_VALUE;
		
		return rowId;
	}
	
	protected boolean delete(BaseTable table, String where, String[] args)
	{
		if(mDbHelper == null || table.getTableName() == null)
			return false;
		
		mDbHelper.delete(table.getTableName(), where, args);
		
		return true;
	}
	
	protected int update(BaseTable table, ContentValues values, String where, String[] args)
	{
		if(mDbHelper == null || table.getTableName() == null || values == null)
			return 0;
		
		return mDbHelper.update(table.getTableName(), values, where, args);
	}
	
	public <T> List<T> query(BaseTable table, String[] cols, String where, String[] args, QueryHandler handler)
	{
		if(mDbHelper == null || table.getTableName() == null || handler == null)
			return null;
		
		Cursor cursor = mDbHelper.query(table.getTableName(), cols, where, args, null, null, null);
		
		return cursorToList(cursor, handler);
	}
	
	public <T> List<T> query(BaseTable table, String[] cols, String where, String[] args, String groupBy, String having, String orderBy ,String limit, QueryHandler handler)
	{
		if(mDbHelper == null || table.getTableName() == null || handler == null)
			return null;
		
		Cursor cursor = mDbHelper.query(table.getTableName(), cols, where, args, groupBy, having, orderBy, limit);
		
		return cursorToList(cursor, handler);
	}
	
	public <T> List<T> query(BaseTable table, String[] cols, String where, String[] args, String groupBy, String having, String orderBy, QueryHandler handler)
	{
		if(mDbHelper == null || table.getTableName() == null || handler == null)
			return null;
		
		Cursor cursor = mDbHelper.query(table.getTableName(), cols, where, args, groupBy, having, orderBy);
		
		return cursorToList(cursor, handler);
	}
	
	public <T> List<T> query(boolean distinct, BaseTable table, String[] cols, String where, String[] args, String groupBy, String having, String orderBy, String limit, QueryHandler handler)
	{
		if(mDbHelper == null || table.getTableName() == null || handler == null)
			return null;
		
		Cursor cursor = mDbHelper.query(distinct, table.getTableName(), cols, where, args, groupBy, having, orderBy, limit);
		
		return cursorToList(cursor, handler);
	}
	
	public <T> List<T> rawQuery(String sql, String[] args, QueryHandler handler)
	{
		if(sql == null || args == null || handler == null)
			return null;
		
		Cursor cursor = mDbHelper.rawQuery(sql, args);
		
		return cursorToList(cursor, handler);
	}
	
	protected <T> List<T> cursorToList(Cursor cursor, QueryHandler handler)
	{
		if(cursor == null || handler == null)
			return null;
		
		if(!cursor.moveToFirst())
		{
			cursor.close();
			return null;
		}
		
		if(cursor.getCount() <= 0)
		{
			cursor.close();
			return null;
		}
		
		List<T> results = new LinkedList<T>();
		if(results == null)
		{
			cursor.close();
			return null;
		}
		
		while(!cursor.isAfterLast())
		{
			T result = handler.handle(cursor);
			
			if(result != null)
				results.add(result);
			
			cursor.moveToNext();
		}
		
		cursor.close();
		
		return results;
	}
	
	protected void execSQL(String sql)
	{
		if(mDbHelper == null || sql == null || sql.length() <= 0)
			return;
		
		mDbHelper.execSQL(sql);
	}
	
	protected void execSQL(String sql, String[] args)
	{
		if(mDbHelper == null || sql == null || sql.length() <= 0 || args == null)
			return;
		
		mDbHelper.execSQL(sql, args);
	}
	
	public void beginTransaction()
	{
		if(mDbHelper == null)
			return;
		
		mDbHelper.beginTransaction();
	}
	
	public void setTransactionSuccessful()
	{
		if(mDbHelper == null)
			return;
		
		mDbHelper.setTransactionSuccessful();
	}
	
	public void endTransaction()
	{
		if(mDbHelper == null)
			return;
		
		mDbHelper.endTransaction();
	}
	
}
