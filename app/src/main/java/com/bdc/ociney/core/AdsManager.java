package com.bdc.ociney.core;

import android.arch.lifecycle.LifecycleRegistry;
import android.content.Context;
import android.view.View;

import com.bdc.ociney.BuildConfig;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.ArrayList;
import java.util.List;

import florent37.github.com.rxlifecycle.RxLifecycle;
import io.reactivex.disposables.CompositeDisposable;

public class AdsManager {

    private final Context context;
    private final LifecycleRegistry lifecycleRegistry;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    protected List<AdView> adViewList = new ArrayList<>();
    private boolean showAdsOnDebug = false;

    public AdsManager(Context context, LifecycleRegistry lifecycleRegistry) {
        this.context = context;
        this.lifecycleRegistry = lifecycleRegistry;

        RxLifecycle.with(lifecycleRegistry)
                .onResume()
                .doOnSubscribe(compositeDisposable::add)
                .subscribe(resume -> this.onResume());

        RxLifecycle.with(lifecycleRegistry)
                .onPause()
                .doOnSubscribe(compositeDisposable::add)
                .subscribe(resume -> this.onPause());

        RxLifecycle.with(lifecycleRegistry)
                .onDestroy()
                .doOnSubscribe(compositeDisposable::add)
                .subscribe(resume -> this.onDestroy());
    }

    public AdsManager showInterstitial(final int stringId, final AdClosedListener adCloseListener) {
        showInterstitial(context.getString(stringId), adCloseListener);
        return this;
    }

    public AdsManager showInterstitial(final String id, final AdClosedListener adCloseListener) {
        if (BuildConfig.DEBUG && !showAdsOnDebug) {
            adCloseListener.onAdClosed();
        } else {
            final InterstitialAd interstitialAd = new InterstitialAd(context);
            final AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
            adRequestBuilder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
            if (BuildConfig.DEBUG) {
                adRequestBuilder.addTestDevice(DeviceIdFounder.getDeviceId(context));
            }
            interstitialAd.setAdUnitId(id);
            interstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();

                    interstitialAd.show();
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    if (adCloseListener != null) {
                        adCloseListener.onAdClosed();
                    }
                }

                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    if (adCloseListener != null) {
                        adCloseListener.onAdClosed();
                    }
                }
            });
            interstitialAd.loadAd(adRequestBuilder.build());
        }
        return this;
    }

    public AdsManager executeAdView(AdView adView) {
        if (BuildConfig.DEBUG && !showAdsOnDebug) {
            adView.setVisibility(View.GONE);
        } else {
            final AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
            if (BuildConfig.DEBUG) {
                adRequestBuilder.addTestDevice(DeviceIdFounder.getDeviceId(context));
            }
            adView.loadAd(adRequestBuilder.build());
            adViewList.add(adView);
        }
        return this;
    }

    public AdsManager showRewardedVideo(final int stringId, final AdVideoClosedListener adVideoClosedListener) {
        if (BuildConfig.DEBUG && !showAdsOnDebug) {
            adVideoClosedListener.onAdClosedWithReward();
        } else {
            final RewardedVideoAd mAd = MobileAds.getRewardedVideoAdInstance(context);
            mAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {

                boolean rewarded = false;

                @Override
                public void onRewardedVideoAdLoaded() {
                    mAd.show();
                }

                @Override
                public void onRewardedVideoAdOpened() {

                }

                @Override
                public void onRewardedVideoStarted() {

                }

                @Override
                public void onRewardedVideoAdClosed() {
                    if (rewarded) {
                        adVideoClosedListener.onAdClosedWithReward();
                    } else {
                        adVideoClosedListener.onAdClosedWithoutReward();
                    }
                }

                @Override
                public void onRewarded(RewardItem rewardItem) {
                    rewarded = true;
                }

                @Override
                public void onRewardedVideoAdLeftApplication() {

                }

                @Override
                public void onRewardedVideoAdFailedToLoad(int i) {

                }
            });

            final AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
            adRequestBuilder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
            if (BuildConfig.DEBUG) {
                adRequestBuilder.addTestDevice(DeviceIdFounder.getDeviceId(context));
            }

            mAd.loadAd(context.getString(stringId), adRequestBuilder.build());
        }
        return this;
    }

    public void onResume() {

        for (AdView adView : adViewList) {
            adView.resume();
        }
    }

    public void onPause() {
        for (AdView adView : adViewList) {
            adView.pause();
        }
    }

    public void onDestroy() {
        for (AdView adView : adViewList) {
            adView.destroy();
        }

        compositeDisposable.clear();
    }

    public interface AdClosedListener {
        void onAdClosed();
    }

    public interface AdVideoClosedListener {
        void onAdClosedWithReward();

        void onAdClosedWithoutReward();
    }
}
