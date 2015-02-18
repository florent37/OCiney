package com.bdc.ociney.modele.Person;

import com.google.gson.annotations.Expose;
import com.bdc.ociney.modele.Feature;
import com.bdc.ociney.modele.Link;
import com.bdc.ociney.modele.Media;
import com.bdc.ociney.modele.ModelObject;
import com.bdc.ociney.modele.News;
import com.bdc.ociney.modele.Participation;
import com.bdc.ociney.modele.Picture;
import com.bdc.ociney.modele.Statistics;

import java.util.ArrayList;
import java.util.List;

public class Person implements Comparable {

    @Expose
    private Integer code;
    @Expose
    private Integer gender;
    @Expose
    private List<ModelObject> nationality = new ArrayList<ModelObject>();

    @Expose
    private String activityShort;
    @Expose
    private String biographyShort;
    @Expose
    private String biography;
    @Expose
    private String birthDate;
    @Expose
    private String birthPlace;
    @Expose
    private Picture picture;
    @Expose
    private String trailerEmbed;
    @Expose
    private Integer hasTopFilmography;
    @Expose
    private List<Link> link = new ArrayList<Link>();
    @Expose
    private List<Participation> participation = new ArrayList<Participation>();
    @Expose
    private List<News> news = new ArrayList<News>();
    @Expose
    private List<Feature> feature = new ArrayList<Feature>();
    @Expose
    private Statistics statistics;
    @Expose
    private List<Media> media = new ArrayList<Media>();


    @Expose
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public List<ModelObject> getNationality() {
        return nationality;
    }

    public void setNationality(List<ModelObject> nationality) {
        this.nationality = nationality;
    }

    public String getActivityShort() {
        return activityShort;
    }

    public void setActivityShort(String activityShort) {
        this.activityShort = activityShort;
    }

    public String getBiographyShort() {
        return biographyShort;
    }

    public void setBiographyShort(String biographyShort) {
        this.biographyShort = biographyShort;
    }

    public String getBiography() {
        String s = biography;
        if (s != null) {
            s = s.replace("<br/> <p><span></span></p> <br/>","").replace("<br>", "\n").replace("<br/>", "\n").replace("<p>", "\n").replace("</p>", "").replaceAll("\\<[^>]*>", "");
        }
        return s;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public String getTrailerEmbed() {
        return trailerEmbed;
    }

    public void setTrailerEmbed(String trailerEmbed) {
        this.trailerEmbed = trailerEmbed;
    }

    public Integer getHasTopFilmography() {
        return hasTopFilmography;
    }

    public void setHasTopFilmography(Integer hasTopFilmography) {
        this.hasTopFilmography = hasTopFilmography;
    }

    public List<Link> getLink() {
        return link;
    }

    public void setLink(List<Link> link) {
        this.link = link;
    }

    public List<Participation> getParticipation() {
        return participation;
    }

    public void setParticipation(List<Participation> participation) {
        this.participation = participation;
    }

    public List<Participation> getParticipation(int number) {
        List<Participation> part = new ArrayList<Participation>();
        int i = 0;

        for (Participation p : participation) {
            part.add(p);
            i++;
            if (i == number)
                break;
        }
        return part;
    }

    public List<News> getNews() {
        return news;
    }

    public void setNews(List<News> news) {
        this.news = news;
    }

    public List<Feature> getFeature() {
        return feature;
    }

    public void setFeature(List<Feature> feature) {
        this.feature = feature;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public List<Media> getMedia() {
        return media;
    }

    public void setMedia(List<Media> media) {
        this.media = media;
    }

    @Override
    public String toString() {
        return "Person{" +
                "code=" + code +
                ", role=" + role +
                ", gender=" + gender +
                ", nationality=" + nationality +
                ", activityShort='" + activityShort + '\'' +
                ", biographyShort='" + biographyShort + '\'' +
                ", biography='" + biography + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", birthPlace='" + birthPlace + '\'' +
                ", picture=" + picture +
                ", trailerEmbed='" + trailerEmbed + '\'' +
                ", hasTopFilmography=" + hasTopFilmography +
                ", link=" + link +
                ", participation=" + participation +
                ", news=" + news +
                ", feature=" + feature +
                ", statistics=" + statistics +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Person) {
            Person lui = (Person) o;

            if (getStatistics() != null && lui.getStatistics() != null) {
                Integer maNote = getStatistics().getRankTopStar();
                Integer saNote = lui.getStatistics().getRankTopStar();
                return saNote.compareTo(maNote);
            } else {
                return 0;
            }
        }
        return 0;
    }
}
