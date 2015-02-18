package com.bdc.ociney.utils.transformation;

import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

/**
 * Created by florentchampigny on 01/05/2014.
 */
public class CropTransformation implements Transformation {

    int targetWidth;

    public CropTransformation(int width) {
        this.targetWidth = width;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
        int targetHeight = (int) (targetWidth * aspectRatio);
        Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
        if (result != source) {
            // Same bitmap is returned if sizes are the same
            source.recycle();
        }
        return result;
    }

    @Override
    public String key() {
        return "CropTransformation " + targetWidth;
    }
}
