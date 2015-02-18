package com.bdc.ociney.task;

import android.os.AsyncTask;

import com.bdc.ociney.modele.Person.PersonFull;
import com.bdc.ociney.service.Service;

import java.util.List;

/**
 * Created by florentchampigny on 01/05/2014.
 */
public class LoadStarsTask extends AsyncTask<Object, Void, List<PersonFull>> {

    boolean erreurReseau = false;
    private LoadStarTaskCallBack callBack;

    public LoadStarsTask(LoadStarTaskCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    protected List<PersonFull> doInBackground(Object... data) {
        int page = 1;

        if (data.length > 0)
            page = ((Integer) data[0]).intValue();
        try {
            return Service.startlist(Service.PERSONLIST_FILTER_PERSON, Service.PROFILE_SMALL, 30, page);
        } catch (Service.NetworkException e) {
            erreurReseau = true;
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<PersonFull> stars) {
        super.onPostExecute(stars);
        try {
            if (callBack != null) {
                if (erreurReseau)
                    callBack.onErreurReseau();
                callBack.onLoadStarTaskCallBack(stars);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface LoadStarTaskCallBack {
        public void onLoadStarTaskCallBack(List<PersonFull> stars);

        public void onErreurReseau();
    }
}
