package com.mintegral.adapter.banneradapter;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventBanner;
import com.google.android.gms.ads.mediation.customevent.CustomEventBannerListener;
import com.mintegral.adapter.common.AdapterTools;
import com.mintegral.msdk.MIntegralConstans;
import com.mintegral.msdk.MIntegralSDK;
import com.mintegral.msdk.out.BannerSize;
import com.mintegral.msdk.out.MIntegralSDKFactory;
import com.mintegral.msdk.out.MTGBannerView;

import org.json.JSONObject;

import java.util.Map;

public class MintegralCustomEventBanner implements CustomEventBanner {


    private String appId = "";
    private String appKey = "";
    private String unitId = "";


    private String packageName = "";

    private MTGBannerView mtgBannerView;
    private boolean hasInitSDK = false;
    private static final String TAG = "Banner";

    @Override
    public void requestBannerAd(Context context, CustomEventBannerListener customEventBannerListener, String s, AdSize adSize, MediationAdRequest mediationAdRequest, Bundle bundle) {
        Log.e(TAG, "requestBannerAd: " );

        parseServer(context, s);

        if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(appKey) || TextUtils.isEmpty(unitId)) {
            if (customEventBannerListener != null) {
                customEventBannerListener.onAdFailedToLoad(AdRequest.ERROR_CODE_INVALID_REQUEST);
            }
            return;
        }

        if (!hasInitSDK) {
            initSDK(context);
        }

        loadAds(context, customEventBannerListener,adSize);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDes " );
        if(mtgBannerView!= null){
            Log.e(TAG, "onDestroy: " );
            mtgBannerView.release();
        }
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
                    AdapterTools.pareseAuthority(context, jo);
                }
            } catch (Exception e) {

            }

        }


    }


    private void initSDK(Context context) {

        MIntegralSDK sdk = MIntegralSDKFactory.getMIntegralSDK();


        Map<String, String> map = sdk.getMTGConfigurationMap(appId, appKey);

        if (!TextUtils.isEmpty(packageName)) {
            map.put(MIntegralConstans.PACKAGE_NAME_MANIFEST, packageName);
        }
        AdapterTools.addChannel();
        sdk.init(map, context);

        hasInitSDK = true;


    }

    private void parseBunld(Bundle bundle) {
        if (bundle.get("packageName") != null) {
            packageName = bundle.get("packageName").toString();
        }
    }

    private void loadAds(Context context, CustomEventBannerListener customEventBannerListener, AdSize adSize) {

        int width = adSize.getWidth();
        int height = adSize.getHeight();
        Log.d(TAG, "loadAds: adsize "+width+" "+height);

        final int w = adSize.getWidthInPixels(context);
        final int h = adSize.getHeightInPixels(context);

        mtgBannerView = new MTGBannerView(context);
        mtgBannerView.setVisibility(View.GONE);
        mtgBannerView.init(new BannerSize(BannerSize.DEV_SET_TYPE,width,height),unitId);


        //mtgBannerView.setAllowShowCloseBtn(false);

        //mtgBannerView.setRefreshTime(10);
        mtgBannerView.setBannerAdListener(new MintegralCustomBannerEventForwarder(customEventBannerListener,mtgBannerView));

        mtgBannerView.load();

        mtgBannerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                AdView.LayoutParams lp = (AdView.LayoutParams) mtgBannerView.getLayoutParams();
                lp.width = w;
                lp.height = h;
                mtgBannerView.setLayoutParams(lp);
            }
        });

    }

}
