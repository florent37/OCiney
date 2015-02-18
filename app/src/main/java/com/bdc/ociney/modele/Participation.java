package com.bdc.ociney.modele;

import com.google.gson.annotations.Expose;
import com.bdc.ociney.modele.Movie.Movie;

public class Participation {

    @Expose
    private Movie movie;
    @Expose
    private ModelObject activity;
    @Expose
    private Tvseries tvseries;
    @Expose
    private String role;

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public ModelObject getActivity() {
        return activity;
    }

    public void setActivity(ModelObject activity) {
        this.activity = activity;
    }

    public Tvseries getTvseries() {
        return tvseries;
    }

    public void setTvseries(Tvseries tvseries) {
        this.tvseries = tvseries;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Participation{" +
                "movie=" + movie +
                ", activity=" + activity +
                ", tvseries=" + tvseries +
                ", role='" + role + '\'' +
                '}';
    }
}
