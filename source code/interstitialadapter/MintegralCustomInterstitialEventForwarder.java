package com.mintegral.adapter.interstitialadapter;

import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitialListener;
import com.mintegral.msdk.out.InterstitialListener;

/**
 * Created by songjunjun on 17/7/4.
 * 1、mobvista没有onAdLeftApplication，所以没有实现
 * 2、错误的时候mobvista返回的是string，无法和code匹配
 */

public class MintegralCustomInterstitialEventForwarder implements InterstitialListener {

    String TAG = this.getClass().getName();
    private CustomEventInterstitialListener mInterstitialListener;


    public MintegralCustomInterstitialEventForwarder(CustomEventInterstitialListener listener){

        this.mInterstitialListener = listener;
    }


    @Override
    public void onInterstitialShowSuccess() {
        Log.e(TAG, "onInterstitialShowSuccess");
        mInterstitialListener.onAdOpened();
    }

    @Override
    public void onInterstitialShowFail(String errorMsg) {
        Log.e(TAG, "onInterstitialShowFail errorMsg:" + errorMsg);

    }

    @Override
    public void onInterstitialLoadSuccess() {
        Log.e(TAG, "onInterstitialLoadSuccess");
        mInterstitialListener.onAdLoaded();
    }

    @Override
    public void onInterstitialLoadFail(String errorMsg) {
        Log.e(TAG, "onInterstitialLoadFail errorMsg:" + errorMsg);
        mInterstitialListener.onAdFailedToLoad(AdRequest.ERROR_CODE_INTERNAL_ERROR);//我们没有对应的code就写死了
    }

    @Override
    public void onInterstitialClosed() {
        Log.e(TAG, "onInterstitialClosed");
        mInterstitialListener.onAdClosed();
    }

    @Override
    public void onInterstitialAdClick() {
        mInterstitialListener.onAdLeftApplication();
        Log.e(TAG, "onInterstitialAdClick");
    }
}
