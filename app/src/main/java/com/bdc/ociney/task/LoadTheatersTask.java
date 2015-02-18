package com.bdc.ociney.task;

import android.os.AsyncTask;

import com.bdc.ociney.modele.Movie.Movie;
import com.bdc.ociney.modele.Theater.Theater;
import com.bdc.ociney.service.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 01/05/2014.
 */
public class LoadTheatersTask extends AsyncTask<Object, Void, List<Theater>> {

    boolean erreurReseau = false;
    private LoadTheatersTaskCallBack callBack;

    public LoadTheatersTask(LoadTheatersTaskCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    protected List<Theater> doInBackground(Object... data) {
        ArrayList<List<Movie>> listMovies = new ArrayList<List<Movie>>();

        try {
            String lat = (String) data[0];
            String lng = (String) data[1];
            List<Theater> theaters = Service.theaterlist(lat, lng, 30, 20, 1);

            return theaters;
        } catch (Service.NetworkException e) {
            erreurReseau = true;
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Theater> theaters) {
        super.onPostExecute(theaters);
        try {
            if (callBack != null) {
                if (erreurReseau)
                    callBack.onErreurReseau();
                else callBack.onLoadTheatersTaskCallBack(theaters);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface LoadTheatersTaskCallBack {
        public void onLoadTheatersTaskCallBack(List<Theater> theaters);

        public void onErreurReseau();
    }
}