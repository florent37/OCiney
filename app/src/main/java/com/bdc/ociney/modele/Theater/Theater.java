package com.bdc.ociney.modele.Theater;

import com.google.gson.annotations.Expose;
import com.bdc.ociney.modele.Geoloc;
import com.bdc.ociney.modele.Link;
import com.bdc.ociney.modele.ModelObject;
import com.bdc.ociney.modele.Picture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Theater {

    public List<TheaterShowtime> showTimes;
    private List<Horaires> horaires = new ArrayList<Horaires>();
    private Map<String, List<Horaires>> horairesSemaine = new HashMap<String, List<Horaires>>();
    private List<String> horairesSemaineJours = new ArrayList<String>();
    @Expose
    private String code;
    @Expose
    private Double distance;
    @Expose
    private String name;
    @Expose
    private String address;
    @Expose
    private String postalCode;
    @Expose
    private String city;
    @Expose
    private Picture picture;
    @Expose
    private Geoloc geoloc;
    @Expose
    private List<Link> link = new ArrayList<Link>();
    @Expose
    private ModelObject cinemaChain;
    @Expose
    private Integer screenCount;
    @Expose
    private Integer hasPRMAccess;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public Geoloc getGeoloc() {
        return geoloc;
    }

    public void setGeoloc(Geoloc geoloc) {
        this.geoloc = geoloc;
    }

    public List<Link> getLink() {
        return link;
    }

    public void setLink(List<Link> link) {
        this.link = link;
    }

    public ModelObject getCinemaChain() {
        return cinemaChain;
    }

    public void setCinemaChain(ModelObject cinemaChain) {
        this.cinemaChain = cinemaChain;
    }

    public Integer getScreenCount() {
        return screenCount;
    }

    public void setScreenCount(Integer screenCount) {
        this.screenCount = screenCount;
    }

    public Integer getHasPRMAccess() {
        return hasPRMAccess;
    }

    public void setHasPRMAccess(Integer hasPRMAccess) {
        this.hasPRMAccess = hasPRMAccess;
    }

    public List<Horaires> getHorraires() {
        return horaires;
    }

    public void setHorraires(List<Horaires> horraires) {
        this.horaires = horraires;
    }

    public Map<String, List<Horaires>> getHorairesSemaine() {
        return horairesSemaine;
    }

    public void setHorairesSemaine(Map<String, List<Horaires>> horairesSemaine) {
        this.horairesSemaine = horairesSemaine;
    }

    public List<Horaires> getHoraires() {
        return horaires;
    }

    public void setHoraires(List<Horaires> horaires) {
        this.horaires = horaires;
    }

    public List<String> getHorairesSemaineJours() {
        return horairesSemaineJours;
    }

    public void setHorairesSemaineJours(List<String> horairesSemaineJours) {
        this.horairesSemaineJours = horairesSemaineJours;
    }

    public void getShowTimes(String idMovie) {

        getHorairesSemaine().clear();
        getHorairesSemaineJours().clear();

        for (TheaterShowtime t : showTimes) {
            if (t.getMovieShowtimes() != null)
                for (MovieShowtime mst : t.getMovieShowtimes()) {

                    boolean avantPremiere = mst.getPreview().equals("true");
                    String formatEcran = null;
                    if (mst.getScreenFormat() != null)
                        formatEcran = mst.getScreenFormat().get$();
                    String version = mst.getVersion().get$();
                    String display = mst.getDisplay();

                    if (mst.getOnShow().getMovie().getCode().toString().equals(idMovie)) {

                        if (mst.getDisplay() != null && !mst.getDisplay().isEmpty()) {
                            String[] joursHorraires = mst.getDisplay().split("Séances du");

                            for (int i = 0; i < joursHorraires.length; i++) {

                                if (joursHorraires != null && joursHorraires.length > 0) {

                                    try {
                                        String[] jrs = joursHorraires[i].split(" : ");

                                        if (jrs.length > 1) {
                                            String jour = jrs[0].trim();
                                            String hrs = jrs[1].trim();

                                            Horaires horaires = new Horaires();
                                            horaires.setAvantPremier(avantPremiere);
                                            horaires.setDisplay(mst.getDisplay());
                                            if (mst.getScreenFormat() != null)
                                                horaires.setFormatEcran(mst.getScreenFormat().get$());
                                            horaires.setVersion(version);
                                            horaires.setDate(jour);

                                            if (horaires.isMoreThanToday()) {
                                                String[] horairesLignes = hrs.split(",");
                                                for (int j = 0; j < horairesLignes.length; ++j) {
                                                    String horraire = horairesLignes[j].trim().split(" ")[0].trim();
                                                    horaires.getSeances().add(horraire);
                                                }

                                                if (this.getHorairesSemaine().containsKey(jour)) {
                                                    this.getHorairesSemaine().get(jour).add(horaires);
                                                } else {
                                                    ArrayList<Horaires> horairesDuJour = new ArrayList<Horaires>();
                                                    horairesDuJour.add(horaires);

                                                    this.getHorairesSemaineJours().add(jour);

                                                    this.getHorairesSemaine().put(jour, horairesDuJour);
                                                }
                                            }
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                            }

                        }
                    }
                }
        }
    }

    @Override
    public String toString() {
        return "Theater{" +
                "code='" + code + '\'' +
                ", distance=" + distance +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", city='" + city + '\'' +
                ", picture=" + picture +
                ", geoloc=" + geoloc +
                ", link=" + link +
                ", cinemaChain=" + cinemaChain +
                ", screenCount=" + screenCount +
                ", hasPRMAccess=" + hasPRMAccess +
                '}';
    }
}
