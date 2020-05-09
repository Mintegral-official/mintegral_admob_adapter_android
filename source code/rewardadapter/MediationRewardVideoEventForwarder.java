package com.mintegral.adapter.rewardadapter;

import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.reward.mediation.MediationRewardedVideoAdListener;
import com.mintegral.msdk.out.RewardVideoListener;


/**
 * Created by songjunjun on 17/3/20.
 */

public class MediationRewardVideoEventForwarder implements RewardVideoListener {
    private static final String TAG = MediationRewardVideoEventForwarder.class.getName();

    private MediationRewardedVideoAdListener mMediationRewardedVideoAdListener;
    private MTGToAdmobRewardVideoAdapter mMTGToAdmobRewardVideoAdapter;


    public MediationRewardVideoEventForwarder(MediationRewardedVideoAdListener listener, MTGToAdmobRewardVideoAdapter MTGToAdmobRewardVideoAdapter) {

        this.mMediationRewardedVideoAdListener = listener;
        this.mMTGToAdmobRewardVideoAdapter = MTGToAdmobRewardVideoAdapter;
    }

    @Override
    public void onVideoLoadSuccess(String placementID, String s) {
        Log.e(TAG, "onVideoLoadSuccess");
        if (mMTGToAdmobRewardVideoAdapter != null) {
            mMediationRewardedVideoAdListener.onAdLoaded(mMTGToAdmobRewardVideoAdapter);
        }
    }

    @Override
    public void onVideoLoadFail(String s) {
        Log.e(TAG, "onVideoLoadFail:" + s);
        mMediationRewardedVideoAdListener.onAdFailedToLoad(mMTGToAdmobRewardVideoAdapter, AdRequest.ERROR_CODE_NO_FILL);
    }

    @Override
    public void onAdShow() {
        Log.e(TAG, "onAdShow");
        mMediationRewardedVideoAdListener.onAdOpened(mMTGToAdmobRewardVideoAdapter);
        mMediationRewardedVideoAdListener.onVideoStarted(mMTGToAdmobRewardVideoAdapter);
    }

    @Override
    public void onAdClose(boolean isCompleteView, String RewardName, float RewardAmout) {
        Log.e(TAG, "onAdClose");
        if (isCompleteView) {
            mMediationRewardedVideoAdListener.onRewarded(mMTGToAdmobRewardVideoAdapter, new MTGRewardItem(RewardName, (int) RewardAmout));

        }
        mMediationRewardedVideoAdListener.onAdClosed(mMTGToAdmobRewardVideoAdapter);
    }

    @Override
    public void onShowFail(String s) {
        Log.e(TAG, "onShowFail:" + s);
    }

    @Override
    public void onVideoAdClicked(String placementID, String s) {
        Log.e(TAG, "onVideoAdClicked");
        mMediationRewardedVideoAdListener.onAdClicked(mMTGToAdmobRewardVideoAdapter);
    }

    @Override
    public void onLoadSuccess(String placementID, String s) {
        Log.e(TAG, "onLoadSuccess:" + s);
    }


    @Override
    public void onEndcardShow(String placementID, String s) {
        Log.e(TAG, "onEndcardShow");
    }

    @Override
    public void onVideoComplete(String placementID, String s) {
        Log.e(TAG, "onVideoComplete");
    }
}
