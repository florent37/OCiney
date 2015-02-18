package com.bdc.ociney.task;

import android.os.AsyncTask;

import com.bdc.ociney.modele.AllocineResponse;
import com.bdc.ociney.modele.Movie.Movie;
import com.bdc.ociney.service.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by florentchampigny on 01/05/2014.
 */
public class SearchMoviesTask extends AsyncTask<Object, Void, List<Movie>> {

    boolean erreurReseau = false;
    private SearchMoviesTaskCallBack callBack;

    public SearchMoviesTask(SearchMoviesTaskCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    protected List<Movie> doInBackground(Object... data) {
        String text = (String) data[0];

        int page = 1;
        if (data.length > 1)
            page = ((Integer) data[1]).intValue();

        try {
            AllocineResponse response = Service.search(text, Service.FILTER_MOVIE, 20, page);

            List<Movie> movies = new ArrayList<Movie>();

            if (response != null && response.getFeed() != null && response.getFeed().getMovie() != null)
                movies = response.getFeed().getMovie();

            Collections.sort(movies);

            return movies;
        } catch (Service.NetworkException e) {
            erreurReseau = true;
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Movie> ms) {
        super.onPostExecute(ms);
        try {
            if (callBack != null) {
                if (erreurReseau)
                    callBack.onErreurReseau();
                else
                    callBack.onSearchMoviesTaskTaskCallBack(ms);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface SearchMoviesTaskCallBack {
        public void onSearchMoviesTaskTaskCallBack(List<Movie> movies);

        public void onErreurReseau();
    }
}