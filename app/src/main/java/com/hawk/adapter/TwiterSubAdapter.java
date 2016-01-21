package com.hawk.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hawk.activity.R;
import com.hawk.util.ImageUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heyong on 15/5/17.
 */
public class TwiterSubAdapter extends BaseAdapter {

    private Context context;
    private List<String> paths;

    public TwiterSubAdapter(Context context, List<String> paths) {
        this.context = context;

        if(paths != null) {
            this.paths = paths;
        } else {
            this.paths = new ArrayList<String>();
        }
    }

    public void setDataSource(List<String> paths) {

        this.paths.clear();

        if(paths != null && paths.size() > 0) {
            this.paths.addAll(paths);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return paths.size();
    }

    @Override
    public Object getItem(int position) {
        return paths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if(convertView == null) {
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(context).inflate(R.layout.item_twiter_sub, parent, false);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.item_grid_image);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String path = paths.get(position);

        Bitmap bm = null;
        try {
            bm = ImageUtil.revitionImageSize(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        viewHolder.imageView.setImageBitmap(bm);
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }

    public final class ViewHolder {
        public ImageView imageView;
    }
}
