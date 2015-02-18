package com.bdc.ociney.utils;

import android.graphics.Bitmap;

/**
 * Created by florentchampigny on 19/04/2014.
 */
public class ColorUtils {

    public static int couleurPrincipale(Bitmap image) {
        Bitmap onePixelBitmap = Bitmap.createScaledBitmap(image, 1, 1, true);
        return onePixelBitmap.getPixel(0, 0);
    }

}
