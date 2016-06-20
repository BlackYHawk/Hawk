package com.hawk.life.ui.activity.base;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.hawk.library.common.context.GlobalContext;
import com.hawk.library.common.utils.ActivityHelper;
import com.hawk.library.common.utils.SystemBarUtils;
import com.hawk.library.support.inject.ViewInject;
import com.hawk.library.ui.activity.basic.BaseActivity;
import com.hawk.library.ui.fragment.ABaseFragment;
import com.hawk.library.ui.fragment.ARefreshFragment;
import com.hawk.library.ui.fragment.AStripTabsFragment;
import com.hawk.library.ui.fragment.ATabLayoutFragment;
import com.hawk.life.base.AppContext;
import com.hawk.life.base.AppSettings;
import com.hawk.life.support.bean.MenuBean;
import com.hawk.life.support.utils.AisenUtils;
import com.hawk.life.support.utils.ThemeUtils;
import com.hawk.life.ui.fragment.base.BizFragment;
import com.hawk.life.ui.fragment.base.MenuFragment;
import com.hawk.life.ui.fragment.base.MenuGenerator;
import com.hawk.life.ui.fragment.timeline.TimelineDefaultFragment;
import com.hawk.ui.activity.R;
import com.melnykov.fab.FloatingActionButton;


/**
 * Created by wangdan on 15/4/12.
 */
public class MainActivity extends BaseActivity implements MyActivityHelper.EnableSwipeback, View.OnLongClickListener {

    public static final String ACTION_LOGIN = "org.aisen.sina.weibo.ACTION_LOGIN";
    public static final String ACTION_NOTIFICATION = "org.aisen.sina.weibo.ACTION_NOTIFICATION";
    public static final String ACTION_NOTIFICATION_MS = "org.aisen.sina.weibo.ACTION_NOTIFICATION_MS";
    public static final String ACTION_NOTIFICATION_MC = "org.aisen.sina.weibo.ACTION_NOTIFICATION_MC";

    public static final String FRAGMENT_TAG = "MainFragment";

    public static void login() {
        Intent intent = new Intent(GlobalContext.getInstance(), MainActivity.class);
        intent.setAction(ACTION_LOGIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        GlobalContext.getInstance().startActivity(intent);
    }

    public static Fragment getContentFragment(MainActivity activity) {
        return activity.getFragmentManager().findFragmentByTag("MainFragment");
    }

    @ViewInject(id = R.id.drawer)
    private DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle mDrawerToggle;

    private MenuBean lastSelectedMenu;

    private MenuFragment menuFragment;

    @ViewInject(id = R.id.btnFable, click = "fabBtnCLicked")
    private FloatingActionButton btnFab;

    private int fabType = -1;

    private static MainActivity mInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AisenUtils.setStatusBar(this);

        mInstance = this;

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ui_main);

        BizFragment.getBizFragment(this);

        if (Build.VERSION.SDK_INT >= 19) {
            ViewGroup drawerRoot = (ViewGroup) findViewById(R.id.drawer_container);
            drawerRoot.setPadding(drawerRoot.getPaddingLeft(),
                                    SystemBarUtils.getStatusBarHeight(this),
                                    drawerRoot.getPaddingRight(),
                                    drawerRoot.getBottom());
        }
        if (Build.VERSION.SDK_INT == 19) {
            ViewGroup rootMain = (ViewGroup) findViewById(R.id.main_container);
            rootMain.setPadding(rootMain.getPaddingLeft(),
                                    rootMain.getPaddingTop(),
                                    rootMain.getPaddingRight(),
                                    rootMain.getBottom() + SystemBarUtils.getNavigationBarHeight(this));
        }

