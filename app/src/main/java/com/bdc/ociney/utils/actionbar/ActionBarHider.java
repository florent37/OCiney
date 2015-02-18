package com.bdc.ociney.utils.actionbar;

import android.app.Activity;
import android.support.v7.widget.Toolbar;

/**
 * Created by florentchampigny on 02/05/2014.
 */
public class ActionBarHider {

    private Activity activity;
    private int idNotMove;
    private Toolbar toolbar;

    public ActionBarHider(Activity activity, Toolbar toolbar, int idNotMove){
        this.activity = activity;
        this.idNotMove = idNotMove;
        this.toolbar = toolbar;
    }

    private int getActionBarHeight() {
        return toolbar.getHeight();
        /*
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, activity.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
        */
    }

    public void setActionBarTranslation(float percent) {
        float y =  (percent * getActionBarHeight());
        toolbar.setTranslationY(y);
        //toolbar.setAlpha(percent);
        /*

        ViewGroup content = ((ViewGroup) activity.findViewById(android.R.id.content).getParent());

        int children = content.getChildCount();
        for (int i = 0; i < children; i++) {
            View child = content.getChildAt(i);

            int id = child.getId();

            if (id != android.R.id.content && id != idNotMove) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    child.setTranslationY(y);
                } else {
                    AnimatorProxy.wrap(child).setTranslationY(y);
                }
            }
        }
        */
    }
}
