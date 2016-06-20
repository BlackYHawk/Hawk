package com.hawk.life.ui.fragment.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hawk.library.common.utils.ActivityHelper;
import com.hawk.library.common.utils.Logger;
import com.hawk.library.common.utils.SystemBarUtils;
import com.hawk.library.component.bitmaploader.core.ImageConfig;
import com.hawk.library.support.adapter.ABaseAdapter;
import com.hawk.library.support.inject.ViewInject;
import com.hawk.library.ui.fragment.AListFragment;
import com.hawk.life.base.AppContext;
import com.hawk.life.support.bean.AccountBean;
import com.hawk.life.support.bean.MenuBean;
import com.hawk.life.support.utils.AisenUtils;
import com.hawk.life.ui.activity.base.MainActivity;
import com.hawk.ui.activity.R;

import java.util.ArrayList;


/**
 * 左侧抽屉菜单
 *
 * Created by wangdan on 15/4/14.
 */
public class MenuFragment extends AListFragment<MenuBean, ArrayList<MenuBean>> {

    public static MenuFragment newInstance(String type) {
        MenuFragment fragment = new MenuFragment();

        if (!TextUtils.isEmpty(type)) {
            Bundle args = new Bundle();
            args.putString("type", type);
            fragment.setArguments(args);
        }

        return fragment;
    }

    public static final String FRAGMENT_TAG = "MenuFragment";
    private MenuBean lastSelectedMenu;

    private int draftSize;

    private View profileHeader;

    TextView txtFollowersNewHint;

    @Override
    protected int inflateContentView() {
        return R.layout.fragment_ui_menu;
    }

    @Override
    protected ABaseAdapter.AbstractItemView<MenuBean> newItemView() {
        return new MenuItemView();
    }

    @Override
    protected void requestData(RefreshMode mode) {

    }

    @Override
    protected void setInitRefreshView(AbsListView refreshView, Bundle savedInstanceSate) {
        super.setInitRefreshView(refreshView, savedInstanceSate);

        profileHeader = View.inflate(getActivity(), R.layout.layout_leftmenu, null);

        txtFollowersNewHint = (TextView) profileHeader.findViewById(R.id.txtFollowersNewHint);

        if (Build.VERSION.SDK_INT >= 19) {
            ViewGroup rootProfile = (ViewGroup) profileHeader.findViewById(R.id.layProfile);
            rootProfile.setPadding(rootProfile.getPaddingLeft(),
                    SystemBarUtils.getStatusBarHeight(getActivity()),
                    rootProfile.getPaddingRight(),
                    rootProfile.getPaddingBottom());
        }

        setAccountItem();

        getListView().addHeaderView(profileHeader);
    }

