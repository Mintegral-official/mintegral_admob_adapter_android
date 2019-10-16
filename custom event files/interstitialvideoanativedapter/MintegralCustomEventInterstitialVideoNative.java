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
import com.mintegral.msdk.MIntegralConstans;
import com.mintegral.msdk.MIntegralSDK;
import com.mintegral.msdk.interstitialvideo.out.InterstitialVideoListener;
import com.mintegral.msdk.interstitialvideo.out.MTGInterstitialVideoHandler;
import com.mintegral.msdk.out.MIntegralSDKFactory;
import com.mintegral.adapter.common.AdapterTools;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by songjunjun on 17/7/4.
 */

public class MintegralCustomEventInterstitialVideoNative implements CustomEventInterstitial,InterstitialVideoListener {


    private MTGInterstitialVideoHandler mInterstitialHandler;

    private String appId = "";
    private String appKey = "";
    private String unitId = "";
    private String mRewardId = "";
    private String packageName = "";


    private CustomEventInterstitialListener mCustomEventInterstitialListener;




    @Override
    public void requestInterstitialAd(Context context, CustomEventInterstitialListener customEventInterstitialListener, String s, MediationAdRequest mediationAdRequest, Bundle bundle) {
        mCustomEventInterstitialListener = customEventInterstitialListener;
        parseServer(context,s);//解析服务端下发
        parseBunld(bundle);//解析传入
        MIntegralSDK sdk = MIntegralSDKFactory.getMIntegralSDK();
        if(TextUtils.isEmpty(appId) || TextUtils.isEmpty(appKey)){
            if(customEventInterstitialListener != null){
                customEventInterstitialListener.onAdFailedToLoad(AdRequest.ERROR_CODE_INVALID_REQUEST);
            }
            return;
        }
        Map<String, String> map = sdk.getMTGConfigurationMap(appId, appKey);
        if(!TextUtils.isEmpty(packageName)){
            map.put(MIntegralConstans.PACKAGE_NAME_MANIFEST,packageName);
        }
        AdapterTools.addChannel();
        sdk.init(map, context);

        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        // 设置广告位ID
        hashMap.put(MIntegralConstans.PROPERTIES_UNIT_ID, unitId);
        if(context instanceof Activity){
            mInterstitialHandler = new MTGInterstitialVideoHandler((Activity)context, unitId);
            mInterstitialHandler.setRewardVideoListener(this);
        }else{
            return;
        }
        mInterstitialHandler.load();
    }


    private void parseServer(Context context,String s){
        JSONObject jo ;
        if(!TextUtils.isEmpty(s)){
            try{
                jo = new JSONObject(s);
                if(jo != null){
                    appId = jo.getString("appId");
                    appKey = jo.getString("appKey");
                    unitId = jo.getString("unitId");
                    AdapterTools.pareseAuthority(context,jo);
                }
            }catch (Exception e){
                Log.e("",e.getMessage(),e);
            }
        }
    }


    private void parseBunld(Bundle bundle){
        if(bundle != null && bundle.get("packageName") != null){
            packageName = bundle.get("packageName").toString();
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
    public void onVideoLoadSuccess(String s) {
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
    public void onVideoAdClicked(String s) {
        mCustomEventInterstitialListener.onAdClicked();
    }

    @Override
    public void onLoadSuccess(String s){

    }

    @Override
    public void onEndcardShow(String s) {

    }

    @Override
    public void onVideoComplete(String s) {

    }
}
