package com.bdc.ociney.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bdc.ociney.R;
import com.bdc.ociney.activity.MovieActivity;
import com.bdc.ociney.activity.StarActivity;
import com.bdc.ociney.activity.TheaterDetailMovie;
import com.bdc.ociney.adapter.BandesAnnoncesPagerAdapter;
import com.bdc.ociney.adapter.BandesAnnoncesPagerAdapter.OnBandeAnnonceClickedListener;
import com.bdc.ociney.adapter.ImagesPagerAdapter;
import com.bdc.ociney.adapter.ObjectAdapter;
import com.bdc.ociney.adapter.ObjectAdapterLayout;
import com.bdc.ociney.fragment.core.BetterFragment;
import com.bdc.ociney.modele.Media;
import com.bdc.ociney.modele.Movie.Movie;
import com.bdc.ociney.modele.Person.CastMember;
import com.bdc.ociney.modele.Person.PersonFull;
import com.bdc.ociney.modele.Theater.Theater;
import com.bdc.ociney.task.LoadMovieFullTask;
import com.bdc.ociney.task.LoadMovieFullTask.LoadMovieFullTaskCallBack;
import com.bdc.ociney.task.LoadShowTimesTask;
import com.bdc.ociney.task.SearchTheatersTask;
import com.bdc.ociney.utils.LocationUtils;
import com.bdc.ociney.utils.actionbar.ActionBarHider;
import com.bdc.ociney.utils.transformation.BlurTransformation;
import com.bdc.ociney.utils.transformation.CircleTransform;
import com.bdc.ociney.utils.transformation.ParallaxTransformer;
import com.bdc.ociney.view.CellMember;
import com.bdc.ociney.view.CellTheater;
import com.bdc.ociney.view.DoubleProgress;
import com.bdc.ociney.view.ObservableScrollViewAnimateVisible;
import com.bdc.ociney.view.textview.TextViewEx;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MovieFragment extends BetterFragment implements ListView.OnItemClickListener, LoadMovieFullTaskCallBack, OnBandeAnnonceClickedListener,
        LocationUtils.LocationUtilsCallBack, LoadShowTimesTask.LoadShowTimesTaskCallBack, SearchTheatersTask.SearchTheatersTaskCallBack, ObjectAdapter.ObjectAdapterLoadMore {

    public static final String MOVIE = "movie";
    public static final String ACTIONBAR_TITLE = "ACTIONBAR_TITLE";
    public String actionbarTitle = "";
    Toolbar toolbar;
    Movie movie;
    List<Theater> cinemas = new ArrayList<Theater>();

    View content;
    ObservableScrollViewAnimateVisible scrollView;
    ActionBarHider actionBarHider;
    SlidingUpPanelLayout slidingDrawer;

    ImageView imageFond;
    ViewGroup fragmentLayout;
    ViewPager viewPager;
    ImagesPagerAdapter imagesAdapter;
    BandesAnnoncesPagerAdapter bandesAnnoncesAdapter;

    View viewTheaters, viewCasting;
    ListView listTheater, listCasting;

    ImageView imagePoster;
    TextView titre;
    TextView duree;
    TextView genre;

    View titreOriginal;
    View dateDeSortie;
    TextViewEx synopsis;
    ViewGroup castingLayout;
    View realisateurView;
    View enSalle;
    TextView enSalleDate;

    TextView ratingLabel;
    RatingBar ratingBar;

    View isVF, is2D, is3D, isVO;

    List<String> imagesUrl = new ArrayList<String>();
    List<String> bandesAnnoncesImagesUrl = new ArrayList<String>();

    ObjectAdapter<Theater> theaterAdapter;

    ViewGroup horairesLayout;

    DoubleProgress doubleProgress;

    boolean premierLancement = true;
    boolean afficherTheaters = false;
    boolean afficherCasting = false;

    View horrairesIci;
    /**
     * ************ SEARCH THEATERS *******************
     */

    int page = 1;
    String texteRecherche;
    AsyncTask task;
    boolean vide;
    boolean recherche = true;

    public static MovieFragment newInstance(Movie movie) {
        MovieFragment fragment = new MovieFragment();
        Bundle args = new Bundle();
        Gson gSon = new Gson();
        args.putString(MOVIE, gSon.toJson(movie));
        fragment.setArguments(args);

        return fragment;
    }

    public static MovieFragment newInstance(String title, String jsonMovie) {
        MovieFragment fragment = new MovieFragment();
        Bundle args = new Bundle();
        args.putString(ACTIONBAR_TITLE, title);
        args.putString(MOVIE, jsonMovie);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie,
                container, false);

        setFragmentView(view);

        toolbar = (Toolbar)((getActivity()).findViewById(R.id.toolbar));

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(ACTIONBAR_TITLE)) {
                actionbarTitle = bundle.getString(ACTIONBAR_TITLE);
                ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(actionbarTitle);
            }
            if (bundle.containsKey(MOVIE)) {
                movie =  new Gson().fromJson(bundle.getString(MOVIE), new TypeToken<Movie>() {
                }.getType());

                charger();
                remplir();
                ajouterListeners();


                tournerRoulette(true, R.id.placeholder_image_loader);
                new LoadMovieFullTask(this).execute(movie.getCode().toString());

                slidingDrawer.setSlidingEnabled(false);
                slidingDrawer.collapsePanel();

                //actionBarHider.setActionBarTranslation(0);//will "hide" an ActionBar
            }
        }


        return view;
    }

    private void changerVueFond(View v) {
        this.fragmentLayout.removeAllViews();

        this.fragmentLayout.addView(v);
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    private void changerVueFond(Fragment f) {
        this.fragmentLayout.removeAllViews();

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentLayout, f);
        ft.commit();
    }

    private void removeElevation(View view){
        view.setElevation(0);
        if(view instanceof ViewGroup){
            ViewGroup viewGroup = (ViewGroup)view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                removeElevation(viewGroup.getChildAt(i));
            }
        }
    }

    @Override
    protected void charger() {
        //this.content = findViewById(R.id.content);
        actionBarHider = new ActionBarHider(getActivity(), toolbar, R.id.fragmentMovieContent);

        scrollView = (ObservableScrollViewAnimateVisible) findViewById(R.id.scrollView);

        slidingDrawer = (SlidingUpPanelLayout) findViewById(R.id.slidingDrawer);

        /*
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            removeElevation(slidingDrawer);
        }
        */

        this.imageFond = (ImageView) findViewById(R.id.imageFond);
        this.fragmentLayout = (ViewGroup) findViewById(R.id.fragmentLayout);

        this.viewPager = (ViewPager) View.inflate(getActivity(), R.layout.view_pager, null);
        changerVueFond(viewPager);

        viewTheaters = View.inflate(getActivity(), R.layout.fragment_movie_theaters, null);
        listTheater = (ListView) viewTheaters.findViewById(R.id.list);

        viewCasting = View.inflate(getActivity(), R.layout.fragment_movie_theaters, null);
        listCasting = (ListView) viewCasting.findViewById(R.id.list);


        this.imagePoster = (ImageView) findViewById(R.id.imagePoster);
        this.titre = (TextView) findViewById(R.id.titre);
        this.duree = (TextView) findViewById(R.id.duree);
        this.genre = (TextView) findViewById(R.id.genre);

        this.ratingLabel = (TextView) findViewById(R.id.ratingUserNum);
        this.ratingBar = (RatingBar) findViewById(R.id.ratingUserStar);

        this.titreOriginal = findViewById(R.id.titreOriginal);
        this.dateDeSortie = findViewById(R.id.dateDeSortie);
        this.synopsis = (TextViewEx) findViewById(R.id.synopsis);

        this.realisateurView = (ViewGroup) findViewById(R.id.realisateurLayout);
        this.castingLayout = (ViewGroup) findViewById(R.id.castingLayout);
        this.enSalle = findViewById(R.id.enSalleLe);
        this.enSalleDate = (TextView) findViewById(R.id.enSalleLeValue);

        this.doubleProgress = (DoubleProgress) findViewById(R.id.critiques);

        isVF = findViewById(R.id.movieVF);
        is2D = findViewById(R.id.movie2D);
        is3D = findViewById(R.id.movie3D);
        isVO = findViewById(R.id.movieVO);

        horrairesIci = findViewById(R.id.horrairesIci);
    }

    @Override
    protected void remplir() {

        isVF.setVisibility(View.GONE);
        is2D.setVisibility(View.GONE);
        is3D.setVisibility(View.GONE);
        isVO.setVisibility(View.GONE);

        if (movie != null) {

            theaterAdapter = new ObjectAdapter<Theater>(getActivity(), cinemas, R.layout.cell_theater, CellTheater.class);
            listTheater.setAdapter(theaterAdapter);

            String poster = this.movie.getUrlPoster(getActivity().getResources().getInteger(R.integer.fragment_movie_fond_blur_height));

            if (poster != null) {
                Picasso.with(getActivity()).load(poster).transform(new BlurTransformation()).into(imageFond);

                imagesUrl.add(this.movie.getUrlPoster(getActivity().getResources().getInteger(R.integer.fragment_movie_fond_height)));

                //Picasso.with(getActivity()).load(this.moviePoster).into(imageFond);
                Picasso.with(getActivity())
                        .load(this.movie.getUrlPoster(getActivity().getResources().getInteger(R.integer.fragment_movie_poster_height)))
                        .into(imagePoster);
            }
            imagesAdapter = new ImagesPagerAdapter(getChildFragmentManager(), getActivity(), imagesUrl);

            viewPager.setAdapter(imagesAdapter);

            viewPager.setPageTransformer(false, new ParallaxTransformer(R.id.parallaxContent));

            chargerRating();

            if (movie.getTitle() != null)
                titre.setText(movie.getTitle());

            duree.setText(movie.getDuree());
            genre.setText(movie.getGenres());

            if (movie.getOriginalTitle() != null && !(movie.sameTitleAndOrignlalTitle())) {
                ((TextView) titreOriginal.findViewById(R.id.text)).setText(movie.getOriginalTitle());
            } else {
                titreOriginal.setVisibility(View.GONE);
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date date1 = sdf.parse(movie.getRelease().getReleaseDate());
                Date date2 = new Date();

                if (date1.before(date2)) {
                    ((TextView) dateDeSortie.findViewById(R.id.text)).setText(movie.getRelease().getReleaseDate());
                } else
                    dateDeSortie.setVisibility(View.GONE);

            } catch (Exception e) {
                Log.e("Erreur date", e.toString());
                dateDeSortie.setVisibility(View.GONE);
            }


            if (movie.getSynopsisShort() != null)
                synopsis.setText(movie.getSynopsisShort(), true);

            doubleProgress.setValeurHaut(movie.getUserRating());
            doubleProgress.setValeurBas(movie.getPressRating());
            doubleProgress.animer();

            if (movie.getHasShowtime() == null || movie.getHasShowtime() != 1) {
                this.findViewById(R.id.horrairesIci).setVisibility(View.GONE);
            }
        }
    }

    private void chargerRating() {

        float rating = (movie.getUserRating() + movie.getPressRating()) / 2;

        if (rating == 0) {
            ratingLabel.setVisibility(View.GONE);
            ratingBar.setVisibility(View.GONE);

            if (movie.getRelease() != null) {
                if (movie.getRelease().getReleaseDate() != null && movie.getRelease().getReleaseDate().length() > 0) {
                    enSalle.setVisibility(View.VISIBLE);
                    enSalleDate.setText(movie.getRelease().getReleaseDate());
                } else {
                    enSalle.setVisibility(View.GONE);
                    enSalleDate.setVisibility(View.GONE);
                }

                findViewById(R.id.critiquesLayout).setVisibility(View.GONE);
                findViewById(R.id.horrairesLayout).setVisibility(View.GONE);
            }
        } else {
            ratingLabel.setText("(" + String.format("%.1f", rating) + ")");
            ratingBar.setRating(rating);
            ratingBar.setIsIndicator(true);
        }
    }

    private void animerVersionsDisponibles() {

        List<AnimatorSet> animations = new ArrayList<AnimatorSet>();

        boolean[] is = new boolean[]{movie.isVF(), movie.is2D(), movie.is3D(), movie.isVO()};
        View[] vs = new View[]{isVF, is2D, is3D, isVO};

        int duree = 200;

        for (int i = 0; i < is.length; ++i) {
            if (is[i]) {
                vs[i].setVisibility(View.VISIBLE);
                vs[i].setAlpha(0);

                AnimatorSet animatorSetRebond1 = new AnimatorSet();
                //animatorSetRebond1.setDuration((int)(duree*0.75f));
                animatorSetRebond1.playTogether(
                        ObjectAnimator.ofFloat(vs[i], "translationX", 100, -10),
                        ObjectAnimator.ofFloat(vs[i], "rotation", 0f, -380f)
                );

                AnimatorSet animatorSetRebond2 = new AnimatorSet();
                //animatorSetRebond2.setDuration((int)(duree*0.25f));
                animatorSetRebond2.playTogether(
                        ObjectAnimator.ofFloat(vs[i], "rotation", -20f, 0f),
                        ObjectAnimator.ofFloat(vs[i], "translationX", -10, 0)
                );

                AnimatorSet animatorSetRebond = new AnimatorSet();
                //animatorSetRebond.setDuration((int)duree);
                animatorSetRebond.playSequentially(
                        animatorSetRebond1,
                        animatorSetRebond2
                );

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setDuration(duree);
                animatorSet.setInterpolator(new DecelerateInterpolator(2));
                animatorSet.playTogether(
                        ObjectAnimator.ofFloat(vs[i], "alpha", 0, 1f),
                        animatorSetRebond
                );


                animations.add(animatorSet);
            }
        }

        AnimatorSet[] anims = new AnimatorSet[animations.size()];
        animations.toArray(anims);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(anims);
        animatorSet.start();
    }

    @Override
    protected void ajouterListeners() {

        horrairesIci.setOnClickListener(this);
        listTheater.setOnItemClickListener(this);

        slidingDrawer.setPanelSlideListener(
                new SlidingUpPanelLayout.PanelSlideListener() {
                    @Override
                    public void onPanelSlide(View panel, float slideOffset) {

                        try {
                            Field f = slidingDrawer.getClass().getDeclaredField("mSlideOffset");
                            f.setAccessible(true);

                            float mSlideOffset = f.getFloat(slidingDrawer);

                            if (!premierLancement) {
                                imageFond.setAlpha(mSlideOffset);
                                fragmentLayout.setAlpha(1 - mSlideOffset);
                            }
                            actionBarHider.setActionBarTranslation(-1 * mSlideOffset);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onPanelCollapsed(View panel) {
                        if (afficherTheaters) {
                            afficherCinemas();
                        }
                        if (afficherCasting) {
                            afficherCasting();
                            afficherCasting = false;
                        }
                    }


                    @Override
                    public void onPanelExpanded(View panel) {
                        premierLancement = false;

                    }

                    @Override
                    public void onPanelAnchored(View panel) {
                    }

                    @Override
                    public void onPanelHidden(View view) {

                    }
                }
        );
    }


    //----------CINEMA-----------

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.horrairesIci:
                this.lancerHorrairesIci(v.findViewById(R.id.img_ici), true);
                break;
            case R.id.realisateurLayout:
                // StarFragment.personId = movie.getRealisateur().getPerson().getCode().toString();
                PersonFull person = PersonFull.transformFull(movie.getRealisateur().getPerson());

                Intent intentRealisateur = new Intent(getActivity(), StarActivity.class);
                intentRealisateur.putExtra(StarFragment.PERSON, (new Gson()).toJson(person));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    Bundle b = ActivityOptions.makeScaleUpAnimation(v.findViewById(R.id.realisateurImage), 0, 0, 100, 100).toBundle();

                    getActivity().startActivity(intentRealisateur, b);
                } else
                    startActivity(intentRealisateur);
                break;
            case R.id.castingPlus:
                afficherCasting = true;
                ((MovieActivity) getActivity()).afficherCasting();
                slidingDrawer.collapsePanel();
                //afficherCasting();
                /*
                ListCastingFragment.casting = movie.getAllCastMember();
                ListCastingFragment.movie = this.movie;

                Intent intentCasting = new Intent(getActivity(), MovieCastingActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    Bundle b = ActivityOptions.makeScaleUpAnimation(v, 0, 0, v.getWidth(), v.getHeight()).toBundle();

                    getActivity().startActivity(intentCasting, b);
                } else
                    startActivity(intentCasting);
                    */
                break;
        }
    }

    @Override
    public void onBandeAnnonceClicked(Media bandeAnnonce) {

        String videoPath = "";

            videoPath = bandeAnnonce.getRendition().get(0).getHref();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(videoPath), "video/mp4");
        startActivity(intent);
    }

    // Lorsque le chargement asynchrone du film a finit
    public void onLoadMovieFullTaskCallBack(Movie movie) {
        MovieFragment.this.movie = movie;

        if (movie == null)
            return;

        tournerRoulette(false, R.id.placeholder_image_loader);

        synopsis.setText(movie.getSynopsis(), true);

        List<String> urlsImages = movie.getUrlImages(getActivity().getResources().getInteger(R.integer.fragment_movie_fond_height));
        if (urlsImages != null && urlsImages.size() > 0) {
            imagesUrl.addAll(urlsImages);
            imagesAdapter.notifyDataSetChanged();
        }

        new ObjectAdapterLayout<CastMember>(getActivity(), movie.getCastMember(3), R.layout.casting_member, CellMember.class, castingLayout);

        CastMember realisateur = movie.getRealisateur();
        if (realisateur == null)
            realisateurView.setVisibility(View.GONE);
        else {
            ((TextView) realisateurView.findViewById(R.id.realisateurNom)).setText(realisateur.getPerson().getName());
            realisateurView.setOnClickListener(this);

            ImageView imageRealisateur = (ImageView) realisateurView.findViewById(R.id.realisateurImage);

            if (realisateur.getPicture() != null && realisateur.getPicture().getHref(0) != null) {
                String pictureRealisateur = realisateur.getPicture().getHref(getActivity().getResources().getInteger(R.integer.fragment_movie_realisateur_height));
                Picasso.with(getActivity()).load(pictureRealisateur).transform(new CircleTransform()).into(imageRealisateur);
            } else
                realisateurView.setVisibility(View.GONE);

        }

        View.inflate(getActivity(), R.layout.casting_plus, castingLayout);

        findViewById(R.id.castingPlus).setOnClickListener(this); //ajout du "Plus"

        bandesAnnoncesAdapter = new BandesAnnoncesPagerAdapter(getActivity(), getChildFragmentManager(), movie.getBandesAnnonces(), movie, this, false);

        animerVersionsDisponibles();

        ObjectAdapter castingAdapter = new ObjectAdapter<CastMember>(getActivity(), movie.getAllCastMember(), R.layout.casting_member, CellMember.class);
        listCasting.addFooterView(View.inflate(getActivity(),R.layout.cell_theater_empty,null));
        listCasting.setAdapter(castingAdapter);

        slidingDrawer.postDelayed(new Runnable() {
            @Override
            public void run() {
                slidingDrawer.setSlidingEnabled(true);
                slidingDrawer.expandPanel();

                scrollView.start();
            }
        }, 3000);

    }

    public void afficherImages() {
        if (imagesAdapter != null)
            viewPager.setAdapter(imagesAdapter);
        viewPager.getAdapter().notifyDataSetChanged();
        slidingDrawer.collapsePanel();
    }

    public void afficherBandesAnnonces() {
        if (bandesAnnoncesAdapter != null)
            viewPager.setAdapter(bandesAnnoncesAdapter);
        viewPager.getAdapter().notifyDataSetChanged();
        slidingDrawer.collapsePanel();
    }

    public void lancerHorrairesIci(final View v, final boolean ici) {

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(600);

        animatorSet.playTogether(
                ObjectAnimator.ofFloat(v, "alpha", 1, 0),
                ObjectAnimator.ofFloat(v, "scaleX", 1, 1.5f),
                ObjectAnimator.ofFloat(v, "scaleY", 1, 1.5f)
        );
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                AnimatorSet animatorSetReverse = new AnimatorSet();
                animatorSetReverse.setDuration(300);
                animatorSetReverse.playTogether(
                        ObjectAnimator.ofFloat(v, "alpha", 0, 1),
                        ObjectAnimator.ofFloat(v, "scaleX", 1.5f, 1f),
                        ObjectAnimator.ofFloat(v, "scaleY", 1.5f, 1f)
                );
                animatorSetReverse.start();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        animatorSet.start();

        //1: demande la localisation
        new LocationUtils(getActivity(), this).chercherLocation();

    }

    @Override
    public void onLocationFound(String lng, String lat) {
        //2: demande les cinemas
        new LoadShowTimesTask(this).execute(movie, lat, lng);
    }

    @Override
    public void onGPSNotFound() {
        /*
        Crouton.makeText(getActivity(), R.string.erreur_gps,
                new Style.Builder().setBackgroundColor(R.color.black50).build()
        ).show();*/

        viewTheaters.findViewById(R.id.list_theater_empty).setVisibility(View.VISIBLE);

        onLoadShowTimesTaskCallBack(new ArrayList<Theater>());
    }

    @Override
    public void onLoadShowTimesTaskCallBack(List<Theater> result) {

        try {
            //3: charge les cinemas
            cinemas.clear();
            cinemas.addAll(result);

            listTheater.addFooterView(View.inflate(getActivity(),R.layout.cell_theater_empty,null));

            theaterAdapter.notifyDataSetChanged();

            slidingDrawer.collapsePanel();

            //4: demande l'affichage de la liste après la fermeture du collapse
            afficherTheaters = true;
        } catch (Exception e) {
        }
    }

    private void afficherCinemas() {
        //5: appellé après le collapse
        changerVueFond(viewTheaters);
        slidingDrawer.setSlidingEnabled(false);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(R.string.projections);
        afficherTheaters = false;

        ((MovieActivity) getActivity()).afficherCinema();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Theater theater = (Theater) theaterAdapter.getItem(i);
        Intent intent = new Intent(getActivity(), TheaterDetailMovie.class);
        intent.putExtra(TheaterDetailMovie.MOVIE, (new Gson()).toJson(movie));
        intent.putExtra(TheaterDetailMovie.THEATER, (new Gson()).toJson(theater));
        startActivity(intent);

        getActivity().overridePendingTransition(0, 0);


    }

    public void fermerCinema() {
        slidingDrawer.setSlidingEnabled(true);
        changerVueFond(viewPager);
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(actionbarTitle);
        slidingDrawer.expandPanel();
    }

    private void afficherCasting() {
        //5: appellé après le collapse
        changerVueFond(viewCasting);
        slidingDrawer.setSlidingEnabled(false);
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(R.string.casting_complet);

        ((MovieActivity) getActivity()).afficherCasting();
    }

    public void fermerCasting() {
        fermerCinema();
    }

    @Override
    public void search(String text) {
        super.search(text);
        this.texteRecherche = text;
        cinemas.clear();
        this.theaterAdapter.notifyDataSetChanged();
        theaterAdapter.listener = this;
        viewTheaters.findViewById(R.id.list_theater_empty).setVisibility(View.VISIBLE);
        if (task == null) {
            task = new SearchTheatersTask(this, movie);
            task.execute(text);
        }
    }

    @Override
    public void onSearchTheatersTaskCallBack(List<Theater> ths) {

        viewTheaters.findViewById(R.id.list_theater_empty).setVisibility(View.GONE);

        if (this.cinemas.isEmpty()) {
            theaterAdapter.notifyDataSetChanged();
        }

        task = null;

        if (ths.isEmpty() && this.cinemas.isEmpty()) {
            afficherVide(true);
            vide = true;
            theaterAdapter.listener = null;
        } else
            afficherVide(false);

        if (ths.isEmpty()) {
            vide = true;
            theaterAdapter.listener = null;
        } else {
            try {
                this.cinemas.addAll(ths);
                theaterAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                theaterAdapter.listener = null;
            }
        }
    }

    @Override
    public void loadMore() {
        page++;
        if (recherche && !vide && task == null) {
            task = new SearchTheatersTask(this, movie);
            task.execute(texteRecherche, page);
        }
    }
}
