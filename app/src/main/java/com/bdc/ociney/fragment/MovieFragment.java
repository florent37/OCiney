package com.bdc.ociney.fragment;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bdc.ociney.R;
import com.bdc.ociney.activity.MovieActivity;
import com.bdc.ociney.activity.StarActivity;
import com.bdc.ociney.adapter.BandesAnnoncesPagerAdapter;
import com.bdc.ociney.adapter.BandesAnnoncesPagerAdapter.OnBandeAnnonceClickedListener;
import com.bdc.ociney.adapter.ImagesPagerAdapter;
import com.bdc.ociney.adapter.ObjectAdapter;
import com.bdc.ociney.adapter.ObjectAdapterLayout;
import com.bdc.ociney.core.BaseActivity;
import com.bdc.ociney.fragment.core.BaseFragment;
import com.bdc.ociney.modele.Media;
import com.bdc.ociney.modele.Movie.Movie;
import com.bdc.ociney.modele.Person.CastMember;
import com.bdc.ociney.modele.Person.PersonFull;
import com.bdc.ociney.modele.Theater.Theater;
import com.bdc.ociney.task.LoadMovieFullTask;
import com.bdc.ociney.task.LoadMovieFullTask.LoadMovieFullTaskCallBack;
import com.bdc.ociney.task.LoadShowTimesTask;
import com.bdc.ociney.utils.actionbar.ActionBarHider;
import com.bdc.ociney.utils.transformation.CircleTransform;
import com.bdc.ociney.utils.transformation.ParallaxTransformer;
import com.bdc.ociney.view.CellMember;
import com.bdc.ociney.view.DoubleProgress;
import com.bdc.ociney.view.ObservableScrollViewAnimateVisible;
import com.bdc.ociney.view.textview.TextViewEx;
import com.github.florent37.androidanalytics.Analytics;
import com.google.android.gms.location.LocationRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.patloew.rxlocation.RxLocation;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.BlurTransformation;

public class MovieFragment extends BaseFragment implements LoadMovieFullTaskCallBack, OnBandeAnnonceClickedListener {

    public static final String MOVIE = "movie";
    public static final String ACTIONBAR_TITLE = "ACTIONBAR_TITLE";
    public String actionbarTitle = "";
    Toolbar toolbar;
    Movie movie;
    List<Theater> cinemas = new ArrayList<Theater>();

    @BindView(R.id.scrollView)
    ObservableScrollViewAnimateVisible scrollView;

    ActionBarHider actionBarHider;
    @BindView(R.id.slidingDrawer)
    SlidingUpPanelLayout slidingDrawer;

    @BindView(R.id.imageFond)
    ImageView imageFond;
    @BindView(R.id.fragmentLayout)
    ViewGroup fragmentLayout;

    ViewPager viewPager;

    ImagesPagerAdapter imagesAdapter;
    BandesAnnoncesPagerAdapter bandesAnnoncesAdapter;

    View viewCasting;
    ListView listCasting;

    @BindView(R.id.imagePoster)
    ImageView imagePoster;
    @BindView(R.id.titre)
    TextView titre;
    @BindView(R.id.duree)
    TextView duree;
    @BindView(R.id.genre)
    TextView genre;

    @BindView(R.id.titreOriginal)
    View titreOriginal;
    @BindView(R.id.dateDeSortie)
    View dateDeSortie;
    @BindView(R.id.synopsis)
    TextViewEx synopsis;
    @BindView(R.id.castingLayout)
    ViewGroup castingLayout;
    @BindView(R.id.realisateurLayout)
    View realisateurView;
    @BindView(R.id.enSalleLe)
    View enSalle;
    @BindView(R.id.enSalleLeValue)
    TextView enSalleDate;

    @BindView(R.id.ratingUserNum)
    TextView ratingLabel;
    @BindView(R.id.ratingUserStar)
    RatingBar ratingBar;

    @BindView(R.id.movieVF)
    View isVF;
    @BindView(R.id.movie2D)
    View is2D;
    @BindView(R.id.movie3D)
    View is3D;
    @BindView(R.id.movieVO)
    View isVO;

    List<String> imagesUrl = new ArrayList<String>();
    List<String> bandesAnnoncesImagesUrl = new ArrayList<String>();

    @BindView(R.id.critiques)
    DoubleProgress doubleProgress;

    boolean premierLancement = true;
    boolean afficherTheaters = false;
    boolean afficherCasting = false;

    @BindView(R.id.horrairesIci)
    View horrairesIci;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setFragmentView(view);
        ButterKnife.bind(this, view);

