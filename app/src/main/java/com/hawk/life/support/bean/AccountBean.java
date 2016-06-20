package com.hawk.life.support.bean;

import com.hawk.orm.annotation.PrimaryKey;

import java.io.Serializable;


public class AccountBean implements Serializable {

	private static final long serialVersionUID = -6805443927915693862L;

	@PrimaryKey(column = "userId")
	private String userId;

    private WeiBoUser user;// 用户的个人信息

    private String account;// 用户的账号

    private String password;// 用户的密码

    private String cookie;// 网页授权的cookie

    public WeiBoUser getUser() {
        return user;
    }

    public void setUser(WeiBoUser user) {
        this.user = user;
    }

    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }
}
