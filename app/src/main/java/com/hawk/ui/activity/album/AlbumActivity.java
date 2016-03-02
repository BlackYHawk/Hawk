package com.hawk.ui.activity.album;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hawk.data.cache.BitmapCache;
import com.hawk.data.model.ImageItem;
import com.hawk.ui.activity.R;
import com.hawk.ui.widget.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/3/2.
 */
public class AlbumActivity extends AppCompatActivity implements SwipeFlingAdapterView.onFlingListener,
        SwipeFlingAdapterView.OnItemClickListener {

    private static String TAG = "AlbumActivity";
    private Toolbar toolbar;
    private SwipeFlingAdapterView swipeView;
    private SwipeFlingrAdapter adapter;

    private List<ImageItem> items;

    private int cardWidth;
    private int cardHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_ui_album);

        init();
        initView();

        new InitImageTask(this).execute();
    }

    private void init() {
        items = new ArrayList<ImageItem>();
    }

    private void initView() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.activity_album);
        this.setSupportActionBar(toolbar);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        float density = dm.density;
        cardWidth = (int) (dm.widthPixels - (2 * 18 * density));
        cardHeight = (int) (dm.heightPixels - (338 * density));

        swipeView = (SwipeFlingAdapterView) findViewById(R.id.swipe_view);
        //swipeView.setIsNeedSwipe(true);
        swipeView.setFlingListener(this);
        swipeView.setOnItemClickListener(this);

        adapter = new SwipeFlingrAdapter();
        swipeView.setAdapter(adapter);
    }

    @Override
    public void onItemClicked(MotionEvent event, View v, Object dataObject) {
        if (v.getTag() instanceof ViewHolder) {
            int x = (int) event.getRawX();
            int y = (int) event.getRawY();
            ViewHolder vh = (ViewHolder) v.getTag();
            View child = vh.portraitView;
            Rect outRect = new Rect();
            child.getGlobalVisibleRect(outRect);
            if (outRect.contains(x, y)) {
            } else {
                outRect.setEmpty();
                child = vh.nameView;
                child.getGlobalVisibleRect(outRect);
                if (outRect.contains(x, y)) {
                }
            }
        }
    }

    @Override
    public void removeFirstObjectInAdapter() {
        adapter.remove(0);
    }

    @Override
    public void onLeftCardExit(Object dataObject) {
    }

    @Override
    public void onRightCardExit(Object dataObject) {
    }

    @Override
    public void onAdapterAboutToEmpty(int itemsInAdapter) {
    }

    @Override
    public void onScroll(float progress, float scrollXProgress) {
    }

    public class InitImageTask extends AsyncTask<Void,Void,Void> {

        private ContentResolver cr;
        private HashMap<String, String> thumbnailList;

        public InitImageTask(Context context) {
            this.cr = context.getContentResolver();
            thumbnailList = new HashMap<String, String>();
        }

        /**
         * 得到缩略图
         */
        private void getThumbnail() {
            String[] projection = { MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.IMAGE_ID,
                    MediaStore.Images.Thumbnails.DATA };
            Cursor cursor = cr.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, projection,
                    null, null, null);
            getThumbnailColumnData(cursor);
        }

        /**
         * 从数据库中得到缩略图
         *
         * @param cur
         */
        private void getThumbnailColumnData(Cursor cur) {
            if (cur.moveToFirst()) {
                int _id;
                int image_id;
                String image_path;

                int _idColumn = cur.getColumnIndex(MediaStore.Images.Thumbnails._ID);
                int image_idColumn = cur.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID);
                int dataColumn = cur.getColumnIndex(MediaStore.Images.Thumbnails.DATA);

                do {
                    _id = cur.getInt(_idColumn);
                    image_id = cur.getInt(image_idColumn);
                    image_path = cur.getString(dataColumn);
                    thumbnailList.put("" + image_id, image_path);
                } while (cur.moveToNext());
            }
            cur.close();
        }

        /**
         * 得到原图
         */
        private void getImageBucket() {
            long startTime = System.currentTimeMillis();
            // 构造相册索引
            String projection[] = new String[] { MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_ID,
                    MediaStore.Images.Media.PICASA_ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.TITLE,
                    MediaStore.Images.Media.SIZE, MediaStore.Images.Media.BUCKET_DISPLAY_NAME };
            Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null,
                    null);
            getImageBucketColumnData(cur);
            long endTime = System.currentTimeMillis();
            Log.d(TAG, "use time: " + (endTime - startTime) + " ms");
        }
        /**
         * 得到图片集
         */
        private void getImageBucketColumnData(Cursor cur) {
            if (cur.moveToFirst()) {
                // 获取指定列的索引
                int photoIDIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                int photoPathIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                int photoNameIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                int photoTitleIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
                int photoSizeIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
                int bucketDisplayNameIndex = cur
                        .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                int bucketIdIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID);
                int picasaIdIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.PICASA_ID);
                // 获取图片总数
                int totalNum = cur.getCount();

                do {
                    String _id = cur.getString(photoIDIndex);
                    String name = cur.getString(photoNameIndex);
                    String path = cur.getString(photoPathIndex);
                    String title = cur.getString(photoTitleIndex);
                    String size = cur.getString(photoSizeIndex);
                    String bucketName = cur.getString(bucketDisplayNameIndex);
                    String bucketId = cur.getString(bucketIdIndex);
                    String picasaId = cur.getString(picasaIdIndex);

                    ImageItem imageItem = new ImageItem();
                    imageItem.imageId = _id;
                    imageItem.imagePath = path;
                    imageItem.thumbnailPath = thumbnailList.get(_id);
                    imageItem.imageName = name;
                    imageItem.imageTitle = title;
                    items.add(imageItem);

                } while (cur.moveToNext());
            }
            cur.close();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            items.clear();
            items.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            getThumbnail();
            getImageBucket();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.clear();
            adapter.addAll(items);
        }
    }

    private class SwipeFlingrAdapter extends BaseAdapter {

        ArrayList<ImageItem> objs;
        private BitmapCache cache;

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
        public SwipeFlingrAdapter() {
            objs = new ArrayList<>();
            cache = BitmapCache.getBitmapCache();
        }

        public void addAll(Collection<ImageItem> collection) {
            if (isEmpty()) {
                objs.addAll(collection);
                notifyDataSetChanged();
            } else {
                objs.addAll(collection);
            }
        }

        public void clear() {
            objs.clear();
        }

        public boolean isEmpty() {
            return objs.isEmpty();
        }

        public void remove(int index) {
            if (index > -1 && index < objs.size()) {
                objs.remove(index);
                notifyDataSetChanged();
            }
        }

        @Override
        public int getCount() {
            return objs.size();
        }

        @Override
        public ImageItem getItem(int position) {
            if(objs==null ||objs.size()==0) return null;
            return objs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        // TODO: getView
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageItem imageItem = getItem(position);
            if (convertView == null)
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_card, parent, false);
            ViewHolder holder = new ViewHolder();
            convertView.setTag(holder);
            convertView.getLayoutParams().width = cardWidth;
            holder.portraitView = (ImageView) convertView.findViewById(R.id.portrait);
            holder.portraitView.getLayoutParams().height = cardHeight;
            holder.nameView = (TextView) convertView.findViewById(R.id.name);

            cache.displayBmp(holder.portraitView, imageItem.thumbnailPath, imageItem.imagePath, callback);
            holder.nameView.setText(String.format("%s", imageItem.imageName));

            return convertView;
        }

    }

    private static class ViewHolder {
        ImageView portraitView;
        TextView nameView;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
