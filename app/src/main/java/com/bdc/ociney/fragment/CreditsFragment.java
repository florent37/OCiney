package com.bdc.ociney.fragment;

import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bdc.ociney.BuildConfig;
import com.bdc.ociney.R;
import com.bdc.ociney.core.BaseActivity;
import com.bdc.ociney.fragment.core.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CreditsFragment extends BaseFragment implements View.OnTouchListener {

    @BindView(R.id.image0) View image0;
    @BindView(R.id.image1) View image1;
    @BindView(R.id.image2) View image2;
    @BindView(R.id.version) TextView version;

    public static CreditsFragment newInstance() {
        Bundle args = new Bundle();
        CreditsFragment fragment = new CreditsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.credits, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setFragmentView(view);

        ((BaseActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        ((BaseActivity)getActivity()).getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        charger();
        remplir();
        ajouterListeners();
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