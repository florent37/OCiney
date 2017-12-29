package com.bdc.ociney.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Rect;
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
import com.bdc.ociney.activity.AppFragmentActivity;
import com.bdc.ociney.activity.MovieActivity;
import com.bdc.ociney.adapter.ObjectAdapter;
import com.bdc.ociney.fragment.core.BaseFragment;
import com.bdc.ociney.modele.Movie.Movie;
import com.bdc.ociney.task.LoadMoviesFavorisTask;
import com.bdc.ociney.task.LoadMoviesTask;
import com.bdc.ociney.task.SearchMoviesTask;
import com.bdc.ociney.view.CellMovie;
import com.bdc.ociney.view.ViewCell;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;


public class ListMoviesFragment extends BaseFragment implements AbsListView.OnScrollListener, LoadMoviesFavorisTask.LoadMoviesFavorisTaskCallBack, AdapterView.OnItemClickListener, ObjectAdapter.ObjectAdapterLoadMore, SearchMoviesTask.SearchMoviesTaskCallBack, LoadMoviesTask.LoadMoviesTaskCallBack {

    public static final String MOVIES = "movies";
    public static final String FAVORIS = "favoris";
    public static final String NOW_SHOWING = "nowshowing";
    public static final String FAVORIS_STRINGS = "favoris_string";

    static int MOVIE_CELL_IMAGE_FOND_HEIGHT;
    static int MOVIE_PARALLAX_SPEED = 100;
    public int page = 1;

    List<Movie> movies;
    ObjectAdapter<Movie> adapter;
    AbsListView list;
    AsyncTask task = null;
    View top = null;
    boolean recherche = false;
    boolean favoris = false;
    String texteRecherche = "";

    boolean nowshowing = false;
    boolean vide = false;


    public ListMoviesFragment() {

    }

    public static ListMoviesFragment newInstance(List<Movie> movies, boolean nowShowing) {
        ListMoviesFragment fragment = new ListMoviesFragment();
        Bundle args = new Bundle();
        Gson gSon = new Gson();
        args.putString(MOVIES, gSon.toJson(movies));
        args.putBoolean(NOW_SHOWING, nowShowing);
        fragment.setArguments(args);

        return fragment;
    }

    public static ListMoviesFragment newInstance(List<String> films) { //dans le cas des favoris
        ListMoviesFragment fragment = new ListMoviesFragment();
        Bundle args = new Bundle();
        Gson gSon = new Gson();
        args.putBoolean(FAVORIS, true);
        args.putString(FAVORIS_STRINGS, gSon.toJson(films));
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        setFragmentView(view);

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(FAVORIS)) {
                favoris = true;

                movies = new ArrayList<Movie>();
                Gson gSon = new Gson();
                List<String> films = gSon.fromJson(bundle.getString(FAVORIS_STRINGS), new TypeToken<List<String>>() {
                }.getType());
                if (films.size() > 0) {
                    super.tournerRoulette(true);
                    new LoadMoviesFavorisTask(this).execute(films);
                } else
                    afficherVide(true);
            } else if (bundle.containsKey(MOVIES)) {
                Gson gSon = new Gson();
                nowshowing = bundle.getBoolean(NOW_SHOWING);
                movies = gSon.fromJson(bundle.getString(MOVIES), new TypeToken<List<Movie>>() {
                }.getType());
            }
        }

        MOVIE_CELL_IMAGE_FOND_HEIGHT = (int) getActivity().getResources().getDimension(R.dimen.cell_movie_image_fond_height);

        charger();
        remplir();
        ajouterListeners();

        refreshAdapter(movies);


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
        adapter = new ObjectAdapter<Movie>(getActivity(), movies, R.layout.cell_movie, CellMovie.class);

        if (!favoris)
            adapter.listener = this;

        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void ajouterListeners() {
        list.setOnItemClickListener(this);
        list.setOnScrollListener(this);
    }

    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Movie movie = (Movie) adapter.getItem(i);

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

    public void refreshAdapter(List<Movie> movies) {
        ViewGroup p = (ViewGroup) this.list.getParent();
        p.removeView(list);

        this.movies = movies;
        page = 1;

        charger();
        remplir();
        ajouterListeners();
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (view.getChildCount() == 0)
            return;

        int first = 0;

        if (top == null)
            top = view.getChildAt(first);

        //GFloat yOffset = ((self.parallaxCollectionView.contentOffset.y


        int last = first + visibleItemCount;


        float centerY = view.getMeasuredHeight() / 2 + view.getTop();

        for (int i = first; i < last; ++i) {
            View viewCell = view.getChildAt(i);
            if(viewCell != null && viewCell.getTag()!=null) {
                ViewCell cell = (ViewCell) viewCell.getTag();
                Rect rect = new Rect();
                viewCell.getGlobalVisibleRect(rect);

                float diffCenter = (centerY - rect.top);

                float yOffset = diffCenter / MOVIE_CELL_IMAGE_FOND_HEIGHT;

                if (yOffset > 1)
                    yOffset = 1;
                if (yOffset < -1)
                    yOffset = -1;

                cell.onScroll((-1f + yOffset) * MOVIE_PARALLAX_SPEED);
            }
        }
    }

    @Override
    public void search(String text) {
        texteRecherche = Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        if (task == null && text != null && !text.isEmpty()) {
            task = new SearchMoviesTask(this).execute(texteRecherche);
            refreshAdapter(new ArrayList<Movie>());
            recherche = true;
        }
    }

    @Override
    public void onSearchMoviesTaskTaskCallBack(final List<Movie> ms) {
        if (ms.isEmpty() && movies.isEmpty()) {
            afficherVide(true);
            vide = true;
            adapter.listener = null;
        }
        else
            afficherVide(false);

        if (ms.isEmpty()) {
            vide = true;
            adapter.listener = null;
        }

        if (ms.size() == 0) //si plus de resultats, on enleve le loadmore
            adapter.listener = null;
        else {

            list.post(
                    new Runnable() {
                        @Override
                        public void run() {
                            movies.addAll(ms);
                            adapter.notifyDataSetChanged();
                        }
                    }
            );
        }

        task = null;
    }

    @Override
    public void loadMore() {
        page++;
        if (recherche && !vide)
            task = new SearchMoviesTask(this).execute(texteRecherche, page);
        else
            new LoadMoviesTask(this).execute(page);
    }

    @Override
    public void onLoadMoviesTaskCallBack(final ArrayList<List<Movie>> ms) {
        try {
            list.post(
                    new Runnable() {
                        @Override
                        public void run() {

                            AppFragmentActivity.moviesNowShowing.addAll(ms.get(0));
                            AppFragmentActivity.moviesCommingSoon.addAll(ms.get(1));

                            if (nowshowing)
                                movies.addAll(ms.get(0));
                            else
                                movies.addAll(ms.get(1));

                            adapter.notifyDataSetChanged();
                        }
                    }
            );

        } catch (Exception e) {
            e.printStackTrace();
            adapter.listener = null;
        }


    }

    @Override
    public void onLoadMoviesFavorisTaskCallBack(List<Movie> ms) {

        if (ms.isEmpty() && movies.isEmpty())
            afficherVide(true);
        else
            afficherVide(false);

        movies.addAll(ms);
        adapter.notifyDataSetChanged();
        tournerRoulette(false);
    }
}