package com.bdc.ociney.service;

import android.util.Log;

import com.bdc.ociney.modele.AllocineResponse;
import com.bdc.ociney.modele.AllocineResponseSmall;
import com.bdc.ociney.modele.Media;
import com.bdc.ociney.modele.Movie.Movie;
import com.bdc.ociney.modele.News;
import com.bdc.ociney.modele.Participation;
import com.bdc.ociney.modele.Person.PersonFull;
import com.bdc.ociney.modele.Review;
import com.bdc.ociney.modele.Theater.Theater;
import com.bdc.ociney.modele.Theater.TheaterShowtime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import retrofit.RestAdapter;

/**
 * Created by florentchampigny on 18/04/2014.
 */
public class Service {

    public static final String FILTER_MOVIE = "movie"; //afficher les films correspondant à la recherche
    public static final String FILTER_THEATER = "theater"; //afficher les cinémas
    public static final String FILTER_PERSON = "person"; //afficher les acteurs, réalisateurs, etc. (personnes)
    public static final String PERSONLIST_FILTER_PERSON = "top"; //filtre la liste des personnalité
    public static final String FILTER_NEWS = "news"; //afficher les news
    public static final String FILTER_TVSERIES = "tvseries"; //afficher les séries TV
    public static final String MOVIELIST_FILTER_COMINGSOON = "comingsoon"; //afficher les films à paraitre
    public static final String MOVIELIST_FILTER_NOWSHOWING = "nowshowing"; //afficher les films actuellement à l'affiche
    public static final String MOVIELIST_ORDER_DATEDESC = "datedesc"; //classement anti-chronologique
    public static final String MOVIELIST_ORDER_DATEASC = "dateasc"; //classement chronologique
    public static final String MOVIELIST_ORDER_THEATERCOUNT = "theatercount"; //classement par nombre de ic_drawer_salles
    public static final String MOVIELIST_ORDER_TOPRANK = "toprank"; //classement par popularité
    public static final String PROFILE_SMALL = "small";
    public static final String PROFILE_MEDIUM = "medium";
        public static final String PROFILE_LARGE = "large";
    public static final String REVIEW_LIST_FILTER_PRESSE = "desk-press";
    public static final String REVIEW_LIST_FILTER_PUBLIC = "public";

    public static String PROFIL_NEWS_CINEMA = "movie";
    public static String PROFIL_NEWS_SERIE = "tvseries";
    public static String PROFIL_NEWS_STAR = "star";

    public static AllocineService getService() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(AllocineService.URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new RestAdapter.Log() {
                    @Override
                    public void log(String message) {
                        Log.d("retrofit", message);
                    }
                })
                .build();

