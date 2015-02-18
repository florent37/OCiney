package com.bdc.ociney.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * A custom ScrollView that can notify a scroll listener when scrolled.
 */
public class ObservableScrollView extends ScrollView {
    private OnScrollListener mScrollListener;

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mScrollListener != null) {
            mScrollListener.onScrollChanged(this);
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
        if (mScrollListener != null) {
            mScrollListener.onScrollChanged(this);
        }
    }

    public boolean isScrollPossible() {
        return computeVerticalScrollRange() > getHeight();
    }

    public void setOnScrollListener(OnScrollListener listener) {
        mScrollListener = listener;
    }

    public static interface OnScrollListener {
        public void onScrollChanged(ObservableScrollView view);
    }
}