package com.bdc.ociney.modele.Movie;

import com.google.gson.annotations.Expose;
import com.bdc.ociney.modele.Feature;
import com.bdc.ociney.modele.Link;
import com.bdc.ociney.modele.Media;
import com.bdc.ociney.modele.ModelObject;
import com.bdc.ociney.modele.News;
import com.bdc.ociney.modele.Person.CastMember;
import com.bdc.ociney.modele.Person.CastingShort;
import com.bdc.ociney.modele.Release;
import com.bdc.ociney.modele.Statistics;
import com.bdc.ociney.modele.Theater.Horaires;
import com.bdc.ociney.modele.Trivium;

import java.util.ArrayList;
import java.util.List;

public class Movie implements Comparable {

    List<Horaires> horaires = new ArrayList<Horaires>();
    List<Media> bandesAnnonces = new ArrayList<Media>();

    String duree = null;
    String genres = null;
    @Expose
    private Integer code;
    @Expose
    private String originalTitle;
    @Expose
    private MovieType movieType;
    @Expose
    private String title;
    @Expose
    private Integer productionYear;
    @Expose
    private List<ModelObject> nationality = new ArrayList<ModelObject>();
    @Expose
    private List<ModelObject> genre = new ArrayList<ModelObject>();
    @Expose
    private Release release;
    @Expose
    private ModelObject color;
    @Expose
    private List<ModelObject> language = new ArrayList<ModelObject>();
    @Expose
    private CastingShort castingShort;
    @Expose
    private Trailer trailer;
    @Expose
    private String trailerEmbed;
    @Expose
    private List<Link> link = new ArrayList<Link>();
    @Expose
    private Statistics statistics;
    @Expose
    private String synopsis;
    @Expose
    private String synopsisShort;
    @Expose
    private String keywords;
    @Expose
    private Integer runtime;
    @Expose
    private Poster poster;
    @Expose
    private List<CastMember> castMember = new ArrayList<CastMember>();
    @Expose
    private Integer hasBluRay;
    @Expose
    private Integer hasDVD;
    @Expose
    private Integer hasShowtime;
    @Expose
    private String dvdReleaseDate;
    @Expose
    private String bluRayReleaseDate;
    @Expose
    private List<News> news = new ArrayList<News>();
    @Expose
    private List<Feature> feature = new ArrayList<Feature>();
    @Expose
    private List<Trivium> trivia = new ArrayList<Trivium>();
    @Expose
    private List<HelpfulPositiveReview> helpfulPositiveReview = new ArrayList<HelpfulPositiveReview>();
    @Expose
    private List<HelpfulNegativeReview> helpfulNegativeReview = new ArrayList<HelpfulNegativeReview>();

    @Expose
    private DefaultMedia defaultMedia;
    @Expose
    private List<Media> media = new ArrayList<Media>();

    /**
     * Retourne les horraires, rangés selon la façon suivante :
     * Les horraires sont rangés par
     */
    public List<Horaires> getHoraires(String date) {
        List<Horaires> hs = new ArrayList<Horaires>();
        for (Horaires h : horaires)
            if (h.getDate().equals(date))
                hs.add(h);
        return hs;
    }

    public boolean sameTitleAndOrignlalTitle() {
        if (title != null)
            return title.equals(originalTitle);
        else
            return false;
    }

