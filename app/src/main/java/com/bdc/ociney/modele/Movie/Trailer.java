package com.bdc.ociney.modele.Movie;

import com.google.gson.annotations.Expose;

public class Trailer {

    @Expose
    private String name;
    @Expose
    private Integer code;
    @Expose
    private String href;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @Override
    public String toString() {
        return "Trailer{" +
                "name='" + name + '\'' +
                ", code=" + code +
                ", href='" + href + '\'' +
                '}';
    }
}
