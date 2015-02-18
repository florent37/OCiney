package com.bdc.ociney.modele.Theater;

import com.google.gson.annotations.Expose;
import com.bdc.ociney.modele.Movie.Movie;

public class OnShow {

    @Expose
    private Movie movie;

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    @Override
    public String toString() {
        return "OnShow{" +
                "movie=" + movie +
                '}';
    }
}
