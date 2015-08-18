package com.hawk.activity.twiter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
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
                    Intent intent = new Intent(TwiterAddActivity.this,
                            ImageChoseGridActivity.class);
                    startActivity(intent);
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
