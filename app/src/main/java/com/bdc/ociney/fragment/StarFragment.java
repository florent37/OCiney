package com.bdc.ociney.fragment;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bdc.ociney.core.BaseActivity;
import com.github.florent37.androidanalytics.Analytics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bdc.ociney.R;
import com.bdc.ociney.activity.AppFragmentActivity;
import com.bdc.ociney.activity.MovieActivity;
import com.bdc.ociney.activity.StarActivity;
import com.bdc.ociney.adapter.ImagesPagerAdapter;
import com.bdc.ociney.adapter.ObjectAdapter;
import com.bdc.ociney.fragment.core.BaseFragment;
import com.bdc.ociney.modele.Media;
import com.bdc.ociney.modele.Movie.Movie;
import com.bdc.ociney.modele.Participation;
import com.bdc.ociney.modele.Person.PersonFull;
import com.bdc.ociney.service.Service;
import com.bdc.ociney.utils.DateUtils;
import com.bdc.ociney.utils.actionbar.ActionBarHider;
import com.bdc.ociney.utils.transformation.CircleTransform;
import com.bdc.ociney.utils.transformation.ParallaxTransformer;
import com.bdc.ociney.view.CellJaquette;
import com.bdc.ociney.view.ObservableScrollViewAnimateVisible;
import com.bdc.ociney.view.textview.TextViewEx;
import com.bdc.ociney.view.textview.TypefacedTextView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.BlurTransformation;


public class StarFragment extends BaseFragment {

    public static final String PERSON = "person";
    private static final int NOMBRE_JAQUETTE_AFFICHE = 7;
    PersonFull person;

    SlidingUpPanelLayout slidingDrawer;

    ViewGroup castingLayout;

    ImageView personImage;
    View top_rank;
    View couronne;
    ImageView image0;
    ImageView image1;
    ImageView image2;

    TextView titreBiographie;
    TextViewEx biographie;
    TextView nameStar;
    TextView role;
    TextView dateNaissance;
    TextView lieuNaissance;

    TextView nationality;
    TextView age;

    ObservableScrollViewAnimateVisible scrollView;

    ActionBarHider actionBarHider;

    AbsListView gridViewFilmo;

    boolean premierLancement = true;

    ViewPager viewPager;

    ImagesPagerAdapter imagesAdapter;
    ObjectAdapter<Participation> filmoAdapter;

    List<String> imagesUrl = new ArrayList<String>();

    View filmo1;
    View filmo2;
    View filmo3;
    View filmo4;
    View filmo5;
    View filmo6;
    View filmo7;
    View filmos;

    View plus_photo;

    List<Participation> participations;

    View filmoPlus;

    ImageView imageFond;

    boolean afficherFilmos = false;
    boolean erreurReseau = false;

    Toolbar toolbar;

    public static StarFragment newInstance(PersonFull person) {
        return newInstance(new Gson().toJson(person));
    }

    public static StarFragment newInstance(String personJson) {
        StarFragment fragment = new StarFragment();
        Bundle args = new Bundle();

        args.putString(PERSON, personJson);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_star,
                container, false);

        setFragmentView(view);

