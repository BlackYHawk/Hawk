package com.hawk.life.base;


import com.hawk.life.support.bean.AccountBean;
import com.hawk.life.support.bean.WeiBoUser;

/**
 * Created by wangdan on 15/4/12.
 */
public class AppContext {

    private static AccountBean accountBean;// 当前登录的用户信息


    public static AccountBean getAccount() {
        return accountBean;
    }

    public static WeiBoUser getUser() {
        return accountBean.getUser();
    }
}
