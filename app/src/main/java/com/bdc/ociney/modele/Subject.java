package com.bdc.ociney.modele;

import com.google.gson.annotations.Expose;
import com.bdc.ociney.modele.Movie.Movie;

/**
 * Created by florentchampigny on 01/05/2014.
 */
public class Subject {

    @Expose
    private Movie movie;

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
