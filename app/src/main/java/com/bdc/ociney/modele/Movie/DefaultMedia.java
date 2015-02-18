package com.bdc.ociney.modele.Movie;

import com.google.gson.annotations.Expose;
import com.bdc.ociney.modele.Media;

public class DefaultMedia {

    @Expose
    private Media media;

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    @Override
    public String toString() {
        return "DefaultMedia{" +
                "media=" + media +
                '}';
    }
}