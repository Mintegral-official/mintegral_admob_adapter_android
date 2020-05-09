package com.mintegral.adapter.interstitialvideoanativedapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitial;
import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitialListener;
import com.mintegral.adapter.common.AdapterTools;
import com.mintegral.adapter.manager.MintegralHandlerManager;
import com.mintegral.msdk.MIntegralConstans;
import com.mintegral.msdk.MIntegralSDK;
import com.mintegral.msdk.interstitialvideo.out.InterstitialVideoListener;
import com.mintegral.msdk.interstitialvideo.out.MTGInterstitialVideoHandler;
import com.mintegral.msdk.out.MIntegralSDKFactory;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by songjunjun on 17/7/4.
 */

public class MintegralCustomEventInterstitialVideoNative implements CustomEventInterstitial, InterstitialVideoListener {


    private MTGInterstitialVideoHandler mInterstitialHandler;

    private String mAppId = "";
    private String mAppKey = "";
    private String mUnitId = "";
    private String mRewardId = "";
    private String mPackageName = "";
    private String mPlacementId = "";
    private static String TAG = MintegralCustomEventInterstitialVideoNative.class.getSimpleName();

    private CustomEventInterstitialListener mCustomEventInterstitialListener;
    private static boolean hasInitMintegralSDK = false;


    @Override
    public void requestInterstitialAd(Context context, CustomEventInterstitialListener customEventInterstitialListener, String s, MediationAdRequest mediationAdRequest, Bundle bundle) {
        Log.e(TAG, "hasInitMintegralSDK:" + "requestInterstitialAd。");
        mCustomEventInterstitialListener = customEventInterstitialListener;
        parseServer(context, s);//解析服务端下发
        parseBunld(bundle);//解析传入

        if (TextUtils.isEmpty(mAppId) || TextUtils.isEmpty(mAppKey)) {
            if (customEventInterstitialListener != null) {
                customEventInterstitialListener.onAdFailedToLoad(AdRequest.ERROR_CODE_INVALID_REQUEST);
            }
            return;
        }

        if (!hasInitMintegralSDK) {
            AdapterTools.addChannel();
            MIntegralSDK sdk = MIntegralSDKFactory.getMIntegralSDK();

            Map<String, String> map = sdk.getMTGConfigurationMap(mAppId, mAppKey);

            sdk.init(map, context.getApplicationContext());
            hasInitMintegralSDK = true;
            Log.e(TAG, "hasInitMintegralSDK:" + hasInitMintegralSDK);
        }


        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        // 设置广告位ID
        hashMap.put(MIntegralConstans.PROPERTIES_UNIT_ID, mUnitId);
        if (context instanceof Activity) {

            mInterstitialHandler = MintegralHandlerManager.getInstance().getMTGInterstitialVideoHandler(mUnitId);
            if (mInterstitialHandler == null) {
                mInterstitialHandler = new MTGInterstitialVideoHandler((Activity) context, mPlacementId, mUnitId);
                MintegralHandlerManager.getInstance().addMTGInterstitialVideoHandler(mUnitId, mInterstitialHandler);
            }

            if (mInterstitialHandler != null) {
                mInterstitialHandler.setRewardVideoListener(this);
            }
        } else {
            return;
        }
        mInterstitialHandler.load();
    }


    private void parseServer(Context context, String s) {
        JSONObject jo;
        if (!TextUtils.isEmpty(s)) {
            try {
                jo = new JSONObject(s);
                if (jo != null) {
                    String appId = jo.getString("appId");
                    String appKey = jo.getString("appKey");
                    String unitId = jo.getString("unitId");
                    String placementId = jo.optString("placementId");

                    if (!TextUtils.isEmpty(appId)) {
                        mAppId = appId;
                    }

                    if (!TextUtils.isEmpty(appKey)) {

                        mAppKey = appKey;
                    }

                    if (!TextUtils.isEmpty(unitId)) {
                        mUnitId = unitId;
                    }


                    if (!TextUtils.isEmpty(placementId)) {
                        mPlacementId = placementId;
                    }
                }
            } catch (Exception e) {
                Log.e("", e.getMessage(), e);
            }


        }
    }


    private void parseBunld(Bundle bundle) {
        if (bundle != null && bundle.get("packageName") != null) {
            mPackageName = bundle.get("packageName").toString();
        }
    }

    @Override
    public void showInterstitial() {

        mInterstitialHandler.show();

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }


    @Override
    public void onVideoLoadSuccess(String placementID, String s) {
        mCustomEventInterstitialListener.onAdLoaded();
    }

    @Override
    public void onVideoLoadFail(String s) {
        mCustomEventInterstitialListener.onAdFailedToLoad(AdRequest.ERROR_CODE_NO_FILL);
    }

    @Override
    public void onAdShow() {
        mCustomEventInterstitialListener.onAdOpened();
    }

    @Override
    public void onAdClose(boolean isCompleteView) {
        mCustomEventInterstitialListener.onAdClosed();
    }

    @Override
    public void onShowFail(String s) {

    }

    @Override
    public void onVideoAdClicked(String placementID, String s) {
        mCustomEventInterstitialListener.onAdClicked();
    }

    @Override
    public void onLoadSuccess(String placementID, String s) {

    }

    @Override
    public void onEndcardShow(String placementID, String s) {

    }

    @Override
    public void onVideoComplete(String placementID, String s) {

    }

    @Override
    public void onAdCloseWithIVReward(boolean b, int i) {

    }
}
