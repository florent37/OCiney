package com.bdc.ociney.activity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bdc.ociney.core.BaseActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bdc.ociney.R;
import com.bdc.ociney.adapter.BandesAnnoncesPagerAdapter;
import com.bdc.ociney.adapter.TheaterHorairesPagerAdapter;
import com.bdc.ociney.fragment.MovieFragment;
import com.bdc.ociney.modele.Media;
import com.bdc.ociney.modele.Movie.Movie;
import com.bdc.ociney.modele.Theater.Horaires;
import com.bdc.ociney.modele.Theater.Theater;
import com.bdc.ociney.task.LoadMovieFullTask;
import com.bdc.ociney.utils.SeanceUtils;
import com.bdc.ociney.utils.transformation.ParallaxTransformer;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by florentchampigny on 21/04/2014.
 */
public class TheaterDetailMovie extends BaseActivity implements BandesAnnoncesPagerAdapter.OnBandeAnnonceClickedListener, View.OnClickListener, LoadMovieFullTask.LoadMovieFullTaskCallBack {

    public static final String MOVIE = "movie";
    public static final String THEATER = "theater";

    Movie movie;
    Theater theater;

    SlidingUpPanelLayout slidingDrawer;
    ImageView imagePoster;
    TextView titre;
    TextView duree;
    TextView genre;

    TextView ratingLabel;
    RatingBar ratingBar;

    View isVF, is2D, is3D, isVO;

    ViewPager pagerHoraires, bandesAnnonces;

