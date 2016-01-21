package com.hawk.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.hawk.activity.R;
import com.hawk.middleware.util.FileUtil;
import com.hawk.util.ImageUtil;
import com.hawk.util.LOG;
import com.hawk.util.SystemUtils;

import java.io.File;
import java.util.List;


/**
 * timeline的图片容器，根据图片个数动态布局ImageView
 * 
 * @author wangdan
 *
 */
public class TimelinePicsView extends ViewGroup {

	public static final String TAG = TimelinePicsView.class.getSimpleName();

	private int mWidth;
	
	private int gap;
	
	private List<String> paths;
	
	private Rect[] picRects;
	
	private boolean large = true;


	public TimelinePicsView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		init();
	}

	public TimelinePicsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		init();
	}

	public TimelinePicsView(Context context) {
		super(context);
		
		init();
	}
	
	private void init() {
		gap = getResources().getDimensionPixelSize(R.dimen.gap_pics);
	}
	
	private void recyle() {
		for (int i = 0; i < getChildCount(); i++) {
			ImageView imgView = (ImageView) getChildAt(i);
			
			imgView.setImageDrawable(null);
		}
	}

    private static Rect[] small9ggRectArr = null;
	private Rect[] getSmallRectArr() {
        if (small9ggRectArr != null)
            return small9ggRectArr;

		int imgW = Math.round((mWidth - 2 * gap) * 1.0f / 3.0f);
		int imgH = imgW;
		
		Rect[] tempRects = new Rect[9];
		
		Rect rect = new Rect(0, 0, imgW, imgH);
		tempRects[0] = rect;
		rect = new Rect(imgW + gap, 0, imgW * 2 + gap, imgH);
		tempRects[1] = rect;
		rect = new Rect(mWidth - imgW, 0, mWidth, imgH);
		tempRects[2] = rect;
		
		rect = new Rect(0, imgH + gap, imgW, imgH * 2 + gap);
		tempRects[3] = rect;
		rect = new Rect(imgW + gap, imgH + gap, imgW * 2 + gap, imgH * 2 + gap);
		tempRects[4] = rect;
		rect = new Rect(mWidth - imgW, imgH + gap, mWidth, imgH * 2 + gap);
		tempRects[5] = rect;
		
		rect = new Rect(0, imgH * 2 + gap * 2, imgW, imgH * 3 + gap * 2);
		tempRects[6] = rect;
		rect = new Rect(imgW + gap, imgH * 2 + gap * 2, imgW * 2 + gap, imgH * 3 + gap * 2);
		tempRects[7] = rect;
		rect = new Rect(mWidth - imgW, imgH * 2 + gap * 2, mWidth, imgH * 3 + gap * 2);
		tempRects[8] = rect;
		
        small9ggRectArr = tempRects;
		return small9ggRectArr;
	}


	// 根据图片尺寸
	private void calculatePicSize() {

		picRects = null;

        int gap = getResources().getDimensionPixelSize(R.dimen.gap_pics);
        mWidth = SystemUtils.getScreenWidth() - 2 * getResources().getDimensionPixelSize(R.dimen.comm_h_gap);

		int size = paths.size();
		int random = 0;
		
		LinearLayout.LayoutParams layoutParams = null;
		Rect[] tempRects = new Rect[size];
		switch (size) {
		case 1:
            int imgW = mWidth;
            int imgH = Math.round(imgW * 4.0f / 3.0f);

            String url = paths.get(0);
            File file = FileUtil.getFile(url);

            if (file != null && file.exists()) {
                long time = System.currentTimeMillis();
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
                LOG.Error(TAG, String.format("耗时%s毫秒", String.valueOf(System.currentTimeMillis() - time)));
                imgH = Math.round(imgW * opts.outHeight * 1.0f / opts.outWidth);
                if (imgH > SystemUtils.getScreenHeight())
                    imgH = imgW;
            }

			Rect rect = new Rect(0, 0, imgW, imgH);
			tempRects[0] = rect;
			
			layoutParams = new LinearLayout.LayoutParams(mWidth, imgH);
			break;
		case 2:
			imgW = (mWidth - gap) / 2;
			imgH = Math.round(imgW * 4.0f / 3.0f);
			
			rect = new Rect(0, 0, imgW, imgH);
			tempRects[0] = rect;
			rect = new Rect(imgW + gap, 0, mWidth, imgH);
			tempRects[1] = rect;
			
			layoutParams = new LinearLayout.LayoutParams(mWidth, imgH);
			break;
		case 3:
			int imgW02 = Math.round((mWidth - gap) * 3.0f / 7.0f);
			int imgH02 = imgW02;
			int imgW01 = Math.round((mWidth - gap) * 4.0f / 7.0f);
			int imgH01 = imgH02 * 2 + gap;
			
			try {
				random = (int) ((Math.random()*100) % 2);
			} catch (Exception e) {
			}
			// 见/doc/3_0.png
			if (random == 0) {
				rect = new Rect(0, 0, imgW01, imgH01);
				tempRects[0] = rect;
				rect = new Rect(gap + imgW01, 0, mWidth, imgH02);
				tempRects[1] = rect;
				rect = new Rect(gap + imgW01, imgH02 + gap, mWidth, imgH01);
				tempRects[2] = rect;
			}
			// 见/doc/3_1.png
			else if (random == 1) {
				rect = new Rect(0, 0, imgW02, imgH02);
				tempRects[0] = rect;
				rect = new Rect(0, imgH02 + gap, imgW02, imgH01);
				tempRects[1] = rect;
				rect = new Rect(gap + imgW02, 0, mWidth, imgH01);
				tempRects[2] = rect;
			}
			
			layoutParams = new LinearLayout.LayoutParams(mWidth, imgH01);
			break;
		case 4:
			imgW = Math.round((mWidth - gap) * 1.0f / 2);
			imgH = Math.round(imgW * 4.0f / 3.0f);
			
			rect = new Rect(0, 0, imgW, imgH);
			tempRects[0] = rect;
			rect = new Rect(gap + imgW, 0, mWidth, imgH);
			tempRects[1] = rect;
			rect = new Rect(0, imgH + gap, imgW, imgH * 2 + gap);
			tempRects[2] = rect;
			rect = new Rect(gap + imgW, imgH + gap, mWidth, imgH * 2 + gap);
			tempRects[3] = rect;
			
			layoutParams = new LinearLayout.LayoutParams(mWidth, imgH * 2 + gap);
			break;
		case 5:
			imgW01 = Math.round((mWidth - gap) * 1.0f / 2);
			imgH01 = Math.round(imgW01 * 4.0f / 3.0f);
			
			imgW02 = Math.round((mWidth - gap * 2) * 1.0f / 3);
			imgH02 = Math.round(imgW02 * 4.0f / 3.0f);
			
			try {
				random = (int) ((Math.random()*100) % 2);
			} catch (Exception e) {
			}
			int height = imgH01 + imgH02 + gap;
			// 见/doc/5_0.png
			if (random == 0) {
				rect = new Rect(0, 0, imgW01, imgH01);
				tempRects[0] = rect;
				rect = new Rect(gap + imgW01, 0, mWidth, imgH01);
				tempRects[1] = rect;
				rect = new Rect(0, imgH01 + gap, imgW02, height);
				tempRects[2] = rect;
				rect = new Rect(imgW02 + gap, imgH01 + gap, imgW02 * 2 + gap, height);
				tempRects[3] = rect;
				rect = new Rect(mWidth - imgW02, imgH01 + gap, mWidth, height);
				tempRects[4] = rect;
			}
			// 见/doc/5_1.png
			else if (random == 1) {
				rect = new Rect(0, 0, imgW02, imgH02);
				tempRects[0] = rect;
				rect = new Rect(imgW02 + gap, 0, imgW02 * 2 + gap, imgH02);
				tempRects[1] = rect;
				rect = new Rect(mWidth - imgW02, 0, mWidth, imgH02);
				tempRects[2] = rect;
				rect = new Rect(0, imgH02 + gap, imgW01, height);
				tempRects[3] = rect;
				rect = new Rect(gap + imgW01, imgH02 + gap, mWidth, height);
				tempRects[4] = rect;
			}
			
			layoutParams = new LinearLayout.LayoutParams(mWidth, imgH01 + imgH02 + gap);
			break;
		case 6:
			imgW01 = Math.round((mWidth - 2 * gap) * 1.0f / 3.0f);
			imgH01 = Math.round(imgW01 * 4.0f / 3.0f);
			
			imgW02 = imgW01 * 2 + gap;
			imgH02 = imgH01 * 2 + gap;
			
			height = imgH01 + imgH02 + gap;
			
			try {
				random = (int) ((Math.random()*100) % 3);
			} catch (Exception e) {
			}
			// 见/doc/6_0.png
			if (random == 0) {
				rect = new Rect(0, 0, imgW01, imgH01);
				tempRects[0] = rect;
				rect = new Rect(gap + imgW01, 0, imgW01 * 2 + gap, imgH01);
				tempRects[1] = rect;
				rect = new Rect(mWidth - imgW01, 0, mWidth, imgH01);
				tempRects[2] = rect;
				rect = new Rect(0, imgH01 + gap, imgW02, height);
				tempRects[3] = rect;
				rect = new Rect(imgW02 + gap, imgH01 + gap, mWidth, height - imgH01 - gap);
				tempRects[4] = rect;
				rect = new Rect(imgW02 + gap, height - imgH01, mWidth, height);
				tempRects[5] = rect;
			}
			// 见/doc/6_1.png
			else if (random == 1) {
				rect = new Rect(0, 0, imgW01, imgH01);
				tempRects[0] = rect;
				rect = new Rect(0, imgH01 + gap, imgW01, imgH01 * 2 + gap);
				tempRects[1] = rect;
				rect = new Rect(gap + imgW01, 0, mWidth, imgH02);
				tempRects[2] = rect;
				rect = new Rect(0, height - imgH01, imgW01, height);
				tempRects[3] = rect;
				rect = new Rect(gap + imgW01, height - imgH01, gap + imgW01 * 2, height);
				tempRects[4] = rect;
				rect = new Rect(imgW02 + gap, height - imgH01, mWidth, height);
				tempRects[5] = rect;
			}
			// 见/doc/6_2.png
			else if (random == 2) {
				rect = new Rect(0, 0, imgW02, imgH02);
				tempRects[0] = rect;
				rect = new Rect(imgW02 + gap, 0, mWidth, imgH01);
				tempRects[1] = rect;
				rect = new Rect(gap + imgW02, imgH01 + gap, mWidth, imgH01 * 2 + gap);
				tempRects[2] = rect;
				rect = new Rect(0, height - imgH01, imgW01, height);
				tempRects[3] = rect;
				rect = new Rect(gap + imgW01, height - imgH01, gap + imgW01 * 2, height);
				tempRects[4] = rect;
				rect = new Rect(imgW02 + gap, height - imgH01, mWidth, height);
				tempRects[5] = rect;
			}
			// 见/doc/6_3.png
			else if (random == 3) {
				rect = new Rect(0, 0, imgW01, imgH01);
				tempRects[0] = rect;
				rect = new Rect(gap + imgW01, 0, imgW01 * 2 + gap, imgH01);
				tempRects[1] = rect;
				rect = new Rect(mWidth - imgW01, 0, mWidth, imgH01);
				tempRects[2] = rect;
				rect = new Rect(0, imgH01 + gap, imgW01, imgH01 * 2 + gap);
				tempRects[3] = rect;
				rect = new Rect(0, height - imgH01, imgW01, height);
				tempRects[4] = rect;
				rect = new Rect(imgW01 + gap, imgH01 + gap, mWidth, height);
				tempRects[5] = rect;
			}
			layoutParams = new LinearLayout.LayoutParams(mWidth, height);
			break;
		case 7:
			imgW01 = Math.round((mWidth - 2 * gap) * 1.0f / 3.0f);
			imgH01 = Math.round(imgW01 * 4.0f / 3.0f);
			
			imgW02 = mWidth;
			imgH02 = Math.round(imgW02 * 3.0f / 4.0f);
			
			height = imgH01 * 2 + imgH02 + gap * 2;
			
			rect = new Rect(0, 0, imgW01, imgH01);
			tempRects[0] = rect;
			rect = new Rect(gap + imgW01, 0, imgW01 * 2 + gap, imgH01);
			tempRects[1] = rect;
			rect = new Rect(mWidth - imgW01, 0, mWidth, imgH01);
			tempRects[2] = rect;
			
			rect = new Rect(0, imgH01 + gap, imgW02, imgH01 + gap + imgH02);
			tempRects[3] = rect;
			
			rect = new Rect(0, imgH01 + gap * 2 + imgH02, imgW01, height);
			tempRects[4] = rect;
			rect = new Rect(gap + imgW01, imgH01 + gap * 2 + imgH02, imgW01 * 2 + gap, height);
			tempRects[5] = rect;
			rect = new Rect(mWidth - imgW01, imgH01 + gap * 2 + imgH02, mWidth, height);
			tempRects[6] = rect;
			
			layoutParams = new LinearLayout.LayoutParams(mWidth, height);
			break;
		case 8:
			imgW01 = Math.round((mWidth - 2 * gap) * 1.0f / 3.0f);
			imgH01 = Math.round(imgW01 * 4.0f / 3.0f);
			
			imgW02 = Math.round((mWidth - gap) * 1.0f / 2.0f);
			imgH02 = Math.round(imgW02 * 4.0f / 3.0f);
			
			height = imgH01 * 2 + imgH02 + gap * 2;
			
			rect = new Rect(0, 0, imgW01, imgH01);
			tempRects[0] = rect;
			rect = new Rect(gap + imgW01, 0, imgW01 * 2 + gap, imgH01);
			tempRects[1] = rect;
			rect = new Rect(mWidth - imgW01, 0, mWidth, imgH01);
			tempRects[2] = rect;
			
			rect = new Rect(0, imgH01 + gap, imgW02, imgH01 + gap + imgH02);
			tempRects[3] = rect;
			rect = new Rect(imgW02 + gap, imgH01 + gap, mWidth, imgH01 + gap + imgH02);
			tempRects[4] = rect;
			
			rect = new Rect(0, imgH01 + gap * 2 + imgH02, imgW01, height);
			tempRects[5] = rect;
			rect = new Rect(gap + imgW01, imgH01 + gap * 2 + imgH02, imgW01 * 2 + gap, height);
			tempRects[6] = rect;
			rect = new Rect(mWidth - imgW01, imgH01 + gap * 2 + imgH02, mWidth, height);
			tempRects[7] = rect;
			
			layoutParams = new LinearLayout.LayoutParams(mWidth, height);
			break;
		case 9:
			imgW01 = Math.round((mWidth - 2 * gap) * 1.0f / 3.0f);
			imgH01 = Math.round(imgW01 * 4.0f / 3.0f);
			
			height = imgH01 * 2 + imgH01 + gap * 2;
			
			rect = new Rect(0, 0, imgW01, imgH01);
			tempRects[0] = rect;
			rect = new Rect(gap + imgW01, 0, imgW01 * 2 + gap, imgH01);
			tempRects[1] = rect;
			rect = new Rect(mWidth - imgW01, 0, mWidth, imgH01);
			tempRects[2] = rect;
			
			rect = new Rect(0, imgH01 + gap, imgW01, imgH01 * 2 + gap);
			tempRects[3] = rect;
			rect = new Rect(gap + imgW01, imgH01 + gap, imgW01 * 2 + gap, imgH01 * 2 + gap);
			tempRects[4] = rect;
			rect = new Rect(mWidth - imgW01, imgH01 + gap, mWidth, imgH01 * 2 + gap);
			tempRects[5] = rect;
			
			rect = new Rect(0, imgH01 + gap * 2 + imgH01, imgW01, height);
			tempRects[6] = rect;
			rect = new Rect(gap + imgW01, imgH01 + gap * 2 + imgH01, imgW01 * 2 + gap, height);
			tempRects[7] = rect;
			rect = new Rect(mWidth - imgW01, imgH01 + gap * 2 + imgH01, mWidth, height);
			tempRects[8] = rect;
			
			layoutParams = new LinearLayout.LayoutParams(mWidth, height);
			break;
		}

		setLayoutParams(layoutParams);
		
		picRects = tempRects;
		
		displayPics();
		
		// 重新绘制
		requestLayout();
	}
	
	public void displayPics() {
		if (picRects == null || paths == null || paths.size() == 0)
			return;
		
		for (int i = 0; i < getChildCount(); i++) {
			ImageView imgView = (ImageView) getChildAt(i);

			// 隐藏多余的View
			if (i >= picRects.length) {
				getChildAt(i).setVisibility(View.GONE);
				
				imgView.setImageDrawable(null);
			}
			else {
				Rect imgRect = picRects[i];
				
				imgView.setVisibility(View.VISIBLE);
				// 如果是一个图片，就显示大一点
				int size = paths.size();
				if (size == 1) {
					imgView.setScaleType(ScaleType.FIT_CENTER);
				}
				else {
					imgView.setScaleType(ScaleType.CENTER_CROP);
				}
                imgView.setLayoutParams(new LayoutParams(imgRect.right - imgRect.left, imgRect.bottom - imgRect.top));

				String url = paths.get(i);
				Bitmap bitmap = ImageUtil.getBitmapByPath(url);

                imgView.setImageBitmap(bitmap);
			}
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (picRects == null)
			return;
		
		for (int i = 0; i < getChildCount(); i++) {
			// 隐藏多余的View
			if (i < picRects.length) {
				Rect imgRect = picRects[i];
				
				ImageView childView = (ImageView) getChildAt(i);

                childView.layout(imgRect.left, imgRect.top, imgRect.right, imgRect.bottom);
			}
			else {
				break;
			}
		}
	}
	
	public void release() {
		for (int i = 0; i < getChildCount(); i++) {
			ImageView imgView = (ImageView) getChildAt(i);
			imgView.setImageDrawable(null);
		}
	}
	
	public void setPics(List<String> paths) {
		this.paths = paths;

		if (paths == null || paths.size() == 0) {
			recyle();
			
			setVisibility(View.GONE);
		}
		else {
			setVisibility(View.VISIBLE);
			
			calculatePicSize();
		}
	}

}
