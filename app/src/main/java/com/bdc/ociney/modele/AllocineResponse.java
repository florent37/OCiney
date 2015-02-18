package com.bdc.ociney.modele;

import com.google.gson.annotations.Expose;
import com.bdc.ociney.modele.Movie.Movie;
import com.bdc.ociney.modele.Person.PersonFull;
import com.bdc.ociney.modele.Theater.Theater;

public class AllocineResponse {

    @Expose
    private Feed feed;

    @Expose
    private Movie movie;

    @Expose
    private Theater theater;

    @Expose
    private PersonFull person;

    public PersonFull getPerson() {
        return person;
    }

    public void setPerson(PersonFull person) {
        this.person = person;
    }

    public Feed getFeed() {
        return feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Theater getTheater() {
        return theater;
    }

    public void setTheater(Theater theater) {
        this.theater = theater;
    }

    @Override
    public String toString() {
        return "Field{" +
                "feed=" + feed +
                ", movie=" + movie +
                ", person=" + person +
                ", theater=" + theater +
                '}';
    }
}
