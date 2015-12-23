package com.hawk.data.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by heyong on 15/5/17.
 */
public class Twiter implements Serializable {

    public String id;
    public String content;
    public List<String> imgPaths;   //上传的图片列表
    public List<String> comments;   //评论列表的ID
    public String time;

    public Twiter () {

    }

    public Twiter(String id, String content, List<String> imgPaths, List<String> comments) {
        this.id = id;
        this.content = content;
        this.imgPaths = imgPaths;
        this.comments = comments;
    }
}
