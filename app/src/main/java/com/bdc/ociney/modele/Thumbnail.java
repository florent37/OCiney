package com.bdc.ociney.modele;

import com.google.gson.annotations.Expose;

/**
 * Created by florentchampigny on 01/05/2014.
 */
public class Thumbnail {
    @Expose
    private String href;

    public String getHref(int height) {
        return href+"/r_10000_"+height;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @Override
    public String toString() {
        return "Thumbnail{" +
                "href='" + href + '\'' +
                '}';
    }
}
