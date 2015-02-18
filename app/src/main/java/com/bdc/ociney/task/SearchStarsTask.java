package com.bdc.ociney.task;

import android.os.AsyncTask;

import com.bdc.ociney.modele.AllocineResponseSmall;
import com.bdc.ociney.modele.Person.PersonSmall;
import com.bdc.ociney.service.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by florentchampigny on 01/05/2014.
 */
public class SearchStarsTask extends AsyncTask<Object, Void, List<PersonSmall>> {

    boolean erreurReseau = false;
    private SearchStarsTaskCallBack callBack;

    public SearchStarsTask(SearchStarsTaskCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    protected List<PersonSmall> doInBackground(Object... data) {
        String text = (String) data[0];

        int page = 1;

        if (data.length > 1)
            page = ((Integer) data[1]).intValue();

        try {
            AllocineResponseSmall response = Service.searchSmall(text, Service.FILTER_PERSON, 20, page);

            List<PersonSmall> stars = new ArrayList<PersonSmall>();

            if (response != null && response.getFeed() != null && response.getFeed().getMovie() != null)
                stars = response.getFeed().getPerson();

            Collections.sort(stars);

            return stars;
        } catch (Service.NetworkException e) {
            erreurReseau = true;
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<PersonSmall> ms) {
        super.onPostExecute(ms);
        try {
            if (callBack != null) {
                if (erreurReseau)
                    callBack.onErreurReseau();
                else
                    callBack.onSearchStarsTaskCallBack(ms);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface SearchStarsTaskCallBack {
        public void onSearchStarsTaskCallBack(List<PersonSmall> stars);

        public void onErreurReseau();
    }
}