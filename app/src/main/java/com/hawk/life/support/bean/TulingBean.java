package com.hawk.life.support.bean;

import java.io.Serializable;

/**
 * Created by heyong on 16/6/20.
 */
public class TulingBean implements Serializable {

    /**
     * 返回码
     */
    private int code;
    /**
     * 返回内容
     */
    private String text;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
