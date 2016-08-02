package com.alenbeyond.androidutils.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.Locale;
import java.util.UUID;

/**
 * 传感器工具类
 */
public final class SensorsDataUtils {

    private static SharedPreferences getSharedPreferences(Context context) {
        final String sharedPrefsName = SHARED_PREF_EDITS_FILE;
        return context.getSharedPreferences(sharedPrefsName, Context.MODE_PRIVATE);
    }

    /**
     * 获取设备ID
     *
     * @param context
     * @return
     */
    public static String getDeviceID(Context context) {
        final SharedPreferences preferences = getSharedPreferences(context);
        String storedDeviceID = preferences.getString(SHARED_PREF_DEVICE_ID_KEY, null);

        if (storedDeviceID == null) {
            storedDeviceID = UUID.randomUUID().toString();
            final SharedPreferences.Editor editor = preferences.edit();
            editor.putString(SHARED_PREF_DEVICE_ID_KEY, storedDeviceID);
            editor.apply();
        }
        return storedDeviceID;
    }

    /**
     * 获取IMEI
     */
    public static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        if (!TextUtils.isEmpty(imei)) {
            return imei;
        }
        return "";
    }

    /**
     * 判断是否为模拟器
     *
     * @return
     */
    public static boolean isInEmulator() {
        if (!Build.HARDWARE.equals("goldfish")) {
            return false;
        }

        if (!Build.BRAND.startsWith("generic")) {
            return false;
        }

        if (!Build.DEVICE.startsWith("generic")) {
            return false;
        }

        if (!Build.PRODUCT.contains("sdk")) {
            return false;
        }

        if (!Build.MODEL.toLowerCase(Locale.US).contains("sdk")) {
            return false;
        }

        return true;
    }

    /**
     * 获取网络类型
     *
     * @param context
     * @return
     */
    public static String networkType(Context context) {
        // Wifi
        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(context.CONNECTIVITY_SERVICE);
        if (manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting()) {
            return "WIFI";
        }

        // Mobile network
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context
                .TELEPHONY_SERVICE);

        int networkType = telephonyManager.getNetworkType();
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return "2G";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "3G";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "4G";
        }

        // disconnected to the internet
        return "NULL";
    }

    private static final String SHARED_PREF_EDITS_FILE = "sensorsdata";
    private static final String SHARED_PREF_DEVICE_ID_KEY = "sensorsdata.device.id";

    private static final String LOGTAG = "SA.SensorsDataUtils";
}