    @Override
    protected void layoutInit(LayoutInflater inflater, Bundle savedInstanceSate) {
        super.layoutInit(inflater, savedInstanceSate);

        setItems(MenuGenerator.generateMenus());

        if (savedInstanceSate == null) {
            int index = getListView().getHeaderViewsCount();

            lastSelectedMenu = MenuGenerator.generateMenu("1");
            if (getArguments() != null) {
                String type = getArguments().getString("type");

                lastSelectedMenu = MenuGenerator.generateMenu(type);
                for (int i = 0; i < getAdapterItems().size(); i++) {
                    MenuBean bean = getAdapterItems().get(i);
                    if (bean.getType().equals(type)) {
                        lastSelectedMenu = bean;
                        index = i + getListView().getHeaderViewsCount();
                        break;
                    }
                }

            }

            if (index <= getListView().getHeaderViewsCount()) {
                onMenuClicked(lastSelectedMenu, null);
            }
            else {
                onItemClick(getListView(), null, index, index);
            }
        }
        else {
            lastSelectedMenu = (MenuBean) savedInstanceSate.getSerializable("lastSelectedMenu");
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("lastSelectedMenu", lastSelectedMenu);
    }

    public void setAccountItem() {
        AccountBean account = AppContext.getAccount();

        // 头像
        ImageView imgPhoto = (ImageView) profileHeader.findViewById(R.id.imgPhoto);
        imgPhoto.setOnClickListener(viewOnClickListener);
//        imgPhoto.setOnLongClickListener(viewOnLongClickListener);
/*        BitmapLoader.getInstance().display(MenuFragment.this,
                user.getAvatar_large(), imgPhoto, ImageConfigUtils.getLargePhotoConfig());*/
        // 名字
        TextView txtName = (TextView) profileHeader.findViewById(R.id.txtName);
        txtName.setText(account.getAccount());

        // 背景
        ImageConfig coverConfig = new ImageConfig();
        coverConfig.setLoadfaildRes(R.drawable.bg_banner_dialog);
        coverConfig.setLoadingRes(R.drawable.bg_banner_dialog);
        final ImageView imgCover = (ImageView) profileHeader.findViewById(R.id.imgCover);
      //  BitmapLoader.getInstance().display(this, user.getCover_image_phone(), imgCover, coverConfig);
    }

    public void setSelectedMenu(MenuBean menu) {
        lastSelectedMenu = menu;
        notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();

        getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();

    }


    private void updateCounter(TextView view, MenuBean menu) {

        view.setVisibility(View.INVISIBLE);

        int count = 0;
        switch (Integer.parseInt(menu.getType())) {
            // 草稿
            case 6:
                count = draftSize;
                break;
            // 设置
            case 5:
                count = ActivityHelper.getBooleanShareData("newVersion", false) ? 1 : 0;
                break;
            default:
                break;
        }

        if (count > 0) {
            if (count > 100)
                view.setText("100+");
            else
                view.setText(String.valueOf(count));

            view.setVisibility(View.VISIBLE);
        }
    }

    private View.OnClickListener viewOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.imgPhoto) {
                if (txtFollowersNewHint != null && txtFollowersNewHint.getVisibility() == View.VISIBLE) {
                    onMenuClicked(MenuGenerator.generateMenu("4"), v);
                    ((MainActivity) getActivity()).closeDrawer();
                }
                else {

                }
            }
        }
    };

    private boolean onMenuClicked(MenuBean menu, View view) {
        if ("1000".equals(menu.getType()))
            return true;

        MainActivity activity = (MainActivity) getActivity();
        if (activity != null && activity.onMenuSelected(menu, false, view)) {
            return true;
        }

        lastSelectedMenu = menu;
        getAdapter().notifyDataSetChanged();

        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
        MenuBean entity = null;

        if (position == 0) {
            entity = MenuGenerator.generateMenu("0");
            return;
        }
        else {
            entity = getAdapterItems().get(position - getListView().getHeaderViewsCount());
        }

        if ("1000".equals(entity.getType()))
            return;

        if ("0".equals(entity.getType()))
            Logger.d("查看用户信息");
        else
            Logger.d(getString(entity.getTitleRes()));

        if (onMenuClicked(entity, view))
            return;

        ((MainActivity) getActivity()).closeDrawer();
    }

    class MenuItemView extends ABaseAdapter.AbstractItemView<MenuBean> {

        @ViewInject(id = R.id.txtTitle)
        TextView txtTitle;
        @ViewInject(id = R.id.txtCounter)
        TextView txtCounter;
        @ViewInject(id = R.id.layIcon)
        View layIcon;
        @ViewInject(id = R.id.imgIcon)
        ImageView imgIcon;
        @ViewInject(id = R.id.viewDivider)
        View viewDivider;
        @ViewInject(id = R.id.layItem)
        View layItem;

        @Override
        public int inflateViewId() {
            return R.layout.layout_item_menu;
        }

        @Override
        public void bindingData(View convertView, MenuBean data) {
            txtTitle.setText(data.getMenuTitleRes());
        }

        @Override
        public void updateConvertView(MenuBean data, View convertView, int selectedPosition) {
            super.updateConvertView(data, convertView, selectedPosition);
            if ("1000".equals(data.getType())) {
                viewDivider.setVisibility(View.VISIBLE);

                layItem.setVisibility(View.GONE);
            }
            else {
                viewDivider.setVisibility(View.GONE);

                layItem.setVisibility(View.VISIBLE);

                updateCounter(txtCounter, data);

                if (lastSelectedMenu != null && lastSelectedMenu.getType().equals(data.getType())) {
                    layItem.setBackgroundResource(R.drawable.abc_list_pressed_holo_light);
                    txtTitle.setTextColor(AisenUtils.getThemeColor(getActivity()));
                }
                else {
                    layItem.setBackgroundColor(Color.TRANSPARENT);
                    txtTitle.setTextColor(Color.parseColor("#ff676767"));
                }

                if (data.getIconRes() > 0) {
                    layIcon.setVisibility(View.VISIBLE);

                    imgIcon.setImageResource(data.getIconRes());
                }
                else {
                    layIcon.setVisibility(View.GONE);
                }
            }
        }

    }

}
