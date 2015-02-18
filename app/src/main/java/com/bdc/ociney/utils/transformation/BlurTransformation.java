package com.bdc.ociney.utils.transformation;

import android.graphics.Bitmap;
import com.bdc.ociney.utils.FastBlurHelper;
import com.squareup.picasso.Transformation;

/**
 * Created by florentchampigny on 01/05/2014.
 */
public class BlurTransformation implements Transformation {
    @Override
    public Bitmap transform(Bitmap source) {
        Bitmap result = FastBlurHelper.doBlur(source, 10, false);

        if (result != source) {
            source.recycle();
        }
        return result;
    }

    @Override
    public String key() {
        return "BlurTransformation";
    }
}