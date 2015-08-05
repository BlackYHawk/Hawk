package com.hawk.middleware.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * database操作封装的接口
 */
public abstract class BaseDbHelper extends SQLiteOpenHelper {
	protected SQLiteDatabase mSQLiteDatabase = null;
	public static final int HY_INT_VALUE = -1;
	public static Byte[] HY_SQLITE_SYNCHRONIZED = new Byte[0];

	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public BaseDbHelper(Context context, String name, CursorFactory factory, int version)
	{
		super(context, name, factory, version);
		mSQLiteDatabase = this.getReadableDatabase();
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}
	
	public abstract void deleteDb(SQLiteDatabase db);

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		if(oldVersion < newVersion && db != null)
		{
			deleteDb(db);
			onCreate(db);
		}
	}
	
	public static int getIntegerValue(Cursor cursor, String key)
	{
		if(cursor == null || key == null)
			return HY_INT_VALUE;
		
		int index = cursor.getColumnIndex(key);
		if(index == -1)
			return HY_INT_VALUE;
		return cursor.getInt(index);
	}

	public static long getLongValue(Cursor cursor, String key)
	{
		if(cursor == null || key == null)
			return HY_INT_VALUE;
		
		int index = cursor.getColumnIndex(key);
		if(index == -1)
			return HY_INT_VALUE;
		return cursor.getLong(index);
	}
	
	public static String getStringValue(Cursor cursor, String key)
	{
		if(cursor == null || key == null)
			return null;
		
		int index = cursor.getColumnIndex(key);
		if(index == -1)
			return null;
		return cursor.getString(index);
	}
	
	public static byte[] getBlobValue(Cursor cursor, String key)
	{
		if(cursor == null || key == null)
			return null;
		
		int index = cursor.getColumnIndex(key);
		if(index == -1)
			return null;
		return cursor.getBlob(index);
	}
	
	public void execSQL(String sql)
	{
		if(mSQLiteDatabase == null)
			return;
		
		synchronized(HY_SQLITE_SYNCHRONIZED)
		{
			if(!mSQLiteDatabase.isOpen())
			{
				mSQLiteDatabase = this.getWritableDatabase();
				if(mSQLiteDatabase == null)
					return;
			}
			mSQLiteDatabase.execSQL(sql);
		}
	}
	
	public void execSQL(String sql, Object[] args)
	{
		if(mSQLiteDatabase == null)
			return;
		
		synchronized(HY_SQLITE_SYNCHRONIZED)
		{
			if(!mSQLiteDatabase.isOpen())
			{
				mSQLiteDatabase = this.getWritableDatabase();
				if(mSQLiteDatabase == null)
					return;
			}
			mSQLiteDatabase.execSQL(sql, args);
		}
	}
	
	public int delete(String tableName, String where, String[] args)
	{
		if(mSQLiteDatabase == null)
			return -1;
		
		synchronized(HY_SQLITE_SYNCHRONIZED)
		{
			if(!mSQLiteDatabase.isOpen())
			{
				mSQLiteDatabase = this.getWritableDatabase();
				if(mSQLiteDatabase == null)
					return -1;
			}
			return mSQLiteDatabase.delete(tableName, where, args);
		}
	}
	
	public long insert(String tableName, String nullCol, ContentValues values)
	{
		if(mSQLiteDatabase == null)
			return -1;
		
		synchronized(HY_SQLITE_SYNCHRONIZED)
		{
			if(!mSQLiteDatabase.isOpen())
			{
				mSQLiteDatabase = this.getWritableDatabase();
				if(mSQLiteDatabase == null)
					return -1;
			}
			return mSQLiteDatabase.insert(tableName, nullCol, values);
		}
	}
	
	public int update(String tableName, ContentValues values, String where, String[] args)
	{
		if(mSQLiteDatabase == null)
			return -1;
		
		synchronized(HY_SQLITE_SYNCHRONIZED)
		{
			if(!mSQLiteDatabase.isOpen())
			{
				mSQLiteDatabase = this.getWritableDatabase();
				if(mSQLiteDatabase == null)
					return -1;
			}
			return mSQLiteDatabase.update(tableName, values, where, args);
		}
	}
	
	public long insertOrThrow(String tableName, String nullCol, ContentValues values)
	{
		if(mSQLiteDatabase == null)
			return -1;
		
		synchronized(HY_SQLITE_SYNCHRONIZED)
		{
			if(!mSQLiteDatabase.isOpen())
			{
				mSQLiteDatabase = this.getWritableDatabase();
				if(mSQLiteDatabase == null)
					return -1;
			}
			return mSQLiteDatabase.insertOrThrow(tableName, nullCol, values);
		}
	}
	
	public Cursor query(String tableName, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
	{
		if(mSQLiteDatabase == null)
			return null;
		
		synchronized(HY_SQLITE_SYNCHRONIZED)
		{
			if(!mSQLiteDatabase.isOpen())
			{
				mSQLiteDatabase = this.getReadableDatabase();
				if(mSQLiteDatabase == null)
					return null;
			}
			return mSQLiteDatabase.query(tableName, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
		}
	}
	
	public Cursor query(String tableName, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
	{
		if(mSQLiteDatabase == null)
			return null;
		
		synchronized(HY_SQLITE_SYNCHRONIZED)
		{
			if(!mSQLiteDatabase.isOpen())
			{
				mSQLiteDatabase = this.getReadableDatabase();
				if(mSQLiteDatabase == null)
					return null;
			}
			return mSQLiteDatabase.query(tableName, columns, selection, selectionArgs, groupBy, having, orderBy);
		}
	}
	
	public Cursor query(boolean distinct, String tableName, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
	{
		if(mSQLiteDatabase == null)
			return null;
		
		synchronized(HY_SQLITE_SYNCHRONIZED)
		{
			if(!mSQLiteDatabase.isOpen())
			{
				mSQLiteDatabase = this.getReadableDatabase();
				if(mSQLiteDatabase == null)
					return null;
			}
			return mSQLiteDatabase.query(distinct, tableName, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
		}
	}
	
	public Cursor rawQuery(String sql, String[] args)
	{
		if(mSQLiteDatabase == null)
			return null;
		
		synchronized(HY_SQLITE_SYNCHRONIZED)
		{
			if(!mSQLiteDatabase.isOpen())
			{
				mSQLiteDatabase = this.getReadableDatabase();
				if(mSQLiteDatabase == null)
					return null;
			}
		}
		return mSQLiteDatabase.rawQuery(sql, args);
	}
	
	public long replace(String tableName, String nullCol, ContentValues values)
	{
		if(mSQLiteDatabase == null)
			return -1;
		
		synchronized(HY_SQLITE_SYNCHRONIZED)
		{
			if(!mSQLiteDatabase.isOpen())
			{
				mSQLiteDatabase = this.getWritableDatabase();
				if(mSQLiteDatabase == null)
					return -1;
			}
			return mSQLiteDatabase.replace(tableName, nullCol, values);
		}
	}
	
	public void beginTransaction()
	{
		if(mSQLiteDatabase != null)
		{
			mSQLiteDatabase.beginTransaction();
		}
	}
	
	public void setTransactionSuccessful()
	{
		if(mSQLiteDatabase != null)
		{
			mSQLiteDatabase.setTransactionSuccessful();
		}
	}
	
	public void endTransaction()
	{
		if(mSQLiteDatabase != null)
		{
			mSQLiteDatabase.endTransaction();
		}
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		if(mSQLiteDatabase == null)
			return;
		
		synchronized(HY_SQLITE_SYNCHRONIZED)
		{
			if(!mSQLiteDatabase.isOpen())
				return;
			mSQLiteDatabase.close();
		}
	}
	
	
	
}
