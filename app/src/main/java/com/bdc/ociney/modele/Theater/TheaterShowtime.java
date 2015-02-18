package com.bdc.ociney.modele.Theater;

import com.google.gson.annotations.Expose;
import com.bdc.ociney.modele.ModelObject;
import com.bdc.ociney.modele.Movie.Movie;
import com.bdc.ociney.modele.Place;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TheaterShowtime {

    @Expose
    private Place place;
    @Expose
    private List<MovieShowtime> movieShowtimes = new ArrayList<MovieShowtime>();

    public static List<Movie> getMovies(List<TheaterShowtime> showtimes, Theater cinema) {
        Map<Integer, Movie> movies = new HashMap<Integer, Movie>();

        try {
            for (TheaterShowtime t : showtimes) {
                if (t.getMovieShowtimes() != null)
                    for (MovieShowtime mst : t.getMovieShowtimes()) {
                        boolean avantPremiere = mst.getPreview().equals("true");
                        String version = mst.getVersion().get$();
                        String screenFormat = mst.getScreenFormat().get$();

                        if (cinema != null)
                            cinema.showTimes = showtimes;

                        if (mst.getOnShow() != null && mst.getOnShow().getMovie() != null) {
                            Movie movie = mst.getOnShow().getMovie();
                            if (!movies.containsKey(movie.getCode()))
                                movies.put(movie.getCode(), movie);
                            else {
                                movie = movies.get(movie.getCode());
                            }

                            Scr scr = mst.getScr().get(0);

                            if (scr.getT() != null) {
                                Horaires h = new Horaires();
                                h.setDate(scr.getD());
                                h.setFormatEcran(screenFormat);
                                h.setVersion(version);
                                h.setAvantPremier(avantPremiere);
                                h.setDisplay(mst.getDisplay());

                                for (ModelObject s : scr.getT()) {
                                    h.getSeances().add(s.get$());
                                }
                                movie.getHoraires().add(h);
                            }
                        }
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Movie>(movies.values());
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public List<MovieShowtime> getMovieShowtimes() {
        return movieShowtimes;
    }

    public void setMovieShowtimes(List<MovieShowtime> movieShowtimes) {
        this.movieShowtimes = movieShowtimes;
    }

    @Override
    public String toString() {
        return "TheaterShowtime{" +
                "place=" + place +
                ", movieShowtimes=" + movieShowtimes +
                '}';
    }


}
