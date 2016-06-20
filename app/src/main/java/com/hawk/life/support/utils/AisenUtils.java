package com.hawk.life.support.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.hawk.library.common.context.GlobalContext;
import com.hawk.library.common.setting.SettingUtility;
import com.hawk.library.common.utils.DateUtils;
import com.hawk.library.common.utils.FileUtils;
import com.hawk.library.common.utils.Logger;
import com.hawk.library.common.utils.SystemUtils;
import com.hawk.library.common.utils.Utils;
import com.hawk.library.component.bitmaploader.core.BitmapDecoder;
import com.hawk.library.ui.activity.basic.BaseActivity;
import com.hawk.library.ui.fragment.ABaseFragment;
import com.hawk.life.base.AppSettings;
import com.hawk.life.support.bean.WeiBoUser;
import com.hawk.ui.activity.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;

/**
 * Created by wangdan on 15/4/12.
 */
public class AisenUtils {

    public static int getThemeColor(Context context) {
        final int materialBlue = Color.parseColor("#ff0000");
        int themeColor = Utils.resolveColor(context, R.attr.theme_color, materialBlue);
        return themeColor;
    }

    public static String getUserScreenName(WeiBoUser user) {
        if (AppSettings.isShowRemark() && !TextUtils.isEmpty(user.getRemark()))
            return user.getRemark();

        return user.getScreen_name();
    }

    public static String getUserKey(String key, WeiBoUser user) {
        return key + "-" + user.getIdstr();
    }

    public static File getUploadFile(File source) {
        Logger.w("原图图片大小" + (source.length() / 1024) + "KB");

        if (source.getName().toLowerCase().endsWith(".gif")) {
            Logger.w("上传图片是GIF图片，上传原图");
            return source;
        }

        File file = null;

        String imagePath = GlobalContext.getInstance().getAppPath() + SettingUtility.getStringSetting("draft") + File.separator;

        int sample = 1;
        int maxSize = 0;

        int type = AppSettings.getUploadSetting();
        // 自动，WIFI时原图，移动网络时高
        if (type == 0) {
            if (SystemUtils.getNetworkType() == SystemUtils.NetWorkType.wifi)
                type = 1;
            else
                type = 2;
        }
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(source.getAbsolutePath(), opts);
        switch (type) {
            // 原图
            case 1:
                Logger.w("原图上传");
                file = source;
                break;
            // 高
            case 2:
                sample = BitmapDecoder.calculateInSampleSize(opts, 1920, 1080);
                Logger.w("高质量上传");
                maxSize = 700 * 1024;
                imagePath = imagePath + "高" + File.separator + source.getName();
                file = new File(imagePath);
                break;
            // 中
            case 3:
                Logger.w("中质量上传");
                sample = BitmapDecoder.calculateInSampleSize(opts, 1280, 720);
                maxSize = 300 * 1024;
                imagePath = imagePath + "中" + File.separator + source.getName();
                file = new File(imagePath);
                break;
            // 低
            case 4:
                Logger.w("低质量上传");
                sample = BitmapDecoder.calculateInSampleSize(opts, 1280, 720);
                maxSize = 100 * 1024;
                imagePath = imagePath + "低" + File.separator + source.getName();
                file = new File(imagePath);
                break;
            default:
                break;
        }

        // 压缩图片
        if (type != 1 && !file.exists()) {
            Logger.w(String.format("压缩图片，原图片 path = %s", source.getAbsolutePath()));
            byte[] imageBytes = FileUtils.readFileToBytes(source);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                out.write(imageBytes);
            } catch (Exception e) {
            }

            Logger.w(String.format("原图片大小%sK", String.valueOf(imageBytes.length / 1024)));
            if (imageBytes.length > maxSize) {
                // 尺寸做压缩
                BitmapFactory.Options options = new BitmapFactory.Options();

                if (sample > 1) {
                    options.inSampleSize = sample;
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length, options);
                    Logger.w(String.format("压缩图片至大小：%d*%d", bitmap.getWidth(), bitmap.getHeight()));
                    out.reset();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    imageBytes = out.toByteArray();
                }

                options.inSampleSize = 1;
                if (imageBytes.length > maxSize) {
                    BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length, options);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length, options);

