package com.bdc.ociney.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bdc.ociney.R;
import com.bdc.ociney.activity.StarActivity;
import com.bdc.ociney.adapter.ObjectAdapter;
import com.bdc.ociney.fragment.core.BaseFragment;
import com.bdc.ociney.modele.Person.PersonFull;
import com.bdc.ociney.modele.Person.PersonSmall;
import com.bdc.ociney.task.LoadStarsFavorisTask;
import com.bdc.ociney.task.LoadStarsTask;
import com.bdc.ociney.task.SearchStarsTask;
import com.bdc.ociney.view.CellStar;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;


public class ListStarFragment extends BaseFragment implements AdapterView.OnItemClickListener, LoadStarsFavorisTask.LoadStarsFavorisTaskCallBack, ObjectAdapter.ObjectAdapterLoadMore, LoadStarsTask.LoadStarTaskCallBack, SearchStarsTask.SearchStarsTaskCallBack {

    public static final String PERSONS = "persons";
    public static final String FAVORIS = "favoris";
    public static final String FAVORIS_STRINGS = "favoris_string";

    List<PersonFull> persons = new ArrayList<PersonFull>();
    ObjectAdapter<PersonFull> adapter;
    AbsListView list;

    AsyncTask task = null;
    String texteRecherche = "";
    boolean recherche = false;
    boolean vide = false;
    boolean favoris = false;

    int page = 1;

    public static ListStarFragment newInstance(List<PersonFull> stars) {
        ListStarFragment fragment = new ListStarFragment();
        Bundle args = new Bundle();
        Gson gSon = new Gson();
        args.putString(PERSONS, gSon.toJson(stars));
        fragment.setArguments(args);

        return fragment;
    }

    public static ListStarFragment newInstance(List<String> stars, boolean b) { //favoris
        ListStarFragment fragment = new ListStarFragment();
        Bundle args = new Bundle();
        Gson gSon = new Gson();
        args.putBoolean(FAVORIS, true);
        args.putString(FAVORIS_STRINGS, gSon.toJson(stars));
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_star,
                container, false);

        setFragmentView(view);

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(FAVORIS)) {
                favoris = true;
                Gson gSon = new Gson();

                List<String> starsFavoris = gSon.fromJson(bundle.getString(FAVORIS_STRINGS), new TypeToken<List<String>>() {
                }.getType());
                if (starsFavoris.size() > 0) {
                    super.tournerRoulette(true);
                    new LoadStarsFavorisTask(this).execute(starsFavoris);
                } else
                    afficherVide(true);
            } else if (bundle.containsKey(PERSONS)) {
                Gson gSon = new Gson();
                persons = gSon.fromJson(bundle.getString(PERSONS), new TypeToken<List<PersonFull>>() {
                }.getType());
            }
        }

        charger();
        remplir();
        ajouterListeners();


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
        list = (AbsListView) findViewById(R.id.list);
        if (!favoris)
            new LoadStarsTask(this).execute();
    }

    @Override
    protected void remplir() {

        adapter = new ObjectAdapter<PersonFull>(getActivity(), persons, R.layout.cell_star, CellStar.class);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (!favoris)
            adapter.listener = this;

    }

    @Override
    protected void ajouterListeners() {
        list.setOnItemClickListener(this);
    }


    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        PersonFull person = (PersonFull) adapter.getItem(i);

        Intent intent = new Intent(getActivity(), StarActivity.class);
        intent.putExtra(StarFragment.PERSON, (new Gson()).toJson(person));

        Bundle b = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            b = ActivityOptions.makeScaleUpAnimation(view, 0, 0, view.getWidth(), view.getHeight()).toBundle();

            getActivity().startActivity(intent, b);
        } else {
            getActivity().startActivity(intent);
            getActivity().overridePendingTransition(0, 0);
        }
    }

    public void refreshAdapter(List<PersonFull> p) {
        this.persons.addAll(p);
        adapter.notifyDataSetChanged();
        //adapter.preloadView();
    }

    @Override
    public void loadMore() {
        page++;
        if (recherche && !vide)
            task = new SearchStarsTask(this).execute(texteRecherche, page);
        else
            new LoadStarsTask(this).execute(page);
    }

    @Override
    public void onLoadStarTaskCallBack(List<PersonFull> stars) {
        if (stars.size() == 0) //si plus de resultats, on enleve le loadmore
            adapter.listener = null;

        refreshAdapter(stars);
    }

    @Override
    public void search(String text) {
        if (task == null && text != null && !text.isEmpty()) {
            texteRecherche = Normalizer.normalize(text, Normalizer.Form.NFD)
                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
            if (task == null && text != null && !text.isEmpty()) {
                if (!recherche) {
                    persons.clear();
                    adapter.notifyDataSetChanged();
                }
                task = new SearchStarsTask(this).execute(texteRecherche);
                recherche = true;
            }
        }
    }

    @Override
    public void onSearchStarsTaskCallBack(List<PersonSmall> ps) {

        if (persons.isEmpty() && this.persons.isEmpty()) {
            afficherVide(true);
            vide = true;
            adapter.listener = null;
        }
        else
            afficherVide(false);

        if (ps.isEmpty()) {
            vide = true;
            adapter.listener = null;
        }

        task = null;
        try {
            refreshAdapter(PersonFull.transformListFull(ps));
        } catch (Exception e) {
            adapter.listener = null;
        }
    }

    @Override
    public void onLoadStarFavorisTaskCallBack(List<PersonFull> stars) {

        if (stars.isEmpty() && this.persons.isEmpty())
            afficherVide(true);
        else
            afficherVide(false);

        super.tournerRoulette(false);
        persons.addAll(stars);
        adapter.notifyDataSetChanged();
    }


}