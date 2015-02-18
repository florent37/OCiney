package com.bdc.ociney.task;

import android.os.AsyncTask;

import com.bdc.ociney.modele.Theater.Theater;
import com.bdc.ociney.service.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 01/05/2014.
 */
public class LoadTheatersFavorisTask extends AsyncTask<Object, Void, List<Theater>> {

    boolean erreurReseau = false;
    private LoadTheatersFavorisTaskCallBack callBack;

    public LoadTheatersFavorisTask(LoadTheatersFavorisTaskCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    protected List<Theater> doInBackground(Object... data) {
        List<String> theatersIds = (List<String>) data[0];


        List<Theater> theaters = new ArrayList<Theater>();
        try {
            for (int i = 0; i < theatersIds.size(); ++i) {
                Theater t = Service.theater(theatersIds.get(i), Service.PROFILE_MEDIUM, Service.FILTER_THEATER);
                if (t != null)
                    theaters.add(t);
            }

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
                else
                    callBack.onLoadTheatersFavorisTaskCallBack(theaters);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface LoadTheatersFavorisTaskCallBack {
        public void onLoadTheatersFavorisTaskCallBack(List<Theater> theaters);

        public void onErreurReseau();
    }
}