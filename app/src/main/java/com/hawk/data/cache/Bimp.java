package com.hawk.data.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bimp {
	public static int max = 0;
//	public static boolean act_bool = true;
	public static List<Bitmap> bmp = new ArrayList<Bitmap>();	
	
	//图片sd地址  上传服务器时把图片调用下面方法压缩后 保存到临时文件夹 图片压缩后小于100KB，失真度不明显
	public static List<String> drr = new ArrayList<String>();

	public static Bitmap revitionImageSize(String path) throws IOException {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

        Bitmap bitmap = BitmapFactory.decodeFile(path, options);

        options.inSampleSize = 5;
        float imagew = 150;
        float imageh = 150;
        int yRatio = (int) Math.ceil(options.outHeight
                / imageh);
        int xRatio = (int) Math
                .ceil(options.outWidth / imagew);

        if (yRatio > 1 || xRatio > 1) {
            if (yRatio > xRatio) {
                options.inSampleSize = yRatio;
            } else {
                options.inSampleSize = xRatio;
            }

        }
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(path, options);

		return bitmap;
	}

    public static void reset(List<Bitmap> bmps, List<String> dir, int intMax) {
        for(Bitmap bitmap : bmp) {
            bitmap.recycle();
        }
        bmp.clear();
        drr.clear();

        bmp.addAll(bmps);
        drr.addAll(dir);
        max = intMax;
    }

    public static void clear() {
        for(Bitmap bitmap : bmp) {
            bitmap.recycle();
        }
        bmp.clear();
        drr.clear();

        max = 0;
    }
}
