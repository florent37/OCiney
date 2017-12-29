package com.bdc.ociney.fragment;

import android.app.ActionBar;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bdc.ociney.R;
import com.bdc.ociney.adapter.ObjectAdapter;
import com.bdc.ociney.fragment.core.BaseFragment;
import com.bdc.ociney.modele.Movie.Movie;
import com.bdc.ociney.modele.Theater.Theater;
import com.bdc.ociney.task.LoadShowTimesTask;
import com.bdc.ociney.utils.LocationUtils;
import com.bdc.ociney.view.CellTheater;

import java.util.ArrayList;
import java.util.List;



public class MovieTheatersFragment extends BaseFragment implements AdapterView.OnItemClickListener, LocationUtils.LocationUtilsCallBack, LoadShowTimesTask.LoadShowTimesTaskCallBack {

    public static final String MOVIE = "movie";

    List<Theater> theaters = new ArrayList<Theater>();
    Movie movie;
    ObjectAdapter<Theater> adapter;
    AdapterView list;

    LocationManager locationManager = null;

    public static MovieTheatersFragment newInstance(Movie movie) {
        Gson gSon = new Gson();
        return newInstance(gSon.toJson(movie));
    }

    public static MovieTheatersFragment newInstance(String movieJson) {
        MovieTheatersFragment fragment = new MovieTheatersFragment();
        Bundle args = new Bundle();
        Gson gSon = new Gson();
        args.putString(MOVIE, movieJson);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_theaters,
                container, false);

        setFragmentView(view);

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(MOVIE)) {
                Gson gSon = new Gson();
                movie = gSon.fromJson(bundle.getString(MOVIE), new TypeToken<Movie>() {
                }.getType());
            }
        }

        getActivity().getActionBar().setDisplayShowTitleEnabled(true);
        getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        charger();
        remplir();
        ajouterListeners();

        chercherLocation();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void charger() {
        list = (AdapterView) View.inflate(getActivity(), R.layout.list, null);
        ((ViewGroup) fragmentView).addView(list);
    }

    @Override
    protected void remplir() {
        adapter = new ObjectAdapter<Theater>(getActivity(), theaters, R.layout.cell_theater, CellTheater.class);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void ajouterListeners() {
        list.setOnItemClickListener(this);
    }

    private void chercherLocation() {
        new LocationUtils(getActivity(), this).chercherLocation();
    }

    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


        Theater theater = (Theater) adapter.getItem(i);

        /*
        TheaterFragment.theater = theater;

        getActivity().startActivity(new Intent(getActivity(), TheaterActivity.class));
        */

        getActivity().overridePendingTransition(0, 0);
    }

    @Override
    public void onLoadShowTimesTaskCallBack(List<Theater> result) {
        try {
            theaters.clear();
            theaters.addAll(result);
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
        }
    }

    //---------------------------LOCATION LISTENER---------------------
    @Override
    public void onLocationFound(String lng, String lat) {
        new LoadShowTimesTask(this).execute(movie, lat, lng);
    }

    @Override
    public void onGPSNotFound() {

        Toast.makeText(getActivity(), R.string.erreur_gps, Toast.LENGTH_SHORT).show();
    }


}