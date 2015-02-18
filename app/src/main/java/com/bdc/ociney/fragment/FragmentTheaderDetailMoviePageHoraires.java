package com.bdc.ociney.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bdc.ociney.R;
import com.bdc.ociney.modele.Theater.Horaires;
import com.bdc.ociney.view.CellTheater;

import java.util.List;

/**
 * Created by florentchampigny on 29/05/2014.
 */
public class FragmentTheaderDetailMoviePageHoraires extends Fragment {

    public static final String JOUR = "jour";
    public static final String HORAIRES = "horaires";
    String jour;
    List<Horaires> horaires;
    View view;
    TextView date;
    ViewGroup listeSeances;

    public static FragmentTheaderDetailMoviePageHoraires newInstance(String jour, List<Horaires> horaires) {
        FragmentTheaderDetailMoviePageHoraires fragment = new FragmentTheaderDetailMoviePageHoraires();
        Bundle args = new Bundle();
        args.putString(JOUR, jour);

        Gson gSon = new Gson();
        args.putString(HORAIRES, gSon.toJson(horaires));

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        Bundle bundle = getArguments();
        this.jour = bundle.getString(JOUR);
        Gson gSon = new Gson();
        this.horaires = gSon.fromJson(bundle.getString(HORAIRES), new TypeToken<List<Horaires>>() {
        }.getType());


        view = inflater.inflate(R.layout.fragment_theater_detail_movie_content_page_horaires, null);

        charger();
        remplir();
        ajouterListener();

        return view;
    }

    private void charger() {
        date = (TextView) view.findViewById(R.id.date);
        listeSeances = (ViewGroup) view.findViewById(R.id.liste_seances);
    }

    private void remplir() {
        date.setText(jour);
        CellTheater.chargerHoraires(listeSeances, horaires, false);
    }

    private void ajouterListener() {

    }
}
