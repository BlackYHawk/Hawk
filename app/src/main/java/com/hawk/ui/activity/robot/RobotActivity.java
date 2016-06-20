package com.hawk.ui.activity.robot;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.hawk.adapter.robot.ChatMsgViewAdapter;
import com.hawk.life.support.bean.AIChatBean;
import com.hawk.ui.activity.R;
import com.hawk.util.UIHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class RobotActivity extends FragmentActivity implements OnClickListener {

	private Button mBtnSend;
	private Button mBtnBack;
	private EditText mEditTextContent;
	private ListView mListView;
	
	private AIHttp aiHttp;
	private ChatMsgViewAdapter mAdapter;
	private List<AIChatBean> mDataArrays;

	private ChatMsgDBManager chatMsgDBManager;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		aiHttp = new AIHttp(this);
		
		this.setContentView(R.layout.activity_chat);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
		
        initView();
        
        initData();
	}
	
    public void initView()
    {
    	mBtnSend = (Button) findViewById(R.id.chat_send);
    	mBtnSend.setOnClickListener(this);
    	mBtnBack = (Button) findViewById(R.id.chat_back);
    	mBtnBack.setOnClickListener(this);
    	mEditTextContent = (EditText) findViewById(R.id.chat_sendmessage);
   
    	mListView = (ListView) findViewById(R.id.chat_listview);
    }

    public void initData()
    {
		chatMsgDBManager = ChatMsgDBManager.getInstance(this);
    	mDataArrays = chatMsgDBManager.getChatMsgs();
    	
    	if(mDataArrays == null)
    	{
    		mDataArrays = new ArrayList<AIChatBean>();
    	}
    	
    	mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
		mListView.setAdapter(mAdapter);
    }
    
    //保存记录
    private void saveData(AIChatBean chatMsg)
    {
		chatMsgDBManager.addChatMsg(chatMsg);
    }
    
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId())
		{
		case R.id.chat_send:
			send();
			break;
		case R.id.chat_back:
			finish();
			break;
		}
	}

	private Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			if(msg.what == 1)
			{
				AIChatBean entity = new AIChatBean();
				entity.date = getTime();
				entity.isComMsg = true;
				entity.text = (String)msg.obj;
				saveData(entity);
				
				mDataArrays.add(entity);
				mAdapter.notifyDataSetChanged();
				
				mListView.setSelection(mListView.getCount() - 1);
			}
			else if(msg.what == 0)
			{
				UIHelper.showToast(RobotActivity.this, (String) msg.obj);
			}
			else if(msg.what == -1)
			{
				UIHelper.showToast(RobotActivity.this, "网络错误");
			}
		}
	};
	
	private void send()
	{
		final String contString = mEditTextContent.getText().toString();
		if (contString.length() > 0)
		{
			new Thread(){
				public void run()
				{
					Map<String,String> param = getTLHttp(contString);
					
					if(param != null)
					{
						String code = param.get("code");
						String text = param.get("text");
						
						int value = Integer.parseInt(code);
						if(value >= 100000)
						{
							Message msg = new Message();
							msg.what = 1;
							msg.obj = text;
							
							handler.sendMessage(msg);
						}
						else{
							Message msg = new Message();
							msg.what = 0;
							msg.obj = text;
							
							handler.sendMessage(msg);
						}
					}
					else
					{
						handler.sendEmptyMessage(-1);
					}
				}
			}.start();
			
			AIChatBean entity = new AIChatBean();
			entity.date = getTime();
			entity.isComMsg = false;
			entity.text = contString;
			saveData(entity);
			
			mDataArrays.add(entity);
			mAdapter.notifyDataSetChanged();
			
			mEditTextContent.setText("");
			
			mListView.setSelection(mListView.getCount() - 1);
		}
		else{
			UIHelper.showToast(this, "请输入");
		}
	}
	
	private Map<String,String> getTLHttp(String request)
	{
		return aiHttp.getAIResult(request);
	}
	
    private String getTime() {
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
    	
		return df.format(new Date());					
    }

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
