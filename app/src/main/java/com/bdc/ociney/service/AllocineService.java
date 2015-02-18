package com.bdc.ociney.service;

import com.bdc.ociney.modele.AllocineResponse;
import com.bdc.ociney.modele.AllocineResponseSmall;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by florentchampigny on 18/04/2014.
 */
public interface AllocineService {

    public static final String ALLOCINE_PARTNER_KEY = "100043982026";
    public static final String ALLOCINE_SECRET_KEY = "29d185d98c984a359e6e6f26a0474269";

    public static final String APP_ID = "27405";
    public static final String FORMAT_JSON = "json";

    public static final String URL = "http://api.allocine.fr/rest/v3";
    public static final String USER_AGENT = "Dalvik/1.6.0 (Linux; U; Android 4.2.2; Nexus 4 Build/JDQ39E)";

    public static String PARTNER = "partner";
    public static String CODE = "code";
    public static String COUNT = "count";
    public static String ORDER = "order";
    public static String FILTER = "filter";
    public static String FORMAT = "format";
    public static String DEFAULT_PARAMS = PARTNER + "=" + ALLOCINE_PARTNER_KEY + "&" + CODE + "=" + APP_ID + "&" + FORMAT + "=" + FORMAT_JSON;
    public static String DEFAULT_PARAMS_MINUS_CODE = PARTNER + "=" + ALLOCINE_PARTNER_KEY + "&" + FORMAT + "=" + FORMAT_JSON;
    public static String SED = "sed";
    public static String SIG = "sig";
    public static String Q = "q";
    public static String PAGE = "page";
    public static String PROFILE = "profile";
    public static String ZIP = "zip";
    public static String LAT = "lat";
    public static String LONG = "long";
    public static String RADIUS = "radius";
    public static String THEATER = "theater";
    public static String LOCATION = "location";
    public static String TYPE = "type";
    public static String MOVIE = "movie";
    public static String DATE = "date";
    public static String THEATERS = "theaters";
    public static String SUBJECT = "subject";
    public static String MEDIAFMT = "mediafmt";

    //---------------------------------------------------------------------------------------------

    /**
     * Recherche
     */
    @GET("/search?" + DEFAULT_PARAMS_MINUS_CODE)
    AllocineResponse search(@Query(Q) String recherche,
                            @Query(FILTER) String filer,
                            @Query(COUNT) int count,
                            @Query(PAGE) int page,
                            @Query(SED) String sed,
                            @Query(SIG) String sig
    );


    //---------------------------------------------------------------------------------------------

    /**
     * Recherche Small
     */
    @GET("/search?" + DEFAULT_PARAMS_MINUS_CODE)
    AllocineResponseSmall searchSmall(@Query(Q) String recherche,
                                      @Query(FILTER) String filer,
                                      @Query(COUNT) int count,
                                      @Query(PAGE) int page,
                                      @Query(SED) String sed,
                                      @Query(SIG) String sig
    );

    //---------------------------------------------------------------------------------------------

    /**
     * Informations sur un film
     */
    @GET("/movie?" + DEFAULT_PARAMS_MINUS_CODE)
    AllocineResponse movie(@Query(CODE) String code,
                           @Query(PROFILE) String profile,
                           //@Query(mediafmt)
                           @Query(FILTER) String fitler,
                           @Query(SED) String sed,
                           @Query(SIG) String sig
    );

    //---------------------------------------------------------------------------------------------

    /**
     * Critiques sur un film (presse et public)
     */
    @GET("/reviewlist?" + DEFAULT_PARAMS_MINUS_CODE)
    AllocineResponse reviewlist(@Query(CODE) String code,
                                @Query(TYPE) String type,
                                @Query(FILTER) String fitler,
                                @Query(COUNT) int count,
                                @Query(PAGE) int page,
                                @Query(SED) String sed,
                                @Query(SIG) String sig
    );

    //---------------------------------------------------------------------------------------------

    /**
     * Informations sur un theater
     */
    @GET("/theater?" + DEFAULT_PARAMS_MINUS_CODE)
    AllocineResponse theater(@Query(CODE) String code,
                             @Query(PROFILE) String profile,
                             //@Query(mediafmt)
                             @Query(FILTER) String fitler,
                             @Query(SED) String sed,
                             @Query(SIG) String sig
    );

    //pour showtime
    //radius : rayon autour du point désigné (entre 1 et 500 km)
    //theaters : liste de codes de cinémas (séparé par une virgule, exemple: P0728,P0093)
    //location : chaîne représentant le cinéma

    /**
     * Horaires des cinémas
     */
    @GET("/showtimelist?" + DEFAULT_PARAMS_MINUS_CODE)
    AllocineResponse showtimelistWithZip(@Query(ZIP) String zip,
                                         @Query(DATE) String date, //YYYY-MM-DD
                                         @Query(COUNT) int count,
                                         @Query(PAGE) int page,
                                         @Query(SED) String sed,
                                         @Query(SIG) String sig
    );

    @GET("/showtimelist?" + DEFAULT_PARAMS_MINUS_CODE)
    AllocineResponse showtimelistForTheater(@Query(THEATERS) String code,
                                            @Query(DATE) String date, //YYYY-MM-DD
                                            @Query(COUNT) int count,
                                            @Query(PAGE) int page,
                                            @Query(SED) String sed,
                                            @Query(SIG) String sig
    );

