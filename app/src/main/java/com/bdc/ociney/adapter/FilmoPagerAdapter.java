package com.bdc.ociney.adapter;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bdc.ociney.R;
import com.bdc.ociney.modele.Participation;
import com.bdc.ociney.view.textview.TypefacedTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by florentchampigny on 01/05/2014.
 */
public class FilmoPagerAdapter extends BaseAdapter {

    Activity activity;
    List<Participation> participations;
    SensorManager sensorManager;
    Sensor sensor;
    int position;

    public FilmoPagerAdapter(Activity activity, List<Participation> participations) {
        this.activity = activity;
        this.participations = participations;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View viewParent, ViewGroup viewGroup) {
        View view;

        view = View.inflate(activity, R.layout.jaquette, null);


        Participation participation = participations.get(i);

        view.setVisibility(View.VISIBLE);

        if (participation.getMovie() != null && participation.getMovie().getPoster() != null) {
            String urlImage = participation.getMovie().getUrlPoster(activity.getResources().getInteger(R.integer.cell_movie_poster_height));
            Picasso.with(activity).load(urlImage).into((ImageView) view.findViewById(R.id.jaquette));
        }

        if (participation.getMovie() != null && participation.getMovie().getTitle() != null) {
            ((TypefacedTextView) view.findViewById(R.id.titreFilmo)).setText(participation.getMovie().getTitle());
        } else {
            if (participation.getMovie() != null && participation.getMovie().getOriginalTitle() != null) {
                ((TypefacedTextView) view.findViewById(R.id.titreFilmo)).setText(participation.getMovie().getOriginalTitle());
            } else {
                view.setVisibility(View.GONE);
            }
        }

        if (participation.getRole() != null)
            ((TypefacedTextView) view.findViewById(R.id.roleFilmo)).setText(participation.getRole());
        else
            ((TypefacedTextView) view.findViewById(R.id.roleFilmo)).setText(activity.getString(R.string.role_non_renseigne));

        try {
            getItem(position + 1);
        } catch (Exception e) {
        }

        return view;
    }

    @Override
    public int getCount() {
        return participations.size();
    }

    @Override
    public Object getItem(int i) {
        return participations.get(i);
    }

}
