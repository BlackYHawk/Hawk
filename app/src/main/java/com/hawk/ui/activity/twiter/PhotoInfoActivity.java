package com.hawk.ui.activity.twiter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hawk.data.cache.Bimp;
import com.hawk.ui.activity.R;
import com.hawk.util.ImageUtil;

import java.util.ArrayList;

public class PhotoInfoActivity extends Activity {

    private Button exit;
    private Button delete;
    private TextView current;
    private TextView total;

	private ArrayList<View> listViews = null;
	private ViewPager pager;
	private MyPageAdapter adapter;
	private int count;

	private RelativeLayout photo_relativeLayout;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ui_photo);

		photo_relativeLayout = (RelativeLayout) findViewById(R.id.photo_relativeLayout);

        current = (TextView) findViewById(R.id.photo_tv_current);
        total = (TextView) findViewById(R.id.photo_tv_total);
        exit = (Button) findViewById(R.id.photo_bt_exit);
        exit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				finish();
			}
		});
		delete = (Button) findViewById(R.id.photo_bt_del);
        delete.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (listViews.size() == 1) {
					Bimp.bmp.clear();
					Bimp.drr.clear();
					Bimp.max = 0;
					finish();
				} else {
                    Log.e("PhotoActivity", "count:"+count);
                    String newStr = Bimp.drr.get(count).substring(
                            Bimp.drr.get(count).lastIndexOf("/") + 1,
                            Bimp.drr.get(count).lastIndexOf("."));
                    Bimp.bmp.remove(count);
                    Bimp.drr.remove(count);
                    Bimp.max--;
                    pager.removeAllViews();
                    listViews.remove(count);
                    adapter.setListViews(listViews);
                    adapter.notifyDataSetChanged();
                    total.setText(Bimp.max+"");
				}
			}
		});

		pager = (ViewPager) findViewById(R.id.viewpager);
		pager.setOnPageChangeListener(pageChangeListener);
		for (int i = 0; i < Bimp.drr.size(); i++) {
            Bitmap bitmap = ImageUtil.getBitmapByPath(Bimp.drr.get(i)); //根据SD路径获取图片
			initListViews(bitmap);
		}

		adapter = new MyPageAdapter(listViews);// 构造adapter
		pager.setAdapter(adapter);// 设置适配器
		Intent intent = getIntent();
		int id = intent.getIntExtra("ID", 0);
		pager.setCurrentItem(id);

        current.setText(id+1+"");
        total.setText(Bimp.max+"");
	}

	private void initListViews(Bitmap bm) {
		if (listViews == null)
			listViews = new ArrayList<View>();
		ImageView imageView = new ImageView(this);// 构造textView对象
        imageView.setBackgroundColor(0xff000000);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setImageBitmap(bm);
        imageView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		listViews.add(imageView);// 添加view
	}

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		public void onPageSelected(int arg0) {// 页面选择响应函数
			count = arg0;
            current.setText(count+1+"");
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {// 滑动中。。。

		}

		public void onPageScrollStateChanged(int arg0) {// 滑动状态改变

		}
	};

	class MyPageAdapter extends PagerAdapter {

		private ArrayList<View> listViews;// content

		private int size;// 页数

		public MyPageAdapter(ArrayList<View> listViews) {// 构造函数
															// 初始化viewpager的时候给的一个页面
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

        public void setListViews(ArrayList<View> listViews) {// 自己写的一个方法用来添加数据
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

		public int getCount() {// 返回数量
			return size;
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {// 销毁view对象
			((ViewPager) arg0).removeView(listViews.get(arg1 % size));
		}

		public void finishUpdate(View arg0) {
		}

		public Object instantiateItem(View arg0, int arg1) {// 返回view对象
			try {
				((ViewPager) arg0).addView(listViews.get(arg1 % size), 0);

			} catch (Exception e) {
			}
			return listViews.get(arg1 % size);
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
