package com.mintegral.adapter.rewardbetaadapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.ads.mediation.Adapter;
import com.google.android.gms.ads.mediation.InitializationCompleteCallback;
import com.google.android.gms.ads.mediation.MediationAdLoadCallback;
import com.google.android.gms.ads.mediation.MediationConfiguration;
import com.google.android.gms.ads.mediation.MediationRewardedAd;
import com.google.android.gms.ads.mediation.MediationRewardedAdCallback;
import com.google.android.gms.ads.mediation.MediationRewardedAdConfiguration;
import com.google.android.gms.ads.mediation.VersionInfo;
import com.google.android.gms.ads.reward.mediation.MediationRewardedVideoAdAdapter;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.mintegral.adapter.common.AdapterTools;
import com.mintegral.adapter.configfiles.ConfigFiles;
import com.mintegral.msdk.MIntegralConstans;
import com.mintegral.msdk.MIntegralSDK;
import com.mintegral.msdk.out.MIntegralSDKFactory;
import com.mintegral.msdk.out.MTGRewardVideoHandler;
import com.mintegral.msdk.out.RewardVideoListener;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class AdmobNewRewardVideoAdapter extends Adapter implements MediationRewardedAd, RewardVideoListener {

    private MTGRewardVideoHandler mMvRewardVideoHandler;
    private String mAPPID = "";
    private String mAPPKey = "";
    private String mRewardUnitId = "";
    private String mRewardId = "1";
    private String mUserId = "";
    private String TAG = "testnewreward";
    static boolean hasInitMintegralSDK = false;

    MediationAdLoadCallback admobLoadListener;
    private MediationRewardedAdCallback mMediationRewardedAdCallback;

    @Override
    public void initialize(Context context, InitializationCompleteCallback initializationCompleteCallback, List<MediationConfiguration> list) {
        Log.e(TAG, "initialize: ");
        for (MediationConfiguration configuration : list) {
            Bundle serverParameters = configuration.getServerParameters();
            String serviceString = serverParameters.getString(MediationRewardedVideoAdAdapter.CUSTOM_EVENT_SERVER_PARAMETER_FIELD);
            if (!TextUtils.isEmpty(serviceString)) {
                parseServiceString(context, serviceString);
            }
        }

        if (TextUtils.isEmpty(mAPPID) || TextUtils.isEmpty(mAPPKey)) {
            initializationCompleteCallback.onInitializationFailed("mintegral appid or appkey is null");
            return;
        }
        AdapterTools.addChannel();
        if (!hasInitMintegralSDK) {
            MIntegralSDK sdk = MIntegralSDKFactory.getMIntegralSDK();
            Map<String, String> map = sdk.getMTGConfigurationMap(mAPPID, mAPPKey);
            sdk.init(map, context.getApplicationContext());
            hasInitMintegralSDK = true;
            Log.e(TAG, "hasInitMintegralSDK:" + hasInitMintegralSDK);

        }


        if (context instanceof Activity) {
            mMvRewardVideoHandler = new MTGRewardVideoHandler((Activity) context, mRewardUnitId);
//            mediationRewardVideoEventForwarder = new MediationRewardVideoEventForwarder(mediationRewardedVideoAdListener,this);
            mMvRewardVideoHandler.setRewardVideoListener(this);
//            mediationRewardedVideoAdListener.onInitializationSucceeded(this);
            initializationCompleteCallback.onInitializationSucceeded();
        }
    }

    @Override
    public VersionInfo getVersionInfo() {
        Log.e(TAG, "getVersionInfo: ");
        String splits[] = ConfigFiles.VERSION_CODE.split("\\.");
        int major = Integer.parseInt(splits[0]);
        int minor = Integer.parseInt(splits[1]);
        int micro = Integer.parseInt(splits[2]);
        return new VersionInfo(major, minor, micro);
    }

    @Override
    public VersionInfo getSDKVersionInfo() {
        Log.e(TAG, "getSDKVersionInfo: ");
        return null;
    }

    @Override
    public void showAd(Context context) {
        Log.e(TAG, "showAd: ");
        if (mMvRewardVideoHandler != null) mMvRewardVideoHandler.show(mRewardId, mUserId);
    }

    @Override
    public void loadRewardedAd(MediationRewardedAdConfiguration configuration, MediationAdLoadCallback<MediationRewardedAd, MediationRewardedAdCallback> mediationAdLoadCallback) {
        Log.e(TAG, "loadRewardedAd: ");

        Context context = configuration.getContext();
        admobLoadListener = mediationAdLoadCallback;

        Bundle serverParameters = configuration.getServerParameters();
        String serviceString = serverParameters.getString(MediationRewardedVideoAdAdapter.CUSTOM_EVENT_SERVER_PARAMETER_FIELD);
        if (!TextUtils.isEmpty(serviceString)) {
            parseServiceString(context, serviceString);
        }

        if (TextUtils.isEmpty(mAPPID) || TextUtils.isEmpty(mAPPKey)) {
            mediationAdLoadCallback.onFailure("mintegral appid or appkey is null");
            return;
        }
        AdapterTools.addChannel();
        if (!hasInitMintegralSDK) {
            MIntegralSDK sdk = MIntegralSDKFactory.getMIntegralSDK();

            Map<String, String> map = sdk.getMTGConfigurationMap(mAPPID, mAPPKey);

            sdk.init(map, context.getApplicationContext());
            hasInitMintegralSDK = true;
            Log.e(TAG, "hasInitMintegralSDK:" + hasInitMintegralSDK);
        }
        if (mMvRewardVideoHandler == null) {
            mMvRewardVideoHandler = new MTGRewardVideoHandler(context, mRewardUnitId);
//            mediationRewardVideoEventForwarder = new MediationRewardVideoEventForwarder(mediationRewardedVideoAdListener,this);
            mMvRewardVideoHandler.setRewardVideoListener(this);
            mMvRewardVideoHandler.load();
        } else {
//            mediationRewardedVideoAdListener.onInitializationSucceeded(this);
            mMvRewardVideoHandler.load();
        }

    }


    private void parseServiceString(Context context, String serviceString) {
        if (TextUtils.isEmpty(serviceString)) {
            return;
        }
        Log.e(TAG,serviceString);
        try {
            JSONObject jsonObject = new JSONObject(serviceString);
            String appId = jsonObject.optString("appId");
            String appKey = jsonObject.optString("appKey");
            String unitId = jsonObject.optString("unitId");
            String rewardId = jsonObject.optString("rewardId");


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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onVideoLoadSuccess(String s) {
        Log.e(TAG, "onVideoLoadSuccess: " + s);
        if (admobLoadListener != null)
            mMediationRewardedAdCallback = (MediationRewardedAdCallback) admobLoadListener.onSuccess(this);
    }

    @Override
    public void onLoadSuccess(String s) {
        Log.e(TAG, "onLoadSuccess: " + s);
    }

    @Override
    public void onVideoLoadFail(String s) {
        Log.e(TAG, "onVideoLoadFail: " + s);
        if (admobLoadListener != null) admobLoadListener.onFailure(s);
    }

    @Override
    public void onAdShow() {
        if (mMediationRewardedAdCallback != null) {
            mMediationRewardedAdCallback.reportAdImpression();
            mMediationRewardedAdCallback.onAdOpened();
            mMediationRewardedAdCallback.onVideoStart();

        }
    }

    @Override
    public void onAdClose(boolean b, String s, float v) {
        Log.e(TAG, "onAdClose: " + b);
        if (mMediationRewardedAdCallback != null) {

            if (b) {
                mMediationRewardedAdCallback.onUserEarnedReward(new RewardItem() {
                    @Override
                    public String getType() {
                        // mintegral SDK does not provide a reward type.
                        return "";
                    }

                    @Override
                    public int getAmount() {
                        // mintegral SDK does not provide reward amount, default to 1.
                        return 1;
                    }
                });

            }
            mMediationRewardedAdCallback.onAdClosed();
        }

    }

    @Override
    public void onShowFail(String s) {
        Log.e(TAG, "onShowFail: " + s);
        if (mMediationRewardedAdCallback != null) mMediationRewardedAdCallback.onAdFailedToShow(s);
    }

    @Override
    public void onVideoAdClicked(String s) {
        if (mMediationRewardedAdCallback != null) mMediationRewardedAdCallback.reportAdClicked();
    }

    @Override
    public void onVideoComplete(String s) {
        if (mMediationRewardedAdCallback != null) mMediationRewardedAdCallback.onVideoComplete();
    }

    @Override
    public void onEndcardShow(String s) {

    }
}
