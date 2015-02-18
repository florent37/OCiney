package com.bdc.ociney.modele;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class Feature {

    @Expose
    private Integer code;
    @Expose
    private Publication publication;
    @Expose
    private String title;
    @Expose
    private Picture picture;
    @Expose
    private List<ModelObject> category = new ArrayList<ModelObject>();

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public List<ModelObject> getCategory() {
        return category;
    }

    public void setCategory(List<ModelObject> category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Feature{" +
                "code=" + code +
                ", publication=" + publication +
                ", title='" + title + '\'' +
                ", picture=" + picture +
                ", category=" + category +
                '}';
    }
}
