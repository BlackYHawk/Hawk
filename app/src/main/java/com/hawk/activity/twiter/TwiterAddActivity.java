package com.hawk.activity.twiter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.hawk.activity.R;
import com.hawk.adapter.GridAdapter;
import com.hawk.application.AppContext;
import com.hawk.data.cache.Bimp;
import com.hawk.data.manager.TwiterDBManager;
import com.hawk.data.model.Twiter;
import com.hawk.middleware.util.FileUtil;
import com.hawk.middleware.util.StringUtil;
import com.hawk.util.Constants;
import com.hawk.util.ImageUtil;
import com.hawk.util.LOG;
import com.hawk.util.UIHelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TwiterAddActivity extends AppCompatActivity {

    private static String TAG = "AddTwiterActivity";
    private Toolbar toolbar;
    private TextView title;
    private ImageButton actionFinish;
    private EditText mContent;        //内容
    private GridView noScrollgridview;
    private GridAdapter adapter;
    private InputMethodManager imm;

    private TwiterDBManager twiterDBManager;
	private Twiter twiter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_twiter_add);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title = (TextView) findViewById(R.id.toolbar_title);
        title.setText(R.string.activity_twiter_add);
        actionFinish = (ImageButton) findViewById(R.id.action_finish);

        mContent = (EditText)this.findViewById(R.id.twiter_pub_content);
        noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(this);
        noScrollgridview.setAdapter(adapter);
        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == Bimp.bmp.size()) {
                    new PopupWindows(TwiterAddActivity.this, noScrollgridview);
                } else {
                    Intent intent = new Intent(TwiterAddActivity.this,
                            PhotoInfoActivity.class);
                    intent.putExtra("ID", arg2);
                    startActivity(intent);
                }
            }
        });
        actionFinish.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // 高清的压缩图片全部就在  list 路径里面了
                // 高清的压缩过的 bmp 对象  都在 Bimp.bmp里面
                // 完成上传服务器后 .........
                //FileUtils.deleteDir();
                finish();
            }
        });

		// 软键盘管理类
	    imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        twiterDBManager = TwiterDBManager.getInstance(this);

	}

    public class LoadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            if (Bimp.max == Bimp.drr.size()) {
                return null;
            } else {
                try {
                    do {
                        String path = Bimp.drr.get(Bimp.max);
                        LOG.Error(TAG, path);
                        Bitmap bm = Bimp.revitionImageSize(path);
                        Bimp.bmp.add(bm);
                        String newStr = FileUtil.getFileName(path);
                        ImageUtil.saveBitmap(bm, Constants.TEMP_IMAGE, newStr);
                        Bimp.max += 1;
                    } while(Bimp.max < Bimp.drr.size());
                    return null;
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.update();
        }
    }

    public class PopupWindows extends PopupWindow {

        public PopupWindows(Context mContext, View parent) {

            View view = View
                    .inflate(mContext, R.layout.item_popupwindows, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_ins));
            LinearLayout ll_popup = (LinearLayout) view
                    .findViewById(R.id.ll_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.push_bottom_in_2));

            setWidth(ViewGroup.LayoutParams.FILL_PARENT);
            setHeight(ViewGroup.LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);

            Button bt1 = (Button) view
                    .findViewById(R.id.item_popupwindows_camera);
            Button bt2 = (Button) view
                    .findViewById(R.id.item_popupwindows_Photo);
            Button bt3 = (Button) view
                    .findViewById(R.id.item_popupwindows_cancel);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            bt1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    photo();
                    dismiss();
                }
            });
            bt2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(TwiterAddActivity.this,
                            ImageChoseGridActivity.class);
                    startActivity(intent);
                    dismiss();
                }
            });
            bt3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });

        }
    }

    private static final int TAKE_PICTURE = 0x000000;
    private String path = "";

    public void photo() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = FileUtil.createFile(Constants.IMAGE, String.valueOf(System.currentTimeMillis())
                + Constants.IMAGE_FORMAT);
        if(file != null) {
            path = file.getPath();
            Uri imageUri = Uri.fromFile(file);
            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(openCameraIntent, TAKE_PICTURE);
        } else {
            UIHelper.showToast(this, "不支持SD卡");
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK ) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    if (Bimp.drr.size() < 9) {
                        Bimp.drr.add(path);
                    }
                    else {
                        UIHelper.showToast(this, "不能超过9张");
                    }
                    break;
            }
        }
    }

    private Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			if(msg.what == 1)
			{
				Toast.makeText(TwiterAddActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
				TwiterAddActivity.this.finish();
			}
			else if(msg.what == 0)
			{
				Toast.makeText(TwiterAddActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
			}
		}
	};

    private void addTwiterAsyn()
	{
		final String content = mContent.getText().toString();
		if(content == null || content.equalsIgnoreCase(""))
		{
			Toast.makeText(TwiterAddActivity.this, "请输入", Toast.LENGTH_SHORT).show();
			return;
		}
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //设置日期格式
		String time = df.format(new Date());
		
		twiter = new Twiter();
        twiter.id = StringUtil.getUUID();
        twiter.content = content;
        twiter.time = time;
		
		new Thread(){
			public void run()
			{
				addTwiter(twiter);
			}
		}.start();
	}
	
	private void addTwiter(Twiter twiter)
	{
		twiterDBManager.addTwiter(twiter);
		handler.sendEmptyMessage(1);

	}

    @Override
    protected void onResume() {
        super.onResume();
        LOG.Error(TAG, "onResume");
        new LoadTask().execute();
    }

    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
        LOG.Error(TAG, "onDestroy");
        Bimp.clear();
        AppContext.getRefWatcher(this).watch(this);
	}

}
