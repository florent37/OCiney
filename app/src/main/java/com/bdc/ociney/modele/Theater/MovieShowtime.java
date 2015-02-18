package com.bdc.ociney.modele.Theater;

import com.google.gson.annotations.Expose;
import com.bdc.ociney.modele.ModelObject;
import com.bdc.ociney.modele.Version;

import java.util.ArrayList;
import java.util.List;

public class MovieShowtime {

    @Expose
    private String preview;
    @Expose
    private String releaseWeek;
    @Expose
    private OnShow onShow;
    @Expose
    private Version version;
    @Expose
    private ModelObject screenFormat;
    @Expose
    private String display;
    @Expose
    private List<Scr> scr = new ArrayList<Scr>();

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getReleaseWeek() {
        return releaseWeek;
    }

    public void setReleaseWeek(String releaseWeek) {
        this.releaseWeek = releaseWeek;
    }

    public OnShow getOnShow() {
        return onShow;
    }

    public void setOnShow(OnShow onShow) {
        this.onShow = onShow;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public ModelObject getScreenFormat() {
        return screenFormat;
    }

    public void setScreenFormat(ModelObject screenFormat) {
        this.screenFormat = screenFormat;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public List<Scr> getScr() {
        return scr;
    }

    public void setScr(List<Scr> scr) {
        this.scr = scr;
    }

    @Override
    public String toString() {
        return "MovieShowtime{" +
                /*
                "preview='" + preview + '\'' +
                ", releaseWeek='" + releaseWeek + '\'' +
                ", onShow=" + onShow +
                ", version=" + version +
                ", screenFormat=" + screenFormat +
                ", display='" + display + '\'' +
                */
                ", scr=" + scr +
                '}';
    }
}
