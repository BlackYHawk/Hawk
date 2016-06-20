package com.hawk.life.support.db;

import com.hawk.life.support.bean.StatusComment;
import com.hawk.life.support.bean.StatusContent;
import com.hawk.orm.utils.FieldUtils;

import java.util.List;

/**
 * Created by heyong on 16/6/20.
 */
public class TwiterDB {

    private static final String OWNER = "";
    private static final String KEY = "";

    /**
     * 发布状态
     *
     * @param bean 要发布的状态消息
     * @return
     */
    public static void statusCreate(StatusContent bean) {
        HawkDB.getSqlite().insertOrReplace(null, bean);
    }
    /**
     * 获取所有状态
     *
     * @return
     */
    public static List<StatusContent> statusSelectAll() {
        String selection = String.format(" %s = '' ", FieldUtils.KEY);
        String[] selectionArgs = null;

        return HawkDB.getSqlite().select(StatusContent.class, selection, selectionArgs);
    }
    /**
     * 删除状态
     *
     * @param id 要删除的状态ID
     * @return
     */
    public static void statusRemove(String id) {
        HawkDB.getSqlite().deleteById(null, StatusContent.class, id);
    }
    /**
     * 评论状态
     *
     * @param bean 要评论的消息
     * @return
     */
    public static void commentCreate(StatusComment bean) {
        HawkDB.getSqlite().insertOrReplace(null, bean);
    }
    /**
     * 获取状态的所有评论
     *
     * @return
     */
    public static List<StatusComment> commentSelectAll() {
        String selection = String.format(" %s = '' ", FieldUtils.KEY);
        String[] selectionArgs = null;

        return HawkDB.getSqlite().select(StatusComment.class, selection, selectionArgs);
    }
    /**
     * 删除评论
     *
     * @param id 要删除的评论ID
     * @return
     */
    public static void commentRemove(String id) {
        HawkDB.getSqlite().deleteById(null, StatusComment.class, id);
    }

}
