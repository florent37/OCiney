package com.bdc.ociney.modele.Person;

import com.google.gson.annotations.Expose;

public class Name {

    @Expose
    private String given;
    @Expose
    private String family;

    public String getGiven() {
        return given;
    }

    public void setGiven(String given) {
        this.given = given;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    @Override
    public String toString() {
        return "Name{" +
                "given='" + given + '\'' +
                ", family='" + family + '\'' +
                '}';
    }
}
