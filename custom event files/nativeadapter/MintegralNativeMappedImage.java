package com.mintegral.adapter.nativeadapter;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.google.android.gms.ads.formats.NativeAd;

/**
 * Created by songjunjun on 17/4/17.
 */

public class MintegralNativeMappedImage extends NativeAd.Image {
    private Drawable mDrawable;
    private Uri mImageUri;
    private double mScale;

    public MintegralNativeMappedImage(Drawable drawable, Uri imageUri, double scale) {
        mDrawable = drawable;
        mImageUri = imageUri;
        mScale = scale;
    }

    @Override
    public Drawable getDrawable() {
        return mDrawable;
    }

    @Override
    public Uri getUri() {
        return mImageUri;
    }

    @Override
    public double getScale() {
        return mScale;
    }
}
