package com.mintegral.adapter.interstitialadapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitial;
import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitialListener;
import com.mintegral.msdk.MIntegralConstans;
import com.mintegral.msdk.MIntegralSDK;
import com.mintegral.msdk.out.MIntegralSDKFactory;
import com.mintegral.msdk.out.MTGInterstitialHandler;
import com.mintegral.adapter.common.AdapterTools;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by songjunjun on 17/7/4.
 */

public class MintegralCustomEventInterstitial implements CustomEventInterstitial {


    private MTGInterstitialHandler mInterstitialHandler;

    private String appId = "";
    private String appKey = "";
    private String unitId = "";
    private String packageName = "";

    MintegralCustomInterstitialEventForwarder mMintegralCustomInterstitialEventForwarder;
    @Override
    public void requestInterstitialAd(Context context, CustomEventInterstitialListener customEventInterstitialListener, String s, MediationAdRequest mediationAdRequest, Bundle bundle) {


        mMintegralCustomInterstitialEventForwarder = new MintegralCustomInterstitialEventForwarder(customEventInterstitialListener);
        parseServer(context,s);//解析服务端下发
        parseBunld(bundle);//解析传入

        MIntegralSDK sdk = MIntegralSDKFactory.getMIntegralSDK();

        if(TextUtils.isEmpty(appId) || TextUtils.isEmpty(appKey)){
            if(mMintegralCustomInterstitialEventForwarder != null){
                mMintegralCustomInterstitialEventForwarder.onInterstitialLoadFail("mobvista appid or appkey is null");
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
        mInterstitialHandler = new MTGInterstitialHandler(context, hashMap);
        mInterstitialHandler.setInterstitialListener(mMintegralCustomInterstitialEventForwarder);

        mInterstitialHandler.preload();//请求广告
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
        if(mInterstitialHandler != null){
            mInterstitialHandler.show();
        }
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
}
