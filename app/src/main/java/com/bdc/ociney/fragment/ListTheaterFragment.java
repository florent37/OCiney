package com.bdc.ociney.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bdc.ociney.R;
import com.bdc.ociney.activity.TheaterMovieActivity;
import com.bdc.ociney.adapter.ObjectAdapter;
import com.bdc.ociney.fragment.core.BaseFragment;
import com.bdc.ociney.modele.Theater.Theater;
import com.bdc.ociney.task.LoadTheatersFavorisTask;
import com.bdc.ociney.task.LoadTheatersTask;
import com.bdc.ociney.task.SearchTheatersTask;
import com.bdc.ociney.utils.LocationUtils;
import com.bdc.ociney.view.CellTheater;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;


public class ListTheaterFragment extends BaseFragment implements AdapterView.OnItemClickListener, SearchTheatersTask.SearchTheatersTaskCallBack, LoadTheatersFavorisTask.LoadTheatersFavorisTaskCallBack, LocationUtils.LocationUtilsCallBack, LoadTheatersTask.LoadTheatersTaskCallBack, ObjectAdapter.ObjectAdapterLoadMore {

    public static final String FAVORIS = "favoris";
    public static final String FAVORIS_STRINGS = "favoris_string";

    List<Theater> theaters = new ArrayList<Theater>();
    ObjectAdapter<Theater> adapter;
    AbsListView list;

    AsyncTask task = null;

    LocationManager locationManager = null;

    String texteRecherche = "";
    boolean recherche = false;
    boolean vide = false;
    boolean favoris = false;

    int page = 1;

    public static ListMoviesFragment newInstance() {
        ListMoviesFragment fragment = new ListMoviesFragment();
        return fragment;
    }

    public static ListTheaterFragment newInstance(List<String> cinemas, boolean f) {
        ListTheaterFragment fragment = new ListTheaterFragment();
        Bundle args = new Bundle();
        Gson gSon = new Gson();
        args.putBoolean(FAVORIS, true);
        args.putString(FAVORIS_STRINGS, gSon.toJson(cinemas));
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_theater,
                container, false);

        setFragmentView(view);

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle != null && bundle.containsKey(FAVORIS)) {
                theaters = new ArrayList<Theater>();
                favoris = true;
                Gson gSon = new Gson();
                List<String> cinemas = gSon.fromJson(bundle.getString(FAVORIS_STRINGS), new TypeToken<List<String>>() {
                }.getType());
                if (cinemas.size() > 0) {
                    super.tournerRoulette(true);
                    new LoadTheatersFavorisTask(this).execute(cinemas);
                } else
                    afficherVide(true);
            }
        }

        charger();
        remplir();
        ajouterListeners();

        if (!favoris)
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
        View v = View.inflate(getActivity(), R.layout.list, null);
        list = (AbsListView) v.findViewById(R.id.list);
        ((ViewGroup) fragmentView).addView(v);
    }

    @Override
    protected void remplir() {
        adapter = new ObjectAdapter<Theater>(getActivity(), theaters, R.layout.cell_theater, CellTheater.class);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.listener = this;
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

        Intent intent = new Intent(getActivity(), TheaterMovieActivity.class);
        intent.putExtra(TheaterMovieActivity.THEATER, (new Gson().toJson(theater)));

        Bundle b = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            b = ActivityOptions.makeScaleUpAnimation(view, 0, 0, view.getWidth(), view.getHeight()).toBundle();

            getActivity().startActivity(intent, b);
        } else {
            getActivity().startActivity(intent);
            getActivity().overridePendingTransition(0, 0);
        }

    }

    @Override
    public void onLoadTheatersTaskCallBack(List<Theater> theaters) {
        ListTheaterFragment.this.theaters.clear();
        ListTheaterFragment.this.theaters.addAll(theaters);
        adapter.notifyDataSetChanged();

        Log.d("THEATER", theaters.size() + "");
    }

    @Override
    public void search(String text) {
        if (task == null && text != null && !text.isEmpty()) {
            texteRecherche = Normalizer.normalize(text, Normalizer.Form.NFD)
                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
            if (task == null && text != null && !text.isEmpty()) {
                if (!recherche) {
                    theaters.clear();
                    adapter.notifyDataSetChanged();
                }
                task = new SearchTheatersTask(this).execute(texteRecherche);
                recherche = true;
            }
        }
    }

    //---------------------------LOCATION LISTENER---------------------
    @Override
    public void onLocationFound(String lng, String lat) {
        new LoadTheatersTask(this).execute(lng, lat);
    }

    @Override
    public void onGPSNotFound() {
        Toast.makeText(getActivity(), R.string.erreur_gps, Toast.LENGTH_SHORT).show();


        findViewById(R.id.list_theater_empty).setVisibility(View.VISIBLE);
    }


    @Override
    public void onLoadTheatersFavorisTaskCallBack(List<Theater> ths) {

        if (ths.isEmpty() && this.theaters.isEmpty())
            afficherVide(true);
        else
            afficherVide(false);

        super.tournerRoulette(false);

        theaters.addAll(ths);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onSearchTheatersTaskCallBack(List<Theater> ths) {
        if (ths.isEmpty() && this.theaters.isEmpty()) {
            afficherVide(true);
            vide = true;
            adapter.listener = null;
        } else
            afficherVide(false);


        if (this.theaters.isEmpty()) {
            adapter.notifyDataSetChanged();
        }

        if (ths.isEmpty()) {
            vide = true;
            adapter.listener = null;
        }

        task = null;
        try {
            this.theaters.addAll(ths);
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            adapter.listener = null;
        }
    }

    @Override
    public void loadMore() {
        page++;
        if (recherche && !vide) {
            task = new SearchTheatersTask(this);
            task.execute(texteRecherche, page);
        }
    }
}