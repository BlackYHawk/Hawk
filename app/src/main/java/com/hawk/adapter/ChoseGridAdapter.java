package com.hawk.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hawk.ui.activity.R;
import com.hawk.data.cache.BitmapCache;
import com.hawk.data.model.ImageItem;
import com.hawk.ui.widget.CheckBox;

import java.util.List;

/**
 * Created by heyong on 15/6/11.
 */
public class ChoseGridAdapter extends BaseAdapter {

    private static String TAG = "ChoseGridAdapter";
    private Context context;
    private LayoutInflater inflater; // 视图容器
    private List<ImageItem> items;

    private ImageCheckListener imageCheckListener;
    private ImageCaptureListener imageCaptureListener;
    private BitmapCache cache;


    public interface ImageCheckListener {
        public void check(boolean chose, ImageItem imageItem);
    }

    public interface ImageCaptureListener {
        public void capture();
    }

    public void setImageCheckListener(ImageCheckListener imageCheckListener) {
        this.imageCheckListener = imageCheckListener;
    }

    public void setImageCaptureListener(ImageCaptureListener imageCaptureListener) {
        this.imageCaptureListener = imageCaptureListener;
    }

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

    public ChoseGridAdapter(Context context, List<ImageItem> items) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        cache = BitmapCache.getBitmapCache();

        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if(convertView == null) {
            holder = new ViewHolder();
            convertView = (View) inflater.inflate(R.layout.item_chose_grid, parent, false);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.selected = (ImageView) convertView.findViewById(R.id.selected);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(position == 0) {
            holder.image.setImageBitmap(BitmapFactory.decodeResource(
                    context.getResources(), R.drawable.ic_camera_black));
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageCaptureListener.capture();
                }
            });
            holder.selected.setVisibility(View.INVISIBLE);
            holder.checkBox.setVisibility(View.INVISIBLE);
        } else {

            final ImageItem item = items.get(position - 1);

            holder.image.setTag(item.imagePath);
            cache.displayBmp(holder.image, item.thumbnailPath, item.imagePath, callback);

            if (item.isSelected) {
                holder.selected.setVisibility(View.VISIBLE);
                holder.selected.setImageResource(R.drawable.bg_transparent);
            } else {
                holder.selected.setVisibility(View.GONE);
            }

            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setOncheckListener(null);
            holder.checkBox.setChecked(item.isSelected);

            final ViewHolder holder1 = holder;
            holder.checkBox.setOncheckListener(new CheckBox.OnCheckListener() {
                @Override
                public void onCheck(boolean check) {
                    if (check) {
                        item.isSelected = true;
                        holder1.selected.setVisibility(View.VISIBLE);
                        holder1.selected.setImageResource(R.drawable.bg_transparent);

                        imageCheckListener.check(true, item);
                    } else {
                        item.isSelected = false;
                        holder1.selected.setVisibility(View.GONE);

                        imageCheckListener.check(false, item);
                    }
                }
            });

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }


        return convertView;
    }

    public class ViewHolder {
        public ImageView image;
        public ImageView selected;
        public CheckBox checkBox;
    }

}
