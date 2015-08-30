package com.hawk.data.manager;

import android.content.Context;
import android.database.Cursor;

import com.hawk.data.model.User;
import com.hawk.data.table.UserTable;
import com.hawk.middleware.sqlite.BaseDbHelper;

import java.util.List;

public class UserDBManager extends HBaseDBManager {
	private static UserDBManager mInstance = null;

	private UserTable mUserTable = null;

	public UserDBManager(Context context) {
		super(context);
		mUserTable = UserTable.getInstance();
	}

	/**
	 * 获取实例
	 */
	public static UserDBManager getInstance(Context context){
		if(null == mInstance && null != context){
			mInstance = new UserDBManager(context.getApplicationContext());
		}

		return mInstance;
	}

	/**
	 * 添加聊天记录
	 * @param user
	 */
	public void addUser(User user) {
		if(null == user || null == mUserTable){
			return;
		}

		//添加到CardTable
		execSQL(mUserTable.generateSql(user));

	}

    /**
     * 删除指定动态
     * @param user
     */
    public void deleteUser(User user) {
        if(null == user){
            return;
        }

        //准备删除条件
        String where = UserTable.NICKNAME + " = ?";
        String[] args = {user.nickname};

        //从NoteTable中删除
        delete(mUserTable, where, args);
    }

	/**
	 * 获取聊天记录
	 * @param
	 */
	public List<User> getUsers() {
		if(null == mUserTable){
			return null;
		}

		List<User> msgs = this.query(mUserTable, null, null, null,  new QueryHandler(){

			@Override
			public <T> T handle(Cursor arg0) {
				// TODO Auto-generated method stub
				return (T)toUser(arg0);
			}

		});

		if(msgs == null || msgs.size() <= 0)
			return null;

		return msgs;

	}

	/**
	 * 添加聊天记录
	 * @param
	 */
	public void saveUsers(List<User> users) {
		if(null == users || users.size() <= 0 || null == mUserTable){
			return;
		}

		for(User user : users)
		{
			//添加到CardTable
			execSQL(mUserTable.generateSql(user));
		}

	}

	/**
	 * 将数据库查询结果转成Twiter
	 * @param cursor
	 * @return CardModel
	 */
	protected User toUser(Cursor cursor) {
		if(null == cursor){
			return null;
		}

		User model = new User();

		model.headPath = BaseDbHelper.getStringValue(cursor, UserTable.HEAD_PATH);
		model.nickname = BaseDbHelper.getStringValue(cursor, UserTable.NICKNAME);

		
		return model;
	}
	
}