    View rideauGauche, rideauDroit;
    boolean tournerRoulette;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.fragment_theater_detail_movie);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ab_back_mtrl_am_alpha));

        Intent intent = getIntent();
        if (intent.getExtras().containsKey(MOVIE) && intent.getExtras().containsKey(THEATER)) {
            Gson gSon = new Gson();
            this.movie = gSon.fromJson(intent.getStringExtra(MOVIE), new TypeToken<Movie>() {
            }.getType());
            this.theater = gSon.fromJson(intent.getStringExtra(THEATER), new TypeToken<Theater>() {
            }.getType());

            if (theater != null && movie != null) {

                getSupportActionBar().setTitle(theater.getName());

                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ab_back_mtrl_am_alpha));

                charger();
                remplir();

                tournerRoulette(true);

                new LoadMovieFullTask(this).execute(movie.getCode().toString());

                //ajouterLsitener();
            }
        }
    }

    private void charger() {
        this.rideauGauche = findViewById(R.id.rideauGauche);
        this.rideauDroit = findViewById(R.id.rideauDroit);

        this.pagerHoraires = (ViewPager) findViewById(R.id.pagerHoraires);
        this.bandesAnnonces = (ViewPager) findViewById(R.id.bandesAnnonces);

        slidingDrawer = (SlidingUpPanelLayout) findViewById(R.id.slidingDrawer);

        this.imagePoster = (ImageView) findViewById(R.id.imagePoster);
        this.titre = (TextView) findViewById(R.id.titre);
        this.duree = (TextView) findViewById(R.id.duree);
        this.genre = (TextView) findViewById(R.id.genre);

        this.ratingLabel = (TextView) findViewById(R.id.ratingUserNum);
        this.ratingBar = (RatingBar) findViewById(R.id.ratingUserStar);

        isVF = findViewById(R.id.movieVF);
        is2D = findViewById(R.id.movie2D);
        is3D = findViewById(R.id.movie3D);
        isVO = findViewById(R.id.movieVO);
    }

    private void remplir() {

        remplirHeader();
        remplirSeances();
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

    private void remplirSeances() {
        pagerHoraires.setAdapter(new TheaterHorairesPagerAdapter(getSupportFragmentManager(), this, theater.getHorairesSemaineJours(), theater.getHorairesSemaine()));
    }

    private void remplirBandesAnnonces(Movie m) {
        bandesAnnonces.setAdapter(new BandesAnnoncesPagerAdapter(getSupportFragmentManager(), m.getBandesAnnonces(), m, this, true));
        bandesAnnonces.setPageTransformer(false, new ParallaxTransformer(R.id.parallaxContent));
    }

    @Override
    public void onBandeAnnonceClicked(Media bandeAnnonce) {

        String videoPath = "";

        videoPath = bandeAnnonce.getRendition().get(0).getHref();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(videoPath), "video/mp4");
        startActivity(intent);
    }

    private void ajouterLsitener() {
        imagePoster.setOnClickListener(this);
    }

    private void opening() {
        rideauDroit.animate().translationX(750).setDuration(2000).setInterpolator(new AccelerateDecelerateInterpolator()).start();
        rideauGauche.animate().translationX(-750).setDuration(2000).setInterpolator(new AccelerateDecelerateInterpolator()).start();
        findViewById(R.id.radial).animate().alpha(0.6f).setDuration(2000).setInterpolator(new AccelerateDecelerateInterpolator()).start();
    }


    protected void remplirHeader() {

        isVF.setVisibility(View.GONE);
        is2D.setVisibility(View.GONE);
        is3D.setVisibility(View.GONE);
        isVO.setVisibility(View.GONE);

        if (movie != null) {

            if (movie.isVF())
                isVF.setVisibility(View.VISIBLE);
            if (movie.is2D())
                is2D.setVisibility(View.VISIBLE);
            if (movie.is3D())
                is3D.setVisibility(View.VISIBLE);
            if (movie.isVO())
                isVO.setVisibility(View.VISIBLE);


            //Picasso.with(getActivity()).load(this.moviePoster).into(imageFond);
            Picasso.with(this)
                    .load(this.movie.getUrlPoster(this.getResources().getInteger(R.integer.fragment_movie_poster_height)))
                    .into(imagePoster);

            chargerRating();

            if (movie.getTitle() != null)
                titre.setText(movie.getTitle());
            duree.setText(movie.getDuree());
            genre.setText(movie.getGenres());

        }
    }

    private void chargerRating() {

        float rating = (movie.getUserRating() + movie.getPressRating()) / 2;

        if (rating == 0) {
            ratingLabel.setVisibility(View.GONE);
            ratingBar.setVisibility(View.GONE);
        } else {
            ratingLabel.setText("(" + String.format("%.1f", rating) + ")");
            ratingBar.setRating(rating);
            ratingBar.setIsIndicator(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.threater_detail_movie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            case R.id.menu_calendar:
                demanderSeance();
                return true;
        }
        ;
        return super.onOptionsItemSelected(item);
    }

    public void demanderSeance() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(R.string.choisir_seance);


        final String jour = theater.getHorairesSemaineJours().get(pagerHoraires.getCurrentItem());
        List<Horaires> horairesJour = theater.getHorairesSemaine().get(jour);

        final List<String> seances = new ArrayList<String>();

        for (int i = 0; i < horairesJour.size(); ++i) {
            for (int j = 0; j < horairesJour.get(i).getSeances().size(); ++j) {
                seances.add(horairesJour.get(i).getSeances().get(j));
            }
        }

        String[] seancesString = new String[seances.size()];
        seancesString = seances.toArray(seancesString);

        b.setItems(seancesString, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                try {

                    String seance = seances.get(which);

                    final SimpleDateFormat parser =
                            new SimpleDateFormat("EEE dd MMM yyyy HH:mm", Locale.FRANCE);

                    String d = jour + " " + seance;

                    Date dateDebut = parser.parse(d);

                    final long hoursInMillis = 60L * 60L * 1000L;
                    Date dateFin = new Date(dateDebut.getTime() +
                            (2L * hoursInMillis));

                    final SimpleDateFormat parserOut =
                            new SimpleDateFormat("yyyy-MM-dd-HH-mm", Locale.FRANCE);

                    String sdateDebut = parserOut.format(dateDebut);
                    String sdateFin = parserOut.format(dateFin);

                    SeanceUtils.ajouterSeance(TheaterDetailMovie.this, movie.getTitle(), theater.getName(), movie.getSynopsisShort(), sdateDebut, sdateFin);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

        b.show();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, MovieActivity.class);
        intent.putExtra(MovieFragment.ACTIONBAR_TITLE, AppFragmentActivity.actionBarSubTitle);
        intent.putExtra(MovieFragment.MOVIE, (new Gson()).toJson(movie));

        Bundle b = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            b = ActivityOptions.makeScaleUpAnimation(view, 0, 0, view.getWidth(), view.getHeight()).toBundle();

            this.startActivity(intent, b);
        } else {
            this.startActivity(intent);
            this.overridePendingTransition(0, 0);
        }
    }

    @Override
    public void onLoadMovieFullTaskCallBack(Movie movie) {
        remplirBandesAnnonces(movie);
        ajouterLsitener();

        tournerRoulette(false);

        slidingDrawer.postDelayed(new Runnable() {
            @Override
            public void run() {
                slidingDrawer.setSlidingEnabled(true);
                slidingDrawer.expandPanel();
                slidingDrawer.setOnTouchListener(null);
                slidingDrawer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        slidingDrawer.setSlidingEnabled(false);
                        opening();
                    }
                }, 750);
            }
        }, 1500);
    }

    @Override
    public void onErreurReseau() {
        Toast.makeText(getApplicationContext(), R.string.erreur_reseau, Toast.LENGTH_SHORT).show();
    }
}
