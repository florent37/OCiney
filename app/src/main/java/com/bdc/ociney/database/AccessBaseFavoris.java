package com.bdc.ociney.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dev-w8-gfi on 27/05/2014.
 */
public class AccessBaseFavoris {

    private static final int VERSION_BDD = 1;
    private static final String BDD_NAME = "gestaticket.db";

    private SQLiteDatabase bdd;
    private BaseFavoris mBase;


    /**
     * Constructor Database
     *
     * @param context
     */
    public AccessBaseFavoris(Context context) {
        mBase = new BaseFavoris(context, BDD_NAME, null, VERSION_BDD);
    }


    /**
     * Open Database
     */
    public void open() {
        bdd = mBase.getWritableDatabase();
        mBase.onCreate(bdd);
    }

    /**
     * Close Database
     */
    public void close() {
        if (bdd != null)
            bdd.close();
    }

    /**
     * Get Database
     *
     * @return SQLiteDatabase - Return database
     */
    public SQLiteDatabase getBdd() {
        return this.bdd;
    }


    /**
     * Method to insert favoris
     */
    public void insertFavoris(String id, int type) {

        // Paramétres du favoris
        ContentValues values = new ContentValues();
        values.put(BaseFavoris.COL_ID, id);
        values.put(BaseFavoris.COL_TYPE, type);

        // Insert favoris in bdd
        long last = bdd.insert(BaseFavoris.TABLE_FAVORIS, null, values);
    }

    public void deleteFavoris(String id, int type) {
        // Delete favoris
        bdd.delete(BaseFavoris.TABLE_FAVORIS, BaseFavoris.COL_ID + "='" + id + "' AND " + BaseFavoris.COL_TYPE + " = " + type, null);
    }


    public List<String> getListFavoris(int type) {
        String query = "SELECT DISTINCT * FROM " + BaseFavoris.TABLE_FAVORIS + " WHERE + " + BaseFavoris.COL_TYPE + " = " + type + ";";
        Cursor cursor = bdd.rawQuery(query, null);
        return convertCursorToListFavoris(cursor);
    }

    private List<String> convertCursorToListFavoris(Cursor cursor) {
        List<String> list = new ArrayList<String>();

        if (cursor.getCount() == 0)
            return list;

        cursor.moveToFirst();

        do {
            list.add(cursor.getString(cursor.getColumnIndex(BaseFavoris.COL_ID)));
        } while (cursor.moveToNext());

        cursor.close();

        return list;
    }

    public boolean isFavoris(String id, int type) {
        String query = "SELECT DISTINCT * FROM " + BaseFavoris.TABLE_FAVORIS + " WHERE " + BaseFavoris.COL_ID + " = '" + id + "' AND " + BaseFavoris.COL_TYPE + " = " + type;
        Cursor cursor = bdd.rawQuery(query, null);

        if (cursor.getCount() == 0)
            return false;

        return true;
    }
}


