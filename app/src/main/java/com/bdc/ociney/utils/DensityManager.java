package com.bdc.ociney.utils;

import android.content.Context;

public class DensityManager {

    public static int pxToDip(Context context, int pixel) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pixel * scale + 0.5f);
    }

    public static float pxToDip(Context context, float pixel) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (pixel * scale + 0.5f);
    }

}
