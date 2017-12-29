package com.bdc.ociney.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.bdc.ociney.R;
import com.bdc.ociney.core.BaseActivity;
import com.bdc.ociney.database.AccessBaseFavoris;
import com.bdc.ociney.database.BaseFavoris;
import com.bdc.ociney.fragment.MovieFragment;
import com.github.florent37.androidanalytics.Analytics;
import com.google.android.gms.ads.AdView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by florentchampigny on 21/04/2014.
 */
public class MovieActivity extends BaseActivity {

    final int STATUS_IMAGES = 1;
    final int STATUS_VIDEOS = 2;
    int status = STATUS_IMAGES;
    MovieFragment fragment;
    boolean afficheCasting = false;
    boolean afficheCinemas = false;
    Menu menu;

    boolean recherche = false;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.adView)
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);

        Analytics.screen("movie");

        getAdsManager().executeAdView(adView);

        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ab_back_mtrl_am_alpha));

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ab_back_mtrl_am_alpha));

        Intent intent = getIntent();
        String title = intent.getStringExtra(MovieFragment.ACTIONBAR_TITLE);
        String jsonMovie = intent.getStringExtra(MovieFragment.MOVIE);
        fragment = MovieFragment.newInstance(title, jsonMovie);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLayout, fragment)
                .commitAllowingStateLoss();

        invalidateOptionsMenu();

    }


    public void afficherCasting() {
        afficheCasting = true;
        invalidateOptionsMenu();
    }

    public void afficherCinemas() {
        afficheCinemas = true;
        invalidateOptionsMenu();
    }

    private void chargerFavoris() {
        AccessBaseFavoris db = new AccessBaseFavoris(this);
        db.open();

        if (fragment != null && fragment.getMovie() != null && db.isFavoris(fragment.getMovie().getCode() + "", BaseFavoris.TYPE_FAVORIS_FILM)) {
            menu.getItem(2).setIcon(R.drawable.ic_favoris_on);
        } else
            menu.getItem(2).setIcon(R.drawable.ic_favoris_off);
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;

        if (afficheCasting) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.movie_menu_cinema, menu);

            /*
            final MenuItem searchItem = menu.findItem(R.id.search);
            final SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                public boolean onQueryTextChange(String text) {
                    return false;
                }

                public boolean onQueryTextSubmit(String text) {
                    getSupportActionBar().setTitle(text);

                    fragment.search(text);
                    searchItem.collapseActionView();
                    recherche = true;
                    return false;
                }
            });

            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    //afficherMovieNow();
                    //refreshActionBar();
                    return false;
                }
            });
            */


        } else {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.movie_menu, menu);

            chargerFavoris();


            try {
                switch (status) {
                    case STATUS_IMAGES:
                        menu.getItem(0).setIcon(R.drawable.ic_picture_80);
                        menu.getItem(1).setIcon(R.drawable.ic_bandeannonce_30);
                        break;
                    case STATUS_VIDEOS:
                        menu.getItem(0).setIcon(R.drawable.ic_picture_30);
                        menu.getItem(1).setIcon(R.drawable.ic_bandeannonce_80);
                        break;
                }
            } catch (Exception e) {
            }
        }

        return true;
    }

    public void changerActionBarColor(int color, int textColor) {
        try {
            ActionBar bar = getActionBar();
            bar.setBackgroundDrawable(new ColorDrawable(color));

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
    public void onBackPressed() {
        if (afficheCasting) {
            afficheCasting = false;
            fragment.fermerCasting();
            invalidateOptionsMenu();
        } else
            super.onBackPressed();

        recherche = false;
    }


    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_images:
                status = STATUS_IMAGES;
                fragment.afficherImages();
                invalidateOptionsMenu();
                return true;
            case R.id.menu_bandesAnnonces:
                status = STATUS_VIDEOS;
                fragment.afficherBandesAnnonces();
                invalidateOptionsMenu();
                return true;

            case R.id.menu_favoris:
                AccessBaseFavoris db = new AccessBaseFavoris(this);
                db.open();

                if (fragment != null && fragment.getMovie() != null) {

                    if (db.isFavoris(fragment.getMovie().getCode() + "", BaseFavoris.TYPE_FAVORIS_FILM)) {
                        db.deleteFavoris(fragment.getMovie().getCode() + "", BaseFavoris.TYPE_FAVORIS_FILM);
                    } else {
                        db.insertFavoris(fragment.getMovie().getCode() + "", BaseFavoris.TYPE_FAVORIS_FILM);
                    }

                }

                invalidateOptionsMenu();

                db.close();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
