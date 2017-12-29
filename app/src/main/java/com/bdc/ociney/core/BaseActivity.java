package com.bdc.ociney.core;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.bdc.ociney.MainApplication;


/**
 * Created by florentchampigny on 07/08/2017.
 */

public class BaseActivity extends AppCompatActivity implements LifecycleRegistryOwner {

    private final LifecycleRegistry mRegistry = new LifecycleRegistry(this);
    private AdsManager adsManager;

    public LifecycleRegistry getLifecycle() {
        return this.mRegistry;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adsManager = new AdsManager(this, getLifecycle());
        //if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
        //    getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        //}
    }

    public AdsManager getAdsManager() {
        return adsManager;
    }

    public MainApplication getApp() {
        return ((MainApplication) getApplication());
    }
}
