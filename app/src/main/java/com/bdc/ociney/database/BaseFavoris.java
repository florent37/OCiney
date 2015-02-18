package com.bdc.ociney.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 */
public class BaseFavoris extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_FAVORIS = "TABLE_FAVORIS";

    public static final int TYPE_FAVORIS_FILM = 1;
    public static final int TYPE_FAVORIS_CINEMA = 2;
    public static final int TYPE_FAVORIS_STAR = 3;
    // Arguments
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS ";
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
    private static final String OBJET_STRING = " TEXT ";
    private static final String OBJET_BOOLEAN = " INTEGER ";
    private static final String OBJET_INTEGER = " INTEGER ";
    private static final String SUIVANT = ", ";
    private static final String FIN = " );";
    private static final String DEBUT = " (";

    // Base colums General
    public static final String COL_ID = "ID";
    public static final String COL_TYPE = "TYPE";

    // Order
    public static final String ASC = "ASC";
    public static final String DESC = "DESC";

    private final String CREATE_TABLE_FAVORIS = CREATE_TABLE + TABLE_FAVORIS + DEBUT
            + COL_ID + OBJET_STRING + SUIVANT
            + COL_TYPE + OBJET_INTEGER
            + FIN;

    /**
     * Constructeur de la base de données
     *
     * @param context - Context de l'application
     * @param name    - Nom de la base de données
     * @param factory
     * @param version - Version de la base de données
     */
    public BaseFavoris(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    /**
     * Méthode permettant de créer les tables si elles n'existent pas encore
     *
     * @param db - Base de données
     */
    @Override
    public void onCreate(SQLiteDatabase db) {



  /*
        db.execSQL(DROP_TABLE + TABLE_FAVORIS);
        */

        // Création des tables dans la base de données
        db.execSQL(CREATE_TABLE_FAVORIS);
    }

    /**
     * Méthode permettant de changer la version de la base de données
     *
     * @param db         - Base de données
     * @param oldVersion - Numéro de l'ancienne version
     * @param newVersion - Numéro de la nouvelle version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (newVersion > DATABASE_VERSION) {

            // Supprime les tables de la base
            db.execSQL(DROP_TABLE + TABLE_FAVORIS);

            // On recrée la base
            onCreate(db);
        }
    }
}
