package com.bdc.ociney.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.bdc.ociney.R;
import com.bdc.ociney.fragment.ListCastingFragment;
import com.bdc.ociney.fragment.core.BetterFragment;

/**
 * Created by florentchampigny on 21/04/2014.
 */
public class MovieCastingActivity extends ActionBarActivity {

    BetterFragment fragment;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_movie_casting);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ab_back_mtrl_am_alpha));

        charger();
        remplir();
        ajouterListener();


    }

    private void charger() {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void remplir() {

        fragment = new ListCastingFragment();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentLayout, fragment);
        ft.commit();
    }

    private void ajouterListener() {

    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();
                return true;
        };
        return super.onOptionsItemSelected(item);
    }
}
