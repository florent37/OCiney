package com.bdc.ociney.modele.Theater;

import com.google.gson.annotations.Expose;
import com.bdc.ociney.modele.ModelObject;

import java.util.ArrayList;
import java.util.List;

public class Scr {

    @Expose
    private String d;
    @Expose
    private List<ModelObject> t = new ArrayList<ModelObject>();

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public List<ModelObject> getT() {
        return t;
    }

    public void setT(List<ModelObject> t) {
        this.t = t;
    }

    @Override
    public String toString() {
        return "Scr{" +
                "d='" + d + '\'' +
                ", t=" + t +
                '}';
    }
}