                    int quality = 90;
                    out.reset();
                    Logger.w(String.format("压缩图片至原来的百分之%d大小", quality));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
                    while (out.toByteArray().length > maxSize) {
                        out.reset();
                        quality -= 10;
                        Logger.w(String.format("压缩图片至原来的百分之%d大小", quality));
                        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
                    }
                }

            }

            try {
                if (!file.getParentFile().exists())
                    file.getParentFile().mkdirs();

                Logger.w(String.format("最终图片大小%sK", String.valueOf(out.toByteArray().length / 1024)));
                FileOutputStream fo = new FileOutputStream(file);
                fo.write(out.toByteArray());
                fo.flush();
                fo.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return file;
    }

    public static void showMenuDialog(ABaseFragment fragment, final View targetView,
                                      String[] menuArr, DialogInterface.OnClickListener onItemClickListener) {
        new AlertDialogWrapper.Builder(fragment.getActivity())
                .setItems(menuArr, onItemClickListener)
                .show();
    }

    public static String getFirstId(@SuppressWarnings("rawtypes") List datas) {
        int size = datas.size();
        if (size > 0)
            return getId(datas.get(0));
        return null;
    }

    public static String getLastId(@SuppressWarnings("rawtypes") List datas) {
        int size = datas.size();
        if (size > 0)
            return getId(datas.get(size - 1));
        return null;
    }

    public static String getId(Object t) {
        try {
            Field idField = t.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            return idField.get(t).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    public static String convDate(String time) {
        Context context = GlobalContext.getInstance();
        Resources res = context.getResources();

        StringBuffer buffer = new StringBuffer();

        Calendar createCal = Calendar.getInstance();
        createCal.setTimeInMillis(Date.parse(time));
        Calendar currentcal = Calendar.getInstance();
        currentcal.setTimeInMillis(System.currentTimeMillis());

        long diffTime = (currentcal.getTimeInMillis() - createCal.getTimeInMillis()) / 1000;

        // 同一月
        if (currentcal.get(Calendar.MONTH) == createCal.get(Calendar.MONTH)) {
            // 同一天
            if (currentcal.get(Calendar.DAY_OF_MONTH) == createCal.get(Calendar.DAY_OF_MONTH)) {
                if (diffTime < 3600 && diffTime >= 60) {
                    buffer.append((diffTime / 60) + res.getString(R.string.msg_few_minutes_ago));
                } else if (diffTime < 60) {
                    buffer.append(res.getString(R.string.msg_now));
                } else {
                    buffer.append(res.getString(R.string.msg_today)).append(" ").append(DateUtils.formatDate(createCal.getTimeInMillis(), "HH:mm"));
                }
            }
            // 前一天
            else if (currentcal.get(Calendar.DAY_OF_MONTH) - createCal.get(Calendar.DAY_OF_MONTH) == 1) {
                buffer.append(res.getString(R.string.msg_yesterday)).append(" ").append(DateUtils.formatDate(createCal.getTimeInMillis(), "HH:mm"));
            }
        }

        if (buffer.length() == 0) {
            buffer.append(DateUtils.formatDate(createCal.getTimeInMillis(), "MM-dd HH:mm"));
        }

        String timeStr = buffer.toString();
        if (currentcal.get(Calendar.YEAR) != createCal.get(Calendar.YEAR)) {
            timeStr = createCal.get(Calendar.YEAR) + " " + timeStr;
        }
        return timeStr;
    }

    public static String convCount(int count) {
        if (count < 10000) {
            return count + "";
        } else {
            Resources res = GlobalContext.getInstance().getResources();
            String result = new DecimalFormat("#.0").format(count * 1.0f / 10000) + res.getString(R.string.msg_ten_thousand);
            return result;
        }
    }

    public static String getCounter(int count) {
        Resources res = GlobalContext.getInstance().getResources();

        if (count < 10000)
            return String.valueOf(count);
        else if (count < 100 * 10000)
            return new DecimalFormat("#.0" + res.getString(R.string.msg_ten_thousand)).format(count * 1.0f / 10000);
        else
            return new DecimalFormat("#" + res.getString(R.string.msg_ten_thousand)).format(count * 1.0f / 10000);
    }

    /**
     * 显示高清头像
     *
     * @param user
     * @return
     */
    public static String getUserPhoto(WeiBoUser user) {
        if (user == null)
            return "";

        if (AppSettings.isLargePhoto()) {
            return user.getAvatar_large();
        }

        return user.getProfile_image_url();
    }

    public static String getGender(WeiBoUser user) {
        Resources res = GlobalContext.getInstance().getResources();
        if (user != null) {
            if ("m".equals(user.getGender())) {
                return res.getString(R.string.msg_male);
            } else if ("f".equals(user.getGender())) {
                return res.getString(R.string.msg_female);
            } else if ("n".equals(user.getGender())) {
                return res.getString(R.string.msg_gender_unknow);
            }
        }
        return "";
    }

    public static String getCommentText(String text) {
        if (TextUtils.isEmpty(text))
            return "";

        try {
            if (text.startsWith("回覆") || text.startsWith("回复")) {
                if (text.indexOf(":") != -1) {
                    text = text.substring(text.indexOf(":") + 1, text.length());
                }
                else if (text.indexOf("：") != -1) {
                    text = text.substring(text.indexOf("：") + 1, text.length());
                }
            }
        } catch (Exception e) {
        }

        return text.trim();
    }

    public static void setTextSize(TextView textView) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, AppSettings.getTextSize());
    }

    public static int getStrLength(String content) {
        int length = 0;
        int tempLength = 0;
        for (int i = 0; i < content.length(); i++) {
            String temp = content.charAt(i) + "";
            if (temp.getBytes().length == 3) {
                length++;
            } else {
                tempLength++;
            }
        }
        length += tempLength / 2 + ((tempLength % 2) == 0 ? 0 : 1);
        return length;
    }

    public static void copyToClipboard(String text) {
        // 得到剪贴板管理器
        try {
            ClipboardManager cmb = (ClipboardManager) GlobalContext.getInstance().getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setPrimaryClip(ClipData.newPlainText(null, text.trim()));
        } catch (Exception e) {
        }
    }

    public static void launchBrowser(Activity from, String url) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        from.startActivity(intent);
    }

    public static String getStatusMulImage(String thumbImage) {
        switch (AppSettings.getPictureMode()) {
            // MODE_AUTO
            case 2:
                if (SystemUtils.getNetworkType() == SystemUtils.NetWorkType.wifi)
                    return thumbImage.replace("thumbnail", "bmiddle");

                return thumbImage;
            // MODE_ALWAYS_ORIG
            case 1:
                return thumbImage.replace("thumbnail", "bmiddle");
            case 3:
                return thumbImage.replace("thumbnail", "bmiddle");
            // MODE_ALWAYS_THUMB
            case 0:
                return thumbImage;
            default:
                return thumbImage;
        }
    }

    public static String getUnit(long length) {
        String sizeStr;
        if (length * 1.0f / 1024 / 1024 > 1)
            sizeStr = String.format("%s M", new DecimalFormat("#.00").format(length * 1.0d / 1024 / 1024));
        else
            sizeStr = String.format("%s Kb", new DecimalFormat("#.00").format(length * 1.0d / 1024));
        return sizeStr;
    }

    public static Drawable getProgressBarDrawable() {
        if (BaseActivity.getRunningActivity() != null) {
            Activity context = BaseActivity.getRunningActivity();

            int color = context.getResources().getColor(ThemeUtils.themeColorArr[AppSettings.getThemeColor()][0]);

            return new CircularProgressDrawable.Builder(context).color(color).build();
        }

        return null;
    }

    public static void setStatusBar(Activity activity) {
//        if (true) return;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    );//| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Utils.resolveColor(activity, R.attr.theme_statusbar_color, Color.BLUE));
            window.setStatusBarColor(Color.parseColor("#20000000"));
            window.setNavigationBarColor(activity.getResources().getColor(ThemeUtils.themeColorArr[AppSettings.getThemeColor()][1]));
//            window.setNavigationBarColor(Utils.resolveColor(activity, R.attr.theme_color, Color.BLUE));

//            Window window = activity.getWindow();
//            window.requestFeature(Window.FEATURE_NO_TITLE);
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Utils.resolveColor(activity, R.attr.theme_statusbar_color, Color.TRANSPARENT));
        }
    }

    public static void setPicStatusBar(Activity activity) {
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            activity.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//            Window window = activity.getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//        }
    }

}
