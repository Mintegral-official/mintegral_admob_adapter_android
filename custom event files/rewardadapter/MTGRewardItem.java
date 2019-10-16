package com.mintegral.adapter.rewardadapter;

import com.google.android.gms.ads.reward.RewardItem;

/**
 * Created by songjunjun on 17/3/20.
 */

public class MTGRewardItem implements RewardItem {


    private String mRewardType;
    private int mRewardAmount;

    public MTGRewardItem(String rewardType, int rewardAmount){

        this.mRewardType = rewardType;
        this.mRewardAmount = rewardAmount;
    }

    public String getType() {
        return mRewardType;
    }

    @Override
    public int getAmount() {
        return mRewardAmount;
    }
}

