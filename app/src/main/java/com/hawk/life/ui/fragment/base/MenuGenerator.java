package com.hawk.life.ui.fragment.base;

import com.hawk.life.support.bean.MenuBean;
import com.hawk.ui.activity.R;

import java.util.ArrayList;

/**
 * 所有的首页菜单维护
 *
 * Created by wangdan on 15/4/14.
 */
public class MenuGenerator {

    public static ArrayList<MenuBean> generateMenus() {
        ArrayList<MenuBean> menuList = new ArrayList<MenuBean>();

        // 首页
        menuList.add(generateMenu("1"));
        // 提及
        menuList.add(generateMenu("2"));
        // 评论
        menuList.add(generateMenu("3"));
        // 私信
        menuList.add(generateMenu("10"));
        // 分割线
        menuList.add(generateMenu("1000"));
        // 热门微博
        menuList.add(generateMenu("11"));
        // 草稿
        menuList.add(generateMenu("6"));
        // 设置
        menuList.add(generateMenu("5"));

        return menuList;
    }

    public static MenuBean generateMenu(String type) {
        MenuBean menuBean = null;

        switch (Integer.parseInt(type)) {
            // 个人信息
            case 0:
                menuBean = new MenuBean(-1, R.string.draw_profile, R.string.draw_profile, "0");
                break;
            // 微博首页
            case 1:
                menuBean = new MenuBean(R.drawable.ic_view_day_grey600_24dp, R.string.draw_timeline, R.string.draw_timeline, "1");
                break;
            // 分割线
            case 1000:
                menuBean = new MenuBean(-1, R.string.app_name, R.string.app_name, "1000");
                break;
        }

        return menuBean;
    }

}
