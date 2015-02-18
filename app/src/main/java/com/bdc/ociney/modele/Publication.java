package com.bdc.ociney.modele;


import com.google.gson.annotations.Expose;


public class Publication {

    @Expose
    private String dateStart;

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    @Override
    public String toString() {
        return "Publication{" +
                "dateStart='" + dateStart + '\'' +
                '}';
    }
}
