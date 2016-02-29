package com.hawk.ui.activity.twiter;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hawk.adapter.itemanimator.CustomItemAnimator;
import com.hawk.data.cache.Bimp;
import com.hawk.data.manager.TwiterDBManager;
import com.hawk.data.model.Twiter;
import com.hawk.middleware.util.StringUtil;
import com.hawk.ui.activity.R;
import com.hawk.ui.activity.twiter.adapter.ImageAdapter;
import com.hawk.util.LOG;
import com.hawk.util.UIHelper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TwiterAddActivity extends AppCompatActivity {

    private static String TAG = "AddTwiterActivity";
    private static final int MAX_TEXT_LENGTH = 160;// 最大输入字数
    private Toolbar toolbar;
    private TextView title;
    private ImageButton actionFinish;

    private LinearLayout mClearwords;
    private EditText mContent;
    private TextView mNumberWords;

    private RecyclerView recyclerView;
    private ImageAdapter adapter;

    private TwiterDBManager twiterDBManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_ui_twiter_add);

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

        mClearwords = (LinearLayout) findViewById(R.id.twiter_add_clearwords);
        mContent = (EditText) findViewById(R.id.twiter_add_content);
        mNumberWords = (TextView) findViewById(R.id.twiter_add_numberwords);

        // 编辑器添加文本监听
        mContent.addTextChangedListener(new TextWatcher(){

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                mNumberWords.setText((MAX_TEXT_LENGTH - s.length()) + "");
            }

        });

        mClearwords.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                String content = mContent.getText().toString();
                if (!StringUtil.isEmpty(content)) {
                    UIHelper.showClearWordsDialog(TwiterAddActivity.this, mContent);
                }
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setItemAnimator(new CustomItemAnimator());

        adapter = new ImageAdapter(this, Bimp.bmp);
        recyclerView.setAdapter(adapter);

        actionFinish.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String content = mContent.getText().toString();
                if(content == null || content.equalsIgnoreCase(""))
                {
                    UIHelper.showToast(TwiterAddActivity.this, "请输入");
                    return;
                }

                new SubmitTask().execute(content);
            }
        });

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
            adapter.notifyDataSetChanged();
        }
    }

    public class SubmitTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //设置日期格式
            String time = df.format(new Date());

            Twiter twiter = new Twiter();
            twiter.id = StringUtil.getUUID();
            twiter.content = params[0];
            twiter.imgPaths = Bimp.drr;
            twiter.comments = null;
            twiter.time = time;

            twiterDBManager.addTwiter(twiter);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            UIHelper.showToast(TwiterAddActivity.this, "发布成功");
            TwiterAddActivity.this.finish();
        }
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
	}

}
