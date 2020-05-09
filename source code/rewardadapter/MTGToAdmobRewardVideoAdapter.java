package com.mintegral.adapter.rewardadapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.reward.mediation.MediationRewardedVideoAdAdapter;
import com.google.android.gms.ads.reward.mediation.MediationRewardedVideoAdListener;
import com.mintegral.adapter.common.AdapterTools;
import com.mintegral.adapter.manager.MintegralHandlerManager;
import com.mintegral.msdk.MIntegralConstans;
import com.mintegral.msdk.MIntegralSDK;
import com.mintegral.msdk.out.MIntegralSDKFactory;
import com.mintegral.msdk.out.MTGRewardVideoHandler;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by songjunjun on 17/3/20.
 */

public class MTGToAdmobRewardVideoAdapter implements MediationRewardedVideoAdAdapter {

    private String TAG = "MTGToAdmobRewardVideoAdapter";

    private MTGRewardVideoHandler mMvRewardVideoHandler;
    private String mAPPID = "";
    private String mAPPKey = "";
    private String mRewardUnitId = "";
    private String mRewardId = "";
    private String mUserId = "";
    private String mPlacementId = "";
    static boolean hasInitMintegralSDK;
    private Context mContext;
    private MediationRewardedVideoAdListener mMediationRewardedVideoAdListener;
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


    private void parseAuthority(JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        try {

        } catch (Throwable t) {
            Log.e(TAG, t.getMessage(), t);
        }
    }

    private void parseServiceString(String serviceString) {
        if (TextUtils.isEmpty(serviceString)) {
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(serviceString);
            String appId = jsonObject.optString("appId");
            String appKey = jsonObject.optString("appKey");
            String unitId = jsonObject.optString("unitId");
            String rewardId = jsonObject.optString("rewardId");
            String placementId = jsonObject.optString("placementId");


            if (!TextUtils.isEmpty(appId)) {
                mAPPID = appId;
            }

            if (!TextUtils.isEmpty(appKey)) {

                mAPPKey = appKey;
            }

            if (!TextUtils.isEmpty(unitId)) {
                mRewardUnitId = unitId;
            }

            if (!TextUtils.isEmpty(rewardId)) {
                mRewardId = rewardId;
            }

            if (!TextUtils.isEmpty(placementId)) {
                mPlacementId = placementId;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(Context context, MediationAdRequest mediationAdRequest, String s, MediationRewardedVideoAdListener mediationRewardedVideoAdListener, Bundle bundle, Bundle bundle1) {
        mContext = context;
        mMediationRewardedVideoAdListener = mediationRewardedVideoAdListener;

        String serviceString = bundle.getString(MediationRewardedVideoAdAdapter.CUSTOM_EVENT_SERVER_PARAMETER_FIELD);
        parseServiceString(serviceString);

        if (bundle1 != null) {
            if (!TextUtils.isEmpty(bundle1.getCharSequence("userId"))) {
                mUserId = bundle1.getCharSequence("userId").toString();
            }

        }

        if (TextUtils.isEmpty(mAPPID) || TextUtils.isEmpty(mAPPKey)) {
            mediationRewardedVideoAdListener.onInitializationFailed(this, AdRequest.ERROR_CODE_INVALID_REQUEST);
            return;
        }


        AdapterTools.addChannel();
        initSDK(context);

        if (context instanceof Activity) {

            mediationRewardedVideoAdListener.onInitializationSucceeded(this);
            mediationRewardVideoEventForwarder = new MediationRewardVideoEventForwarder(mMediationRewardedVideoAdListener, this);

        } else {
            mediationRewardedVideoAdListener.onInitializationFailed(this, AdRequest.ERROR_CODE_INVALID_REQUEST);
            return;
        }

    }

    private void initSDK(Context context) {
        if (!hasInitMintegralSDK) {
            MIntegralSDK sdk = MIntegralSDKFactory.getMIntegralSDK();
            Map<String, String> map = sdk.getMTGConfigurationMap(mAPPID, mAPPKey);
            sdk.init(map, context.getApplicationContext());
            hasInitMintegralSDK = true;
            Log.e(TAG, "hasInitMintegralSDK:" + hasInitMintegralSDK);
        }
    }

    @Override
    public boolean isInitialized() {
        return mMvRewardVideoHandler == null ? false : true;
    }

    @Override
    public void loadAd(MediationAdRequest mediationAdRequest, Bundle bundle, Bundle bundle1) {
        if (mContext != null) {
            String serviceString = bundle.getString(MediationRewardedVideoAdAdapter.CUSTOM_EVENT_SERVER_PARAMETER_FIELD);
            parseServiceString(serviceString);
            mMvRewardVideoHandler = MintegralHandlerManager.getInstance().getMTGRewardVideoHandler(mRewardUnitId);
            if (mMvRewardVideoHandler == null) {//unitArray.containsKey(mRewardUnitId)
                mMvRewardVideoHandler = new MTGRewardVideoHandler((Activity) mContext, mPlacementId, mRewardUnitId);
                MintegralHandlerManager.getInstance().addMTGRewardVideoHandler(mRewardUnitId, mMvRewardVideoHandler);
            }
            if (mMvRewardVideoHandler != null) {
                mMvRewardVideoHandler.setRewardVideoListener(mediationRewardVideoEventForwarder);
                mMvRewardVideoHandler.load();
            }
        }
    }

    @Override
    public void showVideo() {
        mMvRewardVideoHandler.show(mRewardId, mUserId);
    }

    public boolean canShow() {
        if (mMvRewardVideoHandler != null) {
            return mMvRewardVideoHandler.isReady();
        } else {
            return false;
        }

    }

}