        AllocineService service = restAdapter.create(AllocineService.class);
        return service;
    }

    static String format(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(new Date());
    }

    ;

    /**
     * Recherche
     */
    public static AllocineResponseSmall searchSmall(String recherche, String filter, int count, int page) throws NetworkException {
        return searchSmall(recherche, Arrays.asList(filter), count, page);
    }

    //---------------------------------------------------------------------------------------------

    /**
     * Recherche
     */
    public static AllocineResponse search(String recherche, String filter, int count, int page) throws NetworkException {
        return search(recherche, Arrays.asList(filter), count, page);
    }

    /**
     * Recherche
     */
    public static AllocineResponse search(String recherche, List<String> filter, int count, int page) throws NetworkException {

        String params = ServiceSecurity.construireParams(false,
                AllocineService.Q, "" + recherche.replace(" ", "+"),
                AllocineService.FILTER, filter,
                AllocineService.COUNT, "" + count,
                AllocineService.PAGE, "" + page
        );

        String sed = ServiceSecurity.getSED();
        String sig = ServiceSecurity.getSIG(params, sed);

        try {
            return getService().search(recherche, ServiceSecurity.applatir(filter), count, page, sed, sig);

        } catch (Exception e) {
            throw new NetworkException();
        }
    }

    /**
     * Recherche
     */
    public static AllocineResponseSmall searchSmall(String recherche, List<String> filter, int count, int page) throws NetworkException {

        String params = ServiceSecurity.construireParams(false,
                AllocineService.Q, "" + recherche.replace(" ", "+"),
                AllocineService.FILTER, filter,
                AllocineService.COUNT, "" + count,
                AllocineService.PAGE, "" + page
        );

        String sed = ServiceSecurity.getSED();
        String sig = ServiceSecurity.getSIG(params, sed);

        try {
            return getService().searchSmall(recherche, ServiceSecurity.applatir(filter), count, page, sed, sig);

        } catch (Exception e) {
            throw new NetworkException();
        }
    }

    /**
     * Informations sur un film
     */
    public static Movie movie(String idFilm, String profile, String filter) throws NetworkException {
        String params = ServiceSecurity.construireParams(false,
                AllocineService.CODE, idFilm,
                AllocineService.PROFILE, profile,
                AllocineService.FILTER, filter
        );

        String sed = ServiceSecurity.getSED();
        String sig = ServiceSecurity.getSIG(params, sed);

        try {
            AllocineResponse response = getService().movie(idFilm, profile, filter, sed, sig);
            return response.getMovie();
        } catch (Exception e) {
            throw new NetworkException();
        }
    }
    //---------------------------------------------------------------------------------------------

    /**
     * Critiques sur un film (presse et public)
     */
    public static List<Review> reviewlist(String idFilm, String filter, int count, int page) throws NetworkException {

        String type = "movie";

        String params = ServiceSecurity.construireParams(false,
                AllocineService.CODE, idFilm,
                AllocineService.TYPE, type,
                AllocineService.FILTER, filter,
                AllocineService.COUNT, "" + count,
                AllocineService.PAGE, "" + page
        );

        String sed = ServiceSecurity.getSED();
        String sig = ServiceSecurity.getSIG(params, sed);

        try {
            AllocineResponse response = getService().reviewlist(idFilm, type, filter, count, page, sed, sig);
            return response.getFeed().getReview();
        } catch (Exception e) {
            throw new NetworkException();
        }
    }

    //---------------------------------------------------------------------------------------------

    /**
     * Informations sur un film
     */
    public static Theater theater(String idCinema, String profile, String filter) throws NetworkException {
        String params = ServiceSecurity.construireParams(false,
                AllocineService.CODE, idCinema,
                AllocineService.PROFILE, profile,
                AllocineService.FILTER, filter
        );

        String sed = ServiceSecurity.getSED();
        String sig = ServiceSecurity.getSIG(params, sed);

        try {
            AllocineResponse response = getService().theater(idCinema, profile, filter, sed, sig);
            return response.getTheater();
        } catch (Exception e) {
            throw new NetworkException();
        }
    }

    //---------------------------------------------------------------------------------------------

    /**
     * Horaires des cinémas
     */
    public static List<TheaterShowtime> showtimelist(String zip, Date date, int count, int page) throws NetworkException {

        if (date == null)
            date = new Date();
        String d = format(date);

        String params = ServiceSecurity.construireParams(false,
                AllocineService.ZIP, zip,
                AllocineService.DATE, d,
                AllocineService.COUNT, "" + count,
                AllocineService.PAGE, "" + page
        );

        String sed = ServiceSecurity.getSED();
        String sig = ServiceSecurity.getSIG(params, sed);

        try {
            AllocineResponse response = getService().showtimelistWithZip(zip, d, count, page, sed, sig);
            return response.getFeed().getTheaterShowtimes();
        } catch (Exception e) {
            throw new NetworkException();
        }
    }

    public static List<TheaterShowtime> showtimelistForTheater(String code, Date date, int count, int page) throws NetworkException {

        if (date == null)
            date = new Date();
        String d = format(date);

        String params = ServiceSecurity.construireParams(false,
                AllocineService.THEATERS, code,
                AllocineService.DATE, d,
                AllocineService.COUNT, "" + count,
                AllocineService.PAGE, "" + page
        );

        String sed = ServiceSecurity.getSED();
        String sig = ServiceSecurity.getSIG(params, sed);

        try {
            AllocineResponse response = getService().showtimelistForTheater(code, d, count, page, sed, sig);
            return response.getFeed().getTheaterShowtimes();
        } catch (Exception e) {
            throw new NetworkException();
        }
    }

    public static List<TheaterShowtime> showtimelistForTheaterAndMovie(String code, String idFilm, Date date, int count, int page) throws NetworkException {

        if (date == null)
            date = new Date();
        String d = format(date);

        String params = ServiceSecurity.construireParams(false,
                AllocineService.THEATERS, code,
                AllocineService.MOVIE, idFilm,
                AllocineService.DATE, d,
                AllocineService.COUNT, "" + count,
                AllocineService.PAGE, "" + page
        );

        String sed = ServiceSecurity.getSED();
        String sig = ServiceSecurity.getSIG(params, sed);

        try {
            AllocineResponse response = getService().showtimelistForTheaterAndMovie(code, idFilm, d, count, page, sed, sig);
            return response.getFeed().getTheaterShowtimes();
        } catch (Exception e) {
            throw new NetworkException();
        }
    }

    public static List<TheaterShowtime> showtimelistWithMovie(String zip, String idFilm, Date date, int count, int page) throws NetworkException {

        if (date == null)
            date = new Date();
        String d = format(date);

        String params = ServiceSecurity.construireParams(false,
                AllocineService.ZIP, zip,
                AllocineService.MOVIE, idFilm,
                AllocineService.DATE, d,
                AllocineService.COUNT, "" + count,
                AllocineService.PAGE, "" + page
        );

        String sed = ServiceSecurity.getSED();
        String sig = ServiceSecurity.getSIG(params, sed);

        try {
            AllocineResponse response = getService().showtimelistWithZipAndMovie(zip, idFilm, d, count, page, sed, sig);
            return response.getFeed().getTheaterShowtimes();
        } catch (Exception e) {
            throw new NetworkException();
        }
    }

    public static List<TheaterShowtime> showtimelistWithLatLng(String lat, String lng, Date date, int count, int page) throws NetworkException {

        if (date == null)
            date = new Date();
        String d = format(date);

        String params = ServiceSecurity.construireParams(false,
                AllocineService.LAT, lat,
                AllocineService.LONG, lng,
                AllocineService.DATE, d,
                AllocineService.COUNT, "" + count,
                AllocineService.PAGE, "" + page
        );

        String sed = ServiceSecurity.getSED();
        String sig = ServiceSecurity.getSIG(params, sed);

        try {
            AllocineResponse response = getService().showtimelistWithLatLng(lat, lng, d, count, page, sed, sig);
            return response.getFeed().getTheaterShowtimes();
        } catch (Exception e) {
            throw new NetworkException();
        }
    }

    public static List<TheaterShowtime> showtimelistWithLatLngAndMovie(String lat, String lng, String radius, String idFilm, Date date, int count, int page) throws NetworkException {

        if (date == null)
            date = new Date();
        String d = format(date);

        String params = ServiceSecurity.construireParams(false,
                AllocineService.LAT, lat,
                AllocineService.LONG, lng,
                AllocineService.RADIUS, radius,
                AllocineService.MOVIE, idFilm,
                AllocineService.DATE, d,
                AllocineService.COUNT, "" + count,
                AllocineService.PAGE, "" + page
        );

        String sed = ServiceSecurity.getSED();
        String sig = ServiceSecurity.getSIG(params, sed);

        try {
            AllocineResponse response = getService().showtimelistWithLatLngAndMovie(lat, lng, radius, idFilm, d, count, page, sed, sig);
            return response.getFeed().getTheaterShowtimes();
        } catch (Exception e) {
            throw new NetworkException();
        }
    }

    /**
     * Informations sur une vidéo : media
     */

    public static void media() {
    }

    //---------------------------------------------------------------------------------------------

    /**
     * Informations sur une personne
     */
    public static PersonFull person(String idPerson, String profile, String filter) throws NetworkException {

        String params = ServiceSecurity.construireParams(false,
                AllocineService.CODE, idPerson,
                AllocineService.PROFILE, profile,
                AllocineService.FILTER, filter
        );

        String sed = ServiceSecurity.getSED();
        String sig = ServiceSecurity.getSIG(params, sed);

        try {
            AllocineResponse response = getService().person(idPerson, profile, filter, sed, sig);
            return response.getPerson();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //---------------------------------------------------------------------------------------------

    /**
     * Filmographie d'une personne
     */
    public static List<Participation> filmography(String idPerson, String profile, String filter) throws NetworkException {

        String params = ServiceSecurity.construireParams(false,
                AllocineService.CODE, idPerson,
                AllocineService.PROFILE, profile,
                AllocineService.FILTER, filter
        );

        String sed = ServiceSecurity.getSED();
        String sig = ServiceSecurity.getSIG(params, sed);

        try {
            AllocineResponse response = getService().filmography(idPerson, profile, filter, sed, sig);
            return response.getPerson().getParticipation();
        } catch (Exception e) {
            throw new NetworkException();
        }

    }

    //---------------------------------------------------------------------------------------------

    public static List<Movie> movielist(String filter, String profile, String order, int count, int page) throws NetworkException {
        return movielist(Arrays.asList(filter), profile, order, count, page);
    }

    //---------------------------------------------------------------------------------------------

    public static List<Movie> movielist(List<String> filter, String profile, String order, int count, int page) throws NetworkException {
        String params = ServiceSecurity.construireParams(true,
                AllocineService.FILTER, filter,
                AllocineService.PROFILE, profile,
                AllocineService.ORDER, order,
                AllocineService.COUNT, "" + count,
                AllocineService.PAGE, "" + page
        );

        String sed = ServiceSecurity.getSED();
        String sig = ServiceSecurity.getSIG(params, sed);


        try {
            AllocineResponse response = getService().movieList(ServiceSecurity.applatir(filter), profile, order, count, page, sed, sig);
            return response.getFeed().getMovie();
        } catch (Exception e) {
            throw new NetworkException();
        }

    }

    public static List<PersonFull> startlist(String filter, String profile, int count, int page) throws NetworkException {
        return startlist(Arrays.asList(filter), profile, count, page);
    }

    //---------------------------------------------------------------------------------------------

    public static List<PersonFull> startlist(List<String> filter, String profile, int count, int page) throws NetworkException {
        String params = ServiceSecurity.construireParams(true,
                AllocineService.FILTER, filter,
                AllocineService.PROFILE, profile,
                AllocineService.COUNT, "" + count,
                AllocineService.PAGE, "" + page
        );

        String sed = ServiceSecurity.getSED();
        String sig = ServiceSecurity.getSIG(params, sed);


        try {
            AllocineResponse response = getService().personList(ServiceSecurity.applatir(filter), profile, count, page, sed, sig);
            return response.getFeed().getPerson();
        } catch (Exception e) {
            throw new NetworkException();
        }

    }

    public static List<News> newslist(String filter, int count, int page) throws NetworkException {
        return newslist(Arrays.asList(filter), count, page);
    }

    //---------------------------------------------------------------------------------------------

    public static List<News> newslist(List<String> filter, int count, int page) throws NetworkException {
        String params = ServiceSecurity.construireParams(true,
                AllocineService.FILTER, filter,
                AllocineService.COUNT, "" + count,
                AllocineService.PAGE, "" + page
        );


        String sed = ServiceSecurity.getSED();
        String sig = ServiceSecurity.getSIG(params, sed);


        try {
            AllocineResponse response = getService().newsList(ServiceSecurity.applatir(filter), count, page, sed, sig);
            return null;
        } catch (Exception e) {
            throw new NetworkException();
        }


    }

    /**
     * Informations sur une personne
     */
    public static News news(String idNews, String profile) throws NetworkException {

        String params = ServiceSecurity.construireParams(false,
                AllocineService.CODE, idNews,
                AllocineService.PROFILE, profile
        );

        String sed = ServiceSecurity.getSED();
        String sig = ServiceSecurity.getSIG(params, sed);

        try {
            AllocineResponse response = getService().news(idNews, profile, sed, sig);
            return null;
        } catch (Exception e) {
            throw new NetworkException();
        }
    }

    //---------------------------------------------------------------------------------------------

    public static List<Theater> theaterlist(String zip, int count, int page) throws NetworkException {
        String params = ServiceSecurity.construireParams(false,
                AllocineService.ZIP, zip,
                AllocineService.COUNT, "" + count,
                AllocineService.PAGE, "" + page
        );

        String sed = ServiceSecurity.getSED();
        String sig = ServiceSecurity.getSIG(params, sed);

        try {
            AllocineResponse response = getService().theaterlist(zip, count, page, sed, sig);
            return response.getFeed().getTheater();
        } catch (Exception e) {
            throw new NetworkException();
        }
    }

    //---------------------------------------------------------------------------------------------

    public static List<Theater> theaterlist(String lat, String lng, int radius, int count, int page) throws NetworkException {
        String params = ServiceSecurity.construireParams(false,
                AllocineService.LAT, lat,
                AllocineService.LONG, lng,
                AllocineService.RADIUS, "" + radius,
                AllocineService.COUNT, "" + count,
                AllocineService.PAGE, "" + page
        );

        String sed = ServiceSecurity.getSED();
        String sig = ServiceSecurity.getSIG(params, sed);

        try {
            AllocineResponse response = getService().theaterlist(lat, lng, radius, count, page, sed, sig);
            return response.getFeed().getTheater();
        } catch (Exception e) {
            throw new NetworkException();
        }
    }

    public static List<Media> videoList(String code, int count) throws NetworkException {

        String subject = "movie:" + code;
        String mediafmt = "mp4";

        String params = ServiceSecurity.construireParams(false,
                AllocineService.SUBJECT, subject,
                AllocineService.COUNT, "" + count,
                AllocineService.MEDIAFMT, mediafmt
        );

        String sed = ServiceSecurity.getSED();
        String sig = ServiceSecurity.getSIG(params, sed);

        try {
            AllocineResponse response = getService().videoList(subject, count, mediafmt, sed, sig);
            return response.getFeed().getMedia();
        } catch (Exception e) {
            throw new NetworkException();
        }
    }

    //---------------------------------------------------------------------------------------------

    /**
     * Pour l'instant on ne s'interesse pas aux series
     */
    public static void tvseries() {
    }

    public static void season() {
    }

    public static void episode() {
    }


    public static class NetworkException extends Exception {
    }


}
