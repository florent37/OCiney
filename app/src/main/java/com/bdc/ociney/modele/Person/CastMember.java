package com.bdc.ociney.modele.Person;

import com.google.gson.annotations.Expose;
import com.bdc.ociney.modele.ModelObject;
import com.bdc.ociney.modele.Picture;

public class CastMember {

    @Expose
    private PersonSmall person;
    @Expose
    private ModelObject activity;
    @Expose
    private Picture picture;
    @Expose
    private String role;

    public PersonSmall getPerson() {
        return person;
    }

    public void setPerson(PersonSmall person) {
        this.person = person;
    }

    public ModelObject getActivity() {
        return activity;
    }

    public void setActivity(ModelObject activity) {
        this.activity = activity;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "CastMember{" +
                "person=" + person +
                ", activity=" + activity +
                ", picture=" + picture +
                ", role='" + role + '\'' +
                '}';
    }
}