        mToolbar = getToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                getToolbar(), R.string.draw_open, R.string.draw_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);

                invalidateOptionsMenu();

                if (isToolbarShown())
                    btnFab.show(true);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                invalidateOptionsMenu();

                btnFab.hide(true);
            }

        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        lastSelectedMenu = savedInstanceState == null ? null : (MenuBean) savedInstanceState.getSerializable("menu");

        if (ActivityHelper.getBooleanShareData("isFirstLaunch", true)) {
            ActivityHelper.putBooleanShareData("isFirstLaunch", false);

            mDrawerLayout.openDrawer(Gravity.LEFT);
            btnFab.hide();

            getSupportActionBar().setTitle(R.string.draw_timeline);
        } else {
            if (lastSelectedMenu != null)
                getSupportActionBar().setTitle(lastSelectedMenu.getTitleRes());
            else
                getSupportActionBar().setTitle(R.string.draw_timeline);
        }

        if (savedInstanceState == null) {
            String action = getIntent() != null ? getIntent().getAction() : null;
            String type = getActionType(getIntent(), action);

            menuFragment = MenuFragment.newInstance(type);
            getFragmentManager().beginTransaction().add(R.id.menu_frame, menuFragment, MenuFragment.FRAGMENT_TAG).commit();
        } else {
            menuFragment = (MenuFragment) getFragmentManager().findFragmentByTag("MenuFragment");

            // 2014-8-30 解决因为状态保存而导致的耗时阻塞
            if (lastSelectedMenu.getType().equals("1"))
                onMenuSelected(lastSelectedMenu, true, null);
        }

        // 更新FAB的颜色
        btnFab.setColorNormal(AisenUtils.getThemeColor(this));
        btnFab.setColorPressed(AisenUtils.getThemeColor(this));
        btnFab.setColorRipple(AisenUtils.getThemeColor(this));
    }

    @Override
    protected int configTheme() {
        return ThemeUtils.themeArr[AppSettings.getThemeColor()][1];
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (lastSelectedMenu != null)
            outState.putSerializable("menu", lastSelectedMenu);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent == null)
            return;

        String action = intent.getAction();

        MenuBean menuBean = MenuGenerator.generateMenu(getActionType(intent, action));

        lastSelectedMenu = menuBean;

        onMenuSelected(menuBean, true, null);

        if ("1".equals(menuBean.getType())) {
            menuFragment.setAccountItem();
            menuFragment.setSelectedMenu(menuBean);
        }

        if (isDrawerOpened())
            closeDrawer();
    }

    private String getActionType(Intent intent, String action) {
        String type = null;
        // 处理点击Notification时，设置显示菜单
        if (ACTION_LOGIN.equals(action)) {
            type = "1";
        }
        else if (ACTION_NOTIFICATION.equals(action)) {
            type = intent.getStringExtra("type");
        }
        else if (ACTION_NOTIFICATION_MS.equals(action)) {
            ActivityHelper.putShareData("showMensitonType", "showMentionStatus");

            type = "2";
        }
        else if (ACTION_NOTIFICATION_MC.equals(action)) {
            ActivityHelper.putShareData("showMensitonType", "showMentionCmt");

            type = "2";
        }
        return type;
    }

    public boolean onMenuSelected(MenuBean menu, boolean replace, View view) {
        if (!replace && lastSelectedMenu != null && lastSelectedMenu.getType().equals(menu.getType())) {
            closeDrawer();
            return true;
        }

        int type = Integer.parseInt(menu.getType());

        ABaseFragment fragment = null;

        if (mStripView != null)
            mStripView.clearAnimation();

        mStripView = null;

        if (animatorSet != null) {
            animatorSet.removeAllListeners();
            animatorSet.end();
            animatorSet = null;
        }
        mToolbar.setTranslationY(0);

        switch (type) {
            // 首页
            case 1:
                fragment = TimelineDefaultFragment.newInstance();
                break;

        }

        if (fragment == null)
            return true;

        getSupportActionBar().setSubtitle(null);
        getSupportActionBar().setTitle(menu.getTitleRes());

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment, FRAGMENT_TAG).commit();

        lastSelectedMenu = menu;
        menuFragment.setSelectedMenu(menu);

        setFabType();

        return false;
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawers();
    }

    public boolean isDrawerOpened() {
        return mDrawerLayout.isDrawerOpen(Gravity.LEFT) || mDrawerLayout.isDrawerOpen(Gravity.RIGHT);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (mDrawerToggle != null)
            mDrawerToggle.syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();

        setFabType();

        invalidateOptionsMenu();
    }

    private void setFabType() {
        if (fabType != AppSettings.getFabBtnType()) {
            fabType = AppSettings.getFabBtnType();
            invalidateOptionsMenu();
            if (fabType == 0) {
                btnFab.setImageResource(R.drawable.ic_menu_edit_white);
            }
            else {
                btnFab.setImageResource(R.drawable.ic_refresh_light);
            }
        }
        btnFab.setVisibility(canFragmentRefresh() ? View.VISIBLE : View.INVISIBLE);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) btnFab.getLayoutParams();
        params.gravity = Gravity.BOTTOM;
        if (AppSettings.getFabBtnPosition() == 0)
            params.gravity |= Gravity.LEFT;
        else
            params.gravity |= Gravity.RIGHT;
    }

    private boolean canFragmentRefresh() {
        int menu = Integer.parseInt(lastSelectedMenu.getType());

        return menu == 1 || menu == 2 ||
                menu == 3 || menu == 4;
    }

    void fabBtnCLicked(View v) {
        if (AppSettings.getFabBtnType() == 0) {

        }
        else {
            Fragment fragment = getFragmentManager().findFragmentByTag(FRAGMENT_TAG);
            if (fragment != null && fragment instanceof AStripTabsFragment) {
                fragment = ((AStripTabsFragment) fragment).getCurrentFragment();
                if (fragment != null && fragment instanceof ARefreshFragment) {
                    ((ARefreshFragment) fragment).setRefreshingRequestData();
                }
            }
        }
    }

    private boolean canFinish = false;

    @Override
    public boolean onBackClick() {
        if (AppSettings.isAppResident()) {
            if (lastSelectedMenu != null && !"1".equals(lastSelectedMenu.getType())) {
                onMenuSelected(MenuGenerator.generateMenu("1"), true, null);
                return true;
            } else {
                if (isDrawerOpened()) {
                    closeDrawer();

                    return true;
                }
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
            return true;
        }
        else {
            if (!canFinish) {
                canFinish = true;

                showMessage(R.string.comm_hint_exit);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        canFinish = false;
                    }

                }, 1500);

                return true;
            }

            setMDestory(true);
            return super.onBackClick();
        }
    }

    private Toolbar mToolbar;
    private View mStripView;

    private boolean isToolbarShown() {
        return mToolbar != null && mToolbar.getTranslationY() >= 0;
    }

    public void hideToolbar() {
        if (isToolbarShown()) {
            toggleToolbarShown(false);
        }
    }

    public void showToolbar() {
        if (!isToolbarShown()) {
            toggleToolbarShown(true);
        }
    }

    private AnimatorSet animatorSet;
    public void toggleToolbarShown(boolean shown) {
        if (mStripView == null) {
            Fragment fragment = getFragmentManager().findFragmentByTag(FRAGMENT_TAG);
            if (fragment != null && fragment instanceof AStripTabsFragment) {
                mStripView = ((AStripTabsFragment) fragment).getSlidingTabLayout();
            }
            else if (fragment != null && fragment instanceof ATabLayoutFragment) {
                mStripView = ((ATabLayoutFragment) fragment).getTabLayout();
            }
        }

        if (mToolbar == null)
            return;

        if (animatorSet != null && animatorSet.isRunning())
            return;

        if (isToolbarShown() && shown)
            return;
        else if (!isToolbarShown() && !shown)
            return;

        if (!btnFab.isVisible() && shown) {
            btnFab.show(true);
        }
        else if (btnFab.isVisible() && !shown) {
            btnFab.hide(true);
        }

        PropertyValuesHolder toolBarHolder = null;
        if (shown) {
            toolBarHolder = PropertyValuesHolder.ofFloat("translationY", -1 * mToolbar.getHeight(), 0);
        }
        else {
            toolBarHolder = PropertyValuesHolder.ofFloat("translationY", 0, -1 * mToolbar.getHeight());
        }
        ObjectAnimator toolbarObjectAnim = ObjectAnimator.ofPropertyValuesHolder(mToolbar, toolBarHolder);
        toolbarObjectAnim.setDuration(150);

        ObjectAnimator stripObjectAnim = null;
        if (mStripView != null) {
            PropertyValuesHolder stripHolder = null;
            if (shown) {
                stripHolder = PropertyValuesHolder.ofFloat("translationY", -1 * mStripView.getHeight(), 0);
            }
            else {
                stripHolder = PropertyValuesHolder.ofFloat("translationY", 0, -1 * mStripView.getHeight());
            }
            stripObjectAnim = ObjectAnimator.ofPropertyValuesHolder(mStripView, stripHolder);
            stripObjectAnim.setDuration(150);
        }

        AnimatorSet animSet = new AnimatorSet();
        animatorSet = animSet;
        if (shown) {
            if (stripObjectAnim != null) {
//                animSet.playSequentially(toolbarObjectAnim, stripObjectAnim);
                animSet.play(toolbarObjectAnim);
                stripObjectAnim.setStartDelay(100);
                animSet.play(stripObjectAnim);
            }
            else {
                animSet.play(toolbarObjectAnim);
            }
        }
        else {
            if (stripObjectAnim != null) {
//                animSet.playSequentially(stripObjectAnim, toolbarObjectAnim);
                animSet.play(stripObjectAnim);
                toolbarObjectAnim.setStartDelay(100);
                animSet.play(toolbarObjectAnim);
            }
            else {
                animSet.play(toolbarObjectAnim);
            }
        }
        animSet.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.about).setVisible(true);

        // 显示的是首页
        if (lastSelectedMenu != null) {
            if (lastSelectedMenu.getType().equals("1")) {

            }
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item))
            return true;

        if (android.R.id.home == item.getItemId()) {
            if (mDrawerLayout.isDrawerVisible(GravityCompat.START))
                mDrawerLayout.closeDrawers();
            else
                mDrawerLayout.openDrawer(GravityCompat.START);

            return true;
        }

        // 关于
        if (item.getItemId() == R.id.about) {

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean canSwipe() {
        return false;
    }


    @Override
    public boolean onLongClick(View v) {
        String username = "";
        String password = "";

        username = AppContext.getAccount().getAccount();
        password = AppContext.getAccount().getPassword();

        // 这里导出Hprof文件，跟踪分析内存
//        String path = SdcardUtils.getSdcardPath() + File.separator + "aisenweibo" + File.separator;
//        path = path + "hprof" + File.separator;
//        HprofUtils.dumpHprof(path);


        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000 && RESULT_OK == resultCode) {

        }
        else if (requestCode == 2000 && RESULT_OK == resultCode) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mInstance = null;
    }

    public MenuBean getSelectedMenu() {
        return lastSelectedMenu;
    }

    public static MainActivity getInstance() {
        return mInstance;
    }

}
