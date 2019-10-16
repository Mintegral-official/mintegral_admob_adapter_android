package com.mintegral.adapter.nativeadapter;

import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.mediation.NativeContentAdMapper;
import com.mintegral.msdk.out.Campaign;
import com.mintegral.msdk.out.MtgNativeHandler;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by songjunjun on 17/4/20.
 */

public class MintegralNativeContentAdMapper extends NativeContentAdMapper {
    private final Campaign mSampleAd;
    //    private NativeAdOptions mNativeAdOptions;
    private ImageView mInformationIconView;

    private String TAG = this.getClass().getName();

    private MtgNativeHandler mNativeHandle;

    public MintegralNativeContentAdMapper(Campaign ad, MtgNativeHandler nativeHandle) {
        mSampleAd = ad;
//        mNativeAdOptions = adOptions;
        mNativeHandle = nativeHandle;
        setHeadline(mSampleAd.getAppName());
        setBody(mSampleAd.getAppDesc());
        setCallToAction(mSampleAd.getAdCall());
//        setStarRating(mSampleAd.getRating());
//        setStore(mSampleAd.getPackageName());
        setLogo(new MintegralNativeMappedImage(null, Uri.parse(ad.getIconUrl()),
                100));

        List<NativeAd.Image> imagesList = new ArrayList<NativeAd.Image>();
        imagesList.add(new MintegralNativeMappedImage(null, Uri.parse(ad.getImageUrl()),
                1000));
        setImages(imagesList);


        setOverrideClickHandling(true);
        setOverrideImpressionRecording(false);
    }


    @Override
    public void recordImpression() {
        super.recordImpression();
    }


    @Override
    public void handleClick(View view) {
        super.handleClick(view);
    }


    @Override
    public void trackView(View view) {

        if(view instanceof ViewGroup){
            if(mNativeHandle != null){
                mNativeHandle.registerView(view,traversalView(view),mSampleAd);
            }
        }else if(view instanceof View){
            if(mNativeHandle != null){
                mNativeHandle.registerView(view,mSampleAd);
            }
        }




        super.trackView(view);
    }


    @Override
    public void untrackView(View view) {

        super.untrackView(view);
    }


    /**
     * 遍历view
     * @param view
     * @return
     */
    private List traversalView(View view){
        List<View> viewList = new ArrayList<View>();

        if(null == view) {
            return viewList;
        }

        if (view instanceof MediaView) {//如果这里不单独列出，没有办法获取他的点击事件
            viewList.add(view);
        } else if(view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for(int i = 0; i < viewGroup.getChildCount(); i ++) {
                if(viewGroup.getChildAt(i) instanceof ViewGroup) {
                    viewList.addAll(traversalView(viewGroup.getChildAt(i)));
                }else {
                    viewList.add(viewGroup.getChildAt(i));
                }
            }
        }else if(view instanceof View) {
            viewList.add(view);
        }


        return  viewList;
    }
}
