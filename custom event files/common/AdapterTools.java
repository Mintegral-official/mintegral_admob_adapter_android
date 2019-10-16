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

    private static String AUTHORITY_KEY_ALL_INFO = "authority_all_info";
    private static String AUTHORITY_KEY_GENERAL_DATA = "authority_general_data";
    private static String AUTHORITY_KEY_DEVICE_ID = "authority_device_id";
    private static String AUTHORITY_KEY_GPS = "authority_gps";
    private static String AUTHORITY_KEY_IMEI_MAC = "authority_imei_mac";
    private static String AUTHORITY_KEY_ANDROID_ID = "authority_android_id";
    private static String AUTHORITY_KEY_APP_LIST = "authority_applist";
    private static String AUTHORITY_KEY_APP_DOWNLOAD = "authority_app_download";
    private static String AUTHORITY_KEY_APP_PROGRESS = "authority_app_progress";


    private static ArrayList<String> keyList = new ArrayList<String>();

    private static void setSubAuthority(MIntegralSDK sdk, Context context, JSONObject jsonObject, ArrayList<String> keyList){
        if(jsonObject != null && keyList != null){
            for (String key:keyList) {
                String result = jsonObject.optString(key);
                if(!TextUtils.isEmpty(result) ){
                    //0代码关闭，默认都是开
                    if(result.equals("0")){
                        sdk.setUserPrivateInfoType(context,key,0);
                    }else{
                        sdk.setUserPrivateInfoType(context,key,1);
                    }
                }
            }
        }

    }

    public static void pareseAuthority(Context context,JSONObject jsonObject){
        try {
            if(jsonObject == null || context == null){
                return;
            }
            MIntegralSDK sdk = MIntegralSDKFactory.getMIntegralSDK();
            String allInfo = jsonObject.optString(AUTHORITY_KEY_ALL_INFO);
            if(TextUtils.isEmpty(allInfo)){
                if(keyList != null && keyList.size() == 0){
                    keyList.add(AUTHORITY_KEY_GENERAL_DATA);
                    keyList.add(AUTHORITY_KEY_DEVICE_ID);
                    keyList.add(AUTHORITY_KEY_GPS);
                    keyList.add(AUTHORITY_KEY_IMEI_MAC);
                    keyList.add(AUTHORITY_KEY_ANDROID_ID);
                    keyList.add(AUTHORITY_KEY_APP_LIST);
                    keyList.add(AUTHORITY_KEY_APP_DOWNLOAD);
                    keyList.add(AUTHORITY_KEY_APP_PROGRESS);
                }
                setSubAuthority(sdk,context,jsonObject,keyList);
            }else{
                if(allInfo.equals("0")){
                    sdk.setUserPrivateInfoType(context,AUTHORITY_KEY_ALL_INFO,0);
                }else{
                    sdk.setUserPrivateInfoType(context,AUTHORITY_KEY_ALL_INFO,1);
                }

            }
        }catch (Throwable t){
        	Log.e(TAG,t.getMessage(),t);
        }
    }

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
