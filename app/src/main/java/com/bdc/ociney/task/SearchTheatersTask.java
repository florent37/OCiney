package com.bdc.ociney.task;

import android.os.AsyncTask;

import com.bdc.ociney.modele.AllocineResponse;
import com.bdc.ociney.modele.Movie.Movie;
import com.bdc.ociney.modele.Theater.Theater;
import com.bdc.ociney.service.Service;

import java.util.List;

/**
 * Created by florentchampigny on 01/05/2014.
 */
public class SearchTheatersTask extends AsyncTask<Object, Void, List<Theater>> implements LoadShowTimesTask.LoadShowTimesTaskCallBack {

    boolean erreurReseau = false;
    Movie movie = null;
    private SearchTheatersTaskCallBack callBack;

    public SearchTheatersTask(SearchTheatersTaskCallBack callBack, Movie movie) {
        this.callBack = callBack;
        this.movie = movie;
    }

    public SearchTheatersTask(SearchTheatersTaskCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    protected List<Theater> doInBackground(Object... data) {
        String text = (String) data[0];

        int page = 1;

        if (data.length > 1)
            page = ((Integer) data[1]).intValue();

        try {
            List<Theater> theaters = null;
            AllocineResponse response = Service.search(text, Service.FILTER_THEATER, 20, page);

            if (response != null && response.getFeed() != null && response.getFeed().getTheater() != null)
                theaters = response.getFeed().getTheater();

            return theaters;
        } catch (Service.NetworkException e) {
            erreurReseau = true;
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Theater> ms) {
        super.onPostExecute(ms);


        if (callBack != null) {
            if (erreurReseau)
                callBack.onErreurReseau();

            else {
                if (movie != null) {
                    new LoadShowTimesTask(this, true).execute(movie, ms);
                } else {
                    try {
                        callBack.onSearchTheatersTaskCallBack(ms);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onLoadShowTimesTaskCallBack(List<Theater> result) {
        movie = null;
        onPostExecute(result);
    }

    @Override
    public void onErreurReseau() {
        callBack.onErreurReseau();
    }

    public interface SearchTheatersTaskCallBack {
        public void onSearchTheatersTaskCallBack(List<Theater> theaters);

        public void onErreurReseau();
    }
}