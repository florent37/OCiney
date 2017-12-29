package com.bdc.ociney.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bdc.ociney.R;
import com.bdc.ociney.activity.MovieActivity;
import com.bdc.ociney.adapter.ObjectAdapter;
import com.bdc.ociney.fragment.core.BaseFragment;
import com.bdc.ociney.modele.Movie.Movie;
import com.bdc.ociney.modele.Theater.Theater;
import com.bdc.ociney.modele.Theater.TheaterShowtime;
import com.bdc.ociney.service.Service;
import com.bdc.ociney.view.CellMovie;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TheaterFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    public static final String THEATER = "THEATER";

    Theater theater;
    ObjectAdapter<Movie> adapter;
    AdapterView list;
    List<Movie> movies = new ArrayList<Movie>();
    boolean erreurReseau = false;

    public static TheaterFragment newInstance(Theater theater) {
        Gson gSon = new Gson();
        return newInstance(gSon.toJson(theater));
    }

    public static TheaterFragment newInstance(String theaterJson) {
        TheaterFragment fragment = new TheaterFragment();
        Bundle args = new Bundle();
        args.putString(THEATER, theaterJson);
        fragment.setArguments(args);

        return fragment;
    }

    public Theater getTheater() {
        return theater;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cinema,
                container, false);

        setFragmentView(view);

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(THEATER)) {
                Gson gSon = new Gson();
                theater = gSon.fromJson(bundle.getString(THEATER), new TypeToken<Theater>() {
                }.getType());

                charger();
                remplir();
                ajouterListeners();

                new LoadTheaterTask().execute(theater.getCode());
            }
        }

        return view;
    }

    @Override
    protected void charger() {
        list = (AdapterView) findViewById(R.id.list);
    }

    @Override
    protected void remplir() {
        adapter = new ObjectAdapter<Movie>(getActivity(), movies, R.layout.cell_movie, CellMovie.class);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void ajouterListeners() {
        list.setOnItemClickListener(this);
    }

    public void onClick(View v) {
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Movie movie = (Movie) adapter.getItem(i);

        Intent intent = new Intent(getActivity(), MovieActivity.class);
        intent.putExtra(MovieFragment.MOVIE, (new Gson()).toJson(movie));

        getActivity().startActivity(intent);

        getActivity().overridePendingTransition(0, 0);
    }

    //------------------------------------------------------------------

    public void refreshAdapter(List<Movie> movies) {
        this.movies.clear();
        this.movies.addAll(movies);
        adapter.notifyDataSetChanged();
    }

    class LoadTheaterTask extends AsyncTask<Object, Void, List<Movie>> {
        @Override
        protected List<Movie> doInBackground(Object... data) {
            String code = (String) data[0];

            try {
                List<TheaterShowtime> showtimes = Service.showtimelistForTheater(code, new Date(), 10, 1);

                return TheaterShowtime.getMovies(showtimes, theater);
            } catch (Service.NetworkException e) {
                erreurReseau = true;
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
            if (erreurReseau)
                onErreurReseau();
            else {
                TheaterFragment.this.movies.addAll(movies);
                adapter.notifyDataSetChanged();
            }
        }
    }

}