    public String getDuree() {
        if (duree == null) {
            if (getRuntime() != null) {
                int heures = (int) (getRuntime() / 3600);
                int minutes = (getRuntime() / 60 - heures * 60);
                duree = heures + "h";

                if (minutes < 10)
                    duree += "0";
                duree += minutes + "min";
            } else
                duree = "";
        }
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public String getGenres() {
        if (genres == null) {
            StringBuffer sb = new StringBuffer();
            int size = genre.size();
            if (size > 3) //que les 3 premiers
                size = 3;
            for (int i = 0; i < size; ++i) {
                sb.append(genre.get(i).get$());
                if (i < size - 1)
                    sb.append(" / ");
            }
            this.genres = sb.toString();
        }
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getUrlPoster(int hauteur) {
        if (poster != null && poster.getHref() != null && poster.getHref().length() > 0)
            return poster.getHref() + "/r_10000_" + hauteur;
        else
            return null;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Movie) {
            Movie lui = (Movie) o;
            Float maNote = (getUserRating() + getPressRating()) / 2;
            Float saNote = (lui.getUserRating() + lui.getPressRating()) / 2;

            return saNote.compareTo(maNote);
        }
        return 0;
    }

    public float getUserRating() {
        float userRating = 0;
        if (statistics != null && statistics.getUserRating() != null) {
            userRating = statistics.getUserRating().floatValue();
        }
        return userRating;
    }

    public float getPressRating() {
        float pressRating = 0;
        if (statistics != null && statistics.getPressRating() != null) {
            pressRating = statistics.getPressRating().floatValue();
        }
        return pressRating;
    }

    public String getUrlBandeAnnonce(int height) {

        if (defaultMedia != null
                && defaultMedia.getMedia() != null
                && defaultMedia.getMedia().getThumbnail() != null
                && defaultMedia.getMedia().getThumbnail().getHref(0) != null)
            return defaultMedia.getMedia().getThumbnail().getHref(height);

        return null;
    }

    public List<String> getUrlImages(int height) {
        List<String> urlImages = new ArrayList<String>();
        for (Media m : media) {
            if (m.getThumbnail() != null && m.getThumbnail().getHref(height) != null)
                urlImages.add(m.getThumbnail().getHref(height));
        }
        return urlImages;
    }

    public CastMember getRealisateur() {
        for (CastMember m : castMember) {
            try {
                if (m.getActivity().get$().contains("Réalisateur"))
                    return m;
            } catch (Exception e) {
            }
        }
        return null;
    }

    public boolean is2D() {
        return true;
    }

    public boolean is3D() {
        if (getRelease() != null
                && getRelease().getReleaseVersion() != null
                && getRelease().getReleaseVersion().get$().contains("3D"))
            return true;
        return false;
    }

    public boolean isVF() {
        return true;
    }

    public boolean isVO() {
        if (getLanguage() != null) {
            for (ModelObject lanquage : getLanguage()) {
                if (!"Français".equals(lanquage.get$()))
                    return true;
            }

        }
        return false;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "horaires=" + horaires +
                ", duree='" + duree + '\'' +
                ", genres='" + genres + '\'' +
                ", code=" + code +
                ", originalTitle='" + originalTitle + '\'' +
                ", movieType=" + movieType +
                ", title='" + title + '\'' +
                ", productionYear=" + productionYear +
                ", nationality=" + nationality +
                ", genre=" + genre +
                ", release=" + release +
                ", color=" + color +
                ", language=" + language +
                ", castingShort=" + castingShort +
                ", trailer=" + trailer +
                ", trailerEmbed='" + trailerEmbed + '\'' +
                ", link=" + link +
                ", statistics=" + statistics +
                ", synopsis='" + synopsis + '\'' +
                ", synopsisShort='" + synopsisShort + '\'' +
                ", keywords='" + keywords + '\'' +
                ", runtime=" + runtime +
                ", poster=" + poster +
                ", castMember=" + castMember +
                ", hasBluRay=" + hasBluRay +
                ", hasDVD=" + hasDVD +
                ", hasShowtime=" + hasShowtime +
                ", dvdReleaseDate='" + dvdReleaseDate + '\'' +
                ", bluRayReleaseDate='" + bluRayReleaseDate + '\'' +
                ", news=" + news +
                ", feature=" + feature +
                ", trivia=" + trivia +
                ", helpfulPositiveReview=" + helpfulPositiveReview +
                ", helpfulNegativeReview=" + helpfulNegativeReview +
                ", defaultMedia=" + defaultMedia +
                ", media=" + media +
                '}';
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getTitle() {
        if (title == null)
            return getOriginalTitle();
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(Integer productionYear) {
        this.productionYear = productionYear;
    }

    public List<ModelObject> getNationality() {
        return nationality;
    }

    public void setNationality(List<ModelObject> nationality) {
        this.nationality = nationality;
    }

    public List<ModelObject> getGenre() {
        return genre;
    }

    public void setGenre(List<ModelObject> genre) {
        this.genre = genre;
    }

    public Release getRelease() {
        return release;
    }

    public void setRelease(Release release) {
        this.release = release;
    }

    public ModelObject getColor() {
        return color;
    }

    public void setColor(ModelObject color) {
        this.color = color;
    }

    public List<ModelObject> getLanguage() {
        return language;
    }

    public void setLanguage(List<ModelObject> language) {
        this.language = language;
    }

    public CastingShort getCastingShort() {
        return castingShort;
    }

    public void setCastingShort(CastingShort castingShort) {
        this.castingShort = castingShort;
    }

    public Trailer getTrailer() {
        return trailer;
    }

    public void setTrailer(Trailer trailer) {
        this.trailer = trailer;
    }

    public String getTrailerEmbed() {
        return trailerEmbed;
    }

    public void setTrailerEmbed(String trailerEmbed) {
        this.trailerEmbed = trailerEmbed;
    }

    public List<Link> getLink() {
        return link;
    }

    public void setLink(List<Link> link) {
        this.link = link;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public String getSynopsisShort() {
        String s = synopsisShort;
        if (s != null) {
            s = s.replace("<br>", "\n").replace("<br/>", "\n").replaceAll("\\<[^>]*>", "");
        }
        return s;
    }

    public void setSynopsisShort(String synopsisShort) {
        this.synopsisShort = synopsisShort;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public Poster getPoster() {
        return poster;
    }

    public void setPoster(Poster poster) {
        this.poster = poster;
    }

    public String getSynopsis() {
        String s = synopsis;
        if (s != null) {
            s = s.replace("<br>", "\n").replace("<br/>", "\n").replaceAll("\\<[^>]*>", "");
        }
        return s;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public List<CastMember> getCastMember(int number) {
        List<CastMember> cast = new ArrayList<CastMember>();
        int i = 0;
        for (CastMember m : castMember) {
            if (!m.getActivity().get$().contains("Réalisateur")) {
                cast.add(m);
                i++;
            }
            if (i == number)
                break;
        }
        return cast;
    }

    public List<CastMember> getAllCastMember() {
        return castMember;
    }

    public Integer getHasBluRay() {
        return hasBluRay;
    }

    public void setHasBluRay(Integer hasBluRay) {
        this.hasBluRay = hasBluRay;
    }

    public Integer getHasDVD() {
        return hasDVD;
    }

    public void setHasDVD(Integer hasDVD) {
        this.hasDVD = hasDVD;
    }

    public Integer getHasShowtime() {
        return hasShowtime;
    }

    public void setHasShowtime(Integer hasShowtime) {
        this.hasShowtime = hasShowtime;
    }

    public String getDvdReleaseDate() {
        return dvdReleaseDate;
    }

    public void setDvdReleaseDate(String dvdReleaseDate) {
        this.dvdReleaseDate = dvdReleaseDate;
    }

    public String getBluRayReleaseDate() {
        return bluRayReleaseDate;
    }

    public void setBluRayReleaseDate(String bluRayReleaseDate) {
        this.bluRayReleaseDate = bluRayReleaseDate;
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

    public List<Trivium> getTrivia() {
        return trivia;
    }

    public void setTrivia(List<Trivium> trivia) {
        this.trivia = trivia;
    }

    public List<HelpfulPositiveReview> getHelpfulPositiveReview() {
        return helpfulPositiveReview;
    }

    public void setHelpfulPositiveReview(List<HelpfulPositiveReview> helpfulPositiveReview) {
        this.helpfulPositiveReview = helpfulPositiveReview;
    }

    public List<HelpfulNegativeReview> getHelpfulNegativeReview() {
        return helpfulNegativeReview;
    }

    public void setHelpfulNegativeReview(List<HelpfulNegativeReview> helpfulNegativeReview) {
        this.helpfulNegativeReview = helpfulNegativeReview;
    }

    public MovieType getMovieType() {
        return movieType;
    }

    public void setMovieType(MovieType movieType) {
        this.movieType = movieType;
    }

    public List<Media> getBandesAnnonces() {
        return bandesAnnonces;
    }

    public void setBandesAnnonces(List<Media> bandesAnnonces) {
        this.bandesAnnonces = bandesAnnonces;
    }

    public List<CastMember> getCastMember() {
        return castMember;
    }

    public void setCastMember(List<CastMember> castMember) {
        this.castMember = castMember;
    }

    public List<Media> getMedia() {
        return media;
    }

    public void setMedia(List<Media> media) {
        this.media = media;
    }

    public DefaultMedia getDefaultMedia() {
        return defaultMedia;
    }

    public void setDefaultMedia(DefaultMedia defaultMedia) {
        this.defaultMedia = defaultMedia;
    }

    public List<Horaires> getHoraires() {
        return horaires;
    }

    public void setHoraires(List<Horaires> horaires) {
        this.horaires = horaires;
    }


}
