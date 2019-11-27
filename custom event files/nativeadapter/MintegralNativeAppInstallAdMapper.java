package com.mintegral.adapter.nativeadapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.mediation.NativeAppInstallAdMapper;
import com.google.android.gms.ads.mediation.customevent.CustomEventNativeListener;
import com.mintegral.msdk.base.entity.CampaignEx;
import com.mintegral.msdk.nativex.view.MTGMediaView;
import com.mintegral.msdk.out.Campaign;
import com.mintegral.msdk.out.MtgNativeHandler;
import com.mintegral.msdk.out.OnMTGMediaViewListener;


import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by songjunjun on 17/4/17.
 */

public class MintegralNativeAppInstallAdMapper extends NativeAppInstallAdMapper implements OnMTGMediaViewListener {


    private final Campaign mSampleAd;
    //    private NativeAdOptions mNativeAdOptions;

    private String TAG = this.getClass().getName();

    private MtgNativeHandler mNativeHandle;

    private CustomEventNativeListener mCustomEventNativeListener;

    public MintegralNativeAppInstallAdMapper(Context context, Campaign ad, MtgNativeHandler nativeHandle, CustomEventNativeListener nativeListener) {
        mSampleAd = ad;
        this.mCustomEventNativeListener = nativeListener;
//        mNativeAdOptions = adOptions;
        mNativeHandle = nativeHandle;
        setHeadline(mSampleAd.getAppName());
        setBody(mSampleAd.getAppDesc());
        setCallToAction(mSampleAd.getAdCall());
        setStarRating(mSampleAd.getRating());
        setStore(mSampleAd.getPackageName());

        MTGMediaView mediaView = new MTGMediaView(context);
        ViewGroup.LayoutParams layoutParams =
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mediaView.setLayoutParams(layoutParams);
        mediaView.setOnMediaViewListener(this);
        mediaView.setNativeAd(mSampleAd);
//        mediaView.setAllowScreenChange(isAllowFullScreen);

        List<NativeAd.Image> imagesList = new ArrayList<NativeAd.Image>();
        Uri imageUri = Uri.parse(mSampleAd.getImageUrl());

        Uri iconUri = Uri.parse(mSampleAd.getIconUrl());

        MintegralNativeMappedImage image = new MintegralNativeMappedImage(null, imageUri, 1);
        MintegralNativeMappedImage icon = new MintegralNativeMappedImage(null, iconUri, 1);

        imagesList.add(image);

        setImages(imagesList);
        setIcon(icon);


        setMediaView(mediaView);
        CampaignEx campaignEx = (CampaignEx) mSampleAd;

        boolean hasVideo = !TextUtils.isEmpty(campaignEx.getVideoUrlEncode()) ? true : false;
        setHasVideoContent(hasVideo);

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

//        traversalView(view);
        if (view instanceof ViewGroup) {
            if (mNativeHandle != null) {
                mNativeHandle.registerView(view, traversalView(view), mSampleAd);
            }
        } else if (view instanceof View) {
            if (mNativeHandle != null) {
                mNativeHandle.registerView(view, mSampleAd);
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
     *
     * @param view
     * @return
     */
    private List traversalView(View view) {
        List<View> viewList = new ArrayList<View>();

        if (null == view) {
            return viewList;
        }

        if (view instanceof MediaView) {//如果这里不单独列出，没有办法获取他的点击事件
            viewList.add(view);
        } else if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                    viewList.addAll(traversalView(viewGroup.getChildAt(i)));
                } else {
                    viewList.add(viewGroup.getChildAt(i));
                }
            }
        } else if (view instanceof View) {
            viewList.add(view);

        }


        return viewList;
    }

    @Override
    public void onEnterFullscreen() {

    }

    @Override
    public void onExitFullscreen() {

    }

    @Override
    public void onStartRedirection(Campaign campaign, String s) {

    }

    @Override
    public void onFinishRedirection(Campaign campaign, String s) {

    }

    @Override
    public void onRedirectionFailed(Campaign campaign, String s) {

    }

    @Override
    public void onVideoAdClicked(Campaign campaign) {
        if (mCustomEventNativeListener != null) {
            mCustomEventNativeListener.onAdClicked();
//            mCustomEventNativeListener.onAdOpened();
        }
    }

    @Override
    public void onVideoStart() {

    }


    public interface ImageDownloadListener {
        void onDonwloadSuccess();

        void onDonwloadFailed();
    }

    public static class DownloadDrawablesAsync extends AsyncTask<Object, Void, Boolean> {
        private ImageDownloadListener mImageDownloadListener;

        public DownloadDrawablesAsync(ImageDownloadListener listener) {
            this.mImageDownloadListener = listener;
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            MintegralNativeMappedImage mapper = (MintegralNativeMappedImage) params[0];
            ExecutorService executorService = Executors.newCachedThreadPool();
            Uri uri = mapper.getUri();
            Future<Drawable> drawableFuture = getDrawableFuture(uri, executorService);
            Drawable drawable = null;
            try {
                drawable = drawableFuture.get(10, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException exception) {
                return false;
            }
            mapper.setDrawable(drawable);

            return true;
        }

        private Future<Drawable> getDrawableFuture(final Uri uri, ExecutorService executorService) {
            return executorService.submit(new Callable<Drawable>() {
                @Override
                public Drawable call() throws Exception {
                    InputStream in = new URL(uri.toString()).openStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(in);

                    bitmap.setDensity(DisplayMetrics.DENSITY_DEFAULT);
                    return new BitmapDrawable(Resources.getSystem(), bitmap);
                }
            });
        }

        @Override
        protected void onPostExecute(Boolean isDownloadSuccessful) {
            super.onPostExecute(isDownloadSuccessful);
            if (isDownloadSuccessful) {
                mImageDownloadListener.onDonwloadSuccess();
            } else {
                mImageDownloadListener.onDonwloadFailed();
            }
        }
    }

}
