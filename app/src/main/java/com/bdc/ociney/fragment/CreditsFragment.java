package com.bdc.ociney.fragment;

import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bdc.ociney.BuildConfig;
import com.bdc.ociney.R;
import com.bdc.ociney.fragment.core.BetterFragment;


public class CreditsFragment extends BetterFragment implements View.OnTouchListener {

    View image0, image1, image2;
    TextView version;

    public CreditsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.credits,
                container, false);

        setFragmentView(view);

        ((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        charger();
        remplir();
        ajouterListeners();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void charger() {
        image0 = findViewById(R.id.image0);
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        version = (TextView) findViewById(R.id.version);
    }

    @Override
    protected void remplir() {

        try {
            version.setText(BuildConfig.VERSION_NAME);
        }catch (Exception e){}

        long duree = 900;


        image0.setAlpha(0);
        image1.setAlpha(0);
        image2.setAlpha(0);

        image0.animate().alpha(1f).setDuration(duree).start();
        image1.animate().alpha(1f).setDuration(duree).start();
        image2.animate().alpha(1f).setDuration(duree).start();

        float distance = 400;

        ObjectAnimator a = ObjectAnimator.ofFloat(image0, "translationY", distance, 0);
        a.setDuration(duree).start();
        a = ObjectAnimator.ofFloat(image1, "translationY", distance, 0).setDuration(duree);
        a.setStartDelay(100);
        a.start();
        a = ObjectAnimator.ofFloat(image2, "translationY", distance, 0).setDuration(duree);
        a.setStartDelay(200);
        a.start();
    }

    @Override
    protected void ajouterListeners() {
        image0.setOnClickListener(this);
        image1.setOnClickListener(this);
        image2.setOnClickListener(this);

        image0.setOnTouchListener(this);
        image1.setOnTouchListener(this);
        image2.setOnTouchListener(this);
    }

    public void onClick(final View view) {

        String url = null;

        view.animate().scaleX(1.2f).scaleY(1.2f).setDuration(100).start();
        view.animate().scaleX(1).scaleY(1).setDuration(100).setStartDelay(110).start();

        switch (view.getId()) {
            case R.id.image0:
                url = "https://plus.google.com/+florentchampigny";
                break;
            case R.id.image1:
                url = "https://plus.google.com/+DeJesusFerreiraK%C3%A9vin";
                break;
            case R.id.image2:
                url = "https://plus.google.com/+LoganBOURGOIN";
                break;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        Bundle b = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            b = ActivityOptions.makeScaleUpAnimation(view, 0, 0, view.getWidth(), view.getHeight()).toBundle();

            getActivity().startActivity(intent, b);
        } else {
            getActivity().startActivity(intent);
            getActivity().overridePendingTransition(0, 0);
        }

    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                view.animate().scaleX(1.2f).scaleY(1.2f).setDuration(100).start();
            }
            break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                view.animate().scaleX(1).scaleY(1).setDuration(100).start();
            }
            break;
        }
        return false;
    }
}