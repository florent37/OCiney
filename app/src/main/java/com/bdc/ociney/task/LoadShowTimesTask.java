package com.bdc.ociney.task;

import android.os.AsyncTask;
import android.util.Log;

import com.bdc.ociney.modele.ModelObject;
import com.bdc.ociney.modele.Movie.Movie;
import com.bdc.ociney.modele.Theater.Horaires;
import com.bdc.ociney.modele.Theater.MovieShowtime;
import com.bdc.ociney.modele.Theater.Scr;
import com.bdc.ociney.modele.Theater.Theater;
import com.bdc.ociney.modele.Theater.TheaterShowtime;
import com.bdc.ociney.service.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by florentchampigny on 01/05/2014.
 */
public class LoadShowTimesTask extends AsyncTask<Object, Void, List<Theater>> {

    boolean erreurReseau = false;
    boolean cinemaList = false;
    private LoadShowTimesTaskCallBack callBack;

    public LoadShowTimesTask(LoadShowTimesTaskCallBack callBack) {
        this.callBack = callBack;
    }

    public LoadShowTimesTask(LoadShowTimesTaskCallBack callBack, boolean cinemaList) {
        this.callBack = callBack;
        this.cinemaList = cinemaList;
    }

    public static void loadShowTimes(List<Theater> theaters, List<TheaterShowtime> showtimes) {
        for (TheaterShowtime ts : showtimes) {

            Theater cinema = ts.getPlace().getTheater();
            theaters.add(cinema);

            for (MovieShowtime mst : ts.getMovieShowtimes()) {

                boolean avantPremiere = mst.getPreview().equals("true");
                String formatEcran = null;
                if (mst.getScreenFormat() != null)
                    formatEcran = mst.getScreenFormat().get$();
                String version = mst.getVersion().get$();
                String display = mst.getDisplay();

                for (Scr scr : mst.getScr()) {
                    Horaires horaires = new Horaires();
                    horaires.setAvantPremier(avantPremiere);
                    horaires.setDisplay(display);
                    horaires.setFormatEcran(formatEcran);
                    horaires.setVersion(version);
                    horaires.setDate(scr.getD());


                    for (ModelObject t : scr.getT()) {
                        horaires.getSeances().add(t.get$());
                    }

                    if (!horaires.getSeances().isEmpty()) {
                        cinema.getHorraires().add(horaires);
                    }
                }

                if (display != null && !display.isEmpty()) {
                    String[] joursHorraires = display.split("Séances du");

                    for (int i = 0; i < joursHorraires.length; i++) {

                        if (joursHorraires != null && joursHorraires.length > 0) {

                            try {
                                String[] jrs = joursHorraires[i].split(" : ");

                                if (jrs.length > 1) {
                                    String jour = jrs[0].trim();
                                    String hrs = jrs[1].trim();

                                    Horaires horaires = new Horaires();
                                    horaires.setAvantPremier(avantPremiere);
                                    horaires.setDisplay(display);
                                    horaires.setFormatEcran(formatEcran);
                                    horaires.setVersion(version);
                                    horaires.setDate(jour);

                                    if (horaires.isMoreThanToday()) {
                                        String[] horairesLignes = hrs.split(",");
                                        for (int j = 0; j < horairesLignes.length; ++j) {
                                            String horraire = horairesLignes[j].trim().split(" ")[0].trim();
                                            horaires.getSeances().add(horraire);
                                        }

                                        //Log.d("horaires",jour + horaires.getSeances().toString());

                                        if (cinema.getHorairesSemaine().containsKey(jour)) {
                                            cinema.getHorairesSemaine().get(jour).add(horaires);
                                        } else {
                                            ArrayList<Horaires> horairesDuJour = new ArrayList<Horaires>();
                                            horairesDuJour.add(horaires);

                                            cinema.getHorairesSemaineJours().add(jour);
                                            cinema.getHorairesSemaine().put(jour, horairesDuJour);
                                        }
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }

                }
            }
        }
    }

    @Override
    protected List<Theater> doInBackground(Object... data) {

        Movie movie = (Movie) data[0];

        if (cinemaList) {
            List<Theater> cinemas = (List<Theater>) data[1];
            StringBuffer sb = new StringBuffer();

            int taille = cinemas.size();

            Log.e("CINEMA_TAILLE", taille + "");

            for (int i = 0; i < taille; ++i) {
                Log.e("CINEMA", cinemas.get(i).getCode().toString());
                sb.append(cinemas.get(i).getCode());
                if (i < taille - 1)
                    sb.append(",");
            }

            cinemas.clear();

            try {
                List<TheaterShowtime> showtimes = Service.showtimelistForTheaterAndMovie(sb.toString(), movie.getCode().toString(), new Date(), 20, 1);

                //Log.d("SHOWTIME", showtimes.toString());

                //Log.e("SHOWTIMES", showtimes.toString());

                if (showtimes != null)
                    loadShowTimes(cinemas, showtimes);


                return cinemas;
            } catch (Service.NetworkException e) {
                e.printStackTrace();
                erreurReseau = true;
                return null;
            }
        } else {

            String lat = (String) data[1];
            String lng = (String) data[2];

            List<Theater> cinemas = new ArrayList<Theater>();

            try {
                List<TheaterShowtime> showtimes = Service.showtimelistWithLatLngAndMovie(lng, lat, "50", "" + movie.getCode(), new Date(), 10, 1);

                //Log.d("SHOWTIME", showtimes.toString());

                //Log.e("SHOWTIMES", showtimes.toString());

                if (showtimes != null)
                    loadShowTimes(cinemas, showtimes);


                return cinemas;
            } catch (Service.NetworkException e) {
                erreurReseau = true;
                return null;
            }
        }
    }

    @Override
    protected void onPostExecute(List<Theater> result) {
        super.onPostExecute(result);
        try {
            if (callBack != null) {
                if (erreurReseau)
                    callBack.onErreurReseau();
                else
                    callBack.onLoadShowTimesTaskCallBack(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public interface LoadShowTimesTaskCallBack {
        public void onLoadShowTimesTaskCallBack(List<Theater> result);

        public void onErreurReseau();
    }
}