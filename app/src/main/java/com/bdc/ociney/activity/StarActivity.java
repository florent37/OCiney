package com.bdc.ociney.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import com.bdc.ociney.R;
import com.bdc.ociney.core.BaseActivity;
import com.bdc.ociney.database.AccessBaseFavoris;
import com.bdc.ociney.database.BaseFavoris;
import com.bdc.ociney.fragment.StarFragment;
import com.github.florent37.androidanalytics.Analytics;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kevin de jesus ferreira on 21/04/2014.
 */
public class StarActivity extends BaseActivity {

    final int STATUS_IMAGES = 1;
    final int STATUS_FILMO = 2;
    int status = STATUS_IMAGES;
    StarFragment fragment;
    Menu menu;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);

        Analytics.screen("star");

        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ab_back_mtrl_am_alpha));

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ab_back_mtrl_am_alpha));

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLayout, fragment = StarFragment.newInstance(getIntent().getStringExtra(StarFragment.PERSON)))
                .commitAllowingStateLoss();
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

