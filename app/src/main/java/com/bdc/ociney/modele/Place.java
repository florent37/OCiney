package com.bdc.ociney.modele;

import com.google.gson.annotations.Expose;
import com.bdc.ociney.modele.Theater.Theater;

public class Place {

    @Expose
    private Theater theater;

    public Theater getTheater() {
        return theater;
    }

    public void setTheater(Theater theater) {
        this.theater = theater;
    }

    @Override
    public String toString() {
        return "Place{" +
                "theater=" + theater +
                '}';
    }
}
