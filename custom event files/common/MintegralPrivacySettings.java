package com.mintegral.adapter.common;

import android.content.Context;

import com.mintegral.msdk.MIntegralConstans;
import com.mintegral.msdk.MIntegralSDK;
import com.mintegral.msdk.out.MIntegralSDKFactory;

public class MintegralPrivacySettings {

    public static void setHasUserConsent(boolean hasUserConsent, Context context) {
        MIntegralSDK sdk = MIntegralSDKFactory.getMIntegralSDK();
        if (hasUserConsent) {
            sdk.setUserPrivateInfoType(context, MIntegralConstans.AUTHORITY_ALL_INFO, MIntegralConstans.IS_SWITCH_ON);
        } else {
            sdk.setUserPrivateInfoType(context, MIntegralConstans.AUTHORITY_ALL_INFO, MIntegralConstans.IS_SWITCH_OFF);
        }

    }
}
