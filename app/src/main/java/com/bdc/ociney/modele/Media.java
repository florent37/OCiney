package com.bdc.ociney.modele;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Media {

    @SerializedName("class")
    @Expose
    private String _class;
    @Expose
    private Integer code;


    @Expose
    private String title;


    @Expose
    private Thumbnail thumbnail;


    @Expose
    private List<Rendition> rendition = new ArrayList<Rendition>();

    @Expose
    private ModelObject type;
    @Expose
    private Version version;
    @Expose
    private Integer runtime;
    @Expose
    private Statistics statistics;
    @Expose
    private List<Link> link = new ArrayList<Link>();

    @Expose
    private List<Subject> subject = new ArrayList<Subject>();

    public String get_class() {
        return _class;
    }

    public void set_class(String _class) {
        this._class = _class;
    }


    public String getClass_() {
        return _class;
    }

    public void setClass_(String _class) {
        this._class = _class;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }
    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public List<Rendition> getRendition() {
        return rendition;
    }

    public void setRendition(List<Rendition> rendition) {
        this.rendition = rendition;
    }

    public ModelObject getType() {
        return type;
    }

    public void setType(ModelObject type) {
        this.type = type;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public List<Link> getLink() {
        return link;
    }

    public void setLink(List<Link> link) {
        this.link = link;
    }



    public List<Subject> getSubject() {
        return subject;
    }

    public void setSubject(List<Subject> subject) {
        this.subject = subject;
    }
    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Media{" +
                "_class='" + _class + '\'' +
                ", code=" + code +
                ", title='" + title + '\'' +
                ", thumbnail=" + thumbnail +
                '}';
    }
}