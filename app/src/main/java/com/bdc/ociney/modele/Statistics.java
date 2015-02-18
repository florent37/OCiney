package com.bdc.ociney.modele;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class Statistics {

    @Expose
    private Integer viewCount;


    @Expose
    private Double userRating;
    @Expose
    private Integer userReviewCount;
    @Expose
    private Double pressRating;
    @Expose
    private Integer pressReviewCount;
    @Expose
    private Integer userRatingCount;
    @Expose
    private Integer movieCount;
    @Expose
    private Integer movieActorCount;
    @Expose
    private Integer movieDirectorCount;
    @Expose
    private Integer movieProducerCount;
    @Expose
    private Integer seriesCount;
    @Expose
    private Integer seriesActorCount;
    @Expose
    private Integer seriesDirectorCount;
    @Expose
    private Integer seriesProducerCount;
    @Expose
    private Integer rankTopStar;
    @Expose
    private Integer variationTopStar;
    @Expose
    private Integer careerDuration;
    @Expose
    private Integer editorialRatingCount;
    @Expose
    private Integer commentCount;
    @Expose
    private Integer photoCount;
    @Expose
    private Integer videoCount;
    @Expose
    private Integer triviaCount;
    @Expose
    private Integer newsCount;
    @Expose
    private Integer rankTopMovie;
    @Expose
    private Integer variationTopMovie;
    @Expose
    private Integer awardCount;
    @Expose
    private Integer nominationCount;
    @Expose
    private Integer fanCount;
    @Expose
    private Integer wantToSeeCount;
    @Expose
    private Integer releaseWeekPosition;
    @Expose
    private Integer theaterCount;
    @Expose
    private Integer theaterCountOnRelease;

    @Expose
    private List<ModelObject> rating = new ArrayList<ModelObject>();

    public Double getPressRating() {
        return pressRating;
    }

    public void setPressRating(Double pressRating) {
        this.pressRating = pressRating;
    }

    public Integer getPressReviewCount() {
        return pressReviewCount;
    }

    public void setPressReviewCount(Integer pressReviewCount) {
        this.pressReviewCount = pressReviewCount;
    }

    public Double getUserRating() {
        return userRating;
    }

    public void setUserRating(Double userRating) {
        this.userRating = userRating;
    }

    public Integer getUserReviewCount() {
        return userReviewCount;
    }

    public void setUserReviewCount(Integer userReviewCount) {
        this.userReviewCount = userReviewCount;
    }

    public Integer getFanCount() {
        return fanCount;
    }

    public void setFanCount(Integer fanCount) {
        this.fanCount = fanCount;
    }

    public Integer getWantToSeeCount() {
        return wantToSeeCount;
    }

    public void setWantToSeeCount(Integer wantToSeeCount) {
        this.wantToSeeCount = wantToSeeCount;
    }

    public Integer getReleaseWeekPosition() {
        return releaseWeekPosition;
    }

    public void setReleaseWeekPosition(Integer releaseWeekPosition) {
        this.releaseWeekPosition = releaseWeekPosition;
    }

    public Integer getRankTopMovie() {
        return rankTopMovie;
    }

    public void setRankTopMovie(Integer rankTopMovie) {
        this.rankTopMovie = rankTopMovie;
    }

    public Integer getVariationTopMovie() {
        return variationTopMovie;
    }

    public void setVariationTopMovie(Integer variationTopMovie) {
        this.variationTopMovie = variationTopMovie;
    }

    public Integer getUserRatingCount() {
        return userRatingCount;
    }

    public void setUserRatingCount(Integer userRatingCount) {
        this.userRatingCount = userRatingCount;
    }

    public Integer getEditorialRatingCount() {
        return editorialRatingCount;
    }

    public void setEditorialRatingCount(Integer editorialRatingCount) {
        this.editorialRatingCount = editorialRatingCount;
    }

    public Integer getMovieCount() {
        return movieCount;
    }

    public void setMovieCount(Integer movieCount) {
        this.movieCount = movieCount;
    }

    public Integer getMovieActorCount() {
        return movieActorCount;
    }

    public void setMovieActorCount(Integer movieActorCount) {
        this.movieActorCount = movieActorCount;
    }

    public Integer getMovieDirectorCount() {
        return movieDirectorCount;
    }

    public void setMovieDirectorCount(Integer movieDirectorCount) {
        this.movieDirectorCount = movieDirectorCount;
    }

    public Integer getMovieProducerCount() {
        return movieProducerCount;
    }

    public void setMovieProducerCount(Integer movieProducerCount) {
        this.movieProducerCount = movieProducerCount;
    }

    public Integer getSeriesCount() {
        return seriesCount;
    }

    public void setSeriesCount(Integer seriesCount) {
        this.seriesCount = seriesCount;
    }

    public Integer getSeriesActorCount() {
        return seriesActorCount;
    }

    public void setSeriesActorCount(Integer seriesActorCount) {
        this.seriesActorCount = seriesActorCount;
    }

    public Integer getSeriesDirectorCount() {
        return seriesDirectorCount;
    }

    public void setSeriesDirectorCount(Integer seriesDirectorCount) {
        this.seriesDirectorCount = seriesDirectorCount;
    }

    public Integer getSeriesProducerCount() {
        return seriesProducerCount;
    }

    public void setSeriesProducerCount(Integer seriesProducerCount) {
        this.seriesProducerCount = seriesProducerCount;
    }

    public Integer getNewsCount() {
        return newsCount;
    }

    public void setNewsCount(Integer newsCount) {
        this.newsCount = newsCount;
    }

    public Integer getNominationCount() {
        return nominationCount;
    }

    public void setNominationCount(Integer nominationCount) {
        this.nominationCount = nominationCount;
    }

    public Integer getAwardCount() {
        return awardCount;
    }

    public void setAwardCount(Integer awardCount) {
        this.awardCount = awardCount;
    }

    public Integer getRankTopStar() {
        return rankTopStar;
    }

    public void setRankTopStar(Integer rankTopStar) {
        this.rankTopStar = rankTopStar;
    }

    public Integer getVariationTopStar() {
        return variationTopStar;
    }

    public void setVariationTopStar(Integer variationTopStar) {
        this.variationTopStar = variationTopStar;
    }

    public Integer getCareerDuration() {
        return careerDuration;
    }

    public void setCareerDuration(Integer careerDuration) {
        this.careerDuration = careerDuration;
    }

    public List<ModelObject> getRating() {
        return rating;
    }

    public void setRating(List<ModelObject> rating) {
        this.rating = rating;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getPhotoCount() {
        return photoCount;
    }

    public void setPhotoCount(Integer photoCount) {
        this.photoCount = photoCount;
    }

    public Integer getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(Integer videoCount) {
        this.videoCount = videoCount;
    }

    public Integer getTriviaCount() {
        return triviaCount;
    }

    public void setTriviaCount(Integer triviaCount) {
        this.triviaCount = triviaCount;
    }

    public Integer getTheaterCount() {
        return theaterCount;
    }

    public void setTheaterCount(Integer theaterCount) {
        this.theaterCount = theaterCount;
    }

    public Integer getTheaterCountOnRelease() {
        return theaterCountOnRelease;
    }

    public void setTheaterCountOnRelease(Integer theaterCountOnRelease) {
        this.theaterCountOnRelease = theaterCountOnRelease;
    }
    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }


    @Override
    public String toString() {
        return "Statistics{" +
                "userRating=" + userRating +
                ", viewcount=" + viewCount +
                ", userReviewCount=" + userReviewCount +
                ", pressRating=" + pressRating +
                ", pressReviewCount=" + pressReviewCount +
                ", userRatingCount=" + userRatingCount +
                ", movieCount=" + movieCount +
                ", movieActorCount=" + movieActorCount +
                ", movieDirectorCount=" + movieDirectorCount +
                ", movieProducerCount=" + movieProducerCount +
                ", seriesCount=" + seriesCount +
                ", seriesActorCount=" + seriesActorCount +
                ", seriesDirectorCount=" + seriesDirectorCount +
                ", seriesProducerCount=" + seriesProducerCount +
                ", rankTopStar=" + rankTopStar +
                ", variationTopStar=" + variationTopStar +
                ", careerDuration=" + careerDuration +
                ", editorialRatingCount=" + editorialRatingCount +
                ", commentCount=" + commentCount +
                ", photoCount=" + photoCount +
                ", videoCount=" + videoCount +
                ", triviaCount=" + triviaCount +
                ", newsCount=" + newsCount +
                ", rankTopMovie=" + rankTopMovie +
                ", variationTopMovie=" + variationTopMovie +
                ", awardCount=" + awardCount +
                ", nominationCount=" + nominationCount +
                ", fanCount=" + fanCount +
                ", wantToSeeCount=" + wantToSeeCount +
                ", releaseWeekPosition=" + releaseWeekPosition +
                ", theaterCount=" + theaterCount +
                ", theaterCountOnRelease=" + theaterCountOnRelease +
                ", rating=" + rating +
                '}';
    }
}
