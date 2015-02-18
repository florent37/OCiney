package com.bdc.ociney.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.bdc.ociney.R;
import com.bdc.ociney.activity.AppFragmentActivity;
import com.bdc.ociney.activity.MovieActivity;
import com.bdc.ociney.fragment.MovieFragment;
import com.bdc.ociney.modele.Participation;
import com.bdc.ociney.view.textview.TypefacedTextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class CellJaquette extends ViewCell<Participation> implements View.OnClickListener {

    boolean animer = true;

    @Override
    public void construire() {
    }

    @Override
    public void animer() {

    }

    @Override
    public void charger() {
    }

    @Override
    public void remplir() {

        if (getObject() != null) {


            view.setVisibility(View.VISIBLE);

            if (getObject().getMovie() != null && getObject().getMovie().getPoster() != null) {
                ((ImageView) view.findViewById(R.id.jaquette)).setVisibility(View.VISIBLE);
                String urlImage = getObject().getMovie().getUrlPoster(getContext().getResources().getInteger(R.integer.star_filmo_jaquette_height));
                Picasso.with(getContext()).load(urlImage).into((ImageView) view.findViewById(R.id.jaquette), new Callback() {
                    @Override
                    public void onSuccess() {
                        if (animer) {
                            AnimatorSet animatorSet = new AnimatorSet().setDuration(500);
                            animatorSet.playTogether(ObjectAnimator.ofFloat(view.findViewById(R.id.jaquetteWhite), "alpha", 0, 1), ObjectAnimator.ofFloat(view.findViewById(R.id.placeholder_filmo), "alpha", 1, 0));
                            animer = false;
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });
            } else
                ((ImageView) view.findViewById(R.id.jaquette)).setVisibility(View.GONE);

            if (getObject().getMovie() != null && getObject().getMovie().getTitle() != null) {
                ((TypefacedTextView) view.findViewById(R.id.titreFilmo)).setText(getObject().getMovie().getTitle());
            } else {
                if (getObject().getMovie() != null && getObject().getMovie().getOriginalTitle() != null) {
                    ((TypefacedTextView) view.findViewById(R.id.titreFilmo)).setText(getObject().getMovie().getOriginalTitle());
                } else {
                    view.setVisibility(View.GONE);
                }
            }

            if (getObject().getRole() != null)
                ((TypefacedTextView) view.findViewById(R.id.roleFilmo)).setText(getObject().getRole());
            else
                ((TypefacedTextView) view.findViewById(R.id.roleFilmo)).setText(getContext().getString(R.string.role_non_renseigne));
        }
    }

    @Override
    public void ajouterListeners() {
        getView().setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getContext(), MovieActivity.class);
        intent.putExtra(MovieFragment.ACTIONBAR_TITLE, AppFragmentActivity.actionBarSubTitle);
        intent.putExtra(MovieFragment.MOVIE, (new Gson()).toJson(getObject().getMovie()));

        Bundle b = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            b = ActivityOptions.makeScaleUpAnimation(view, 0, 0, view.getWidth(), view.getHeight()).toBundle();
            getContext().startActivity(intent, b);
        } else {
            getContext().startActivity(intent);
            ((Activity) getContext()).overridePendingTransition(0, 0);
        }

    }
}
