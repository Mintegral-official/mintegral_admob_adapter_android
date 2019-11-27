package com.mintegral.adapter.common;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;


import com.mintegral.msdk.MIntegralSDK;
import com.mintegral.msdk.base.common.net.Aa;
import com.mintegral.msdk.out.MIntegralSDKFactory;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by songjunjun on 2018/5/8.
 */

public class AdapterTools {
    private static String TAG = "AdapterTools";



    public static void addChannel(){
        try {
            Aa a = new Aa();
            Class c = a.getClass();
            Method method = c.getDeclaredMethod("b",String.class);
            method.setAccessible(true);
            method.invoke(a,"Y+H6DFttYrPQYcIT+F2F+F5/Hv==");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}