package com.bdc.ociney.activity;

import android.app.ActionBar;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bdc.ociney.R;
import com.bdc.ociney.adapter.SpinnerActionBar;
import com.bdc.ociney.core.BaseActivity;
import com.bdc.ociney.database.AccessBaseFavoris;
import com.bdc.ociney.database.BaseFavoris;
import com.bdc.ociney.fragment.CreditsFragment;
import com.bdc.ociney.fragment.ListMoviesFragment;
import com.bdc.ociney.fragment.ListStarFragment;
import com.bdc.ociney.fragment.ListTheaterFragment;
import com.bdc.ociney.fragment.core.BaseFragment;
import com.bdc.ociney.modele.Movie.Movie;
import com.bdc.ociney.modele.Person.PersonFull;
import com.bdc.ociney.task.LoadListStarTask;
import com.bdc.ociney.view.textview.TypefacedTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AppFragmentActivity extends BaseActivity implements View.OnClickListener, ActionBar.OnNavigationListener, LoadListStarTask.LoadListStarTaskCallBack {

    private static final int POSITION_NOW_SHOWING = 0;
    private static final int POSITION_COMMING_SOON = 1;

    public static String actionBarTitle = "";
    public static String actionBarSubTitle = "";
    public static List<Movie> moviesNowShowing = new ArrayList<Movie>();
    public static List<Movie> moviesCommingSoon = new ArrayList<Movie>();
    BaseFragment activeFragment;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    ActionBarDrawerToggle drawerToggle;
    boolean firstTime = true;

    boolean recherche = false;
    boolean favoris = false;

    int navigationMode = ActionBar.NAVIGATION_MODE_LIST;
    boolean displayTitle = false;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.spinner_nav)
    Spinner spinner;

    @BindView(R.id.drawerStars)
    View drawerStars;

    @BindView(R.id.drawerMovies)
    View drawerMovies;

    @BindView(R.id.drawerTheaters)
    View drawerTheaters;

    @BindView(R.id.drawerFavoris)
    View drawerFavoris;

    @BindView(R.id.drawerCredits)
    View drawerCredits;

    String recherchePlaceholder = "";

    int spinnerVisibilty = View.VISIBLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_activity);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        findViewById(R.id.left_drawer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ajouterFragment(ListMoviesFragment.newInstance(moviesNowShowing, true));

        ajouterListener();

        TypedArray a = getTheme().obtainStyledAttributes(R.style.AppTheme, new int[]{R.attr.homeAsUpIndicator});
        Drawable drawable = AppCompatDrawableManager.get().getDrawable(this, a.getResourceId(0, 0));
        drawable.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
        drawerToggle.setHomeAsUpIndicator(drawable);

        recherchePlaceholder = "Rechercher un film";

    }

    public void ajouterFragment(BaseFragment fragment) {
        if (activeFragment != null)
            activeFragment.onPause();

        activeFragment = fragment;

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLayout, fragment)
                .commitAllowingStateLoss();
    }

    private void viderTitresDrawer() {
        String fontName = "Roboto-Thin.ttf";
        ((TypefacedTextView) drawerStars.findViewById(R.id.text)).setTypeFace(fontName);
        ((TypefacedTextView) drawerMovies.findViewById(R.id.text)).setTypeFace(fontName);
        ((TypefacedTextView) drawerTheaters.findViewById(R.id.text)).setTypeFace(fontName);
        ((TypefacedTextView) drawerFavoris.findViewById(R.id.text)).setTypeFace(fontName);
        ((TypefacedTextView) drawerCredits.findViewById(R.id.text)).setTypeFace(fontName);
    }

    public void setTitreActif(View v) {
        viderTitresDrawer();
        String fontName = "Roboto-Bold.ttf";
        ((TypefacedTextView) v.findViewById(R.id.text)).setTypeFace(fontName);

    }

    private void ajouterListener() {

        drawerStars.setOnClickListener(this);
        drawerMovies.setOnClickListener(this);
        drawerTheaters.setOnClickListener(this);
        drawerFavoris.setOnClickListener(this);
        drawerCredits.setOnClickListener(this);

        drawerToggle = new ActionBarDrawerToggle(this, drawer,
                R.string.app_name, R.string.app_name) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();

                getSupportActionBar().setDisplayShowTitleEnabled(displayTitle);
                getSupportActionBar().setTitle(actionBarTitle);
                spinner.setVisibility(spinnerVisibilty);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                invalidateOptionsMenu();

                getSupportActionBar().setDisplayShowTitleEnabled(true);
                getSupportActionBar().setTitle(R.string.app_name);
                spinner.setVisibility(View.GONE);
            }
        };
        drawer.setDrawerListener(drawerToggle);
        afficherMovieNow();
        refreshActionBar();

        final String[] items = {getString(R.string.a_laffiche), getString(R.string.prochainement)};
        spinner.setAdapter(new SpinnerActionBar(this, R.layout.actionbar_spinner, items, getApplicationContext().getString(R.string.films)));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int itemPosition, long l) {
                if (activeFragment != null && activeFragment instanceof ListMoviesFragment) {

                    actionBarSubTitle = items[itemPosition];

                    if (!firstTime) {

                        switch (itemPosition) {

                            case POSITION_NOW_SHOWING:
                                if (activeFragment instanceof ListMoviesFragment)
                                    ((ListMoviesFragment) activeFragment).refreshAdapter(moviesNowShowing);
                                break;

                            case POSITION_COMMING_SOON:
                                if (activeFragment instanceof ListMoviesFragment)
                                    ((ListMoviesFragment) activeFragment).refreshAdapter(moviesCommingSoon);
                                break;
                        }
                    }
                    firstTime = false;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (recherche) {
            onBackPressed();
            return true;
        } else {

            // Pass the event to ActionBarDrawerToggle, if it returns
            // true, then it has handled the app icon touch event
            if (drawerToggle.onOptionsItemSelected(item)) {

                return true;
            }
            // Handle your other action bar items...

            return super.onOptionsItemSelected(item);
        }
    }

    public void afficherMovieNow() {

        recherchePlaceholder = "Rechercher un film";
        actionBarTitle = "";
        displayTitle = false;
        spinnerVisibilty = View.VISIBLE;
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        spinner.setVisibility(spinnerVisibilty);
        setTitreActif(drawerMovies);
        this.drawer.postDelayed(new Runnable() {
            @Override
            public void run() {
                ajouterFragment(ListMoviesFragment.newInstance(moviesNowShowing, true));
            }
        }, 200);


        this.drawer.postDelayed(new Runnable() {
            @Override
            public void run() {

                firstTime = true;
                final String[] items = {getString(R.string.a_laffiche), getString(R.string.prochainement)};
                spinner.setAdapter(new SpinnerActionBar(AppFragmentActivity.this, R.layout.actionbar_spinner, items, getApplicationContext().getString(R.string.films)));


                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int itemPosition, long l) {

                        if (activeFragment != null && activeFragment instanceof ListMoviesFragment) {

                            actionBarSubTitle = items[itemPosition];

                            if (!firstTime) {

                                switch (itemPosition) {

                                    case POSITION_NOW_SHOWING:
                                        if (activeFragment instanceof ListMoviesFragment)
                                            ((ListMoviesFragment) activeFragment).refreshAdapter(moviesNowShowing);
                                        break;

                                    case POSITION_COMMING_SOON:
                                        if (activeFragment instanceof ListMoviesFragment)
                                            ((ListMoviesFragment) activeFragment).refreshAdapter(moviesCommingSoon);
                                        break;
                                }
                            }
                            firstTime = false;

                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        }, 200);

    }

    public void afficherStars() {
        recherchePlaceholder = "Rechercher une star";
        displayTitle = true;
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        spinnerVisibilty = View.GONE;
        spinner.setVisibility(spinnerVisibilty);
        actionBarTitle = getString(R.string.stars);
        setTitreActif(drawerStars);
        this.drawer.postDelayed(new Runnable() {
            @Override
            public void run() {
                ajouterFragment(new ListStarFragment());
            }
        }, 200);
    }

    public void afficherTheaters() {
        recherchePlaceholder = "Rechercher un cinéma";

        displayTitle = true;
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        spinnerVisibilty = View.GONE;
        spinner.setVisibility(spinnerVisibilty);
        actionBarTitle = getString(R.string.salles);
        setTitreActif(drawerTheaters);
        this.drawer.postDelayed(new Runnable() {
            @Override
            public void run() {
                ajouterFragment(new ListTheaterFragment());
            }
        }, 200);

    }

    public void afficherCredits() {
        displayTitle = true;
        spinnerVisibilty = View.GONE;
        spinner.setVisibility(spinnerVisibilty);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        actionBarTitle = getString(R.string.credits);
        setTitreActif(drawerCredits);
        this.drawer.postDelayed(new Runnable() {
            @Override
            public void run() {
                ajouterFragment(new CreditsFragment());
            }
        }, 200);

    }

    private void afficherFilmsFavoris() {

    }

    public void afficherFavoris() {
        actionBarTitle = "";
        displayTitle = false;
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        spinnerVisibilty = View.VISIBLE;
        spinner.setVisibility(spinnerVisibilty);
        setTitreActif(drawerFavoris);
        this.drawer.postDelayed(new Runnable() {
            @Override
            public void run() {

                firstTime = true;

                final String[] items = {getString(R.string.films), getString(R.string.theaters), getString(R.string.stars)};

                spinner.setAdapter(new SpinnerActionBar(AppFragmentActivity.this, R.layout.actionbar_spinner, items, getApplicationContext().getString(R.string.favoris)));
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int itemPosition, long l) {

                        AccessBaseFavoris db = new AccessBaseFavoris(AppFragmentActivity.this);
                        db.open();
                        actionBarSubTitle = items[itemPosition];

                        switch (itemPosition) {

                            case 0:
                                List<String> films = db.getListFavoris(BaseFavoris.TYPE_FAVORIS_FILM);
                                ajouterFragment(ListMoviesFragment.newInstance(films));
                                break;
                            case 1:
                                List<String> cinemas = db.getListFavoris(BaseFavoris.TYPE_FAVORIS_CINEMA);
                                ajouterFragment(ListTheaterFragment.newInstance(cinemas, true));
                                break;
                            case 2:
                                List<String> stars = db.getListFavoris(BaseFavoris.TYPE_FAVORIS_STAR);
                                ajouterFragment(ListStarFragment.newInstance(stars, true));
                                break;
                        }

                        db.close();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        }, 200);

    }

    public void refreshActionBar() {
        invalidateOptionsMenu();

        spinner.setVisibility(spinnerVisibilty);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(actionBarTitle);

    }

    public void onClick(View v) {

        favoris = false;

        switch (v.getId()) {
            case R.id.drawerMovies:
                afficherMovieNow();
                break;
            case R.id.drawerStars:
                afficherStars();
                break;
            case R.id.drawerTheaters:
                afficherTheaters();
                break;
            case R.id.drawerFavoris:
                afficherFavoris();
                favoris = true;
                break;
            case R.id.drawerCredits:
                afficherCredits();
                break;

        }
        invalidateOptionsMenu();
        refreshActionBar();
        drawer.closeDrawers();

    }

    public void changerActionBarColor(int color, int textColor) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));

            int actionBarTitleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
            if (actionBarTitleId > 0) {
                ((TextView) findViewById(actionBarTitleId)).setTextColor(textColor);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(int i, long l) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (activeFragment != null) {
            MenuInflater inflater = getMenuInflater();
            if (!favoris && (activeFragment instanceof ListMoviesFragment || activeFragment instanceof ListStarFragment || activeFragment instanceof ListTheaterFragment)) {
                inflater.inflate(R.menu.movie_list_menu, menu);

                /*
                final MenuItem searchItem = menu.findItem(R.id.search);
                if (searchItem != null) {

                    final SearchView searchView = (SearchView) searchItem.getActionView();


                    SearchView.SearchAutoComplete theTextArea = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
                    theTextArea.setTextColor(Color.WHITE);
                    theTextArea.setHintTextColor(getResources().getColor(R.color.white30));


                    theTextArea.setHint(recherchePlaceholder);

                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        public boolean onQueryTextChange(String text) {


                        if (activeFragment != null && text.length()>=3) {
                            activeFragment.search(text);
                            //searchItem.collapseActionView();
                            recherche = true;
                        }

                            return false;
                        }

                        public boolean onQueryTextSubmit(String text) {
                            if (activeFragment != null) {

                                displayTitle = true;
                                // getSupportActionBar().setDisplayShowTitleEnabled(displayTitle);
                                actionBarTitle = text;


                                activeFragment.search(text);
                                searchItem.collapseActionView();
                                drawerToggle.setDrawerIndicatorEnabled(false);
                                recherche = true;

                                //refreshActionBar();
                            }
                            return false;
                        }
                    });
                }
*/
            }

        }

        return true;
    }

    @Override
    public void onBackPressed() {

        drawerToggle.setDrawerIndicatorEnabled(true);
        if (recherche) {
            if (activeFragment instanceof ListMoviesFragment) {
                afficherMovieNow();
            } else if (activeFragment instanceof ListStarFragment) {
                afficherStars();
            } else if (activeFragment instanceof ListTheaterFragment) {
                afficherTheaters();
            }

            refreshActionBar();
            recherche = false;
        } else
            super.onBackPressed();

    }

    @Override
    public void onLoadListStarTaskCallBack(List<PersonFull> movies) {

    }
}