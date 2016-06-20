
package com.hawk.life.support.bean;

import com.hawk.orm.annotation.PrimaryKey;

import java.io.Serializable;

public class AIChatBean implements Serializable {
    private static final long serialVersionUID = -6805443927915693862L;

    @PrimaryKey(column = "aiChatId")
	public int chatId;
	
    public String date;

    public String text;

    public boolean isComMsg = true;

    public AIChatBean() {
    }

    public AIChatBean(String date, String text, boolean isComMsg) {
        super();
        this.date = date;
        this.text = text;
        this.isComMsg = isComMsg;
    }

}
