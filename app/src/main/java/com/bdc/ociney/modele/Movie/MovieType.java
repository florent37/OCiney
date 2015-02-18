package com.bdc.ociney.modele.Movie;

import com.google.gson.annotations.Expose;

public class MovieType {

    @Expose
    private DefaultMedia defaultMedia;

    public DefaultMedia getDefaultMedia() {
        return defaultMedia;
    }

    public void setDefaultMedia(DefaultMedia defaultMedia) {
        this.defaultMedia = defaultMedia;
    }

    @Override
    public String toString() {
        return "MovieType{" +
                "defaultMedia=" + defaultMedia +
                '}';
    }
}