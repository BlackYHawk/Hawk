package com.hawk.data.model;

import java.io.Serializable;
/**
 * Created by heyong on 15/5/17.
 */
public class Twiter implements Serializable {

    public String id;
    public String title;
    public String content;
    public String time;

    public Twiter () {

    }

    public Twiter(String title, String content, String time) {
        this.title = title;
        this.content = content;
        this.time = time;
    }
}
