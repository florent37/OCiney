package com.bdc.ociney.task;

import android.os.AsyncTask;

import com.bdc.ociney.modele.Movie.Movie;
import com.bdc.ociney.service.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 01/05/2014.
 */
public class LoadMoviesTask extends AsyncTask<Object, Void, ArrayList<List<Movie>>> {

    boolean erreurReseau = false;
    private LoadMoviesTaskCallBack callBack;

    public LoadMoviesTask(LoadMoviesTaskCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    protected ArrayList<List<Movie>> doInBackground(Object... data) {
        ArrayList<List<Movie>> listMovies = new ArrayList<List<Movie>>();

        int page = 1;

        if (data.length > 0)
            page = ((Integer) data[0]).intValue();

        try {
            List<Movie> moviesNowShowing = Service.movielist(Service.MOVIELIST_FILTER_NOWSHOWING, Service.PROFILE_SMALL, Service.MOVIELIST_ORDER_TOPRANK, 20, page);
            List<Movie> moviesCommingSoon = Service.movielist(Service.MOVIELIST_FILTER_COMINGSOON, Service.PROFILE_SMALL, Service.MOVIELIST_ORDER_TOPRANK, 20, page);
            listMovies.add(moviesNowShowing);
            listMovies.add(moviesCommingSoon);

            return listMovies;
        } catch (Service.NetworkException e) {
            erreurReseau = true;
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<List<Movie>> ms) {
        super.onPostExecute(ms);
        try {
            if (callBack != null) {
                if (erreurReseau)
                    callBack.onErreurReseau();
                else
                    callBack.onLoadMoviesTaskCallBack(ms);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface LoadMoviesTaskCallBack {
        public void onLoadMoviesTaskCallBack(ArrayList<List<Movie>> movies);

        public void onErreurReseau();
    }
}