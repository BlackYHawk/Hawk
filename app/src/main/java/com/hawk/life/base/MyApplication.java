package com.hawk.life.base;

import com.baidu.mapapi.SDKInitializer;
import com.hawk.library.common.context.GlobalContext;
import com.hawk.library.common.setting.SettingUtility;
import com.hawk.library.common.utils.Logger;
import com.hawk.library.component.bitmaploader.BitmapLoader;
import com.hawk.life.support.bean.AccountBean;
import com.hawk.life.support.db.AccountDB;
import com.hawk.life.support.db.EmotionsDB;

public class MyApplication extends GlobalContext {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		//baidu map
		SDKInitializer.initialize(this);
		// 初始化图片加载
		BitmapLoader.newInstance(this, getImagePath());

		// 设置登录账户
		AccountBean accountBean = AccountDB.getLogedinAccount();
		SettingUtility.addSettings("meizt_actions");

		// 检查表情
		try {
			EmotionsDB.checkEmotions();
		} catch (Exception e) {
		}
		// 打开Debug日志
		Logger.DEBUG = true;
	}

}
