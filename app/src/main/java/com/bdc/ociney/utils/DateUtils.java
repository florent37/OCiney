package com.bdc.ociney.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

/**
 * Created by dev-w8-gfi on 21/05/2014.
 */
public class DateUtils {


    public static String dateFormat(String date) {

        try {
            StringTokenizer tk = new StringTokenizer(date, "-");
            String annee = tk.nextToken();
            String mois = tk.nextToken();
            String jour = tk.nextToken();

            return jour + " " + moisInLetter(Integer.parseInt(mois)) + " " + annee;
        } catch (Exception e) {
            return date;
        }
    }

    public static String age(String date) {
        String age = "";

        try {
            StringTokenizer tk = new StringTokenizer(date, "-");
            String annee = tk.nextToken();

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            int year = cal.get(Calendar.YEAR);

            return (year - Integer.parseInt(annee)) + " ans";
        } catch (Exception e) {
            return age;
        }
    }

    public static GregorianCalendar stringToGregorian(String date) {

        try {
            StringTokenizer tk = new StringTokenizer(date, "-");
            String annee = tk.nextToken();
            String mois = tk.nextToken();
            String jour = tk.nextToken();
            String heure = tk.nextToken();
            String min = tk.nextToken();

            return new GregorianCalendar(Integer.parseInt(annee), Integer.parseInt(mois) - 1, Integer.parseInt(jour), Integer.parseInt(heure), Integer.parseInt(min));

        } catch (Exception e) {
            return null;
        }

    }

    public static String moisInLetter(int mois) {
        switch (mois) {

            case 1:
                return "janvier";
            case 2:
                return "février";
            case 3:
                return "mars";
            case 4:
                return "avril";
            case 5:
                return "mai";
            case 6:
                return "juin";
            case 7:
                return "juillet";
            case 8:
                return "août";
            case 9:
                return "septembre";
            case 10:
                return "octobre";
            case 11:
                return "novembre";
            case 12:
                return "décembre";
            default:
                return "";
        }
    }
}
