package com.bdc.ociney.task;

import android.os.AsyncTask;

import com.bdc.ociney.modele.Person.PersonFull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dev-w8-gfi on 28/05/2014.
 */
public class LoadListStarTask extends AsyncTask<Object, Void, List<PersonFull>> {


    private LoadListStarTaskCallBack callBack;

    public LoadListStarTask(LoadListStarTaskCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    protected List<PersonFull> doInBackground(Object... data) {
        ArrayList<List<PersonFull>> listMovies = new ArrayList<List<PersonFull>>();
        return null;
    }

    @Override
    protected void onPostExecute(List<PersonFull> ms) {
        super.onPostExecute(ms);
        try {
            if (callBack != null)
                callBack.onLoadListStarTaskCallBack(ms);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface LoadListStarTaskCallBack {
        public void onLoadListStarTaskCallBack(List<PersonFull> movies);
    }
}
