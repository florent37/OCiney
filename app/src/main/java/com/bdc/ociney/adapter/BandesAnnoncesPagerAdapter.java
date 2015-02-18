package com.bdc.ociney.adapter;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bdc.ociney.R;
import com.bdc.ociney.modele.Media;
import com.bdc.ociney.modele.Movie.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by florentchampigny on 01/05/2014.
 */

public class BandesAnnoncesPagerAdapter extends FragmentStatePagerAdapter {

    List<Media> bandesAnnonces;
    Activity activity;
    OnBandeAnnonceClickedListener listener;
    Movie movie;
    BitmapDrawable bobine;
    boolean theater = false;

    public BandesAnnoncesPagerAdapter(Activity activity, FragmentManager fm, List<Media> bandesAnnonces, Movie movie, OnBandeAnnonceClickedListener listener, boolean theater) {
        super(fm);
        this.activity = activity;
        this.bandesAnnonces = bandesAnnonces;
        this.listener = listener;
        this.movie = movie;
        this.theater = theater;

        //bobine = new BitmapDrawable(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pellicule));
        //bobine.setTileModeX(Shader.TileMode.REPEAT);
        //bobine.setTileModeY(Shader.TileMode.CLAMP);
    }

    @Override
    public Fragment getItem(int position) {
        final int p = position;

        return new Fragment() {
            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState) {

                final Media bandeAnnonce = bandesAnnonces.get(p);

                View view = null;
                if (theater)
                    view = View.inflate(getActivity(), R.layout.bande_annonce_pager_theater, null);
                else
                    view = View.inflate(getActivity(), R.layout.bande_annonce_pager_movie, null);

                ImageView image = (ImageView) view.findViewById(R.id.image);
                String urlImage = bandeAnnonce.getThumbnail().getHref(activity.getResources().getInteger(R.integer.fragment_movie_bande_annonce_height));
                Picasso.with(getActivity()).load(urlImage).into(image);

                /*
                view.findViewById(R.id.bobineTop).setBackgroundDrawable(bobine);
                view.findViewById(R.id.bobineBottom).setBackgroundDrawable(bobine);
                */

                String titre = bandeAnnonce.getTitle().replace(movie.getTitle(), "").replace(movie.getOriginalTitle(), "").replace("\"\"", "").replace(":", "").trim();
                if (titre.startsWith("-"))
                    titre = titre.substring(1, titre.length()).trim();

                TextView title = (TextView) view.findViewById(R.id.title);
                title.setText(titre);

                view.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                listener.onBandeAnnonceClicked(bandeAnnonce);
                                            }
                                        }
                );

                return view;
            }
        };

    }

    @Override
    public int getCount() {
        return bandesAnnonces.size();
    }

    public interface OnBandeAnnonceClickedListener {
        public void onBandeAnnonceClicked(Media bandeAnnonce);
    }
}