package com.bdc.ociney.task;

import android.os.AsyncTask;

import com.bdc.ociney.modele.Movie.Movie;
import com.bdc.ociney.service.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 01/05/2014.
 */
public class LoadMoviesFavorisTask extends AsyncTask<Object, Void, List<Movie>> {

    boolean erreurReseau = false;
    private LoadMoviesFavorisTaskCallBack callBack;

    public LoadMoviesFavorisTask(LoadMoviesFavorisTaskCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    protected List<Movie> doInBackground(Object... data) {
        List<Movie> listMovies = new ArrayList<Movie>();

        List<String> idMovies = (List<String>) data[0];

        try {
            for (int i = 0; i < idMovies.size(); ++i) {
                Movie m = Service.movie(idMovies.get(i), Service.PROFILE_MEDIUM, Service.FILTER_MOVIE);
                if (m != null)
                    listMovies.add(m);
            }
        } catch (Service.NetworkException e) {
            erreurReseau = true;
        }

        return listMovies;
    }

    @Override
    protected void onPostExecute(List<Movie> ms) {
        super.onPostExecute(ms);
        try {
            if (callBack != null) {
                if (erreurReseau)
                    callBack.onErreurReseau();
                callBack.onLoadMoviesFavorisTaskCallBack(ms);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface LoadMoviesFavorisTaskCallBack {
        public void onLoadMoviesFavorisTaskCallBack(List<Movie> movies);

        public void onErreurReseau();
    }
}