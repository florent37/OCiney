package com.bdc.ociney.modele;

import com.google.gson.annotations.Expose;
import com.bdc.ociney.modele.Movie.Movie;
import com.bdc.ociney.modele.Person.PersonSmall;

public class AllocineResponseSmall {

    @Expose
    private FeedSmall feed;

    @Expose
    private Movie movie;

    @Expose
    private PersonSmall person;

    public PersonSmall getPerson() {
        return person;
    }

    public void setPerson(PersonSmall person) {
        this.person = person;
    }

    public FeedSmall getFeed() {
        return feed;
    }

    public void setFeed(FeedSmall feed) {
        this.feed = feed;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    @Override
    public String toString() {
        return "Field{" +
                "feed=" + feed +
                ", movie=" + movie +
                ", person=" + person +
                '}';
    }
}
