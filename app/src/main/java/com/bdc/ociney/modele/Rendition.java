package com.bdc.ociney.modele;

import com.google.gson.annotations.Expose;

/**
 * Created by florentchampigny on 01/05/2014.
 */
public class Rendition {

    @Expose
    private Integer code;
    @Expose
    private String href;
    @Expose
    private Publication publication;
    @Expose
    private ModelObject bandwidth; //FORMAT !
    @Expose
    private ModelObject format;
    @Expose
    private ModelObject encodingProfile;
    @Expose
    private Integer width;
    @Expose
    private Integer height;
    @Expose
    private Integer size;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }

    public ModelObject getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(ModelObject bandwidth) {
        this.bandwidth = bandwidth;
    }

    public ModelObject getFormat() {
        return format;
    }

    public void setFormat(ModelObject format) {
        this.format = format;
    }

    public ModelObject getEncodingProfile() {
        return encodingProfile;
    }

    public void setEncodingProfile(ModelObject encodingProfile) {
        this.encodingProfile = encodingProfile;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

}
