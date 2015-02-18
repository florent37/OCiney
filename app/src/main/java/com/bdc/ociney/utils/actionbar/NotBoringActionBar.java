package com.bdc.ociney.utils.actionbar;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.RectF;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.bdc.ociney.R;

/**
 * Created by florentchampigny on 19/04/2014.
 */
public class NotBoringActionBar implements AbsListView.OnScrollListener, com.bdc.ociney.utils.actionbar.ObservableScrollView.OnScrollListener {

    private static final String TAG = "NoBoringActionBarActivity";
    Activity activity;
    private int mActionBarTitleColor;
    private int mActionBarHeight;
    private int mHeaderHeight;
    private int mMinHeaderTranslation;
    private AccelerateDecelerateInterpolator mSmoothInterpolator;

    private RectF mRect1 = new RectF();
    private RectF mRect2 = new RectF();

    private AlphaForegroundColorSpan mAlphaForegroundColorSpan;
    private SpannableString mSpannableString;

    private TypedValue mTypedValue = new TypedValue();

    private View mHeader;
    private View mPlaceHolderView;
    private ImageView mHeaderLogo;


    private Object scrollView;

    public NotBoringActionBar(Activity activity, View mHeader, View mPlaceHolderView, ImageView mHeaderLogo, Object scrollView) {
        this.activity = activity;
        this.mHeader = mHeader;
        this.mPlaceHolderView = mPlaceHolderView;
        this.mHeaderLogo = mHeaderLogo;
        this.scrollView = scrollView;

        ActionBar actionBar = activity.getActionBar();
        actionBar.setIcon(R.drawable.ic_transparent);

        mSmoothInterpolator = new AccelerateDecelerateInterpolator();
        mHeaderHeight = activity.getResources().getDimensionPixelSize(R.dimen.header_height);
        mMinHeaderTranslation = -mHeaderHeight + getActionBarHeight();

        mActionBarTitleColor = activity.getResources().getColor(R.color.actionbar_title_color);

        mSpannableString = new SpannableString(activity.getString(R.string.app_name));
        mAlphaForegroundColorSpan = new AlphaForegroundColorSpan(mActionBarTitleColor);

        if (scrollView instanceof ListView)
            ((ListView) scrollView).setOnScrollListener(this);
        else if (scrollView instanceof com.bdc.ociney.utils.actionbar.ObservableScrollView)
            ((ObservableScrollView) scrollView).setOnScrollListener(this);

    }

    public static float clamp(float value, float max, float min) {
        return Math.max(Math.min(value, min), max);
    }

    public int getActionBarHeight() {
        if (mActionBarHeight != 0) {
            return mActionBarHeight;
        }
        activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, mTypedValue, true);
        mActionBarHeight = TypedValue.complexToDimensionPixelSize(mTypedValue.data, activity.getResources().getDisplayMetrics());
        return mActionBarHeight;
    }

    private ImageView getActionBarIconView() {
        return (ImageView) activity.findViewById(android.R.id.home);
    }

    private RectF getOnScreenRect(RectF rect, View view) {
        rect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        return rect;
    }


    public int getScrollY() {
        if (scrollView instanceof ListView) {
            ListView listView = (ListView) scrollView;
            View c = listView.getChildAt(0);
            if (c == null) {
                return 0;
            }

            int firstVisiblePosition = listView.getFirstVisiblePosition();
            int top = c.getTop();

            int headerHeight = 0;
            if (firstVisiblePosition >= 1) {
                headerHeight = mPlaceHolderView.getHeight();
            }

            return -top + firstVisiblePosition * c.getHeight() + headerHeight;
        } else if (scrollView instanceof com.bdc.ociney.utils.actionbar.ObservableScrollView) {
            ObservableScrollView s = (com.bdc.ociney.utils.actionbar.ObservableScrollView) scrollView;

            return s.getScrollY();
        } else
            return 0;
    }

    private void interpolate(View view1, View view2, float interpolation) {
        getOnScreenRect(mRect1, view1);
        getOnScreenRect(mRect2, view2);

        float scaleX = 1.0F + interpolation * (mRect2.width() / mRect1.width() - 1.0F);
        float scaleY = 1.0F + interpolation * (mRect2.height() / mRect1.height() - 1.0F);
        float translationX = 0.5F * (interpolation * (mRect2.left + mRect2.right - mRect1.left - mRect1.right));
        float translationY = 0.5F * (interpolation * (mRect2.top + mRect2.bottom - mRect1.top - mRect1.bottom));

        view1.setTranslationX(translationX);
        view1.setTranslationY(translationY - mHeader.getTranslationY());
        view1.setScaleX(scaleX);
        view1.setScaleY(scaleY);
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    private void onScroll() {
        int scrollY = getScrollY();

        //sticky actionbar
        mHeader.setTranslationY(Math.max(-scrollY, mMinHeaderTranslation));
        //header_logo --> actionbar icon
        float ratio = clamp(mHeader.getTranslationY() / mMinHeaderTranslation, 0.0f, 1.0f);
        interpolate(mHeaderLogo, getActionBarIconView(), mSmoothInterpolator.getInterpolation(ratio));
        //actionbar title alpha
        //getActionBarTitleView().setAlpha(clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F));
        //---------------------------------
        //better way thanks to @cyrilmottier
        setTitleAlpha(clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F));
    }

    //ListView
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        onScroll();
    }

    //ScrollView
    @Override
    public void onScrollChanged(com.bdc.ociney.utils.actionbar.ObservableScrollView view) {
        onScroll();
    }

    private void setTitleAlpha(float alpha) {
        mAlphaForegroundColorSpan.setAlpha(alpha);
        mSpannableString.setSpan(mAlphaForegroundColorSpan, 0, mSpannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        activity.getActionBar().setTitle(mSpannableString);
    }


}
