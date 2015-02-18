package com.bdc.ociney.view;

import android.content.Context;
import android.view.View;

/**
 * Created by florentchampigny on 20/04/2014.
 */
public abstract class ViewCell<OBJECT> {

    Context context;
    View view;
    OBJECT object;
    int position;

    public ViewCell() {

    }

    public abstract void animer();

    public void construire(Context context, View view) {
        this.context = context;
        this.view = view;
        this.charger();
    }

    public void construire(OBJECT object, int position) {
        this.object = object;
        this.position = position;
        this.remplir();
    }

    public abstract void construire();

    public abstract void charger();

    public abstract void remplir();

    public abstract void ajouterListeners();

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public OBJECT getObject() {
        return object;
    }

    public void setObject(OBJECT object) {
        this.object = object;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void onScroll(float yOffset) {
    }
}
