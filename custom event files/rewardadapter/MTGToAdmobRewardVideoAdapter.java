package com.mintegral.adapter.rewardadapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.reward.mediation.MediationRewardedVideoAdAdapter;
import com.google.android.gms.ads.reward.mediation.MediationRewardedVideoAdListener;
import com.mintegral.msdk.MIntegralConstans;
import com.mintegral.msdk.MIntegralSDK;
import com.mintegral.msdk.out.MIntegralSDKFactory;
import com.mintegral.msdk.out.MTGRewardVideoHandler;
import com.mintegral.adapter.common.AdapterTools;


import org.json.JSONObject;

import java.util.Map;

/**
 * Created by songjunjun on 17/3/20.
 */

public class MTGToAdmobRewardVideoAdapter implements MediationRewardedVideoAdAdapter {

    private String TAG = "MTGToAdmobRewardVideoAdapter";

    private MTGRewardVideoHandler mMvRewardVideoHandler;
    private String mAPPID="";
    private String mAPPKey = "";
    private String mRewardUnitId = "";
    private String mRewardId = "";
    private String mUserId = "";
    private String mPackageName = "";


    private MediationRewardVideoEventForwarder mediationRewardVideoEventForwarder;







    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

    }


    private void parseAuthority(JSONObject jsonObject){
        if(jsonObject == null){
            return;
        }
        try {

        }catch (Throwable t){
            Log.e(TAG,t.getMessage(),t);
        }
    }

    private void parseServiceString(Context context,String serviceString){
        if(TextUtils.isEmpty(serviceString)){
            return;
        }
        try{
            JSONObject jsonObject = new JSONObject(serviceString);
            String appId = jsonObject.optString("appId");
            String appKey = jsonObject.optString("appKey");
            String unitId = jsonObject.optString("unitId");
            String rewardId = jsonObject.optString("rewardId");


            if(!TextUtils.isEmpty(appId) ){
                mAPPID = appId;
            }

            if(!TextUtils.isEmpty(appKey) ){

                mAPPKey = appKey;
            }

            if(!TextUtils.isEmpty(unitId)){
                mRewardUnitId = unitId;
            }

            if(!TextUtils.isEmpty(rewardId)){
                mRewardId = rewardId;
            }
            AdapterTools.pareseAuthority(context,jsonObject);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(Context context, MediationAdRequest mediationAdRequest, String s, MediationRewardedVideoAdListener mediationRewardedVideoAdListener, Bundle bundle, Bundle bundle1) {

        String  serviceString = bundle.getString(MediationRewardedVideoAdAdapter.CUSTOM_EVENT_SERVER_PARAMETER_FIELD);
        parseServiceString(context,serviceString);


        if(bundle1!= null){
            if(!TextUtils.isEmpty(bundle1.getCharSequence("userId"))){
                mUserId = bundle1.getCharSequence("userId").toString();
            }

            if(!TextUtils.isEmpty(bundle1.getCharSequence("packageName"))){
                mPackageName = bundle1.getCharSequence("packageName").toString();
            }
        }
        MIntegralSDK sdk = MIntegralSDKFactory.getMIntegralSDK();
        if(TextUtils.isEmpty(mAPPID) || TextUtils.isEmpty(mAPPKey)){
            mediationRewardedVideoAdListener.onInitializationFailed(this,AdRequest.ERROR_CODE_INVALID_REQUEST);
            return;
        }
        Map<String, String> map = sdk.getMTGConfigurationMap(mAPPID, mAPPKey);


        if(!TextUtils.isEmpty(mPackageName)){
            map.put(MIntegralConstans.PACKAGE_NAME_MANIFEST, mPackageName);
        }
        AdapterTools.addChannel();
        sdk.init(map, context.getApplicationContext());

        if(context instanceof Activity){
            mMvRewardVideoHandler = new MTGRewardVideoHandler((Activity)context, mRewardUnitId);
           mediationRewardVideoEventForwarder = new MediationRewardVideoEventForwarder(mediationRewardedVideoAdListener,this);
            mMvRewardVideoHandler.setRewardVideoListener(mediationRewardVideoEventForwarder);
            mediationRewardedVideoAdListener.onInitializationSucceeded(this);
        }else{
            mediationRewardedVideoAdListener.onInitializationFailed(this,AdRequest.ERROR_CODE_INVALID_REQUEST);
            return;
        }

    }

    @Override
    public boolean isInitialized() {
        return mMvRewardVideoHandler == null ? false:true;
    }

    @Override
    public void loadAd(MediationAdRequest mediationAdRequest, Bundle bundle, Bundle bundle1) {


            mMvRewardVideoHandler.load();
    }

    @Override
    public void showVideo() {

            if (mMvRewardVideoHandler.isReady()) {
                mMvRewardVideoHandler.show(mRewardId, mUserId);
            }
    }
    public boolean canShow() {
        if(mMvRewardVideoHandler != null){
            return mMvRewardVideoHandler.isReady();
        }else{
            return false;
        }

    }

}


