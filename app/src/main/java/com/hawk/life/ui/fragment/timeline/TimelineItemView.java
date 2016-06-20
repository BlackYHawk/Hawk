package com.hawk.life.ui.fragment.timeline;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.hawk.library.common.context.GlobalContext;
import com.hawk.library.component.bitmaploader.BitmapLoader;
import com.hawk.library.support.adapter.ABaseAdapter;
import com.hawk.library.support.inject.ViewInject;
import com.hawk.library.ui.fragment.ABaseFragment;
import com.hawk.life.base.AppContext;
import com.hawk.life.base.AppSettings;
import com.hawk.life.support.bean.StatusContent;
import com.hawk.life.support.bean.WeiBoUser;
import com.hawk.life.support.utils.AisenUtils;
import com.hawk.life.support.utils.ImageConfigUtils;
import com.hawk.life.ui.fragment.base.BizFragment;
import com.hawk.life.ui.widget.AisenTextView;
import com.hawk.life.ui.widget.TimelinePicsView;
import com.hawk.ui.activity.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wangdan on 15/4/15.
 */
public class TimelineItemView extends ABaseAdapter.AbstractItemView<StatusContent>
                implements View.OnClickListener {

    protected ABaseFragment fragment;
    protected BizFragment bizFragment;

    private boolean showRetweeted;
    private StatusContent reStatus;

    public TimelineItemView(ABaseFragment fragment, boolean showRetweeted) {
        this(fragment, null, showRetweeted);
    }

    // 2014-08-24 新增这个构造方法，解决转发列表，点击转发菜单时，没有带上原微博的BUG
    public TimelineItemView(ABaseFragment fragment, StatusContent reStatue, boolean showRetweeted) {
        this();

        this.fragment = fragment;
        this.reStatus = reStatue;
        try {
            bizFragment = BizFragment.getBizFragment(fragment);
        } catch (Exception e) {
        }
        this.showRetweeted = showRetweeted;
    }

    private TimelineItemView() {
        vPadding = GlobalContext.getInstance().getResources().getDimensionPixelSize(R.dimen.comm_v_gap);

    }

    @ViewInject(id = R.id.imgPhoto)
    ImageView imgPhoto;
    @ViewInject(id = R.id.txtName)
    TextView txtName;
    @ViewInject(id = R.id.imgVerified)
    ImageView imgVerified;
    @ViewInject(id = R.id.txtDesc)
    TextView txtDesc;

    @ViewInject(id = R.id.btnLike)
    View btnLike;
    @ViewInject(id = R.id.imgLike)
    ImageView imgLike;
    @ViewInject(id = R.id.txtLike)
    TextView txtLike;
    @ViewInject(id = R.id.btnRepost)
    View btnRepost;
    @ViewInject(id = R.id.txtRepost)
    protected TextView txtRepost;
    @ViewInject(id = R.id.btnCmt)
    View btnComment;
    @ViewInject(id = R.id.txtComment)
    protected TextView txtComment;

    @ViewInject(id = R.id.txtContent)
    AisenTextView txtContent;

    @ViewInject(id = R.id.layRe)
    View layRe;

    @ViewInject(id = R.id.txtReContent)
    AisenTextView txtReContent;

    @ViewInject(id = R.id.layPicturs)
    public TimelinePicsView layPicturs;

    @ViewInject(id = R.id.btnMenus)
    View btnMenus;

    @ViewInject(id = R.id.txtPics)
    TextView txtPics;
    @ViewInject(id = R.id.txtVisiable)
    TextView txtVisiable;

    private int vPadding;
    private static Map<String, String> groupMap;

    private float textSize;

    @Override
    public int inflateViewId() {
        return R.layout.item_timeline;
    }

    @Override
    public void bindingData(View convertView, StatusContent data) {
        if (bizFragment == null) {
            try {
                bizFragment = BizFragment.getBizFragment(fragment);
            } catch (Exception e) {
            }
            if (bizFragment == null)
                return;
        }

        if (textSize != AppSettings.getTextSize())
            textSize = AppSettings.getTextSize();

        WeiBoUser user = data.getUser();

        // userInfo
        setUserInfo(user, txtName, imgPhoto, imgVerified);
//		setTextSize(txtName, textSize);

        // desc
        String createAt = "";
        if (!TextUtils.isEmpty(data.getCreated_at()))
            createAt = AisenUtils.convDate(data.getCreated_at());
        String from = "";
        if (!TextUtils.isEmpty(data.getSource()))
            from = String.format("%s", Html.fromHtml(data.getSource()));
        String desc = String.format("%s %s", createAt, from);
        txtDesc.setText(desc);
//		setTextSize(txtDesc, textSize * 0.9f);


        if (btnRepost != null) {
            btnRepost.setTag(data);
            btnRepost.setOnClickListener(this);

            if (data.getVisible() == null || "0".equals(data.getVisible().getType()))
                btnRepost.setVisibility(View.VISIBLE);
            else
                btnRepost.setVisibility(View.GONE);
        }

        if (btnComment != null) {
            btnComment.setTag(data);
            btnComment.setOnClickListener(this);
        }

        txtContent.setContent(data.getText());
        setTextSize(txtContent, textSize);

        btnMenus.setTag(data);
        btnMenus.setOnClickListener(this);
    }

    public static void setTextSize(TextView textView, float size) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    private void setUserInfo(WeiBoUser user, TextView txtName, ImageView imgPhoto, ImageView imgVerified) {
        if (user != null) {
            txtName.setText(AisenUtils.getUserScreenName(user));

            if (imgPhoto != null) {
                BitmapLoader.getInstance().display(fragment, AisenUtils.getUserPhoto(user), imgPhoto, ImageConfigUtils.getLargePhotoConfig());
                bizFragment.userShow(imgPhoto, user);
            }
        }
        else {
            if (imgPhoto != null) {
                imgPhoto.setImageDrawable(new ColorDrawable(Color.GRAY));
                bizFragment.userShow(imgPhoto, null);
            }

            imgVerified.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        // 溢出菜单
        if (v.getId() == R.id.btnMenus) {
            final String[] timelineMenuArr = GlobalContext.getInstance().getResources().getStringArray(R.array.timeline_menus);
            final StatusContent status = (StatusContent) v.getTag();

            List<String> menuList = new ArrayList<String>();

//			menuList.add(timelineMenuArr[3]);
//			if (status.getVisible() == null || "0".equals(status.getVisible().getType()))
//				menuList.add(timelineMenuArr[2]);
            menuList.add(timelineMenuArr[4]);
            menuList.add(timelineMenuArr[5]);
            menuList.add(timelineMenuArr[1]);
            if (status.getUser() != null && status.getUser().getIdstr().equals(AppContext.getUser().getIdstr()))
                menuList.add(timelineMenuArr[6]);


            final String[] menus = new String[menuList.size()];
            for (int i = 0; i < menuList.size(); i++)
                menus[i] = menuList.get(i);

            AisenUtils.showMenuDialog(fragment, v, menus,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
        }
    }

    protected void animScale(final View likeView) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.5f, 1.0f, 1.5f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(200);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.start();
        likeView.startAnimation(scaleAnimation);
        likeView.postDelayed(new Runnable() {

            @Override
            public void run() {
                ScaleAnimation scaleAnimation = new ScaleAnimation(1.5f, 1.0f, 1.5f, 1.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setDuration(200);
                scaleAnimation.setFillAfter(true);
                likeView.startAnimation(scaleAnimation);
            }

        }, 200);
    }

}
