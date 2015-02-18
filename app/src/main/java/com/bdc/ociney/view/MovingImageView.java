package com.bdc.ociney.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;

import com.bdc.ociney.utils.FastBlurHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MovingImageView extends KenBurnsView {

    Activity activity;
    List<String> urls = new ArrayList<String>();
    int activeUrl = -1;
    boolean blur = false;
    int blurRadius = 60;
    MovingImageViewDelegate delegate = null;

    public MovingImageView(Context context) {
        super(context);
    }

    public MovingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MovingImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public static int couleurPrincipale(Bitmap image) {
        Bitmap onePixelBitmap = Bitmap.createScaledBitmap(image, 1, 1, true);
        return onePixelBitmap.getPixel(0, 0);
    }

    public void setDelegate(MovingImageViewDelegate delegate) {
        this.delegate = delegate;
    }

    public void refresh() {
        if (activeUrl >= 0 && activeImageView != null) {
            RequestCreator rq = Picasso.with(getContext()).load(urls.get(activeUrl));
            if (blur)
                rq = rq.transform(new BlurTransformation());
            rq.into(activeImageView);
        }
    }

    public MovingImageView activerBlur(int radius) {
        this.blurRadius = radius;
        this.blur = true;

        refresh();

        return this;
    }


    public MovingImageView desactiverBlur() {
        this.blur = false;

        refresh();

        return this;
    }

    public MovingImageView activerAnimations() {
        activerKenBurns = true;
        onResume();

        return this;
    }

    public void changerCouleurActionBar(Bitmap image) {

        if (delegate != null) {

            int color = couleurPrincipale(image);

            int r = Color.red(color);
            int g = Color.green(color);
            int b = Color.blue(color);

            int a = 150;

            int newColor = Color.argb(a, r, g, b);

            int textColor = Color.WHITE;
            if (r > 200 && g > 200 && b > 200)
                textColor = Color.BLACK;

            delegate.changerActionBarColor(newColor, textColor);
        }
    }

    protected void fillImageViews() {
    }

    public void loadFromUrl(final Activity activity, String url) {
        this.loadFromUrls(activity, url, url);
    }

    public void loadFromUrls(final Activity activity, String... urls) {
        loadFromUrls(activity, Arrays.asList(urls));
    }

    public void loadFromUrls(final Activity activity, List<String> urls) {
        this.activity = activity;

        this.urls = urls;
        onResume();
        startKenBurnsAnimation();
        swapImage();
    }

    protected void swapImage() {
        if (this.urls != null && this.urls.size() > 0) {

            super.swapImage();
            if (activeImageView != null) {

                activeUrl++;
                if (activeUrl >= this.urls.size())
                    activeUrl = 0;

                String newUrl = this.urls.get(activeUrl);
                if (newUrl != null) {


                    RequestCreator rq = Picasso.with(getContext()).load(newUrl);
                    if (blur)
                        rq = rq.transform(new BlurTransformation());
                    rq.into(activeImageView);
                }
            }
        }

    }

    public interface MovingImageViewDelegate {
        public void changerActionBarColor(int color, int textColor);
    }

    public class BlurTransformation implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            changerCouleurActionBar(source);
            Bitmap result = FastBlurHelper.doBlur(source,blurRadius,false);

            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override
        public String key() {
            return "MovingImageView BlurTransformation" + blurRadius;
        }
    }

}
