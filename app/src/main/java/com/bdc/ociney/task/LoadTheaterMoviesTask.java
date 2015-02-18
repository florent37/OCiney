package com.bdc.ociney.task;

import android.os.AsyncTask;

import com.bdc.ociney.modele.Movie.Movie;
import com.bdc.ociney.modele.Theater.Theater;
import com.bdc.ociney.modele.Theater.TheaterShowtime;
import com.bdc.ociney.service.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by florentchampigny on 01/05/2014.
 */
public class LoadTheaterMoviesTask extends AsyncTask<Object, Void, List<Movie>> {

    boolean erreurReseau = false;
    private LoadTheaterMoviesTaskCallBack callBack;

    public LoadTheaterMoviesTask(LoadTheaterMoviesTaskCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    protected List<Movie> doInBackground(Object... data) {

        Theater theater = (Theater) data[0];
        String code = theater.getCode();

        try {
            List<TheaterShowtime> showtimes = Service.showtimelistForTheater(code, new Date(), 10, 1);

            return TheaterShowtime.getMovies(showtimes, theater);
        } catch (Service.NetworkException e) {
            erreurReseau = true;
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Movie> result) {
        super.onPostExecute(result);
        try {
            if (callBack != null) {
                if (erreurReseau) {
                    callBack.onErreurReseau();
                } else
                    callBack.onLoadTheaterMoviesTaskCallBack(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface LoadTheaterMoviesTaskCallBack {
        public void onLoadTheaterMoviesTaskCallBack(List<Movie> movies);

        public void onErreurReseau();
    }
}