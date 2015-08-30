package com.hawk.data.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by heyong on 15/5/17.
 */
public class Twiter implements Serializable {

    public String id;
    public List<String> imgPaths;   //上传的图片列表
    public List<String> comments;   //评论列表的ID

    public Twiter () {

    }

    public Twiter(String id, List<String> imgPaths, List<String> comments) {
        this.id = id;
        this.imgPaths = imgPaths;
        this.comments = comments;
    }
}
