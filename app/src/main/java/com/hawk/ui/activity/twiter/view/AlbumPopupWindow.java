package com.hawk.ui.activity.twiter.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;

import com.hawk.ui.activity.R;
import com.hawk.ui.activity.twiter.adapter.AlbumAdapter;
import com.hawk.life.support.bean.ImageBucket;
import com.hawk.adapter.itemanimator.CustomItemAnimator;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/7/28.
 */
public class AlbumPopupWindow extends PopupWindow {


    public interface AlbumClickListener {
        public void click(int position);
    }

    public AlbumPopupWindow(Context mContext, HashMap<String, ImageBucket> buckets, AlbumClickListener clickListener) {

        View view = View
                .inflate(mContext, R.layout.item_album_popupwindow, null);
        view.startAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.fade_ins));

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        setContentView(view);

        RecyclerView recyclerView = (RecyclerView) view
                .findViewById(R.id.ll_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setItemAnimator(new CustomItemAnimator());
        AlbumAdapter albumAdapter = new AlbumAdapter(mContext, buckets);
        albumAdapter.setAlbumClickListener(clickListener);
        recyclerView.setAdapter(albumAdapter);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

}
