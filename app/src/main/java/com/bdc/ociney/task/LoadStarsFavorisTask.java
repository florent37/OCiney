package com.bdc.ociney.task;

import android.os.AsyncTask;

import com.bdc.ociney.modele.Person.PersonFull;
import com.bdc.ociney.service.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 01/05/2014.
 */
public class LoadStarsFavorisTask extends AsyncTask<Object, Void, List<PersonFull>> {

    boolean erreurReseau = false;
    private LoadStarsFavorisTaskCallBack callBack;

    public LoadStarsFavorisTask(LoadStarsFavorisTaskCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    protected List<PersonFull> doInBackground(Object... data) {

        List<PersonFull> persons = new ArrayList<PersonFull>();

        try {
            List<String> personsIds = ((List<String>) data[0]);
            for (int i = 0; i < personsIds.size(); ++i) {
                PersonFull p = Service.person(personsIds.get(i), Service.PROFILE_SMALL, Service.FILTER_PERSON);
                if (p != null)
                    persons.add(p);
            }

            return persons;
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
                else
                    callBack.onLoadStarFavorisTaskCallBack(stars);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface LoadStarsFavorisTaskCallBack {
        public void onLoadStarFavorisTaskCallBack(List<PersonFull> stars);

        public void onErreurReseau();
    }
}
