package com.bdc.ociney.modele;

import com.google.gson.annotations.Expose;
import com.bdc.ociney.modele.Movie.Distributor;

public class Release {

    @Expose
    private String releaseDate;
    @Expose
    private ModelObject country;
    @Expose
    private ModelObject releaseState;
    @Expose
    private ModelObject releaseVersion;
    @Expose
    private Distributor distributor;

    public String getReleaseDate() {
        try {
            String[] releaseCut = releaseDate.split("-");

            return releaseCut[2] + "/" + releaseCut[1] + "/" + releaseCut[0];
        }catch (Exception e){
            return releaseDate;
        }
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public ModelObject getCountry() {
        return country;
    }

    public void setCountry(ModelObject country) {
        this.country = country;
    }

    public ModelObject getReleaseState() {
        return releaseState;
    }

    public void setReleaseState(ModelObject releaseState) {
        this.releaseState = releaseState;
    }

    public ModelObject getReleaseVersion() {
        return releaseVersion;
    }

    public void setReleaseVersion(ModelObject releaseVersion) {
        this.releaseVersion = releaseVersion;
    }

    public Distributor getDistributor() {
        return distributor;
    }

    public void setDistributor(Distributor distributor) {
        this.distributor = distributor;
    }

    @Override
    public String toString() {
        return "Release{" +
                "releaseDate='" + releaseDate + '\'' +
                ", country=" + country +
                ", releaseState=" + releaseState +
                ", releaseVersion=" + releaseVersion +
                ", distributor=" + distributor +
                '}';
    }
}