        toolbar = (Toolbar) ((getActivity()).findViewById(R.id.toolbar));

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(ACTIONBAR_TITLE)) {
                actionbarTitle = bundle.getString(ACTIONBAR_TITLE);
                ((BaseActivity) getActivity()).getSupportActionBar().setTitle(actionbarTitle);
            }
            if (bundle.containsKey(MOVIE)) {
                movie = new Gson().fromJson(bundle.getString(MOVIE), new TypeToken<Movie>() {
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

        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragmentLayout, f)
                .commitAllowingStateLoss();
    }

    @Override
    protected void charger() {
        //this.content = findViewById(R.id.content);
        actionBarHider = new ActionBarHider(getActivity(), toolbar, R.id.fragmentMovieContent);

        /*
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            removeElevation(slidingDrawer);
        }
        */

        this.viewPager = (ViewPager) View.inflate(getActivity(), R.layout.view_pager, null);
        changerVueFond(viewPager);

        viewCasting = View.inflate(getActivity(), R.layout.fragment_movie_theaters, null);
        listCasting = (ListView) viewCasting.findViewById(R.id.list);
    }

    @Override
    protected void remplir() {

        isVF.setVisibility(View.GONE);
        is2D.setVisibility(View.GONE);
        is3D.setVisibility(View.GONE);
        isVO.setVisibility(View.GONE);

        if (movie != null) {
            String poster = this.movie.getUrlPoster(getActivity().getResources().getInteger(R.integer.fragment_movie_fond_blur_height));

            if (poster != null) {
                Picasso.with(getActivity()).load(poster).transform(new BlurTransformation(getContext(), 10)).into(imageFond);

                imagesUrl.add(this.movie.getUrlPoster(getActivity().getResources().getInteger(R.integer.fragment_movie_fond_height)));

                //Picasso.with(getActivity()).load(this.moviePoster).into(imageFond);
                Picasso.with(getActivity())
                        .load(this.movie.getUrlPoster(getActivity().getResources().getInteger(R.integer.fragment_movie_poster_height)))
                        .into(imagePoster);
            }
            imagesAdapter = new ImagesPagerAdapter(getChildFragmentManager(), imagesUrl);

            viewPager.setAdapter(imagesAdapter);

            viewPager.setPageTransformer(false, new ParallaxTransformer(R.id.parallaxContent));

            chargerRating();

            if (movie.getTitle() != null)
                titre.setText(movie.getTitle());


            Analytics.screen("movie_" + movie.getTitle());


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

                final AnimatorSet animatorSetRebond1 = new AnimatorSet();
                //animatorSetRebond1.setDuration((int)(duree*0.75f));
                animatorSetRebond1.playTogether(
                        ObjectAnimator.ofFloat(vs[i], "translationX", 100, -10),
                        ObjectAnimator.ofFloat(vs[i], "rotation", 0f, -380f)
                );

                final AnimatorSet animatorSetRebond2 = new AnimatorSet();
                //animatorSetRebond2.setDuration((int)(duree*0.25f));
                animatorSetRebond2.playTogether(
                        ObjectAnimator.ofFloat(vs[i], "rotation", -20f, 0f),
                        ObjectAnimator.ofFloat(vs[i], "translationX", -10, 0)
                );

                final AnimatorSet animatorSetRebond = new AnimatorSet();
                //animatorSetRebond.setDuration((int)duree);
                animatorSetRebond.playSequentially(
                        animatorSetRebond1,
                        animatorSetRebond2
                );

                final AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setDuration(duree);
                animatorSet.setInterpolator(new DecelerateInterpolator(2));
                animatorSet.playTogether(
                        ObjectAnimator.ofFloat(vs[i], "alpha", 0, 1f),
                        animatorSetRebond
                );


                animations.add(animatorSet);
            }
        }

        final AnimatorSet[] anims = new AnimatorSet[animations.size()];
        animations.toArray(anims);

        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(anims);
        animatorSet.start();
    }

    @Override
    protected void ajouterListeners() {

        horrairesIci.setOnClickListener(this);

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
                PersonFull person = PersonFull.transformFull(movie.getRealisateur().getPerson());

                Intent intentRealisateur = new Intent(getActivity(), StarActivity.class)
                        .putExtra(StarFragment.PERSON, (new Gson()).toJson(person));

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
                break;
        }
    }

    @Override
    public void onBandeAnnonceClicked(Media bandeAnnonce) {
        startActivity(new Intent(Intent.ACTION_VIEW)
                .setDataAndType(Uri.parse(bandeAnnonce.getRendition().get(0).getHref()), "video/mp4"));
    }

    // Lorsque le chargement asynchrone du film a finit
    public void onLoadMovieFullTaskCallBack(Movie movie) {
        this.movie = movie;

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
                Picasso.with(getActivity())
                        .load(realisateur.getPicture().getHref(getActivity().getResources().getInteger(R.integer.fragment_movie_realisateur_height)))
                        .transform(new CircleTransform())
                        .into(imageRealisateur);
            } else
                realisateurView.setVisibility(View.GONE);

        }

        castingLayout.addView(LayoutInflater.from(getActivity()).inflate(R.layout.casting_plus, castingLayout, false));
        getView().findViewById(R.id.castingPlus).setOnClickListener(this); //ajout du "Plus"

        bandesAnnoncesAdapter = new BandesAnnoncesPagerAdapter(getChildFragmentManager(), movie.getBandesAnnonces(), movie, this, false);

        animerVersionsDisponibles();

        ObjectAdapter castingAdapter = new ObjectAdapter<CastMember>(getActivity(), movie.getAllCastMember(), R.layout.casting_member, CellMember.class);
        listCasting.addFooterView(View.inflate(getActivity(), R.layout.cell_theater_empty, null));
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
        animatorSet.addListener(new AnimatorListenerAdapter() {

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

        });

        animatorSet.start();

        //find location
        ;
    }

    private void afficherCasting() {
        //5: appellé après le collapse
        changerVueFond(viewCasting);
        slidingDrawer.setSlidingEnabled(false);
        ((BaseActivity) getActivity()).getSupportActionBar().setTitle(R.string.casting_complet);

        ((MovieActivity) getActivity()).afficherCasting();
    }

    public void fermerCasting() {
        slidingDrawer.setSlidingEnabled(true);
        changerVueFond(viewPager);
        ((BaseActivity) getActivity()).getSupportActionBar().setTitle(actionbarTitle);
        slidingDrawer.expandPanel();
    }
}