        toolbar = (Toolbar)((getActivity()).findViewById(R.id.toolbar));

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(PERSON)) {
                person = new Gson().fromJson(bundle.getString(PERSON), new TypeToken<PersonFull>() {
                }.getType());
            }
        }

        charger();
        ajouterListeners();

        remplirLocal();

        slidingDrawer.setSlidingEnabled(false);

        tournerRoulette(true, R.id.placeholder_image_loader);

        new LoadPersonTask().execute(person.getCode() + "");

        return view;
    }

    @Override
    protected void charger() {
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        findViewById(R.id.fragmentStarFond).setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, size.y + 200));
        // GridView Jaquette
        gridViewFilmo = (AbsListView) findViewById(R.id.gridViewFilmo);

        slidingDrawer = (SlidingUpPanelLayout) findViewById(R.id.slidingDrawer);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            slidingDrawer.setElevation(0);
        }

        scrollView = (ObservableScrollViewAnimateVisible) findViewById(R.id.scrollView);

        actionBarHider = new ActionBarHider(getActivity(), toolbar, R.id.fragmentStarContent);

        this.viewPager = (ViewPager) findViewById(R.id.viewPager);


        // On charge les caracteristiques de la personnalité
        titreBiographie = (TextView) findViewById(R.id.titreBiographie);
        personImage = (ImageView) findViewById(R.id.personImage);
        biographie = (TextViewEx) findViewById(R.id.biographie);
        castingLayout = (ViewGroup) findViewById(R.id.castingLayout);
        nameStar = (TextView) findViewById(R.id.nameStar);
        role = (TextView) findViewById(R.id.role);

        this.top_rank = findViewById(R.id.topstar);
        this.couronne = findViewById(R.id.couronne);

        // On charge les photos

        image0 = (ImageView) findViewById(R.id.relative_photo).findViewById(R.id.image0);
        image1 = (ImageView) findViewById(R.id.relative_photo).findViewById(R.id.image1);
        image2 = (ImageView) findViewById(R.id.relative_photo).findViewById(R.id.image2);


        // On charge les jaquettes de la filmo
        filmo1 = findViewById(R.id.filmo1);
        filmo2 = findViewById(R.id.filmo2);
        filmo3 = findViewById(R.id.filmo3);
        filmo4 = findViewById(R.id.filmo4);
        filmo5 = findViewById(R.id.filmo5);
        filmo6 = findViewById(R.id.filmo6);
        filmo7 = findViewById(R.id.filmo7);
        filmos = findViewById(R.id.filmos);
        // On charge le bouton plus
        filmoPlus = findViewById(R.id.filmoPlus);

        // On charge les informations de naissance

        dateNaissance = (TextView) findViewById(R.id.dateNaissance);
        lieuNaissance = (TextView) findViewById(R.id.lieuNaissance);

        // Plus pour les photos
        plus_photo = findViewById(R.id.plus_photo);

        this.imageFond = (ImageView) findViewById(R.id.imageFond);

        // Nationality
        this.nationality = (TextView) findViewById(R.id.nationality);

        // Age
        this.age = (TextView) findViewById(R.id.age);
    }

    public void remplirLocal() {

        if (person.getPicture() != null && person.getPicture().getHref(0) != null) {
            String urlFond = person.getPicture().getHref(getActivity().getResources().getInteger(R.integer.fragment_movie_fond_personnality_height));

            Picasso.with(getActivity()).load(urlFond).transform(new CircleTransform()).into(personImage);

            imagesUrl.add(urlFond);

            Picasso.with(getActivity()).load(urlFond).transform(new BlurTransformation(getContext())).into(imageFond);
        }

        if (person.getFullName() != null) {
            Analytics.screen("star_"+person.getFullName());

            String name = "";

            if (person.getFullName().getGiven() != null)
                name += person.getFullName().getGiven() + " ";


            if (person.getFullName().getFamily() != null)
                name += person.getFullName().getFamily();

            this.nameStar.setText(name);

            // On met le nom de la star dans l'action bar
            ((BaseActivity) getActivity()).getSupportActionBar().setTitle(name);
        }

        this.top_rank.setVisibility(View.GONE);
        this.couronne.setVisibility(View.GONE);
        if (person.getStatistics() != null) {
            int top_rank = person.getStatistics().getRankTopStar();

            switch (top_rank) {
                case 1:
                    this.top_rank.setVisibility(View.VISIBLE);
                    this.couronne.setVisibility(View.VISIBLE);
                    this.top_rank.setBackgroundResource(R.drawable.ic_topstar_1);
                    break;
                case 2:
                    this.top_rank.setVisibility(View.VISIBLE);
                    this.top_rank.setBackgroundResource(R.drawable.ic_topstar_2);
                    break;

                case 3:
                    this.top_rank.setVisibility(View.VISIBLE);
                    this.top_rank.setBackgroundResource(R.drawable.ic_topstar_3);
                    break;
            }

        }

        String activities = person.getActivities();

        if (activities.trim().length() > 0) {
            this.role.setText(activities);
        } else {
            this.role.setText(getActivity().getString(R.string.activite_non_renseigne));
        }

    }

    @Override
    protected void remplir() {

        tournerRoulette(false, R.id.placeholder_image_loader);

        try {
            String urImage0 = person.getMedia().get(0).getThumbnail().getHref(getResources().getInteger(R.integer.star_photo_height));
            String urImage1 = person.getMedia().get(1).getThumbnail().getHref(getResources().getInteger(R.integer.star_photo_height));
            String urImage2 = person.getMedia().get(2).getThumbnail().getHref(getResources().getInteger(R.integer.star_photo_height));

            Picasso.with(getActivity()).load(urImage0).into(image0);
            Picasso.with(getActivity()).load(urImage1).into(image1);
            Picasso.with(getActivity()).load(urImage2).into(image2);

            findViewById(R.id.relative_photo).setVisibility(View.VISIBLE);

            imagesUrl.add(urImage0);
            imagesUrl.add(urImage1);
            imagesUrl.add(urImage2);

            //imageFond.activerBlur(getActivity().getResources().getInteger(R.integer.fragment_movie_fond_blur))
            //        .activerAnimations().loadFromUrl(getActivity(), urImage0);
            //Picasso.with(getActivity()).load(urImage0).transform(new CircleTransform()).into(personImage);

        } catch (Exception e) {
            e.printStackTrace();
            findViewById(R.id.relative_photo).setVisibility(View.GONE);

        }


        if (person != null && person.getNationality() != null) {
            this.nationality.setText("(" + person.getNationality().get(0).get$() + ")");
            this.nationality.setVisibility(View.VISIBLE);
        }

        if (person.getStatistics() != null) {
            int top_rank = person.getStatistics().getRankTopStar();

            switch (top_rank) {
                case 1:
                    this.couronne.setVisibility(View.VISIBLE);
                    this.top_rank.setVisibility(View.VISIBLE);
                    this.top_rank.setBackgroundResource(R.drawable.ic_topstar_1);
                    break;
                case 2:
                    this.top_rank.setVisibility(View.VISIBLE);
                    this.top_rank.setBackgroundResource(R.drawable.ic_topstar_2);
                    break;

                case 3:
                    this.top_rank.setVisibility(View.VISIBLE);
                    this.top_rank.setBackgroundResource(R.drawable.ic_topstar_3);
                    break;
            }

        }

        // On change le rôle de la star
        role.setText(person.getActivities());

        findViewById(R.id.metiers_container).setVisibility(View.VISIBLE);
        if (person.getBiography() != null) {
            // On écrit la biographie
            biographie.setText(person.getBiography(), true);
            findViewById(R.id.relative_biographie).setVisibility(View.VISIBLE);
        }

        if (person.getBirthDate() != null) {
            dateNaissance.setText(DateUtils.dateFormat(person.getBirthDate()));
            findViewById(R.id.relative_date_naissance).setVisibility(View.VISIBLE);

            // Calculer l'age
            this.age.setText(DateUtils.age(person.getBirthDate()));
            this.age.setVisibility(View.VISIBLE);

        }

        if (person.getBirthPlace() != null) {
            lieuNaissance.setText(person.getBirthPlace());
            findViewById(R.id.relative_lieu_naissance).setVisibility(View.VISIBLE);
        }

        if (this.person.getParticipation() != null) {


            findViewById(R.id.relative_filmographie).setVisibility(View.VISIBLE);

            // On affiche la photo de la star s'il y en a une
            if (person.getPicture() != null && person.getPicture().getHref(0) != null) {
                participations = person.getParticipation(NOMBRE_JAQUETTE_AFFICHE);
            }


            if (this.person.getParticipation().size() > 0) {

                // Ajoute 4 films
                this.person.getParticipation().add(new Participation());
                this.person.getParticipation().add(new Participation());
                this.person.getParticipation().add(new Participation());
                this.person.getParticipation().add(new Participation());


                findViewById(R.id.relative_filmographie).setVisibility(View.VISIBLE);

                afficherJaquette(this.filmo1, 0);
                afficherJaquette(this.filmo2, 1);
                afficherJaquette(this.filmo3, 2);
                afficherJaquette(this.filmo4, 3);
                afficherJaquette(this.filmo5, 4);
                afficherJaquette(this.filmo6, 5);
                afficherJaquette(this.filmo7, 6);

                if (person.getParticipation() != null && person.getParticipation().size() > NOMBRE_JAQUETTE_AFFICHE) {
                    filmoPlus.setVisibility(View.VISIBLE);
                }
                filmoAdapter = new ObjectAdapter<Participation>(getActivity(), this.person.getParticipation(), R.layout.jaquette, CellJaquette.class);
                gridViewFilmo.setAdapter(filmoAdapter);
            }
        }

        slidingDrawer.postDelayed(new Runnable() {
            @Override
            public void run() {
                slidingDrawer.setSlidingEnabled(true);
                slidingDrawer.expandPanel();
                scrollView.start();
            }
        }, 2000);


        viewPager.setPageTransformer(false, new ParallaxTransformer(R.id.parallaxContent));

        for (Media m : person.getMedia()) {
            if (m.getThumbnail() != null && m.getThumbnail().getHref(600) != null)
                imagesUrl.add(m.getThumbnail().getHref(600));
        }

        imagesAdapter = new ImagesPagerAdapter(getChildFragmentManager(), imagesUrl);
        viewPager.setAdapter(imagesAdapter);
    }

    private void afficherJaquette(View view, int i) {

        Participation participation;

        if (participations != null && participations.size() > i) {
            participation = participations.get(i);

            view.setVisibility(View.VISIBLE);

            if (participation.getMovie() != null && participation.getMovie().getPoster() != null) {
                String urlImage = participation.getMovie().getUrlPoster(getActivity().getResources().getInteger(R.integer.star_filmo_jaquette_height));
                Picasso.with(getActivity()).load(urlImage).into((ImageView) view.findViewById(R.id.jaquette));
            }

            if (participation.getMovie() != null && participation.getMovie().getTitle() != null) {
                ((TypefacedTextView) view.findViewById(R.id.titreFilmo)).setText(participation.getMovie().getTitle());
            } else {
                if (participation.getMovie() != null && participation.getMovie().getOriginalTitle() != null) {
                    ((TypefacedTextView) view.findViewById(R.id.titreFilmo)).setText(participation.getMovie().getOriginalTitle());
                } else {
                    // participations.remove(i);
                    view.setVisibility(View.GONE);
                }
            }

            if (participation.getRole() != null)
                ((TypefacedTextView) view.findViewById(R.id.roleFilmo)).setText(participation.getRole());
            else
                ((TypefacedTextView) view.findViewById(R.id.roleFilmo)).setText(getString(R.string.role_non_renseigne));
        }
    }

    @Override
    protected void ajouterListeners() {

        filmo1.setOnClickListener(this);
        filmo2.setOnClickListener(this);
        filmo3.setOnClickListener(this);
        filmo4.setOnClickListener(this);
        filmo5.setOnClickListener(this);
        filmo6.setOnClickListener(this);
        filmo7.setOnClickListener(this);

        plus_photo.setOnClickListener(this);
        image0.setOnClickListener(this);
        image1.setOnClickListener(this);
        image2.setOnClickListener(this);

        slidingDrawer.setPanelSlideListener(
                new SlidingUpPanelLayout.PanelSlideListener() {
                    @Override
                    public void onPanelSlide(View panel, float slideOffset) {

                        try {
                            Field f = slidingDrawer.getClass().getDeclaredField("mSlideOffset");
                            f.setAccessible(true);
                            float mSlideOffset = f.getFloat(slidingDrawer);

                            imageFond.setAlpha(mSlideOffset);
                            viewPager.setAlpha(1 - mSlideOffset);
                            filmos.setAlpha(1 - mSlideOffset);
                            //findViewById(R.id.fondFilmo).setAlpha(mSlideOffset);
                            actionBarHider.setActionBarTranslation(-1 * mSlideOffset);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onPanelCollapsed(View panel) {
                        if (afficherFilmos) {
                            afficherFilmosFond();
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

        filmoPlus.setOnClickListener(this);

    }

    public void onClick(View view) {

        Movie movie = null;

        switch (view.getId()) {
            case R.id.filmo1:
                if (participations.get(0) != null)
                    movie = participations.get(0).getMovie();

                break;

            case R.id.filmo2:
                if (participations.get(1) != null)
                    movie = participations.get(1).getMovie();
                break;

            case R.id.filmo3:
                if (participations.get(2) != null)
                    movie = participations.get(2).getMovie();
                break;

            case R.id.filmo4:
                if (participations.get(3) != null)
                    movie = participations.get(3).getMovie();
                break;

            case R.id.filmo5:
                if (participations.get(4) != null)
                    movie = participations.get(4).getMovie();
                break;

            case R.id.filmo6:
                if (participations.get(5) != null)
                    movie = participations.get(5).getMovie();
                break;

            case R.id.filmo7:
                if (participations.get(6) != null)
                    movie = participations.get(6).getMovie();
                break;
            case R.id.filmoPlus:
                afficherFilmo();
                break;
            case R.id.image0:
                afficherPhotos(1);
                break;
            case R.id.image1:
                afficherPhotos(2);
                break;
            case R.id.image2:
                afficherPhotos(3);
                break;
            case R.id.plus_photo:
                afficherPhotos(4);
                break;
        }

        if (movie != null) {
            lancerMovie(view, movie);
        }


    }

    public void refreshAdapter(List<Object> movies) {

    }

    public void lancerMovie(View view, Movie movie) {
        Intent intent = new Intent(getActivity(), MovieActivity.class);
        intent.putExtra(MovieFragment.ACTIONBAR_TITLE, AppFragmentActivity.actionBarSubTitle);
        intent.putExtra(MovieFragment.MOVIE, (new Gson()).toJson(movie));

        Bundle b = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            b = ActivityOptions.makeScaleUpAnimation(view, 0, 0, view.getWidth(), view.getHeight()).toBundle();

            getActivity().startActivity(intent, b);
        } else {
            getActivity().startActivity(intent);
            getActivity().overridePendingTransition(0, 0);
        }
    }

    public void afficherPhotos(int currentItem) {
        ((StarActivity) getActivity()).afficherPhoto();
        findViewById(R.id.fondFilmo).setVisibility(View.GONE);
        filmos.setVisibility(View.GONE);
        if (imagesAdapter != null)
            viewPager.setAdapter(imagesAdapter);
        viewPager.getAdapter().notifyDataSetChanged();
        viewPager.setCurrentItem(currentItem);
        slidingDrawer.collapsePanel();
    }

    public void afficherFilmo() {
        ((StarActivity) getActivity()).afficherFilmo();
        findViewById(R.id.fondFilmo).setVisibility(View.VISIBLE);
        afficherFilmos = true;
        slidingDrawer.collapsePanel();
    }

    public void afficherFilmosFond() {
        findViewById(R.id.fondFilmo).setVisibility(View.VISIBLE);
        if (filmoAdapter != null) {
            filmos.setVisibility(View.VISIBLE);
        }
        afficherFilmos = false;

    }

    public PersonFull getPerson() {
        return person;
    }

    class LoadPersonTask extends AsyncTask<Object, Void, PersonFull> {
        @Override
        protected PersonFull doInBackground(Object... data) {

            String idPerson = (String) data[0];
            try {
                PersonFull person = Service.person(idPerson, Service.PROFILE_LARGE, Service.FILTER_PERSON);

                return person;
            } catch (Service.NetworkException e) {
                erreurReseau = true;
                return null;
            }
        }

        @Override
        protected void onPostExecute(PersonFull person) {
            super.onPostExecute(person);
            try {
                if (erreurReseau)
                    onErreurReseau();
                else {
                    StarFragment.this.person = person;
                    remplir();
                }
            } catch (Exception e) {

            }
        }
    }
}
