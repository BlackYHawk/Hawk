package com.hawk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hawk.ui.activity.R;
import com.hawk.data.model.Twiter;
import com.hawk.ui.widget.TimelinePicsView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heyong on 15/5/17.
 */
public class TwiterAdapter extends BaseAdapter {

    private Context context;
    private List<Twiter> twiters;

    public TwiterAdapter(Context context, List<Twiter> twiters ) {
        this.context = context;

        if(twiters != null) {
            this.twiters = twiters;
        } else {
            this.twiters = new ArrayList<Twiter>();
        }
    }

    public void setDataSource(List<Twiter> twiters) {

        this.twiters.clear();

        if(twiters != null && twiters.size() > 0) {
            this.twiters.addAll(twiters);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return twiters.size();
    }

    @Override
    public Object getItem(int position) {
        return twiters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null) {
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(context).inflate(R.layout.item_twiter_list, parent, false);
            viewHolder.content = (TextView) convertView.findViewById(R.id.twiter_content);
            viewHolder.timelinePicsView = (TimelinePicsView) convertView.findViewById(R.id.layPicturs);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Twiter twiter = twiters.get(position);

        viewHolder.content.setText(twiter.content);

        if(twiter.imgPaths != null && twiter.imgPaths.size() > 0) {
            viewHolder.timelinePicsView.setPics(twiter.imgPaths);
        }
        else {
            viewHolder.timelinePicsView.setVisibility(View.GONE);
        }

        return convertView;
    }

    public final class ViewHolder {
        public TextView content;
        public TimelinePicsView timelinePicsView;
    }
}