    @GET("/showtimelist?" + DEFAULT_PARAMS_MINUS_CODE)
    AllocineResponse showtimelistForTheaterAndMovie(@Query(THEATERS) String code,
                                                    @Query(MOVIE) String idFilm,
                                                    @Query(DATE) String date, //YYYY-MM-DD
                                                    @Query(COUNT) int count,
                                                    @Query(PAGE) int page,
                                                    @Query(SED) String sed,
                                                    @Query(SIG) String sig
    );

    @GET("/showtimelist?" + DEFAULT_PARAMS_MINUS_CODE)
    AllocineResponse showtimelistWithZipAndMovie(@Query(ZIP) String zip,
                                                 @Query(MOVIE) String idFilm,
                                                 @Query(DATE) String date, //YYYY-MM-DD
                                                 @Query(COUNT) int count,
                                                 @Query(PAGE) int page,
                                                 @Query(SED) String sed,
                                                 @Query(SIG) String sig
    );

    @GET("/showtimelist?" + DEFAULT_PARAMS_MINUS_CODE)
    AllocineResponse showtimelistWithLatLng(@Query(LAT) String lat,
                                            @Query(LONG) String lng,
                                            @Query(DATE) String date, //YYYY-MM-DD
                                            @Query(COUNT) int count,
                                            @Query(PAGE) int page,
                                            @Query(SED) String sed,
                                            @Query(SIG) String sig
    );

    @GET("/showtimelist?" + DEFAULT_PARAMS_MINUS_CODE)
    AllocineResponse showtimelistWithLatLngAndMovie(@Query(LAT) String lat,
                                                    @Query(LONG) String lng,
                                                    @Query(RADIUS) String radius,
                                                    @Query(MOVIE) String idFilm,
                                                    @Query(DATE) String date, //YYYY-MM-DD
                                                    @Query(COUNT) int count,
                                                    @Query(PAGE) int page,
                                                    @Query(SED) String sed,
                                                    @Query(SIG) String sig
    );

    //---------------------------------------------------------------------------------------------

    /**
     * Informations sur une vidéo : media
     */

    //---------------------------------------------------------------------------------------------

    /**
     * Informations sur une personne
     */

    @GET("/person?" + DEFAULT_PARAMS_MINUS_CODE)
    AllocineResponse person(@Query(CODE) String idPerson,
                            @Query(PROFILE) String profile,
                            @Query(FILTER) String filter,
                            @Query(SED) String sed,
                            @Query(SIG) String sig
    );

    //---------------------------------------------------------------------------------------------

    /**
     * Informations sur une news
     */

    @GET("/news?" + DEFAULT_PARAMS_MINUS_CODE)
    AllocineResponse news(@Query(CODE) String idNews,
                          @Query(PROFILE) String profile,
                          @Query(SED) String sed,
                          @Query(SIG) String sig
    );

    //---------------------------------------------------------------------------------------------

    /**
     * Filmographie d'une personne
     */

    @GET("/filmography?" + DEFAULT_PARAMS_MINUS_CODE)
    AllocineResponse filmography(@Query(CODE) String idPerson,
                                 @Query(PROFILE) String profile,
                                 @Query(FILTER) String filter,
                                 @Query(SED) String sed,
                                 @Query(SIG) String sig
    );

    //---------------------------------------------------------------------------------------------

    @GET("/movielist?" + DEFAULT_PARAMS)
    AllocineResponse movieList(@Query(FILTER) String filer,
                               @Query(PROFILE) String profile,
                               @Query(ORDER) String order,
                               @Query(COUNT) int count,
                               @Query(PAGE) int page,
                               @Query(SED) String sed,
                               @Query(SIG) String sig
    );

    //---------------------------------------------------------------------------------------------
    @GET("/personList?" + DEFAULT_PARAMS)
    AllocineResponse personList(@Query(FILTER) String filer,
                                @Query(PROFILE) String profile,
                                @Query(COUNT) int count,
                                @Query(PAGE) int page,
                                @Query(SED) String sed,
                                @Query(SIG) String sig
    );

    //---------------------------------------------------------------------------------------------

    @GET("/videolist?" + DEFAULT_PARAMS_MINUS_CODE)
    AllocineResponse videoList(@Query(SUBJECT) String sbubject,
                               @Query(COUNT) int count,
                               @Query(MEDIAFMT) String mediafmt,
                               @Query(SED) String sed,
                               @Query(SIG) String sig
    );


    //---------------------------------------------------------------------------------------------

    @GET("/newslist?" + DEFAULT_PARAMS)
    AllocineResponse newsList(@Query(FILTER) String filer,
                              @Query(COUNT) int count,
                              @Query(PAGE) int page,
                              @Query(SED) String sed,
                              @Query(SIG) String sig
    );


    //---------------------------------------------------------------------------------------------

    @GET("/theaterlist?" + DEFAULT_PARAMS_MINUS_CODE)
    AllocineResponse theaterlist(@Query(ZIP) String zip,
                                 @Query(COUNT) int count,
                                 @Query(PAGE) int page,
                                 @Query(SED) String sed,
                                 @Query(SIG) String sig
    );

    @GET("/theaterlist?" + DEFAULT_PARAMS_MINUS_CODE)
    AllocineResponse theaterlist(@Query(LAT) String lat,
                                 @Query(LONG) String lng,
                                 @Query(RADIUS) int radius,
                                 @Query(COUNT) int count,
                                 @Query(PAGE) int page,
                                 @Query(SED) String sed,
                                 @Query(SIG) String sig
    );

    //---------------------------------------------------------------------------------------------

}
