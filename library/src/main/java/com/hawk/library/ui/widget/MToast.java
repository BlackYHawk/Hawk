package com.hawk.library.ui.widget;

import android.widget.Toast;

import com.hawk.library.common.context.GlobalContext;


/**
 * Created by wangdan on 15/4/15.
 */
public class MToast {

    public static void showMessage(String text) {
        Toast.makeText(GlobalContext.getInstance(), text, Toast.LENGTH_SHORT).show();
    }

}
