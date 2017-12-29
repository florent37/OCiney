package com.bdc.ociney.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.bdc.ociney.R;
import com.bdc.ociney.core.BaseActivity;
import com.bdc.ociney.database.AccessBaseFavoris;
import com.bdc.ociney.database.BaseFavoris;
import com.bdc.ociney.fragment.TheaterFragment;
import com.bdc.ociney.fragment.core.BaseFragment;

/**
 * Created by florentchampigny on 21/04/2014.
 */
public class TheaterActivity extends BaseActivity {

    TheaterFragment fragment;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_movie);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ab_back_mtrl_am_alpha));

        charger();
        remplir();
        ajouterListener();


    }

    public void ajouterFragment(BaseFragment fragment) {

    }

    private void charger() {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void remplir() {
        Intent intent = getIntent();
        String theaterJson = intent.getStringExtra(TheaterFragment.THEATER);
        fragment = TheaterFragment.newInstance(theaterJson);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentLayout, fragment);
        ft.commit();
    }

    private void ajouterListener() {

    }

    public void changerActionBarColor(int color, int textColor) {
        try {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));

            int actionBarTitleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
            if (actionBarTitleId > 0) {
                TextView title = (TextView) findViewById(actionBarTitleId);
                if (title != null) {
                    title.setTextColor(textColor);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        } else {
            if (item.getItemId() == R.id.menu_favoris) {
                AccessBaseFavoris db = new AccessBaseFavoris(this);
                db.open();

                if (fragment.getTheater().getCode() != null) {

                    if (db.isFavoris(fragment.getTheater().getCode() + "", BaseFavoris.TYPE_FAVORIS_CINEMA)) {
                        db.deleteFavoris(fragment.getTheater().getCode() + "", BaseFavoris.TYPE_FAVORIS_CINEMA);
                        //Toast.makeText(this, "On supprime cinema", Toast.LENGTH_SHORT).show();
                    } else {
                        db.insertFavoris(fragment.getTheater() + "", BaseFavoris.TYPE_FAVORIS_CINEMA);
                        //Toast.makeText(this, "On insere cinema", Toast.LENGTH_SHORT).show();

                    }

                }
                db.close();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
