package com.hawk.data.model;

import java.io.Serializable;

/**
 * Created by heyong on 15/8/27.
 */
public class Comment implements Serializable {

    public String id;
    public String nickname;
    public String comment;
    public String time;

    public Comment() {
    }

    public Comment(String id, String comment, String nickname, String time) {
        this.id = id;
        this.comment = comment;
        this.nickname = nickname;
        this.time = time;
    }
}
