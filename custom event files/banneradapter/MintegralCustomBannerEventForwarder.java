package com.mintegral.adapter.banneradapter;

import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventBannerListener;
import com.mintegral.msdk.out.BannerAdListener;
import com.mintegral.msdk.out.MTGBannerView;

public class MintegralCustomBannerEventForwarder implements BannerAdListener {

    private CustomEventBannerListener bannerListener;
    private MTGBannerView mBannerView;
    private final String TAG = MintegralCustomBannerEventForwarder.class.getSimpleName();


    public MintegralCustomBannerEventForwarder(CustomEventBannerListener customEventBannerListener, MTGBannerView mtgBannerView) {
        bannerListener = customEventBannerListener;
        mBannerView = mtgBannerView;
    }

    @Override
    public void onLoadFailed(String s) {
        Log.e(TAG, "onLoadFailed: "+s );
        if (bannerListener != null) {
            bannerListener.onAdFailedToLoad(AdRequest.ERROR_CODE_NO_FILL);
        }
    }

    @Override
    public void onLoadSuccessed() {
        if (bannerListener != null) {
            bannerListener.onAdLoaded(mBannerView);
            mBannerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLogImpression() {

    }

    @Override
    public void onClick() {
        if (bannerListener != null) {
            bannerListener.onAdClicked();
        }
    }

    @Override
    public void onLeaveApp() {
        if (bannerListener != null) {
            bannerListener.onAdLeftApplication();
        }

    }

    @Override
    public void showFullScreen() {
        if (bannerListener != null) {
            bannerListener.onAdOpened();

        }
    }

    @Override
    public void closeFullScreen() {
        if (bannerListener != null) {
            bannerListener.onAdClosed();

        }
    }
}
