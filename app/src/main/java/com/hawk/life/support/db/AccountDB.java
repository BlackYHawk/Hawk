package com.hawk.life.support.db;

import com.hawk.life.support.bean.AccountBean;
import com.hawk.orm.extra.Extra;
import com.hawk.orm.utils.FieldUtils;

import java.util.List;


public class AccountDB {

	private static final String OWNER = "";
	private static final String KEY = "Logedin";

	public static void setLogedinAccount(AccountBean bean) {
		// 删除之前登录的账户
		String whereClause = String.format(" %s = ? ", FieldUtils.KEY);
		String[] whereArgs = new String[]{ KEY };
		HawkDB.getSqlite().delete(AccountBean.class, whereClause, whereArgs);

		if (bean != null) {
			// 设置当前登录账户
			HawkDB.getSqlite().insertOrReplace(new Extra(OWNER, KEY), bean);
		}
		else {
			HawkDB.getSqlite().delete(AccountBean.class, whereClause, whereArgs);
		}
	}
	
	public static AccountBean getLogedinAccount() {
		String selection = String.format(" %s = ? ", FieldUtils.KEY);
		String[] selectionArgs = new String[]{ KEY };
		
		List<AccountBean> beans = HawkDB.getSqlite().select(AccountBean.class, selection, selectionArgs);
		if (beans.size() > 0)
			return beans.get(0);
		
		return null;
	}
	
	public static void newAccount(AccountBean bean) {
		HawkDB.getSqlite().insertOrReplace(null, bean);
	}
	
	public static List<AccountBean> query() {
		String selection = String.format(" %s = '' ", FieldUtils.KEY);
		String[] selectionArgs = null;
		
		return HawkDB.getSqlite().select(AccountBean.class, selection, selectionArgs);
	}
	
	public static void remove(String id) {
		HawkDB.getSqlite().deleteById(null, AccountBean.class, id);
	}
	
}
