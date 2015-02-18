package com.bdc.ociney.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.bdc.ociney.view.ViewCell;

import java.util.List;

/**
 * Created by florentchampigny on 20/04/2014.
 */
public class ObjectAdapterLayout<OBJECT> extends ObjectAdapter<OBJECT> {

    public ObjectAdapterLayout(Context context, List<OBJECT> objets, int layoutId, Class<? extends ViewCell> cellClass, ViewGroup layout) {
        super(context, objets, layoutId, cellClass);

        for (int i = 0; i < getCount(); ++i) {
            try {
                layout.addView(getView(i, null, layout));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
