package com.bdc.ociney.modele;

import com.google.gson.annotations.Expose;

public class ModelObject {

    @Expose
    private Integer code;
    @Expose
    private String value;
    @Expose
    private String $;
    @Expose
    private Integer p;
    @Expose
    private Double note;
    @Expose
    private String type;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String get$() {
        return $;
    }

    public void set$(String $) {
        this.$ = $;
    }

    public Integer getP() {
        return p;
    }

    public void setP(Integer p) {
        this.p = p;
    }

    public Double getNote() {
        return note;
    }

    public void setNote(Double note) {
        this.note = note;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setScheme(String scheme) {
        this.value = scheme;
    }

    @Override
    public String toString() {
        return "ModelObject{" +
                "code=" + code +
                ", $= " + $ +
                ", value='" + value + '\'' +
                ", p=" + p +
                ", type= " + type +
                ", note=" + note +
                '}';
    }
}
