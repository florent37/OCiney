package com.bdc.ociney.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bdc.ociney.R;
import com.bdc.ociney.modele.Movie.Movie;
import com.bdc.ociney.modele.Theater.Horaires;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CellMovie extends ViewCell<Movie> {

    public ImageView imageFond;
    View imageFondLayout;
    ImageView imagePoster;
    View imagePosterLayout;
    TextView titre;
    TextView duree;
    TextView genre;
    View enSalle;
    TextView enSalleDate;

    ViewGroup horraires;

    TextView ratingLabel;
    RatingBar ratingBar;

    private static void moveView(View v, float y) {


    }

    @Override
    public void construire() {

    }

    public void animer() {

        imagePosterLayout.setAlpha(0f);
        ratingBar.setAlpha(0f);
        ratingLabel.setAlpha(0f);
        titre.setAlpha(0f);
        genre.setAlpha(0f);
        duree.setAlpha(0f);

        enSalle.setAlpha(0f);
        enSalleDate.setAlpha(0f);

/*
        final AnimatorSet a = new AnimatorSet();
        a.setDuration(300);
        a.playTogether(
                ObjectAnimator.ofFloat(movingView, "alpha", 0, 1),
                ObjectAnimator.ofFloat(movingView,"translationY",-100,0)
        );

        movingView.postDelayed(new Runnable() {
            @Override
            public void run() {
                a.start();
            }
        }, 100);
        */


        AnimatorSet animLabels = new AnimatorSet().setDuration(300);
        animLabels.playTogether(
                ObjectAnimator.ofFloat(ratingBar, "alpha", 0, 1),
                ObjectAnimator.ofFloat(ratingLabel, "alpha", 0, 1),
                ObjectAnimator.ofFloat(titre, "alpha", 0, 1),
                ObjectAnimator.ofFloat(genre, "alpha", 0, 1),
                ObjectAnimator.ofFloat(duree, "alpha", 0, 1),
                ObjectAnimator.ofFloat(enSalle, "alpha", 0, 1),
                ObjectAnimator.ofFloat(enSalleDate, "alpha", 0, 1)
        );

        AnimatorSet animPoster = new AnimatorSet();
        animPoster.setDuration(300);
        animPoster.playTogether(
                ObjectAnimator.ofFloat(imagePosterLayout, "alpha", 0, 1),
                ObjectAnimator.ofFloat(imagePosterLayout, "translationX", -200, 0)

        );

        final AnimatorSet anim = new AnimatorSet();
        anim.playSequentially(animPoster, animLabels);

        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                anim.start();
            }
        }, 100);

    }

    @Override
    public void charger() {
        this.imageFond = (ImageView) getView().findViewById(R.id.imageFond);
        this.imageFondLayout = getView().findViewById(R.id.imageFondLayout);
        this.imagePoster = (ImageView) getView().findViewById(R.id.imagePoster);
        this.imagePosterLayout = getView().findViewById(R.id.imagePosterLayout);
        this.titre = (TextView) getView().findViewById(R.id.titre);
        this.duree = (TextView) getView().findViewById(R.id.duree);
        this.genre = (TextView) getView().findViewById(R.id.genre);
        this.horraires = (ViewGroup) getView().findViewById(R.id.horraires);

        this.ratingLabel = (TextView) getView().findViewById(R.id.ratingUserNum);
        this.ratingBar = (RatingBar) getView().findViewById(R.id.ratingUserStar);

        this.enSalle = getView().findViewById(R.id.enSalleLe);
        this.enSalleDate = (TextView) getView().findViewById(R.id.enSalleLeValue);

    }

    @Override
    public void remplir() {
        if (getObject() != null) {
            String urlFond = getObject().getUrlBandeAnnonce(context.getResources().getInteger(R.integer.cell_movie_fond_height));
            if (urlFond == null)
                urlFond = getObject()
                        .getUrlPoster(context.getResources().getInteger(R.integer.cell_movie_fond_height));
            Picasso.with(context).load(getObject()
                    .getUrlPoster(context.getResources().getInteger(R.integer.cell_movie_poster_height)))
                    .into(imagePoster);
            Picasso.with(context).load(urlFond).into(imageFond);
            //Picasso.with(context).load(getObject().getPoster().getHref()).transform(new BlurTransformation()).into(imageFond);

            if (getObject().getTitle() != null)
                this.titre.setText(getObject().getTitle());

            duree.setText(getObject().getDuree());
            genre.setText(getObject().getGenres());

            chargerRating();


            chargerHorraires();

            tournerRoulette();
        }
    }

    private void chargerRating() {
        float rating = (getObject().getUserRating() + getObject().getPressRating()) / 2;
        if (rating == 0) {
            ratingLabel.setVisibility(View.GONE);
            ratingBar.setVisibility(View.GONE);

            if (getObject().getRelease() != null) {
                try {
                    String date = getObject().getRelease().getReleaseDate();
                    int annee = Integer.parseInt(date.split("/")[2]);
                    int anneeEncours = GregorianCalendar.getInstance().get(Calendar.YEAR);

                    if (annee >= anneeEncours) {
                        enSalle.setVisibility(View.VISIBLE);
                        enSalleDate.setText(getObject().getRelease().getReleaseDate());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            enSalle.setVisibility(View.GONE);
            ratingLabel.setVisibility(View.VISIBLE);
            ratingBar.setVisibility(View.VISIBLE);
            ratingLabel.setText("(" + String.format("%.1f", rating) + ")");
            ratingBar.setRating(rating);
            ratingBar.setIsIndicator(true);
        }
    }

    private void chargerHorraires() {
        if (getObject().getHoraires() != null && getObject().getHoraires().size() > 0) {
            for (Horaires h : getObject().getHoraires()) {
                if (h.isToday()) {

                    View vueHorraires = View.inflate(context, R.layout.cell_movie_horraires, null);
                    TextView texte = (TextView) vueHorraires.findViewById(R.id.text);

                    StringBuffer sb = new StringBuffer();

                    sb.append(h.getFormatEcran()).append(" ");
                    if (h.getVersion() != null)
                        sb.append(h.getVersion()).append(" ");

                    for (String seance : h.getSeances())
                        sb.append(seance).append(" ");

                    texte.setText(sb.toString());

                    horraires.addView(vueHorraires);
                }
            }
        }
    }

    protected void tournerRoulette() {
        int previousDegrees = 0;
        int degrees = 360;
        RotateAnimation animation = new RotateAnimation(previousDegrees, degrees,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setFillEnabled(true);
        animation.setFillAfter(true);
        animation.setRepeatMode(Animation.INFINITE);
        animation.setRepeatCount(3);
        animation.setDuration(2000);//Set the duration of the animation to 1 sec.
        getView().findViewById(R.id.placeholder_image).startAnimation(animation);
    }

    @Override
    public void ajouterListeners() {

    }

    @Override
    public void onScroll(float yOffset) {
        // imageFond.setY(yOffset);
        if (imageFond != null)
            imageFond.setTranslationY(yOffset);
    }

}
