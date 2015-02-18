package com.bdc.ociney.modele.Theater;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by florentchampigny on 03/05/2014.
 */
public class Horaires {
    boolean avantPremier = false;
    String date;
    String formatEcran;
    String display; //affichage complet
    List<String> seances = new ArrayList<String>();
    String version;

    public boolean isAvantPremier() {
        return avantPremier;
    }

    public void setAvantPremier(boolean avantPremier) {
        this.avantPremier = avantPremier;
    }

    public String getDate() {
        try {
            String[] d = date.split("-");
            return d[2] + "/" + d[1] + "/" + d[0];
        } catch (Exception e) {
            return date;
        }
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFormatEcran() {
        return formatEcran;
    }

    public void setFormatEcran(String formatEcran) {
        this.formatEcran = formatEcran;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public List<String> getSeances() {
        return seances;
    }

    public void setSeances(List<String> seances) {
        this.seances = seances;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isToday() {
        String dateFormatted = getDate();

        Date now = new Date();
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
        String nowFormatted = formater.format(now);

        Log.d("DATE", dateFormatted + " " + nowFormatted);

        return dateFormatted.equals(nowFormatted);
    }

    public boolean isMoreThanToday() {
            try {
                String dateFormatted = getDate();

                Date now = new Date();
                now.setHours(0);
                now.setSeconds(0);
                now.setMinutes(0);

                SimpleDateFormat formater = new SimpleDateFormat("E dd MMMMM yyyy", Locale.FRANCE);
                Date date = formater.parse(dateFormatted);

                date.setHours(13);

                return date.equals(now) || date.after(now);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
    }

    @Override
    public String toString() {
        return "Horraires{" +
                ", date='" + date + '\'' +
                ", seances=" + seances +
                '}';
    }
}