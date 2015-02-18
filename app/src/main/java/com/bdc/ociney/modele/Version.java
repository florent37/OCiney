package com.bdc.ociney.modele;

import com.google.gson.annotations.Expose;

public class Version {

    @Expose
    private String original;
    @Expose
    private Integer code;
    @Expose
    private Integer lang;
    @Expose
    private String $;

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getLang() {
        return lang;
    }

    public void setLang(Integer lang) {
        this.lang = lang;
    }

    public String get$() {
        return $;
    }

    public void set$(String $) {
        this.$ = $;
    }

    @Override
    public String toString() {
        return "Version{" +
                "original='" + original + '\'' +
                ", code=" + code +
                ", lang=" + lang +
                ", $='" + $ + '\'' +
                '}';
    }
}
