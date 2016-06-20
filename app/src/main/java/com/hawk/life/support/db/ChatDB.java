package com.hawk.life.support.db;

import com.hawk.life.support.bean.AIChatBean;
import com.hawk.orm.utils.FieldUtils;

import java.util.List;


public class ChatDB {

	private static final String OWNER = "";
	private static final String KEY = "";

	public static void newAIChat(AIChatBean bean) {
		HawkDB.getSqlite().insertOrReplace(null, bean);
	}
	
	public static List<AIChatBean> query() {
		String selection = String.format(" %s = '' ", FieldUtils.KEY);
		String[] selectionArgs = null;
		
		return HawkDB.getSqlite().select(AIChatBean.class, selection, selectionArgs);
	}
	
	public static void remove(String id) {
		HawkDB.getSqlite().deleteById(null, AIChatBean.class, id);
	}
	
}
