package com.hawk.data.model;

import java.io.Serializable;

/**
 * Created by heyong on 15/8/27.
 */
public class User implements Serializable {

    public String headPath;
    public String nickname;

    public User() {
    }

    public User(String headPath, String nickname) {
        this.headPath = headPath;
        this.nickname = nickname;
    }
}
