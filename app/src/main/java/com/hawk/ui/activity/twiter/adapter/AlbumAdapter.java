package com.hawk.ui.activity.twiter.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hawk.ui.activity.R;
import com.hawk.ui.activity.twiter.view.AlbumPopupWindow;
import com.hawk.data.cache.BitmapCache;
import com.hawk.data.model.ImageBucket;
import com.hawk.data.model.ImageItem;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Administrator on 2015/7/28.
 */
public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private static String TAG = "AlbumAdapter";
    private BitmapCache cache;
    private Context context;
    private AlbumPopupWindow.AlbumClickListener clickListener;
    private HashMap<String, ImageBucket> buckets;

    BitmapCache.ImageCallback callback = new BitmapCache.ImageCallback() {
        @Override
        public void imageLoad(ImageView imageView, Bitmap bitmap,
                              Object... params) {
            if (imageView != null && bitmap != null) {
                String url = (String) params[0];
                if (url != null && url.equals((String) imageView.getTag())) {
                    ((ImageView) imageView).setImageBitmap(bitmap);
                } else {
                    Log.e(TAG, "callback, bmp not match");
                }
            } else {
                Log.e(TAG, "callback, bmp null");
            }
        }
    };

    public void setAlbumClickListener(AlbumPopupWindow.AlbumClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public AlbumAdapter(Context context, HashMap<String, ImageBucket> buckets) {
        this.context = context;
        cache = BitmapCache.getBitmapCache();

        if(buckets != null) {
            this.buckets = buckets;
        } else {
            this.buckets = new HashMap<String, ImageBucket>();
        }
    }

    @Override
    public int getItemCount() {
        return buckets != null ? buckets.size() : 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_album_list, parent, false);
        return new ViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageBucket bucket = null;
        Iterator iterator = buckets.entrySet().iterator();
        int i = 0;
        while (iterator.hasNext()) {
            HashMap.Entry<String, ImageBucket> entry = (HashMap.Entry<String, ImageBucket>)iterator.next();

            if(position == i) {
                bucket = entry.getValue();
                break;
            } else {
                i++;
            }
        }

        if(bucket == null) {
            return;
        }

        ImageItem cover = bucket.imageList.get(0);
        holder.bucketCover.setTag(cover.imagePath);
        cache.displayBmp(holder.bucketCover, cover.thumbnailPath, cover.imagePath, callback);
        holder.bucketName.setText(bucket.bucketName);
        holder.bucketCount.setText(bucket.count+"");
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView bucketCover;
        public TextView bucketName;
        public TextView bucketCount;
        public AlbumPopupWindow.AlbumClickListener clickListener;

        public ViewHolder(View itemView, AlbumPopupWindow.AlbumClickListener clickListener) {
            super(itemView);
            bucketCover = (ImageView) itemView.findViewById(R.id.bucket_cover);
            bucketName = (TextView) itemView.findViewById(R.id.bucket_title);
            bucketCount = (TextView) itemView.findViewById(R.id.bucket_count);
            itemView.setOnClickListener(this);
            this.clickListener = clickListener;
        }

        @Override
        public void onClick(View v) {
            if(clickListener != null) {
                clickListener.click(getPosition());
            }
        }
    }
}
