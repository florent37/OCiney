package com.bdc.ociney.utils;

import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;

/**
 * Created by dev-w8-gfi on 27/05/2014.
 */
public class SeanceUtils {

    // Date format YYYY-MM-JJ-HH-MI
    public static void ajouterSeance(Context context,String titreFilm,String location,String description,String dateDebut,String dateFin){
        Intent calIntent = new Intent(Intent.ACTION_INSERT);
        calIntent.setType("vnd.android.cursor.item/event");
        calIntent.putExtra(CalendarContract.Events.TITLE, titreFilm);
        calIntent.putExtra(CalendarContract.Events.EVENT_LOCATION,location);
        calIntent.putExtra(CalendarContract.Events.DESCRIPTION, description);

        calIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);
        calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                DateUtils.stringToGregorian(dateDebut).getTimeInMillis());
        calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                DateUtils.stringToGregorian(dateFin).getTimeInMillis());

        context.startActivity(calIntent);
    }
}
