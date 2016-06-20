package com.hawk.life.ui.fragment.base;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.hawk.library.common.utils.Logger;
import com.hawk.library.ui.fragment.ABaseFragment;
import com.hawk.life.support.bean.WeiBoUser;
import com.hawk.life.ui.activity.base.MainActivity;

/**
 * 程序一系列业务逻辑处理，如下:<br/>
 */
public class BizFragment extends ABaseFragment {

    @Override
    protected int inflateContentView() {
        return 0;
    }

    public static BizFragment getBizFragment(ABaseFragment fragment) {
        if (fragment != null && fragment.getActivity() != null) {
            BizFragment bizFragment = (BizFragment) fragment.getActivity().getFragmentManager().findFragmentByTag("org.aisen.android.ui.BizFragment");

            if (bizFragment == null) {
                bizFragment = new BizFragment();
                fragment.getActivity().getFragmentManager().beginTransaction().add(bizFragment, "org.aisen.android.ui.BizFragment").commit();
            }

            return bizFragment;
        }

        return null;
    }

    public static BizFragment getBizFragment(Activity context) {
        BizFragment bizFragment = (BizFragment) context.getFragmentManager().findFragmentByTag("BizFragment");
        if (bizFragment == null) {
            bizFragment = new BizFragment();
            context.getFragmentManager().beginTransaction().add(bizFragment, "BizFragment").commit();
        }
        return bizFragment;
    }

    // XXX /*查看用户详情*/
    View.OnClickListener UserShowListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            WeiBoUser user = (WeiBoUser) v.getTag();
            if (user != null) {

            }
        }
    };

    public void userShow(View view, WeiBoUser user) {
        view.setTag(user);
        view.setOnClickListener(UserShowListener);
    }

	/* 回到首页 */

    public void backToMainActivity(Activity context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        Logger.v("回到首页");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

        }
    }

}
