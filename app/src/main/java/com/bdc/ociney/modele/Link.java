package com.bdc.ociney.modele;

import com.google.gson.annotations.Expose;

public class Link {

    @Expose
    private String rel;
    @Expose
    private String href;
    @Expose
    private String name;

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Link{" +
                "rel='" + rel + '\'' +
                ", href='" + href + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
