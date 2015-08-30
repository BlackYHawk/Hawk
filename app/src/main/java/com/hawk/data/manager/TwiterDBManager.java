package com.hawk.data.manager;

import android.content.Context;
import android.database.Cursor;

import com.hawk.data.model.Twiter;
import com.hawk.data.table.TwiterTable;
import com.hawk.middleware.sqlite.BaseDbHelper;
import com.hawk.middleware.util.StringUtil;

import java.util.List;

public class TwiterDBManager extends HBaseDBManager {
	private static TwiterDBManager mInstance = null;
	
	private TwiterTable mTwiterTable = null;

	public TwiterDBManager(Context context) {
		super(context);
		mTwiterTable = TwiterTable.getInstance();
	}

	/**
	 * 获取实例
	 */
	public static TwiterDBManager getInstance(Context context){
		if(null == mInstance && null != context){
			mInstance = new TwiterDBManager(context.getApplicationContext());
		}
		
		return mInstance;
	}
	
	/**
	 * 添加聊天记录
	 * @param twiter
	 */
	public void addTwiter(Twiter twiter) {
		if(null == twiter || null == mTwiterTable){
			return;
		}
		
		//添加到CardTable
		execSQL(mTwiterTable.generateSql(twiter));

	}

    /**
     * 删除指定动态
     * @param twiter
     */
    public void deleteTwiter(Twiter twiter) {
        if(null == twiter){
            return;
        }

        //准备删除条件
        String where = TwiterTable.ID + " = ?";
        String[] args = {twiter.id};

        //从NoteTable中删除
        delete(mTwiterTable, where, args);
    }
	
	/**
	 * 获取聊天记录
	 * @param
	 */
	public List<Twiter> getTwiters() {
		if(null == mTwiterTable){
			return null;
		}
		
		List<Twiter> msgs = this.query(mTwiterTable, null, null, null,  new QueryHandler(){

			@Override
			public <T> T handle(Cursor arg0) {
				// TODO Auto-generated method stub
				return (T)toTwiter(arg0);
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
	public void saveTwiters(List<Twiter> twiters) {
		if(null == twiters || twiters.size() <= 0 || null == mTwiterTable){
			return;
		}
		
		for(Twiter twiter : twiters)
		{
			//添加到CardTable
			execSQL(mTwiterTable.generateSql(twiter));
		}
		
	}

	/**
	 * 将数据库查询结果转成Twiter
	 * @param cursor
	 * @return CardModel
	 */
	protected Twiter toTwiter(Cursor cursor) {
		if(null == cursor){
			return null;
		}
		
		Twiter model = new Twiter();
		
		model.id = BaseDbHelper.getStringValue(cursor, TwiterTable.ID);

		String imgPaths = BaseDbHelper.getStringValue(cursor, TwiterTable.IMG_PATHS);
		String comments = BaseDbHelper.getStringValue(cursor, TwiterTable.COMMENTS);

		model.imgPaths = StringUtil.transformStringToListEX(imgPaths);
		model.comments = StringUtil.transformStringToListEX(comments);

		
		return model;
	}
	
}
