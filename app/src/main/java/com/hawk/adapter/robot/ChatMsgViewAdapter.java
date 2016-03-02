
package com.hawk.adapter.robot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hawk.data.model.ChatMsg;
import com.hawk.ui.activity.R;

import java.util.List;


public class ChatMsgViewAdapter extends BaseAdapter {

	public static interface IMsgViewType
	{
		int IMVT_COM_MSG = 0;
		int IMVT_TO_MSG = 1;
	}

	private List<ChatMsg> coll;

	private Context ctx;

	private LayoutInflater mInflater;

	public ChatMsgViewAdapter(Context context, List<ChatMsg> coll) {
		ctx = context;
		this.coll = coll;
		mInflater = LayoutInflater.from(ctx);
	}

	public int getCount() {
		return coll.size();
	}

	public Object getItem(int position) {
		return coll.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		ChatMsg entity = coll.get(position);

		if (entity.isComMsg)
		{
			return IMsgViewType.IMVT_COM_MSG;
		}else{
			return IMsgViewType.IMVT_TO_MSG;
		}

	}

	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ChatMsg entity = coll.get(position);
		boolean isComMsg = entity.isComMsg;

		ViewHolder viewHolder = null;	
		if (convertView == null)
		{
			if (isComMsg)
			{
				convertView = mInflater.inflate(R.layout.item_chatting_msg_text_left, null);
			}else{
				convertView = mInflater.inflate(R.layout.item_chatting_msg_text_right, null);
			}

			viewHolder = new ViewHolder();
			viewHolder.iSendTime = (TextView) convertView.findViewById(R.id.chat_item_sendtime);
			viewHolder.iContent = (TextView) convertView.findViewById(R.id.chat_item_chatcontent);
			viewHolder.isComMsg = isComMsg;

			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.iSendTime.setText(entity.date);
		viewHolder.iContent.setText(entity.text);

		return convertView;
	}


	static class ViewHolder { 
		public TextView iSendTime;
		public TextView iContent;
		public boolean isComMsg = true;
	}


}
