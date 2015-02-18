package com.bdc.ociney.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * A custom ScrollView that can notify a scroll listener when scrolled.
 */
public class ObservableScrollViewAnimateVisible extends ObservableScrollView {
    List<View> vuesAAnimer = new ArrayList<View>();

    public ObservableScrollViewAnimateVisible(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void start() {
        /*


        //chargement des animations
        this.vuesAAnimer.addAll(ViewUtils.getViewsByTag(this, getResources().getString(R.string.animer)));
        for(View v : vuesAAnimer)
        {
            v.setAlpha(0);
        }
        postDelayed(new Runnable() {
            @Override
            public void run() {
                animerVues();
            }
        }, 500);

        */
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        animerVues();
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);

        animerVues();
    }

    public void animerVues() {
        List<View> vs = new ArrayList<View>();
        for (View v : vuesAAnimer) {
            Rect scrollBounds = new Rect();
            getHitRect(scrollBounds);
            if (v.getLocalVisibleRect(scrollBounds)) {
                vs.add(v);
            } else {
                // NONE of the view is within the visible window
            }
        }

        for (View v : vs) {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.setDuration(1000);
            animatorSet.playTogether(
                    ObjectAnimator.ofFloat(v, "alpha", 0, 1),
                    ObjectAnimator.ofFloat(v, "translationY", 200, 0)
            );
            animatorSet.start();

            vuesAAnimer.remove(v);
        }

    }

}