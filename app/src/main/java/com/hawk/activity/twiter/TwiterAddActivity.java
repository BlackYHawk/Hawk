package com.hawk.activity.twiter;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hawk.activity.R;
import com.hawk.activity.twiter.adapter.ImageAdapter;
import com.hawk.application.AppContext;
import com.hawk.data.cache.Bimp;
import com.hawk.data.manager.TwiterDBManager;
import com.hawk.data.model.Twiter;
import com.hawk.itemanimator.CustomItemAnimator;
import com.hawk.middleware.util.StringUtil;
import com.hawk.util.LOG;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TwiterAddActivity extends AppCompatActivity {

    private static String TAG = "AddTwiterActivity";
    private static final int MAX_TEXT_LENGTH = 160;// 最大输入字数
    private Toolbar toolbar;
    private TextView title;
    private ImageButton actionFinish;

    private RecyclerView recyclerView;
    private ImageAdapter adapter;

    private TwiterDBManager twiterDBManager;

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

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setItemAnimator(new CustomItemAnimator());

        adapter = new ImageAdapter(this, Bimp.bmp);
        recyclerView.setAdapter(adapter);

        actionFinish.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                new SubmitTask().execute();
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

    public class SubmitTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //设置日期格式
            String time = df.format(new Date());

            Twiter twiter = new Twiter();
            twiter.id = StringUtil.getUUID();
            twiter.imgPaths = Bimp.drr;
            twiter.comments = null;
            twiter.time = time;

            twiterDBManager.addTwiter(twiter);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(TwiterAddActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
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
        AppContext.getRefWatcher(this).watch(this);
	}

}
