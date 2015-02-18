package com.bdc.ociney.view;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bdc.ociney.R;
import com.bdc.ociney.modele.Person.PersonFull;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class CellStar extends ViewCell<PersonFull> {

    ImageView image;
    TextView nameStar;
    TextView role;
    ImageView top_rank;
    View couronne;

    @Override
    public void construire() {

    }

    @Override
    public void animer() {

    }

    @Override
    public void charger() {
        this.image = (ImageView) getView().findViewById(R.id.imageFond);
        this.nameStar = (TextView) getView().findViewById(R.id.nameStar);
        this.role = (TextView) getView().findViewById(R.id.role);
        this.couronne = getView().findViewById(R.id.couronne);
        this.top_rank = (ImageView) getView().findViewById(R.id.ic_topstar);
    }

    @Override
    public void remplir() {

        this.couronne.setVisibility(View.GONE);
        this.top_rank.setVisibility(View.GONE);

        image.setImageResource(R.drawable.ic_placeholder_people);

        if (getObject() != null) {

            if (getObject().getPicture() != null && getObject().getPicture().getHref(0) != null) {
                String urlFond = getObject().getPicture().getHref(context.getResources().getInteger(R.integer.fragment_movie_fond_personnality_height));

                Picasso.with(context).load(urlFond).into(image);
            } else
                image.setImageResource(R.drawable.ic_placeholder_people);

            if (getObject().getFullName() != null) {
                String name = "";

                if (getObject().getFullName().getGiven() != null)
                    name += getObject().getFullName().getGiven() + " ";


                if (getObject().getFullName().getFamily() != null)
                    name += getObject().getFullName().getFamily();

                this.nameStar.setText(name);
            } else
                this.nameStar.setText("");

            if (getObject().getStatistics() != null) {
                int top_rank = getObject().getStatistics().getRankTopStar();

                switch (top_rank) {
                    case 1:
                        this.couronne.setVisibility(View.VISIBLE);
                        this.top_rank.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        this.top_rank.setVisibility(View.VISIBLE);
                        this.top_rank.setImageResource(R.drawable.ic_topstar_2);
                        break;

                    case 3:
                        this.top_rank.setVisibility(View.VISIBLE);
                        this.top_rank.setImageResource(R.drawable.ic_topstar_3);
                        break;
                }

            }
            try {
                String activities = getObject().getActivities();

                if (activities.trim().length() > 0) {
                    this.role.setText(activities);
                } else {
                    this.role.setText(getContext().getString(R.string.activite_non_renseigne));
                }
            } catch (Exception e) {
                this.role.setText(getContext().getString(R.string.activite_non_renseigne));
            }

        } else {
        }
    }

    @Override
    public void ajouterListeners() {

    }

    private class CropPosterTransformation implements Transformation {

        int targetWidth;

        public CropPosterTransformation(int width) {
            this.targetWidth = width;
        }

        @Override
        public Bitmap transform(Bitmap source) {
            double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
            int targetHeight = (int) (targetWidth * aspectRatio);
            Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
            if (result != source) {
                // Same bitmap is returned if sizes are the same
                source.recycle();
            }
            return result;
        }

        @Override
        public String key() {
            return "cropPosterTransformation " + targetWidth;
        }
    }
}
