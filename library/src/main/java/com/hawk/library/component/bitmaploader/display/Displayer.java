package com.hawk.library.component.bitmaploader.display;

import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

public interface Displayer {

	/**
	 * 图片加载完成 回调的函数
	 * 
	 * @param imageView
	 * @param drawable
	 */
	public void loadCompletedisplay(ImageView imageView, BitmapDrawable drawable);

	/**
	 * 图片加载失败回调的函数
	 * 
	 * @param imageView
	 * @param drawable
	 */
	public void loadFailDisplay(ImageView imageView, BitmapDrawable drawable);

}
