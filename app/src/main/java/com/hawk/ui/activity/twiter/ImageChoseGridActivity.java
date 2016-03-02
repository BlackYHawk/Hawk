package com.hawk.ui.activity.twiter;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hawk.ui.activity.R;
import com.hawk.ui.activity.twiter.view.AlbumPopupWindow;
import com.hawk.adapter.ChoseGridAdapter;
import com.hawk.data.cache.Bimp;
import com.hawk.data.model.ImageBucket;
import com.hawk.data.model.ImageItem;
import com.hawk.middleware.util.FileUtil;
import com.hawk.util.Constants;
import com.hawk.util.UIHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class ImageChoseGridActivity extends AppCompatActivity implements View.OnClickListener,
        AlbumPopupWindow.AlbumClickListener, ChoseGridAdapter.ImageCheckListener,
        ChoseGridAdapter.ImageCaptureListener {

    private static String TAG = "ImageChoseGridActivity";
    private Toolbar toolbar;
    private TextView title;
    private ImageButton actionFinish;
    private TextView album;
    private TextView preview;

    private GridView noScrollgridview;
    private ChoseGridAdapter choseGridAdapter;
    private HashMap<String, ImageBucket> buckets;
    private List<ImageItem> wholeItems;
    private List<ImageItem> items;
    private List<ImageItem> choseItems;

    private RelativeLayout album_relativeLayout;
    private AlbumPopupWindow albumPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_image_chose_grid);

        init();
        initView();

        new InitImageTask(this).execute();
    }

    private void init() {
        buckets = new HashMap<String, ImageBucket>();
        items = new ArrayList<ImageItem>();
        wholeItems = new ArrayList<ImageItem>();
        choseItems = new ArrayList<ImageItem>();
    }

    private void initView() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        album_relativeLayout = (RelativeLayout) findViewById(R.id.album_relativeLayout);
        album_relativeLayout.setBackgroundColor(0x70000000);

        title = (TextView) findViewById(R.id.toolbar_title);
        title.setText(R.string.activity_image_chose);
        actionFinish = (ImageButton) findViewById(R.id.action_finish);
        actionFinish.setOnClickListener(this);
        album = (TextView) findViewById(R.id.photo_album);
        album.setOnClickListener(this);
        preview = (TextView) findViewById(R.id.photo_preview);
        preview.setOnClickListener(this);

        noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
        choseGridAdapter = new ChoseGridAdapter(this, items);
        choseGridAdapter.setImageCheckListener(this);
        choseGridAdapter.setImageCaptureListener(this);
        noScrollgridview.setAdapter(choseGridAdapter);
    }

    @Override
    public void check(boolean chose, ImageItem imageItem) {
        if(chose) {
            choseItems.add(imageItem);
        }
        else {
            choseItems.remove(imageItem);
        }
    }

    @Override
    public void click(int position) {
        String bucketId = null;
        ImageBucket bucket = null;
        Iterator iterator = buckets.entrySet().iterator();
        int i = 0;
        while (iterator.hasNext()) {
            HashMap.Entry<String, ImageBucket> entry = (HashMap.Entry<String, ImageBucket>)iterator.next();

            if(position == i) {
                bucketId = entry.getKey();
                bucket = entry.getValue();
                break;
            } else {
                i++;
            }
        }
        if(bucketId != null && bucket != null) {
            album.setText(bucket.bucketName);
            getTotalImageItem(bucketId);
            choseGridAdapter.notifyDataSetChanged();
            noScrollgridview.smoothScrollToPosition(0);
        }
        albumPopupWindow.dismiss();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.action_finish :
                if (Bimp.drr.size() < 9) {
                    for(ImageItem imageItem : choseItems) {
                        Bimp.drr.add(imageItem.imagePath);
                    }
                }
                finish();
                break;
            case R.id.photo_album :
                albumPopupWindow = new AlbumPopupWindow(ImageChoseGridActivity.this, buckets, this);
                albumPopupWindow.showAtLocation(album_relativeLayout, Gravity.BOTTOM, 0, -album_relativeLayout.getHeight());
                break;
            case R.id.photo_preview :

                break;
        }
    }

    @Override
    public void capture() {
        photo();
    }

    private static final int TAKE_PICTURE = 0x000000;
    private String path = "";

    public void photo() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = FileUtil.createFile(Constants.IMAGE, String.valueOf(System.currentTimeMillis())
                + Constants.IMAGE_FORMAT);
        if(file != null) {
            path = file.getPath();
            Uri imageUri = Uri.fromFile(file);
            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(openCameraIntent, TAKE_PICTURE);
        } else {
            UIHelper.showToast(this, "不支持SD卡");
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK ) {
            switch (requestCode) {
                case TAKE_PICTURE: {
                    if (Bimp.drr.size() < 9) {
                        Bimp.drr.add(path);
                    } else {
                        UIHelper.showToast(this, "不能超过9张");
                    }
                    finish();
                    break;
                }
            }
        }
    }


    //从相册中获取所有图片
    private void forEachBuckets() {
        Iterator<Map.Entry<String, ImageBucket>> itr = buckets.entrySet()
                .iterator();
        while (itr.hasNext()) {
            Map.Entry<String, ImageBucket> entry = (Map.Entry<String, ImageBucket>) itr
                    .next();
            ImageBucket bucket = entry.getValue();

            for (int i = 0; i < bucket.imageList.size(); i++) {
                ImageItem image = bucket.imageList.get(i);
                items.add(image);
                wholeItems.add(image);
            }
        }
    }

    private void getTotalImageItem(String bucketId) {
        items.clear();

        if(bucketId == null || bucketId.equals("")) {
            forEachBuckets();

            ImageBucket bucket = buckets.get("-1");
            if (bucket == null) {
                bucket = new ImageBucket();
                bucket.imageList = items;
                bucket.bucketName = "所有";
                bucket.count = items.size();
                buckets.put("-1", bucket);
            }
        }
        else {
            ImageBucket bucket = buckets.get(bucketId);

            if(bucketId.equals("-1")) {
                items.addAll(wholeItems);
            } else {
                for (int i = 0; i < bucket.imageList.size(); i++) {
                    ImageItem image = bucket.imageList.get(i);
                    items.add(image);
                }
            }
        }
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

                    ImageBucket bucket = buckets.get(bucketId);
                    if (bucket == null) {
                        bucket = new ImageBucket();
                        buckets.put(bucketId, bucket);
                        bucket.imageList = new ArrayList<ImageItem>();
                        bucket.bucketName = bucketName;
                    }
                    bucket.count++;
                    ImageItem imageItem = new ImageItem();
                    imageItem.imageId = _id;
                    imageItem.imagePath = path;
                    imageItem.thumbnailPath = thumbnailList.get(_id);
                    bucket.imageList.add(imageItem);

                } while (cur.moveToNext());
            }
            cur.close();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            buckets.clear();
            items.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            getThumbnail();
            getImageBucket();
            getTotalImageItem(null);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            choseGridAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wholeItems.clear();
        wholeItems = null;
        items.clear();
        items = null;
    }
}
