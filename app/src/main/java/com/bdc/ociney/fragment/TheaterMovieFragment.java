package com.bdc.ociney.fragment;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bdc.ociney.R;
import com.bdc.ociney.activity.TheaterDetailMovie;
import com.bdc.ociney.modele.Movie.Movie;
import com.bdc.ociney.modele.Theater.Horaires;
import com.bdc.ociney.modele.Theater.Theater;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class TheaterMovieFragment extends Fragment implements View.OnClickListener {

    public static final String THEATER = "theater";
    public static final String MOVIE = "movie";

    View view;
    ImageView poster;
    TextView titre;
    ViewGroup horaires;

    View layoutImage;

    Movie movie;
    Theater theater;

    public static TheaterMovieFragment newInstance(Movie movie, Theater theater) {
        Gson gson = new Gson();
        return newInstance(gson.toJson(movie), gson.toJson(theater));
    }

    public static TheaterMovieFragment newInstance(String movieJson, String theaterJson) {
        TheaterMovieFragment fragment = new TheaterMovieFragment();
        Bundle args = new Bundle();
        args.putString(MOVIE, movieJson);
        args.putString(THEATER, theaterJson);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_theater_poster, null);

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(THEATER) && bundle.containsKey(MOVIE)) {
                Gson gSon = new Gson();
                theater = gSon.fromJson(bundle.getString(THEATER), new TypeToken<Theater>() {
                }.getType());
                movie = gSon.fromJson(bundle.getString(MOVIE), new TypeToken<Movie>() {
                }.getType());


                charger();

                remplir();

                ajouterListener();
            }
        }

        return view;

    }

    private void ajouterListener() {
        this.view.setOnClickListener(this);
    }

    public void charger() {
        this.poster = (ImageView) this.view.findViewById(R.id.poster);
        this.titre = (TextView) this.view.findViewById(R.id.title);
        this.horaires = (ViewGroup) this.view.findViewById(R.id.horaires);
        this.layoutImage = this.view.findViewById(R.id.bg_darker_poster);
    }

    public void remplir() {
        layoutImage.setAlpha(0f);
        Picasso.with(getActivity()).load(this.movie.getUrlPoster(600)).into(this.poster, new Callback() {
            @Override
            public void onSuccess() {
                layoutImage.animate().alphaBy(0).alpha(1).setDuration(500).start();
            }

            @Override
            public void onError() {

            }
        });
        this.titre.setText(this.movie.getTitle());


        boolean isToday = true;
        ViewGroup layout = horaires;
        layout.removeAllViews();

        Context context = layout.getContext();

        if (movie.getHoraires() != null && movie.getHoraires().size() > 0) {

            for (Horaires h : movie.getHoraires()) {
                if (((isToday && h.isToday())) || (!isToday)) {

                    View vueHorraires = View.inflate(context, R.layout.cell_theater_horraires, null);
                    TextView texte = (TextView) vueHorraires.findViewById(R.id.text);
                    ImageView version = (ImageView) vueHorraires.findViewById(R.id.version);

                    if (h.getFormatEcran().contains("3D"))
                        version.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_version_white_3d));
                    else if (h.getFormatEcran().contains("Numérique"))
                        version.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_version_white_2d));
                    else if (!h.getVersion().contains("Français"))
                        version.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_version_white_vo));
                    else
                        version.setVisibility(View.GONE);

                    StringBuffer sb = new StringBuffer();
                    for (String seance : h.getSeances())
                        sb.append(seance).append(" ");

                    texte.setText(sb.toString());

                    layout.addView(vueHorraires);
                }
            }
        }
    }


    @Override
    public void onClick(View view) {

        theater.getShowTimes(movie.getCode().toString());

        Intent intent = new Intent(getActivity(), TheaterDetailMovie.class);
        intent.putExtra(TheaterDetailMovie.MOVIE, (new Gson()).toJson(movie));
        intent.putExtra(TheaterDetailMovie.THEATER, (new Gson()).toJson(theater));

        Bundle b = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            b = ActivityOptions.makeScaleUpAnimation(layoutImage, 0, 0, layoutImage.getWidth(), layoutImage.getHeight()).toBundle();

            getActivity().startActivity(intent, b);
        } else {
            getActivity().startActivity(intent);
            getActivity().overridePendingTransition(0, 0);
        }
    }
}
