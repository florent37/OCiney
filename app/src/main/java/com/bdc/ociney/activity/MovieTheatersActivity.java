package com.bdc.ociney.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.bdc.ociney.R;
import com.bdc.ociney.core.BaseActivity;
import com.bdc.ociney.fragment.MovieFragment;
import com.bdc.ociney.fragment.MovieTheatersFragment;
import com.bdc.ociney.fragment.core.BaseFragment;


public class MovieTheatersActivity extends BaseActivity {

    BaseFragment fragment;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_movie_theaters);

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
        String jsonMovie = intent.getStringExtra(MovieFragment.MOVIE);
        fragment = MovieTheatersFragment.newInstance(jsonMovie);


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentLayout, fragment);
        ft.commit();

    }

    private void ajouterListener() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        ;
        return super.onOptionsItemSelected(item);
    }
}
