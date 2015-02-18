package com.bdc.ociney.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bdc.ociney.R;
import com.bdc.ociney.modele.Participation;
import com.squareup.picasso.Picasso;

public class CellFilmo extends ViewCell<Participation> implements View.OnClickListener {

    ImageView image;
    TextView nom;
    TextView prenom;
    TextView roles;

    @Override
    public void construire() {
    }

    @Override
    public void animer() {

    }

    @Override
    public void charger() {
        this.image = (ImageView) getView().findViewById(R.id.image);

        this.nom = (TextView) getView().findViewById(R.id.nom);
        this.prenom = (TextView) getView().findViewById(R.id.prenom);
        this.roles = (TextView) getView().findViewById(R.id.roles);
    }

    @Override
    public void remplir() {

        if (getObject() != null) {

            ((ImageView) getView().findViewById(R.id.ic_placeholder)).setImageResource(R.drawable.ic_drawer_films);

            if (getObject().getMovie() != null && getObject().getMovie().getPoster() != null) {
                String urlImage = getObject().getMovie().getUrlPoster(context.getResources().getInteger(R.integer.fragment_movie_casting_height));
                Picasso.with(context).load(urlImage).into(image);
            } else
                image.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_drawer_films));

            String title = getObject().getMovie().getTitle();

            if (title != null && !title.trim().equalsIgnoreCase("")) {
                this.nom.setText(title);
                this.prenom.setText(title);
            } else {
                this.nom.setText(getObject().getMovie().getOriginalTitle());
                this.prenom.setText(getObject().getMovie().getOriginalTitle());
            }

            if (getObject().getRole() != null && !getObject().getRole().trim().equalsIgnoreCase(""))
                this.roles.setText(getObject().getRole());
            else
                this.roles.setText(context.getString(R.string.role_non_renseigne));


        } else {
        }
    }

    @Override
    public void ajouterListeners() {
        getView().setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {


    }
}
