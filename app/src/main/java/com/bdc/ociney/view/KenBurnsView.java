package com.bdc.ociney.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bdc.ociney.R;

import java.util.Random;

/**
 * Created by f.laurent on 21/11/13.
 */
public class KenBurnsView extends FrameLayout {

    private static final String TAG = "KenBurnsView";

    private final Handler mHandler;
    private final Random random = new Random();
    protected ImageView[] mImageViews;
    protected int mActiveImageIndex = -1;

    protected ImageView activeImageView;
    protected ImageView inactiveImageView;
    protected int mSwapMs = 10000;
    protected int mFadeInOutMs = 1000;
    protected boolean activerKenBurns = false;
    private int[] mResourceIds;
    private float maxScaleFactor = 1.5F;
    private float minScaleFactor = 1.2F;
    private boolean isActive = false;

    public KenBurnsView(Context context) {
        this(context, null);
    }

    public KenBurnsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KenBurnsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mHandler = new Handler();
    }

    public void setResourceIds(int... resourceIds) {
        mResourceIds = resourceIds;
        fillImageViews();
    }

    public void onPause() {
        onDetachedFromWindow();
    }

    public void onResume() {
        onAttachedToWindow();
    }

    protected void swapImage() {
        if (activerKenBurns) {
            if (mActiveImageIndex == -1) {
                mActiveImageIndex = 1;
                if (isActive)
                    animate(mImageViews[mActiveImageIndex]);
                return;
            }

            int inactiveIndex = mActiveImageIndex;
            mActiveImageIndex = (1 + mActiveImageIndex) % mImageViews.length;

            activeImageView = mImageViews[mActiveImageIndex];

            inactiveImageView = mImageViews[inactiveIndex];

            if (isActive)
                animate(activeImageView);

            try {

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setDuration(mFadeInOutMs);
                animatorSet.playTogether(
                        ObjectAnimator.ofFloat(inactiveImageView, "alpha", 1.0f, 0.0f),
                        ObjectAnimator.ofFloat(activeImageView, "alpha", 0.0f, 1.0f)
                );
                animatorSet.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void start(View view, long duration, float fromScale, float toScale, float fromTranslationX, float fromTranslationY, float toTranslationX, float toTranslationY) {
        if (isActive && activerKenBurns) {
            try {
                view.setScaleX(fromScale);
                view.setScaleY(fromScale);
                view.setTranslationX(fromTranslationX);
                view.setTranslationY(fromTranslationY);
                ViewPropertyAnimator propertyAnimator = view.animate().translationX(toTranslationX).translationY(toTranslationY).scaleX(toScale).scaleY(toScale).setDuration(duration);
                propertyAnimator.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private float pickScale() {
        return this.minScaleFactor + this.random.nextFloat() * (this.maxScaleFactor - this.minScaleFactor);
    }

    private float pickTranslation(int value, float ratio) {
        return value * (ratio - 1.0f) * (this.random.nextFloat() - 0.5f);
    }

    public void animate(View view) {
        if (isActive && activerKenBurns) {
            float fromScale = pickScale();
            float toScale = pickScale();
            float fromTranslationX = pickTranslation(view.getWidth(), fromScale);
            float fromTranslationY = pickTranslation(view.getHeight(), fromScale);
            float toTranslationX = pickTranslation(view.getWidth(), toScale);
            float toTranslationY = pickTranslation(view.getHeight(), toScale);
            start(view, this.mSwapMs, fromScale, toScale, fromTranslationX, fromTranslationY, toTranslationX, toTranslationY);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (activerKenBurns) {
            if (!isActive) {
                isActive = true;
                startKenBurnsAnimation();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isActive = false;
        mHandler.removeCallbacks(mSwapImageRunnable);
    }

    protected void startKenBurnsAnimation() {
        try {
            mHandler.removeCallbacks(mSwapImageRunnable);
        } catch (Exception e) {
        }
        mHandler.post(mSwapImageRunnable);
    }

    protected void fillImageViews() {
        for (int i = 0; i < mImageViews.length; i++) {
            mImageViews[i].setImageResource(mResourceIds[i]);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View view = inflate(getContext(), R.layout.view_kenburns, this);

        mImageViews = new ImageView[2];
        mImageViews[0] = (ImageView) view.findViewById(R.id.image0);
        mImageViews[1] = (ImageView) view.findViewById(R.id.image1);
    }

    private Runnable mSwapImageRunnable = new Runnable() {
        @Override
        public void run() {
            if (activerKenBurns) {
                if (isActive)
                    swapImage();
                mHandler.postDelayed(mSwapImageRunnable, mSwapMs - mFadeInOutMs * 2);
            }
        }
    };


}
