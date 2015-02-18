package com.bdc.ociney.task;

import android.os.AsyncTask;

import com.bdc.ociney.modele.Movie.Movie;
import com.bdc.ociney.service.Service;

/**
 * Created by florentchampigny on 01/05/2014.
 */
public class LoadMovieFullTask extends AsyncTask<Object, Void, Movie> {

    boolean erreurReseau = false;
    private LoadMovieFullTaskCallBack callBack;

    public LoadMovieFullTask(LoadMovieFullTaskCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    protected Movie doInBackground(Object... data) {

        String idMovie = (String) data[0];

        Movie movie = null;
        try {
            movie = Service.movie(idMovie, Service.PROFILE_LARGE, Service.FILTER_MOVIE);
            if (movie != null)
                movie.setBandesAnnonces(Service.videoList(idMovie, 20)); //chargement des bandes annonces
        } catch (Service.NetworkException e) {
            e.printStackTrace();
            erreurReseau = true;
        }

        return movie;
    }

    @Override
    protected void onPostExecute(Movie movie) {
        super.onPostExecute(movie);
        try {
            if (callBack != null) {
                if (erreurReseau)
                    callBack.onErreurReseau();
                else
                    callBack.onLoadMovieFullTaskCallBack(movie);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface LoadMovieFullTaskCallBack {
        public void onLoadMovieFullTaskCallBack(Movie movie);

        public void onErreurReseau();
    }
}
