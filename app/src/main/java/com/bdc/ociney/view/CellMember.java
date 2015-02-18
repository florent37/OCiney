package com.bdc.ociney.view;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.bdc.ociney.R;
import com.bdc.ociney.activity.StarActivity;
import com.bdc.ociney.fragment.StarFragment;
import com.bdc.ociney.modele.Person.CastMember;
import com.bdc.ociney.modele.Person.PersonFull;
import com.squareup.picasso.Picasso;

public class CellMember extends ViewCell<CastMember> implements View.OnClickListener {

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
        if (getObject() != null && getObject().getPerson() != null) {

            if (getObject().getPicture() != null && getObject().getPicture().getHref(0) != null) {
                String urlImage = getObject().getPicture().getHref(context.getResources().getInteger(R.integer.fragment_movie_casting_height));
                Picasso.with(context).load(urlImage).into(image);
            } else
                image.setImageResource(R.drawable.ic_placeholder_people);

            this.nom.setText(getObject().getPerson().getFullName());
            this.prenom.setText(getObject().getPerson().getFullName());

            this.roles.setText(getObject().getRole());

        }
    }

    @Override
    public void ajouterListeners() {
        getView().setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        try {
            Intent intent = new Intent(context, StarActivity.class);

            getObject().getPerson().setPicture(getObject().getPicture());

            PersonFull person = PersonFull.transformFull(getObject().getPerson());

            intent.putExtra(StarFragment.PERSON, (new Gson()).toJson(person));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                Bundle b = ActivityOptions.makeScaleUpAnimation(image, 0, 0, 100, 100).toBundle();

                context.startActivity(intent, b);
            } else
                context.startActivity(intent);
        } catch (Exception e) {
        }

    }
}
