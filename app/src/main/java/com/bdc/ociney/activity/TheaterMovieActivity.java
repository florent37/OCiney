package com.bdc.ociney.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

import com.bdc.ociney.core.BaseActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bdc.ociney.R;
import com.bdc.ociney.database.AccessBaseFavoris;
import com.bdc.ociney.database.BaseFavoris;
import com.bdc.ociney.fragment.TheaterMovieFragment;
import com.bdc.ociney.modele.Movie.Movie;
import com.bdc.ociney.modele.Theater.Theater;
import com.bdc.ociney.task.LoadTheaterMoviesTask;
import com.bdc.ociney.view.CellTheater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TheaterMovieActivity extends BaseActivity implements LoadTheaterMoviesTask.LoadTheaterMoviesTaskCallBack {

    //public static Theater theaterStatic;

    public static final String THEATER = "theater";

    List<Movie> films = new ArrayList<Movie>();
    ViewPager viewPager;
    FragmentStatePagerAdapter adapter;
    ViewGroup theater_view;
    Menu menu;
    boolean tournerRoulette;
    private Theater theater;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_list_films_theater);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ab_back_mtrl_am_alpha));

        try {
            Intent intent = getIntent();
            String jsonTheater = intent.getStringExtra(THEATER);
            Gson gSon = new Gson();
            theater = gSon.fromJson(jsonTheater, new TypeToken<Theater>() {
            }.getType());

            charger();
            remplir();
            getSupportActionBar().setTitle(theater.getName());

            tournerRoulette(true);
            new LoadTheaterMoviesTask(this).execute(theater);

            viewPager.setAlpha(0f);
            viewPager.setPageMargin((int) getResources().getDimensionPixelSize(R.dimen.theater_viewpager_negative_margin));
            viewPager.setHorizontalFadingEdgeEnabled(true);
            viewPager.setFadingEdgeLength(30);
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.threater_menu, menu);
        chargerFavoris();

        return true;
    }

    private void chargerFavoris() {

        if (theater != null && theater.getCode() != null) {

            String code = theater.getCode();

            AccessBaseFavoris db = new AccessBaseFavoris(this);
            db.open();

            if (db.isFavoris(code + "", BaseFavoris.TYPE_FAVORIS_CINEMA)) {
                menu.getItem(0).setIcon(R.drawable.ic_favoris_on);
            } else {
                menu.getItem(0).setIcon(R.drawable.ic_favoris_off);
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
            case R.id.menu_favoris:
                AccessBaseFavoris db = new AccessBaseFavoris(this);
                db.open();

                if (theater != null && theater.getCode() != null) {

                    String code = theater.getCode();

                    if (db.isFavoris(code, BaseFavoris.TYPE_FAVORIS_CINEMA)) {
                        db.deleteFavoris(code, BaseFavoris.TYPE_FAVORIS_CINEMA);
                        //Toast.makeText(this,"On supprime",Toast.LENGTH_SHORT).show();
                    } else {
                        db.insertFavoris(code, BaseFavoris.TYPE_FAVORIS_CINEMA);
                        //Toast.makeText(this,"On insere",Toast.LENGTH_SHORT).show();

                    }

                }
                db.close();

                try {
                    invalidateOptionsMenu();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return true;
        }


        return false;
    }

    public void charger() {
        this.viewPager = (ViewPager) findViewById(R.id.poster_pager);
        this.theater_view = (ViewGroup) findViewById(R.id.theater_view);
    }

    private void remplir() {

        View cellTheater = View.inflate(this, R.layout.cell_theater, null);
        CellTheater cell = new CellTheater();
        cell.construire(this, cellTheater);
        cell.construire(theater, 0);
        this.theater_view.removeAllViews();
        this.theater_view.addView(cellTheater);
        cell.afficherEnEntier();

        adapter = new FragmentStatePagerAdapter(this.getSupportFragmentManager()) {

            HashMap<Integer, Fragment> fragments = new HashMap<Integer, Fragment>();

            @Override
            public Fragment getItem(int position) {
                if (fragments.containsKey(position))
                    return fragments.get(position);
                else {
                    if (position + 1 < getCount())
                        getItem(position + 1);//preload
                    Fragment f = TheaterMovieFragment.newInstance(films.get(position), theater);
                    fragments.put(position, f);
                    return f;
                }
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                //super.destroyItem(container, position, object);
            }

            @Override
            public int getCount() {
                return films.size();
            }
        };
        this.viewPager.setAdapter(adapter);
    }

    @Override
    public void onLoadTheaterMoviesTaskCallBack(List<Movie> movies) {
        films.clear();
        films.addAll(movies);
        adapter.notifyDataSetChanged();

        tournerRoulette(false);

        AnimatorSet anim = new AnimatorSet().setDuration(500);
        anim.playTogether(
                ObjectAnimator.ofFloat(viewPager, "alpha", 0f, 1f),
                ObjectAnimator.ofFloat(viewPager, "translationX", 500f, 0f)
        );
        anim.start();
    }

    protected void tournerRoulette(boolean tourner) {
        tournerRoulette(tourner, R.id.placeholder_image_loader);
    }

    protected void tournerRoulette(boolean tourner, int id) {

        final View roulette = findViewById(id);

        if (roulette != null) {
            if (tourner) {
                roulette.setVisibility(View.VISIBLE);
                int previousDegrees = 0;
                int degrees = 360;
                final RotateAnimation animation = new RotateAnimation(previousDegrees, degrees,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setFillEnabled(true);
                animation.setFillAfter(true);
                animation.setDuration(1500);//Set the duration of the animation to 1 sec.
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (tournerRoulette) {
                            roulette.startAnimation(animation);
                        } else
                            roulette.animate().alpha(0).start();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                roulette.startAnimation(animation);
            } else {
                tournerRoulette = false;
            }
        }
    }

    @Override
    public void onErreurReseau() {
        Toast.makeText(getApplicationContext(), R.string.erreur_reseau, Toast.LENGTH_SHORT).show();
    }

}
