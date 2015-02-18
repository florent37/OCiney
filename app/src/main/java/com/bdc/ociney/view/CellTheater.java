package com.bdc.ociney.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bdc.ociney.R;
import com.bdc.ociney.modele.Theater.Horaires;
import com.bdc.ociney.modele.Theater.Theater;
import com.bdc.ociney.utils.LocationUtils;

import java.util.List;

public class CellTheater extends ViewCell<Theater> implements View.OnClickListener {

    View layout;

    ImageView image;
    TextView nom;
    TextView chaine;
    TextView adresse;
    TextView distance;
    View accesHandicape;

    ViewGroup horaires;

    public static void chargerHoraires(ViewGroup layout, List<Horaires> horaires, boolean isToday) {

        layout.removeAllViews();

        Context context = layout.getContext();

        if (horaires != null && horaires.size() > 0) {

            for (Horaires h : horaires) {
                if (((isToday && h.isToday())) || (!isToday)) {

                    View vueHorraires = View.inflate(context, R.layout.cell_theater_horraires, null);
                    TextView texte = (TextView) vueHorraires.findViewById(R.id.text);
                    ImageView version = (ImageView) vueHorraires.findViewById(R.id.version);

                    if (h.getFormatEcran() != null) {
                        if (h.getFormatEcran().contains("3D"))
                            version.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_version_white_3d));
                        else if (h.getFormatEcran().contains("Numérique"))
                            version.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_version_white_2d));
                        else if (!h.getVersion().contains("Français"))
                            version.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_version_white_vo));
                        else
                            version.setVisibility(View.GONE);
                    } else
                        version.setVisibility(View.GONE);

                    StringBuffer sb = new StringBuffer();
                    for (String seance : h.getSeances())
                        sb.append(seance).append(" ");

                    texte.setText(sb.toString());

                    layout.addView(vueHorraires);
                }
            }
        }
    }

    @Override
    public void construire() {
        charger();
        remplir();
        ajouterListeners();
    }

    @Override
    public void charger() {
        this.layout = getView().findViewById(R.id.layout);

        this.image = (ImageView) getView().findViewById(R.id.image);
        this.nom = (TextView) getView().findViewById(R.id.nom);
        this.adresse = (TextView) getView().findViewById(R.id.adresse);
        this.accesHandicape = getView().findViewById(R.id.accesHandicape);
        this.distance = (TextView) getView().findViewById(R.id.distance);

        this.horaires = (ViewGroup) getView().findViewById(R.id.horaires);
    }

    @Override
    public void remplir() {
        if (getObject() != null) {


            if (getObject().getName() != null)
                this.nom.setText(getObject().getName());

            adresse.setText(getObject().getPostalCode() + " " + getObject().getCity());

            if (getObject().getHasPRMAccess() != null && getObject().getHasPRMAccess().intValue() == 1)
                accesHandicape.setVisibility(View.VISIBLE);
            else
                accesHandicape.setVisibility(View.GONE);
            chargerHoraires(horaires, getObject().getHoraires(), true);

            chargerEnFonctionDuCinema();


            if (getObject().getGeoloc() != null) {
                //calcul distance
                double lat = getObject().getGeoloc().getLat();
                double lng = getObject().getGeoloc().getLong();
                Location location = new Location("");
                location.setLatitude(lat);
                location.setLongitude(lng);

                Location myLocation = new Location("");
                if (LocationUtils.lastLat != null) {
                    myLocation.setLatitude(Double.parseDouble(LocationUtils.lastLat));
                    myLocation.setLongitude(Double.parseDouble(LocationUtils.lastLng));

                    float distanceKm = myLocation.distanceTo(location) / 1000;

                    distance.setText("" + String.format("%.1f", distanceKm) + " km");
                }
            }
        }
    }

    private void chargerEnFonctionDuCinema() {

        String name = getObject().getName().replace("é", "e").toUpperCase();
        int imageFond = -1;
        String couleurFond = "#E0E0E0";

        if (name.contains("CGR")) {
            imageFond = R.drawable.logo_cgr;
            couleurFond = "#ff8171";
        } else if (name.contains("GAUMONT")) {
            imageFond = R.drawable.logo_gaumont;
            couleurFond = "#d9002f";
        } else if (name.contains("GAUMONT-PATHE")) {
            imageFond = R.drawable.logo_gaumont_pathe;
            couleurFond = "#eba14c";
        } else if (name.contains("KINEPOLIS")) {
            imageFond = R.drawable.logo_kinepolis;
            couleurFond = "#2170a1";
        } else if (name.contains("MEGARAMA")) {
            imageFond = R.drawable.logo_megarama;
            couleurFond = "#6990ff";
        } else if (name.contains("MK2")) {
            imageFond = R.drawable.logo_mk2;
            couleurFond = "#505050";
        } else if (name.contains("PATHE")) {
            imageFond = R.drawable.logo_pathe;
            couleurFond = "#dac872";
        } else if (name.contains("UGC")) {
            imageFond = R.drawable.logo_ugc;
            couleurFond = "#4763bf";
        } else {
            imageFond = R.drawable.logo_default;
            couleurFond = "#A0A0A0";
        }

        if (imageFond != -1 && couleurFond != null) {
            image.setImageResource(imageFond);
            this.layout.setBackgroundColor(Color.parseColor(couleurFond));
        }

    }

    public void animer() {

        image.setAlpha(0f);

        if (image != null) {
            final AnimatorSet a = new AnimatorSet();
            a.setDuration(300);
            a.playTogether(
                    ObjectAnimator.ofFloat(image, "alpha", 0f, 0.2f),
                    ObjectAnimator.ofFloat(image, "translationX", 200f, 0f)
            );


            image.postDelayed(new Runnable() {
                @Override
                public void run() {
                    a.start();
                }
            }, 300);
        }
    }

    public void afficherEnEntier() {
        image.setAlpha(0.2f);
    }

    @Override
    public void ajouterListeners() {
        if (view.findViewById(R.id.gps) != null)
            view.findViewById(R.id.gps).setOnClickListener(this);
        //getView().setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        try {

            double lat = getObject().getGeoloc().getLat();
            double lng = getObject().getGeoloc().getLong();

            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("geo:0,0?q=" + lat + "," + lng + " (" + getObject().getName() + ")"));
            ((Activity) context).startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
        TheaterFragment.theater = getObject();

        context.startActivity(new Intent(context, TheaterActivity.class));


        */
    }
}
