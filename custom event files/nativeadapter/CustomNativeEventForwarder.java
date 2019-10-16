package com.mintegral.adapter.nativeadapter;

import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.mediation.NativeMediationAdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventNativeListener;
import com.mintegral.msdk.out.Campaign;
import com.mintegral.msdk.out.Frame;
import com.mintegral.msdk.out.MtgNativeHandler;
import com.mintegral.msdk.out.NativeListener;


import java.util.List;

/**
 * Created by songjunjun on 17/4/17.
 */

public class CustomNativeEventForwarder implements NativeListener.NativeAdListener {

    private static final String TAG="CustomNativeEvent";
    private CustomEventNativeListener mNativeListener;
    private NativeMediationAdRequest mNativeMediationAdRequest;
    private MtgNativeHandler mNativeHandle;


    public CustomNativeEventForwarder(
            CustomEventNativeListener listener, NativeMediationAdRequest nativeMediationAdRequest, MtgNativeHandler nativeHandle) {
        this.mNativeListener = listener;
        this.mNativeMediationAdRequest = nativeMediationAdRequest;
        this.mNativeHandle = nativeHandle;
    }


    @Override
    public void onAdClick(Campaign campaign) {

        if(mNativeListener != null){
            mNativeListener.onAdOpened();
            mNativeListener.onAdClicked();
        }

    }

    @Override
    public void onAdFramesLoaded(List<Frame> list) {

    }

    @Override
    public void onLoggingImpression(int adsourceType) {

        Log.i(TAG,"onLoggingImpression adsourceType:"+adsourceType);
        if (mNativeListener!=null){
            Log.i(TAG,"onLoggingImpression onAdImpression");
            mNativeListener.onAdImpression();
        }
    }

    @Override
    public void onAdLoaded(List<Campaign> list, int i) {



        if(list == null || list.size() == 0){
            mNativeListener.onAdFailedToLoad(AdRequest.ERROR_CODE_NO_FILL);
            return;
        }

        Campaign ad = list.get(0);

        if (!containsRequiredAppInstallAdAssets(ad)) {
            // Each system-defined native ad format (App Install and Content) has a set of
            // "Always Included" assets. Mediated networks must check and fail the request if any
            // of the "Always Included" assets are not available for the ad loaded (the sample
            // SDK will always provide these assets, but this check is added as an example).
            mNativeListener.onAdFailedToLoad(AdRequest.ERROR_CODE_NO_FILL);
            return;
        }

        if(mNativeMediationAdRequest.isAppInstallAdRequested()){
            MintegralNativeAppInstallAdMapper mapper =
                    new MintegralNativeAppInstallAdMapper(ad,mNativeHandle);
            mNativeListener.onAdLoaded(mapper);
            return;
        }

        if(mNativeMediationAdRequest.isContentAdRequested()){
            MintegralNativeContentAdMapper mapper =
                    new MintegralNativeContentAdMapper(ad,mNativeHandle);
            mNativeListener.onAdLoaded(mapper);
            return;
        }

    }

    @Override
    public void onAdLoadError(String s) {


        mNativeListener.onAdFailedToLoad(AdRequest.ERROR_CODE_INTERNAL_ERROR);


    }


    /**
     * This method will check whether or not he provided {@link Campaign} contains
     * all the required assets (headline, body, image, app icon and call to action) for it to be
     * mapped onto an AdMob native app install ad.
     *
     * @param appInstallAd the sample native app install ad to be checked.
     * @return {@code true} if the provided sample native app install ad contains all the
     * necessary assets for it to be mapped, {@code false} otherwise.
     */
    private boolean containsRequiredAppInstallAdAssets(Campaign appInstallAd) {
        return (appInstallAd != null && appInstallAd.getAppName() != null
                && appInstallAd.getAppDesc() != null && appInstallAd.getImageUrl() != null
                && appInstallAd.getIconUrl() != null && appInstallAd.getAdCall() != null);
    }
}
