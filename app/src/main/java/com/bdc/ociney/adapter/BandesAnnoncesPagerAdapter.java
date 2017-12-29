package com.bdc.ociney.adapter;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by florentchampigny on 01/05/2014.
 */

public class BandesAnnoncesPagerAdapter extends FragmentStatePagerAdapter {

    final List<Media> bandesAnnonces;
    final OnBandeAnnonceClickedListener listener;
    final Movie movie;
    final boolean theater;
    BitmapDrawable bobine;

    public BandesAnnoncesPagerAdapter(FragmentManager fm, List<Media> bandesAnnonces, Movie movie, OnBandeAnnonceClickedListener listener, boolean theater) {
        super(fm);
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
        return BandeAnnonceFragment.newInstance(position, bandesAnnonces.get(position), movie, theater).setListener(listener);
    }

    @Override
    public int getCount() {
        return bandesAnnonces.size();
    }

    public interface OnBandeAnnonceClickedListener {
        void onBandeAnnonceClicked(Media bandeAnnonce);
    }

    public static class BandeAnnonceFragment extends Fragment {

        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.title)
        TextView title;
        private OnBandeAnnonceClickedListener listener;

        public static BandeAnnonceFragment newInstance(int position, Media media, Movie movie, boolean theater) {
            final Bundle args = new Bundle();
            args.putInt("position", position);
            args.putBoolean("theater", theater);
            final Gson gson = new Gson();
            args.putString("media", gson.toJson(media));
            args.putString("movie", gson.toJson(movie));
            final BandeAnnonceFragment fragment = new BandeAnnonceFragment();
            fragment.setArguments(args);
            return fragment;
        }

        public BandeAnnonceFragment setListener(OnBandeAnnonceClickedListener listener) {
            this.listener = listener;
            return this;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            ButterKnife.bind(this, view);

            final Gson gson = new Gson();
            final Media bandeAnnonce = gson.fromJson(getArguments().getString("media"), Media.class);
            final Movie movie = gson.fromJson(getArguments().getString("movie"), Movie.class);

            String urlImage = bandeAnnonce.getThumbnail().getHref(getContext().getResources().getInteger(R.integer.fragment_movie_bande_annonce_height));
            Picasso.with(getActivity()).load(urlImage).into(image);

                /*
                view.findViewById(R.id.bobineTop).setBackgroundDrawable(bobine);
                view.findViewById(R.id.bobineBottom).setBackgroundDrawable(bobine);
                */


            String titre = bandeAnnonce.getTitle().replace(movie.getTitle(), "").replace(movie.getOriginalTitle(), "").replace("\"\"", "").replace(":", "").trim();
            if (titre.startsWith("-"))
                titre = titre.substring(1, titre.length()).trim();

            title.setText(titre);

            view.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (listener != null) {
                                                listener.onBandeAnnonceClicked(bandeAnnonce);
                                            }
                                        }
                                    }
            );
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            if (getArguments().getBoolean("theater"))
                return inflater.inflate(R.layout.bande_annonce_pager_theater, container, false);
            else
                return inflater.inflate(R.layout.bande_annonce_pager_movie, container, false);
        }

    }
}