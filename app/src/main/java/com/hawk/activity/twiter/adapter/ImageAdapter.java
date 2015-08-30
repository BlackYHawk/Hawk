package com.hawk.activity.twiter.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hawk.activity.R;
import com.hawk.activity.twiter.ImageChoseGridActivity;
import com.hawk.activity.twiter.PhotoInfoActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heyong on 15/8/30.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private Context context;
    private List<Bitmap> bmp;

    public ImageAdapter(Context context, List<Bitmap> bmp) {
        this.context = context;

        if(bmp != null) {
            this.bmp = bmp;
        } else {
            this.bmp = new ArrayList<Bitmap>();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_twiter_add_grid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final int pos = position;

        if (pos == bmp.size()) {
            holder.image.setImageBitmap(BitmapFactory.decodeResource(
                    context.getResources(), R.drawable.icon_addpic_unfocused));
        } else {
            holder.image.setImageBitmap(bmp.get(position));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pos == bmp.size()) {
                    Intent intent = new Intent(context,
                            ImageChoseGridActivity.class);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context,
                            PhotoInfoActivity.class);
                    intent.putExtra("ID", pos);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return bmp.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.item_grida_image);
        }
    }
}
