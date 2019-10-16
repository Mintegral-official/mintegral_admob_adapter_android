package com.mintegral.adapter.rewardadapter;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.reward.mediation.MediationRewardedVideoAdListener;
import com.mintegral.msdk.out.RewardVideoListener;


/**
 * Created by songjunjun on 17/3/20.
 */

public class MediationRewardVideoEventForwarder implements RewardVideoListener {


    private MediationRewardedVideoAdListener mMediationRewardedVideoAdListener;
    private MTGToAdmobRewardVideoAdapter mMTGToAdmobRewardVideoAdapter;


    public MediationRewardVideoEventForwarder(MediationRewardedVideoAdListener listener, MTGToAdmobRewardVideoAdapter MTGToAdmobRewardVideoAdapter) {

        this.mMediationRewardedVideoAdListener = listener;
        this.mMTGToAdmobRewardVideoAdapter = MTGToAdmobRewardVideoAdapter;
    }

    @Override
    public void onVideoLoadSuccess(String s) {
        if (mMTGToAdmobRewardVideoAdapter != null) {
            if (mMTGToAdmobRewardVideoAdapter.canShow()) {
                mMediationRewardedVideoAdListener.onAdLoaded(mMTGToAdmobRewardVideoAdapter);
            }
        }
    }

    @Override
    public void onVideoLoadFail(String s) {
        mMediationRewardedVideoAdListener.onAdFailedToLoad(mMTGToAdmobRewardVideoAdapter, AdRequest.ERROR_CODE_NO_FILL);
    }

    @Override
    public void onAdShow() {
        mMediationRewardedVideoAdListener.onAdOpened(mMTGToAdmobRewardVideoAdapter);
        mMediationRewardedVideoAdListener.onVideoStarted(mMTGToAdmobRewardVideoAdapter);
    }

    @Override
    public void onAdClose(boolean isCompleteView, String RewardName, float RewardAmout) {

        if (isCompleteView) {
            mMediationRewardedVideoAdListener.onRewarded(mMTGToAdmobRewardVideoAdapter, new MTGRewardItem(RewardName, (int) RewardAmout));

        }
        mMediationRewardedVideoAdListener.onAdClosed(mMTGToAdmobRewardVideoAdapter);
    }

    @Override
    public void onShowFail(String s) {

    }

    @Override
    public void onVideoAdClicked(String s) {

        mMediationRewardedVideoAdListener.onAdClicked(mMTGToAdmobRewardVideoAdapter);
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
