package com.bdc.ociney.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by florentchampigny on 20/04/2014.
 */
public class Session {

    private static final String PREFS_SESSION = "SESSION";
    Context context;
    SharedPreferences session;

    public Session(Context context) {
        this.context = context;
        session = context.getSharedPreferences(PREFS_SESSION, 0);
    }

    public void put(String key, String value) {
        SharedPreferences.Editor editor = session.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String get(String key) {
        return session.getString(key, "");
    }

}
