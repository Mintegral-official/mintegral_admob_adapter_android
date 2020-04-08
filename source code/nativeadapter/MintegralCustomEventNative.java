package com.mintegral.adapter.nativeadapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.android.gms.ads.mediation.NativeMediationAdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventNative;
import com.google.android.gms.ads.mediation.customevent.CustomEventNativeListener;
import com.mintegral.adapter.common.AdapterTools;
import com.mintegral.msdk.MIntegralConstans;
import com.mintegral.msdk.MIntegralSDK;
import com.mintegral.msdk.out.MIntegralSDKFactory;
import com.mintegral.msdk.out.MtgNativeHandler;
import com.mintegral.msdk.out.NativeListener;


import org.json.JSONObject;

import java.util.Map;

/**
 * Created by songjunjun on 17/4/17.
 */

public class MintegralCustomEventNative implements CustomEventNative {

    private String appId = "";
    private String appKey = "";
    private String unitId = "";


    private MtgNativeHandler mNativeHandle;

    private String userId = "";


    private NativeMediationAdRequest nativeMediationAdRequest;
    private static boolean hasInitMintegralSDK = false;


    @Override
    public void requestNativeAd(Context context, CustomEventNativeListener customEventNativeListener, String s, NativeMediationAdRequest nativeMediationAdRequest, Bundle bundle) {

        parseServer(context, s);
        parseNativeMediation(nativeMediationAdRequest);

        if (!hasInitMintegralSDK) {
            initMobvistaSDK(context);
            hasInitMintegralSDK = true;
        }

        loadMintegralAds(context, customEventNativeListener);

    }


    @Override
    public void onDestroy() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    private void parseServer(Context context, String s) {

        JSONObject jo;
        if (!TextUtils.isEmpty(s)) {
            try {
                jo = new JSONObject(s);
                if (jo != null) {
                    appId = jo.getString("appId");
                    appKey = jo.getString("appKey");
                    unitId = jo.getString("unitId");
//                    AdapterTools.pareseAuthority(context, jo);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }

    private void parseNativeMediation(NativeMediationAdRequest nativeMediationAdRequest) {

        this.nativeMediationAdRequest = nativeMediationAdRequest;

    }

    private void initMobvistaSDK(Context context) {

        MIntegralSDK sdk = MIntegralSDKFactory.getMIntegralSDK();

        if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(appKey)) {
            return;
        }


        Map<String, String> map = sdk.getMTGConfigurationMap(appId, appKey);


        AdapterTools.addChannel();
        sdk.init(map, context);


    }


    private void loadMintegralAds(Context context, CustomEventNativeListener customEventNativeListener) {

        Map<String, Object> properties = MtgNativeHandler.getNativeProperties(unitId);
        properties.put(MIntegralConstans.PROPERTIES_AD_NUM, 1);
        mNativeHandle = new MtgNativeHandler(properties, context);
        mNativeHandle.addTemplate(new NativeListener.Template(MIntegralConstans.TEMPLATE_BIG_IMG, 1));

        mNativeHandle.setAdListener(new CustomNativeEventForwarder(customEventNativeListener, nativeMediationAdRequest, mNativeHandle, context));
        mNativeHandle.load();

    }

}

