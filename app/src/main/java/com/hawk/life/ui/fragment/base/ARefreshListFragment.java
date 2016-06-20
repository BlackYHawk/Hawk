package com.hawk.life.ui.fragment.base;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;

import com.hawk.library.common.utils.Utils;
import com.hawk.library.ui.fragment.ASwipeRefreshListFragment;
import com.hawk.life.ui.widget.MainListView;
import com.hawk.ui.activity.R;

import java.io.Serializable;

/**
 * 设置一个刷新列表中间层，更换刷新控件修改这里的父类即可
 *
 * Created by wangdan on 15/4/14.
 */
public abstract class ARefreshListFragment<T extends Serializable, Ts extends Serializable>
                            extends ASwipeRefreshListFragment<T, Ts> {

    @Override
    protected int inflateContentView() {

        if (isInMain()) {
            return R.layout.fragment_ui_main_swipelist;
        }

        return super.inflateContentView();
    }

    @Override
    protected void setInitSwipeRefresh(ListView listView, SwipeRefreshLayout swipeRefreshLayout, Bundle savedInstanceState) {
        super.setInitSwipeRefresh(listView, swipeRefreshLayout, savedInstanceState);

        if (isInMain()) {
            int toolbarHeight = getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material);

            int progressBarStartMargin = getResources().getDimensionPixelSize(
                    R.dimen.swipe_refresh_progress_bar_start_margin);
            int progressBarEndMargin = getResources().getDimensionPixelSize(
                    R.dimen.swipe_refresh_progress_bar_end_margin);
            swipeRefreshLayout.setProgressViewOffset(false,
                    Utils.dip2px(50) + toolbarHeight + progressBarStartMargin,
                    Utils.dip2px(50) + toolbarHeight + progressBarEndMargin);

            setPadding(listView);
            setPadding(findViewById(R.id.layoutEmpty));
            setPadding(findViewById(R.id.layoutLoadFailed));
            setPadding(findViewById(R.id.layoutLoading));

            ((MainListView) getRefreshView()).setFragment(this);
        }
    }

    private void setPadding(View view) {
        if (view == null)
            return;

        int toolbarHeight = getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material);

        view.setPadding(view.getPaddingLeft(),
                Utils.dip2px(50) + toolbarHeight,
                view.getPaddingRight(),
                view.getPaddingBottom());
    }

    private boolean isInMain() {
//        boolean isMainFragment = this instanceof TimelineDefaultFragment;
//
//        return isMainFragment && getActivity() instanceof MainActivity;

        return true;
    }

}
