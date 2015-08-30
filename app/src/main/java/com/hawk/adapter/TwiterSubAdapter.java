package com.hawk.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hawk.activity.R;
import com.hawk.util.ImageUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heyong on 15/5/17.
 */
public class TwiterSubAdapter extends RecyclerView.Adapter<TwiterSubAdapter.ViewHolder> {

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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_twiter_sub, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String path = paths.get(position);

        Bitmap bm = null;
        try {
            bm = ImageUtil.revitionImageSize(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.imageView.setImageBitmap(bm);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return paths != null ? paths.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.item_grid_image);
        }
    }
}
