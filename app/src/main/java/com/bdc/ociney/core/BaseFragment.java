package com.bdc.ociney.core;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.bdc.ociney.MainApplication;

/**
 * Created by florentchampigny on 07/08/2017.
 */

public class BaseFragment extends Fragment implements LifecycleRegistryOwner {

    private final LifecycleRegistry mRegistry = new LifecycleRegistry(this);
    private AdsManager adsManager;

    public LifecycleRegistry getLifecycle() {
        return this.mRegistry;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adsManager = new AdsManager(getContext(), getLifecycle());
    }

    public AdsManager getAdsManager() {
        return adsManager;
    }

    public MainApplication getApp() {
        return ((MainApplication) getContext().getApplicationContext());
    }
}
