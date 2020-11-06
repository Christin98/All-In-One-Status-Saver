package com.kcdeveloperss.status.downloader.util;

import android.content.Context;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.kcdeveloperss.status.downloader.R;

public class AdsUtils {
    public static void showGoogleBannerAd(Context context, AdView adView) {
        adView.setVisibility(View.VISIBLE);
        MobileAds.initialize(context, initializationStatus -> {

        });
        adView.loadAd(new Builder().build());
    }

    public static void showGoogleInterstitialAd(Context context) {
        MobileAds.initialize(context, initializationStatus -> {

        });
        final InterstitialAd interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdUnitId(context.getResources().getString(R.string.admob_interstitial_ad));
        interstitialAd.loadAd(new Builder().build());
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {

            }

            @Override
            public void onAdClosed() {

            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {

            }

            @Override
            public void onAdOpened() {

            }

            @Override
            public void onAdLoaded() {
                interstitialAd.show();
            }
        });
    }
}
