package com.hawk.data.manager;

import android.content.Context;
import android.database.Cursor;

import com.hawk.data.model.Comment;
import com.hawk.data.table.CommentTable;
import com.hawk.middleware.sqlite.BaseDbHelper;

import java.util.List;

public class CommentDBManager extends HBaseDBManager {
	private static CommentDBManager mInstance = null;

	private CommentTable mCommentTable = null;

	public CommentDBManager(Context context) {
		super(context);
		mCommentTable = CommentTable.getInstance();
	}

	/**
	 * 获取实例
	 */
	public static CommentDBManager getInstance(Context context){
		if(null == mInstance && null != context){
			mInstance = new CommentDBManager(context.getApplicationContext());
		}

		return mInstance;
	}

	/**
	 * 添加聊天记录
	 * @param comment
	 */
	public void addComment(Comment comment) {
		if(null == comment || null == mCommentTable){
			return;
		}

		//添加到CardTable
		execSQL(mCommentTable.generateSql(comment));

	}

    /**
     * 删除指定动态
     * @param comment
     */
    public void deleteComment(Comment comment) {
        if(null == comment){
            return;
        }

        //准备删除条件
        String where = CommentTable.ID + " = ?";
        String[] args = {comment.id};

        //从NoteTable中删除
        delete(mCommentTable, where, args);
    }

	/**
	 * 获取聊天记录
	 * @param
	 */
	public List<Comment> getComments() {
		if(null == mCommentTable){
			return null;
		}

		List<Comment> msgs = this.query(mCommentTable, null, null, null,  new QueryHandler(){

			@Override
			public <T> T handle(Cursor arg0) {
				// TODO Auto-generated method stub
				return (T)toComment(arg0);
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
	public void saveComments(List<Comment> comments) {
		if(null == comments || comments.size() <= 0 || null == mCommentTable){
			return;
		}

		for(Comment comment : comments)
		{
			//添加到CardTable
			execSQL(mCommentTable.generateSql(comment));
		}

	}

	/**
	 * 将数据库查询结果转成Twiter
	 * @param cursor
	 * @return CardModel
	 */
	protected Comment toComment(Cursor cursor) {
		if(null == cursor){
			return null;
		}

		Comment model = new Comment();

		model.id = BaseDbHelper.getStringValue(cursor, CommentTable.ID);
		model.nickname = BaseDbHelper.getStringValue(cursor, CommentTable.NICKNAME);
		model.comment = BaseDbHelper.getStringValue(cursor, CommentTable.COMMENT);
		model.time = BaseDbHelper.getStringValue(cursor, CommentTable.TIME);
		
		return model;
	}
	
}
