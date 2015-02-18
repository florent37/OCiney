package com.bdc.ociney.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.bdc.ociney.R;
import com.bdc.ociney.database.AccessBaseFavoris;
import com.bdc.ociney.database.BaseFavoris;
import com.bdc.ociney.fragment.MovieFragment;
import com.bdc.ociney.fragment.core.BetterFragment;

/**
 * Created by florentchampigny on 21/04/2014.
 */
public class MovieActivity extends ActionBarActivity {

    final int STATUS_IMAGES = 1;
    int status = STATUS_IMAGES;
    final int STATUS_VIDEOS = 2;
    MovieFragment fragment;
    boolean afficheCinemas = false;
    boolean afficheCasting = false;
    Menu menu;

    boolean recherche = false;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ab_back_mtrl_am_alpha));

        charger();
        remplir();
        ajouterListener();

        invalidateOptionsMenu();

    }

    public void afficherCinema() {
        afficheCinemas = true;
        invalidateOptionsMenu();
    }


    public void afficherCasting() {
        afficheCasting = true;
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

        if (afficheCinemas || afficheCasting) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.movie_menu_cinema, menu);

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

    public void ajouterFragment(BetterFragment fragment) {

    }

    private void charger() {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ab_back_mtrl_am_alpha));
    }

    private void remplir() {


        Intent intent = getIntent();
        String title = intent.getStringExtra(MovieFragment.ACTIONBAR_TITLE);
        String jsonMovie = intent.getStringExtra(MovieFragment.MOVIE);
        fragment = MovieFragment.newInstance(title, jsonMovie);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentLayout, fragment);
        ft.commit();
    }

    private void ajouterListener() {

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
        if (afficheCinemas) {
            afficheCinemas = false;
            fragment.fermerCinema();
            invalidateOptionsMenu();
        } else if (afficheCasting) {
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
        ;


        return super.onOptionsItemSelected(item);
    }

}
