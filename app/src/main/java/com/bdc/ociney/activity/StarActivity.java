package com.bdc.ociney.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import com.bdc.ociney.R;
import com.bdc.ociney.database.AccessBaseFavoris;
import com.bdc.ociney.database.BaseFavoris;
import com.bdc.ociney.fragment.StarFragment;
import com.bdc.ociney.fragment.core.BetterFragment;

/**
 * Created by kevin de jesus ferreira on 21/04/2014.
 */
public class StarActivity extends ActionBarActivity {

    final int STATUS_IMAGES = 1;
    int status = STATUS_IMAGES;
    final int STATUS_FILMO = 2;
    StarFragment fragment;
    Menu menu;

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
        String jsonStar = intent.getStringExtra(StarFragment.PERSON);
        fragment = StarFragment.newInstance(jsonStar);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentLayout, fragment);
        ft.commit();
    }

    private void ajouterListener() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.star_menu, menu);
        chargerFavoris();

        try {
            switch (status) {
                case STATUS_IMAGES:
                    afficherPhoto();
                    break;
                case STATUS_FILMO:
                    afficherFilmo();
                    break;
            }
        } catch (Exception e) {
        }

        return true;
    }

    private void chargerFavoris() {

        if (fragment != null && fragment.getPerson() != null && fragment.getPerson().getCode() != null) {
            Integer code = fragment.getPerson().getCode();

            AccessBaseFavoris db = new AccessBaseFavoris(this);
            db.open();

            if (db.isFavoris(code + "", BaseFavoris.TYPE_FAVORIS_STAR)) {
                menu.getItem(2).setIcon(R.drawable.ic_favoris_on);
            } else {
                menu.getItem(2).setIcon(R.drawable.ic_favoris_off);
            }

            db.close();
        }
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            case R.id.menu_images:
                status = STATUS_IMAGES;
                fragment.afficherPhotos(0);
                invalidateOptionsMenu();
                return true;
            case R.id.menu_filmo:
                status = STATUS_FILMO;
                fragment.afficherFilmosFond();
                invalidateOptionsMenu();
                return true;

            case R.id.menu_favoris:
                AccessBaseFavoris db = new AccessBaseFavoris(this);
                db.open();

                if (fragment != null && fragment.getPerson() != null && fragment.getPerson().getCode() != null) {

                    Integer code = fragment.getPerson().getCode();

                    if (db.isFavoris(code + "", BaseFavoris.TYPE_FAVORIS_STAR)) {
                        db.deleteFavoris(code + "", BaseFavoris.TYPE_FAVORIS_STAR);
                        //Toast.makeText(this,"On supprime",Toast.LENGTH_SHORT).show();
                    } else {
                        db.insertFavoris(code + "", BaseFavoris.TYPE_FAVORIS_STAR);
                        //Toast.makeText(this,"On insere",Toast.LENGTH_SHORT).show();

                    }

                }
                db.close();

                invalidateOptionsMenu();

                return true;
        }
        ;
        return super.onOptionsItemSelected(item);
    }

    public void afficherFilmo() {
        menu.getItem(0).setIcon(R.drawable.ic_picture2_30);
        menu.getItem(1).setIcon(R.drawable.ic_filmo_80);
    }

    public void afficherPhoto() {
        menu.getItem(0).setIcon(R.drawable.ic_picture2_80);
        menu.getItem(1).setIcon(R.drawable.ic_filmo_30);
    }
}

