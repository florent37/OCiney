package com.bdc.ociney.view.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.bdc.ociney.R;

public class TypefacedTextView extends TextView {

    public TypefacedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //Typeface.createFromAsset doesn't work in the layout editor. Skipping...
        if (isInEditMode()) {
            return;
        }

        TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.TypefacedTextView);
        String fontName = styledAttrs.getString(R.styleable.TypefacedTextView_typeface);
        styledAttrs.recycle();

        setTypeFace(fontName);
    }

    public void setTypeFace(String fontName) {
        try {
            Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + fontName);
            setTypeface(typeface);

        } catch (Exception e) {
            //Log.e("FONT", "Police " + fontName + " introuvable grosse !");
        }
    }

}