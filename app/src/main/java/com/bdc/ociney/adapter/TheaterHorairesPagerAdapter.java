package com.bdc.ociney.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bdc.ociney.fragment.FragmentTheaderDetailMoviePageHoraires;
import com.bdc.ociney.modele.Theater.Horaires;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by florentchampigny on 01/05/2014.
 */
public class TheaterHorairesPagerAdapter extends FragmentStatePagerAdapter {

    Activity activity;
    Map<String, List<Horaires>> horaires;

    List<String> jours;

    HashMap<Integer, Fragment> fragmentHashMap = new HashMap<Integer, Fragment>();

    public TheaterHorairesPagerAdapter(FragmentManager fm, Activity activity, List<String> jours, Map<String, List<Horaires>> horaires) {
        super(fm);
        this.activity = activity;
        this.horaires = horaires;

        this.jours = jours;
    }

    @Override
    public Fragment getItem(final int position) {

        if (fragmentHashMap.containsKey(position)) {
            return fragmentHashMap.get(position);
        } else {

            String jour = jours.get(position);

            Fragment f = FragmentTheaderDetailMoviePageHoraires.newInstance(jour, horaires.get(jour));
            fragmentHashMap.put(position, f);
            return f;
        }


    }

    @Override
    public int getCount() {
        return jours.size();
    }

}
