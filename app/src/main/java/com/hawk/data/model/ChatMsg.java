
package com.hawk.data.model;

public class ChatMsg {

	public int id;
	
    public String date;

    public String text;

    public boolean isComMsg = true;

    public ChatMsg() {
    }

    public ChatMsg(String date, String text, boolean isComMsg) {
        super();
        this.date = date;
        this.text = text;
        this.isComMsg = isComMsg;
    }

}
