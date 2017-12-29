package com.bdc.ociney;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.github.florent37.androidanalytics.Analytics;
import com.github.florent37.androidanalytics.google.GoogleAnalyticsProvider;
import com.google.android.gms.ads.MobileAds;

import io.fabric.sdk.android.Fabric;

public class MainApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        MobileAds.initialize(this, getString(R.string.admob_app));

        Analytics.registerProvider(
                new GoogleAnalyticsProvider(this, R.xml.global_tracker)
        );
    }
}
