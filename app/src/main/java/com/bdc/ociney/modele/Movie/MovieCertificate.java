package com.bdc.ociney.modele.Movie;

import com.google.gson.annotations.Expose;
import com.bdc.ociney.modele.ModelObject;

public class MovieCertificate {

    @Expose
    private ModelObject certificate;

    public ModelObject getCertificate() {
        return certificate;
    }

    public void setCertificate(ModelObject certificate) {
        this.certificate = certificate;
    }

    @Override
    public String toString() {
        return "MovieCertificate{" +
                "certificate=" + certificate +
                '}';
    }
}
