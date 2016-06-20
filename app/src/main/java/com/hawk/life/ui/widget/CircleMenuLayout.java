package com.hawk.life.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hawk.ui.activity.R;

/**
 * Created by Administrator on 2016/3/2.
 */
public class CircleMenuLayout extends ViewGroup {

    private static final float DEFAULT_CENTER_DIMENSION_RATIO = 1/3f;
    private static final float DEFAULT_CHILD_DIMENSION_RATIO = 1/4f;
    private static final int DEFAULT_CIRCLE_CENTER = R.drawable.circlemenu_bg;
    private float mCenterItemDimensionRatio;
    private float mChildItemDimensionRatio;
    private int mCircleCenter;
    private int mRadius;

    public CircleMenuLayout(Context context) {
        super(context);
        init(context);
    }

    public CircleMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleMenuLayout, defStyleAttr, 0);
        mCenterItemDimensionRatio = typedArray.getFloat(R.styleable.CircleMenuLayout_center_ratio, DEFAULT_CENTER_DIMENSION_RATIO);
        mChildItemDimensionRatio = typedArray.getFloat(R.styleable.CircleMenuLayout_child_ratio, DEFAULT_CHILD_DIMENSION_RATIO);
        mCircleCenter = typedArray.getResourceId(R.styleable.CircleMenuLayout_circle_center, DEFAULT_CIRCLE_CENTER);
        typedArray.recycle();
        init(context);
    }

    private void init(Context context) {
        ImageView circleCenter = new ImageView(context);
        circleCenter.setId(R.id.circle_center);
        circleCenter.setImageResource(mCircleCenter);
        addView(circleCenter);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getSuggestedMinimumWidth(), getSuggestedMinimumHeight());
        mRadius = Math.max(widthMeasureSpec, heightMeasureSpec);

        int childCount = getChildCount();
        int childSize = (int) (mRadius * mChildItemDimensionRatio);
        int childMode = MeasureSpec.EXACTLY;

        for(int i = 0; i < childCount; i++) {
            View view = getChildAt(i);

            if(view.getVisibility() == GONE) {
                continue;
            }
            int makeMeasureSpec = -1;
            if(view.getId() == R.id.circle_center) {
                makeMeasureSpec = MeasureSpec.makeMeasureSpec((int)(mRadius*mCenterItemDimensionRatio), childMode);
            }
            else {
                makeMeasureSpec = MeasureSpec.makeMeasureSpec(childSize, childMode);
            }
            view.measure(makeMeasureSpec, makeMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int layoutWidth = r - l;
        int layoutHeight = b - t;
        int layoutRadius = Math.max(layoutWidth, layoutHeight);

        int radius =(int)(layoutRadius * mChildItemDimensionRatio);
        float angleDegree = 360 / (getChildCount() - 1);
        int left, top;
        float startAngle = 0;

        for(int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);

            if(view.getId() == R.id.circle_center)
                continue;
            if(view.getVisibility() == GONE)
                continue;

            startAngle %= 360;
            float tmp = layoutRadius*(1 - mCenterItemDimensionRatio)/2  ;
            left = layoutRadius/2 + (int)Math.round(tmp * Math.cos(Math.toRadians(startAngle)) - radius * 1/2f);
            top = layoutRadius/2 + (int)Math.round(tmp * Math.sin(Math.toRadians(startAngle)) - radius * 1/2f);

            view.layout(left, top, left + radius, top + radius);
            startAngle += angleDegree;
        }
        View centerView = findViewById(R.id.circle_center);
        int centerL = (layoutRadius - centerView.getMeasuredWidth() ) / 2;
        int centerR = centerL + centerView.getMeasuredWidth();
        centerView.layout(centerL, centerL, centerR, centerR);
    }
}
