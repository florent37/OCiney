package com.bdc.ociney.modele;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class Season {

    @Expose
    private Integer code;
    @Expose
    private Integer seasonNumber;
    @Expose
    private Integer episodeCount;
    @Expose
    private Integer yearStart;
    @Expose
    private Integer yearEnd;
    @Expose
    private List<Link> link = new ArrayList<Link>();

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(Integer seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public Integer getEpisodeCount() {
        return episodeCount;
    }

    public void setEpisodeCount(Integer episodeCount) {
        this.episodeCount = episodeCount;
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

    public List<Link> getLink() {
        return link;
    }

    public void setLink(List<Link> link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "Season{" +
                "code=" + code +
                ", seasonNumber=" + seasonNumber +
                ", episodeCount=" + episodeCount +
                ", yearStart=" + yearStart +
                ", yearEnd=" + yearEnd +
                ", link=" + link +
                '}';
    }
}
