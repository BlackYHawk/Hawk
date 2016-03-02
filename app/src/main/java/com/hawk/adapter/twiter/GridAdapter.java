package com.hawk.adapter.twiter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hawk.ui.activity.R;
import com.hawk.data.cache.Bimp;

/**
 * Created by heyong on 15/6/11.
 */
public class GridAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater; // 视图容器

    public GridAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void update() {
        notifyDataSetChanged();
    }

    public int getCount() {
        return (Bimp.bmp.size() + 1);
    }

    public Object getItem(int arg0) {

        return null;
    }

    public long getItemId(int arg0) {

        return 0;
    }

    /**
     * ListView Item设置
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.item_twiter_add_grid,
                    parent, false);
            holder = new ViewHolder();
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.image = (ImageView) convertView
                .findViewById(R.id.item_grida_image);

        if (position == Bimp.bmp.size()) {
            holder.image.setImageBitmap(BitmapFactory.decodeResource(
                    context.getResources(), R.drawable.icon_addpic_unfocused));
            if (position == 9) {
                holder.image.setVisibility(View.GONE);
            }
        } else {
            holder.image.setImageBitmap(Bimp.bmp.get(position));
        }

        return convertView;
    }

    public class ViewHolder {
        public ImageView image;
    }

}
