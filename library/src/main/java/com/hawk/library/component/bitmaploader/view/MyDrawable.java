package com.hawk.library.component.bitmaploader.view;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.hawk.library.component.bitmaploader.BitmapLoader;
import com.hawk.library.component.bitmaploader.core.ImageConfig;
import com.hawk.library.component.bitmaploader.core.MyBitmap;

import java.lang.ref.WeakReference;


public class MyDrawable extends BitmapDrawable {

	private MyBitmap myBitmap;
	private ImageConfig config;
	private WeakReference<BitmapLoader.MyBitmapLoaderTask> task;

	public MyDrawable(Resources res, MyBitmap myBitmap, ImageConfig config, WeakReference<BitmapLoader.MyBitmapLoaderTask> task) {
		this(res, myBitmap.getBitmap());
		this.myBitmap = myBitmap;
		this.config = config;
		this.task = task;
	}

	public MyBitmap getMyBitmap() {
		return myBitmap;
	}

	public void setMyBitmap(MyBitmap myBitmap) {
		this.myBitmap = myBitmap;
	}

	public MyDrawable(Resources res, Bitmap bitmap) {
		super(res, bitmap);
	}

	public ImageConfig getConfig() {
		return config;
	}

	public void setConfig(ImageConfig config) {
		this.config = config;
	}

	public WeakReference<BitmapLoader.MyBitmapLoaderTask> getTask() {
		return task;
	}

	public void setTask(WeakReference<BitmapLoader.MyBitmapLoaderTask> task) {
		this.task = task;
	}

}
