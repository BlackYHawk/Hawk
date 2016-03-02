package com.hawk.data.manager;

import android.content.Context;
import android.database.Cursor;

import com.hawk.data.model.ChatMsg;
import com.hawk.data.table.ChatMsgTable;
import com.hawk.middleware.sqlite.BaseDbHelper;

import java.util.List;

public class ChatMsgDBManager extends HBaseDBManager {
	private static ChatMsgDBManager mInstance = null;
	
	private ChatMsgTable mChatMsgTable = null;

	public ChatMsgDBManager(Context context) {
		super(context);
		mChatMsgTable = ChatMsgTable.getInstance();
	}

	/**
	 * 获取实例
	 */
	public static ChatMsgDBManager getInstance(Context context){
		if(null == mInstance && null != context){
			mInstance = new ChatMsgDBManager(context.getApplicationContext());
		}
		
		return mInstance;
	}
	
	/**
	 * 添加聊天记录
	 * @param chatMsg
	 */
	public void addChatMsg(ChatMsg chatMsg) {
		if(null == chatMsg || null == mChatMsgTable){
			return;
		}
		
		//添加到CardTable
		execSQL(mChatMsgTable.generateSql(chatMsg));

	}
	
	/**
	 * 获取聊天记录
	 */
	public List<ChatMsg> getChatMsgs() {
		if(null == mChatMsgTable){
			return null;
		}
		
		List<ChatMsg> msgs = this.query(mChatMsgTable, null, null, null,  new QueryHandler(){

			@Override
			public <T> T handle(Cursor arg0) {
				// TODO Auto-generated method stub
				return (T)toChatMsg(arg0);
			}
			
		});
		
		if(msgs == null || msgs.size() <= 0)
			return null;
		
		return msgs;

	}
	
	/**
	 * 添加聊天记录
	 * @param chatMsgs
	 */
	public void saveChatMsgs(List<ChatMsg> chatMsgs) {
		if(null == chatMsgs || chatMsgs.size() <= 0 || null == mChatMsgTable){
			return;
		}
		
		for(ChatMsg chatMsg : chatMsgs)
		{
			//添加到CardTable
			execSQL(mChatMsgTable.generateSql(chatMsg));
		}
		
	}

	/**
	 * 将数据库查询结果转成ChatMsg
	 * @param cursor
	 * @return CardModel
	 */
	protected ChatMsg toChatMsg(Cursor cursor) {
		if(null == cursor){
			return null;
		}
		
		ChatMsg model = new ChatMsg();
		
		model.id = BaseDbHelper.getIntegerValue(cursor, ChatMsgTable.ID);
		model.text = BaseDbHelper.getStringValue(cursor, ChatMsgTable.CONTENT);
		model.date = BaseDbHelper.getStringValue(cursor, ChatMsgTable.TIME);
		model.isComMsg = BaseDbHelper.getIntegerValue(cursor, ChatMsgTable.TYPE)==0?true:false;
		
		return model;
	}
	
}
