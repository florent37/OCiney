package com.bdc.ociney.modele.Person;

import com.google.gson.annotations.Expose;

public class CastingShort {

    @Expose
    private String directors;
    @Expose
    private String actors;

    public String getDirectors() {
        return directors;
    }

    public void setDirectors(String directors) {
        this.directors = directors;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    @Override
    public String toString() {
        return "CastingShort{" +
                "directors='" + directors + '\'' +
                ", actors='" + actors + '\'' +
                '}';
    }
}
