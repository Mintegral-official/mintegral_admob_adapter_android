package com.mintegral.adapter.common;

import com.mintegral.msdk.base.common.net.Aa;
import java.lang.reflect.Method;

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