package com.bdc.ociney.modele;

import com.google.gson.annotations.Expose;

public class NewsSource {

    @Expose
    private Integer code;
    @Expose
    private String name;
    @Expose
    private String href;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @Override
    public String toString() {
        return "NewsSource{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", href='" + href + '\'' +
                '}';
    }
}
