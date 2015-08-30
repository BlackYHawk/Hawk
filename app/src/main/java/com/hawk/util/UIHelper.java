package com.hawk.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.Toast;

import com.hawk.activity.R;

public class UIHelper {
	
	/**
	 * 显示提示
	 * 
	 * @param content
	 */
	public static void showToast(final Context context, String content)
	{
		Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
	}

    /**
     * 清除文字
     *
     * @param cont
     * @param editer
     */
    public static void showClearWordsDialog(final Context cont, final EditText editer) {
        AlertDialog.Builder builder = new AlertDialog.Builder(cont);
        builder.setTitle(R.string.ui_clearwords);
        builder.setPositiveButton(R.string.sure,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // 清除文字
                        editer.setText("");
                    }
                });
        builder.setNegativeButton(R.string.cancle,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

}
