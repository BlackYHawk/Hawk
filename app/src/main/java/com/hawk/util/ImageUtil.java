package com.hawk.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.hawk.middleware.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ImageUtil {

    /**
     * 保存图片
     *
     * @param bm
     * @param dir
     * @param picName
     */
	public static boolean saveBitmap(Bitmap bm, String dir, String picName) {
		try {
			if (!FileUtil.checkDirectoryExists(dir)) {
                FileUtil.createDirectory(dir);
			}
            File file = FileUtil.createFile(dir, picName);

			if (file == null) {
                Log.e("test", "创建图片文件失败");
				return false;
			}
			FileOutputStream out = new FileOutputStream(file);
			bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
			Log.e("", "保存图片文件成功");
            return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
        return false;
	}

    /**
     * 获取图片
     *
     * @param filePath
     * @param opts
     */
    public static Bitmap getBitmapByPath(String filePath, BitmapFactory.Options opts) {
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            File file = new File(filePath);
            fis = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(fis, null, opts);
            Log.e("", "获取图片文件成功");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return bitmap;
    }

    /**
     * 获取图片
     *
     * @param dir
     * @param fileName
     * @param opts
     */
    public static Bitmap getBitmapByPath(String dir, String fileName,
                                         BitmapFactory.Options opts) {
        if (!FileUtil.checkDirectoryExists(dir)) {
            Log.e("", "图片文件夹不存在");
            return null;
        }
        File file = FileUtil.getFile(dir, fileName);

        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            fis = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(fis, null, opts);
            Log.e("", "获取图片文件成功");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return bitmap;
    }

}
