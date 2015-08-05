package com.hawk.itemanimator;

import android.support.v7.widget.RecyclerView;
import java.util.List;
import java.util.ArrayList;
import android.view.View;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.animation.Animator;
/**
 * Created by heyong on 15/5/17.
 */
public class CustomItemAnimator extends RecyclerView.ItemAnimator {

    List<RecyclerView.ViewHolder> mPendingAdd = new ArrayList<RecyclerView.ViewHolder>();
    List<RecyclerView.ViewHolder> mPendingRemove = new ArrayList<RecyclerView.ViewHolder>();

    @Override

    public void runPendingAnimations() {
        int animationDuration = 300;

        if (!mPendingAdd.isEmpty()) {
            for (final RecyclerView.ViewHolder viewHolder : mPendingAdd) {
                View target = viewHolder.itemView;
                target.setPivotX(target.getMeasuredWidth() / 2);
                target.setPivotY(target.getMeasuredHeight() / 2);

                AnimatorSet animator = new AnimatorSet();

                animator.playTogether(
                        ObjectAnimator.ofFloat(target, "translationX", -target.getMeasuredWidth(), 0.0f),
                        ObjectAnimator.ofFloat(target, "alpha", target.getAlpha(), 1.0f)
                );

                animator.setTarget(target);
                animator.setDuration(animationDuration);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setStartDelay((animationDuration * viewHolder.getPosition()) / 10);
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mPendingAdd.remove(viewHolder);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                animator.start();
            }
        }
        if (!mPendingRemove.isEmpty()) {
            for (final RecyclerView.ViewHolder viewHolder : mPendingRemove) {
                View target = viewHolder.itemView;
                target.setPivotX(target.getMeasuredWidth() / 2);
                target.setPivotY(target.getMeasuredHeight() / 2);

                AnimatorSet animator = new AnimatorSet();

                animator.playTogether(
                        ObjectAnimator.ofFloat(target, "translationX", 0.0f, target.getMeasuredWidth()),
                        ObjectAnimator.ofFloat(target, "alpha", target.getAlpha(), 0.0f)
                );

                animator.setTarget(target);
                animator.setDuration(animationDuration);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setStartDelay((animationDuration * viewHolder.getPosition()) / 10);
                animator.start();
            }
        }
    }

    @Override
    public boolean animateRemove(RecyclerView.ViewHolder viewHolder) {
        mPendingRemove.add(viewHolder);
        return false;
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        holder.itemView.setAlpha(0.0f);
        return false;
    }

    @Override
    public boolean animateMove(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
        return false;
    }

    @Override
    public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromLeft, int fromTop, int toLeft, int toTop) {
        return false;
    }

    @Override
    public void endAnimation(RecyclerView.ViewHolder item) {

    }

    @Override
    public void endAnimations() {

    }

    @Override
    public boolean isRunning() {
        return !mPendingAdd.isEmpty() || !mPendingRemove.isEmpty();
    }
}
