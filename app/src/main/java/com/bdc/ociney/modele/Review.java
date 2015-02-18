package com.bdc.ociney.modele;

import com.google.gson.annotations.Expose;

import javax.security.auth.Subject;

public class Review {

    @Expose
    private Integer code;
    @Expose
    private String creationDate;
    @Expose
    private ModelObject type;
    @Expose
    private Subject subject;
    @Expose
    private NewsSource newsSource;
    @Expose
    private String author;
    @Expose
    private Link reviewUrl;
    @Expose
    private String body;
    @Expose
    private Double rating;
    @Expose
    private Writer writer;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public ModelObject getType() {
        return type;
    }

    public void setType(ModelObject type) {
        this.type = type;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public NewsSource getNewsSource() {
        return newsSource;
    }

    public void setNewsSource(NewsSource newsSource) {
        this.newsSource = newsSource;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Link getReviewUrl() {
        return reviewUrl;
    }

    public void setReviewUrl(Link reviewUrl) {
        this.reviewUrl = reviewUrl;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Writer getWriter() {
        return writer;
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
    }


    @Override
    public String toString() {
        return "Review{" +
                "code=" + code +
                ", creationDate='" + creationDate + '\'' +
                ", type=" + type +
                ", subject=" + subject +
                ", newsSource=" + newsSource +
                ", author='" + author + '\'' +
                ", reviewUrl=" + reviewUrl +
                ", body='" + body + '\'' +
                ", rating=" + rating +
                ", writer=" + writer +
                '}';
    }
}
