package com.bdc.ociney.activity;

import android.graphics.Color;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;

import com.bdc.ociney.R;
import com.bdc.ociney.core.BaseActivity;
import com.bdc.ociney.fragment.ListCastingFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by florentchampigny on 21/04/2014.
 */
public class MovieCastingActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_movie_casting);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ab_back_mtrl_am_alpha));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLayout, ListCastingFragment.newInstance())
                .commitAllowingStateLoss();
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
