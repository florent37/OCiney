package com.bdc.ociney.fragment.core;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

import com.bdc.ociney.R;

/**
 * Created by florentchampigny on 20/04/2014.
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    protected View fragmentView;
    boolean tournerRoulette = false;

    public void setFragmentView(View fragmentView) {
        this.fragmentView = fragmentView;
    }

    public View findViewById(int id) {
        if (fragmentView != null)
            return fragmentView.findViewById(id);
        else
            return null;
    }

    protected void charger(){}

    protected void remplir(){}

    protected void ajouterListeners(){}

    public void search(String text) {
    }

    public void afficherVide(boolean afficher) {
        try {
            if (afficher)
                fragmentView.findViewById(R.id.empty).setVisibility(View.VISIBLE);
            else
                fragmentView.findViewById(R.id.empty).setVisibility(View.INVISIBLE);
        } catch (Exception e) {
        }
    }

    protected void tournerRoulette(boolean tourner) {
        tournerRoulette(tourner, R.id.placeholder_image);
    }

    protected void tournerRoulette(boolean tourner, int id) {

        final View roulette = findViewById(id);

        if (roulette != null) {
            if (tourner) {
                roulette.setVisibility(View.VISIBLE);
                int previousDegrees = 0;
                int degrees = 360;
                final RotateAnimation animation = new RotateAnimation(previousDegrees, degrees,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setFillEnabled(true);
                animation.setFillAfter(true);
                animation.setDuration(1500);//Set the duration of the animation to 1 sec.
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (tournerRoulette) {
                            roulette.startAnimation(animation);
                        } else
                            roulette.animate().alpha(0).start();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                roulette.startAnimation(animation);
            } else {
                tournerRoulette = false;
            }
        }
    }

    public void onErreurReseau() {
        //Crouton.makeText(getActivity(), R.string.erreur_reseau, Style.ALERT).show();
        Toast.makeText(getActivity(), R.string.erreur_reseau, Toast.LENGTH_SHORT).show();
    }
}
