package com.mintegral.adapter.rewardadapter;

import android.os.Bundle;

/**
 * Created by songjunjun on 17/4/12.
 */

public  final class MintegralExtrasBuilder {

    private static final String KEY_USER_ID = "userId";
    private static final String KEY_PACKAGE_NAME = "packageName";



    private String mUserId;
    private String mpackageName;

    public MintegralExtrasBuilder setUserId(String userId){
        mUserId = userId;
        return MintegralExtrasBuilder.this;
    }


    public MintegralExtrasBuilder setPackageName(String packageName){
        mpackageName = packageName;
        return MintegralExtrasBuilder.this;
    }

    public Bundle build() {
        Bundle extras = new Bundle();
        extras.putString(KEY_USER_ID, mUserId);
        extras.putString(KEY_PACKAGE_NAME,mpackageName);
        return extras;
    }

}
