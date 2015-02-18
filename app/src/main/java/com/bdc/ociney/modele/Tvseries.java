package com.bdc.ociney.modele;

import com.google.gson.annotations.Expose;
import com.bdc.ociney.modele.Movie.Poster;
import com.bdc.ociney.modele.Person.CastMember;

import java.util.ArrayList;
import java.util.List;

public class Tvseries {

    @Expose
    private Integer code;
    @Expose
    private ModelObject seriesType;
    @Expose
    private String originalTitle;
    @Expose
    private String title;
    @Expose
    private Integer yearStart;
    @Expose
    private Integer yearEnd;
    @Expose
    private String synopsisShort;
    @Expose
    private List<CastMember> castMember = new ArrayList<CastMember>();
    @Expose
    private Poster poster;
    @Expose
    private List<Season> season = new ArrayList<Season>();

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public ModelObject getSeriesType() {
        return seriesType;
    }

    public void setSeriesType(ModelObject seriesType) {
        this.seriesType = seriesType;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYearStart() {
        return yearStart;
    }

    public void setYearStart(Integer yearStart) {
        this.yearStart = yearStart;
    }

    public Integer getYearEnd() {
        return yearEnd;
    }

    public void setYearEnd(Integer yearEnd) {
        this.yearEnd = yearEnd;
    }

    public String getSynopsisShort() {
        return synopsisShort;
    }

    public void setSynopsisShort(String synopsisShort) {
        this.synopsisShort = synopsisShort;
    }

    public List<CastMember> getCastMember() {
        return castMember;
    }

    public void setCastMember(List<CastMember> castMember) {
        this.castMember = castMember;
    }

    public Poster getPoster() {
        return poster;
    }

    public void setPoster(Poster poster) {
        this.poster = poster;
    }

    public List<Season> getSeason() {
        return season;
    }

    public void setSeason(List<Season> season) {
        this.season = season;
    }

    @Override
    public String toString() {
        return "Tvseries{" +
                "code=" + code +
                ", seriesType=" + seriesType +
                ", originalTitle='" + originalTitle + '\'' +
                ", title='" + title + '\'' +
                ", yearStart=" + yearStart +
                ", yearEnd=" + yearEnd +
                ", synopsisShort='" + synopsisShort + '\'' +
                ", castMember=" + castMember +
                ", poster=" + poster +
                ", season=" + season +
                '}';
    }
}
