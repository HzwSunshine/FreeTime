package com.hzwsunshine.freetime.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.hzwsunshine.freetime.Application.Application;

/**
 * Created by He Zhiwei on 2015/8/19.
 */
public class NetWorkUtil extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    Application.connectedType = ConnectivityManager.TYPE_WIFI;
                    return;
                }
                if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    String isWifiConn = (String) SharedUtils.get(context, "net_conn", String.class);
                    if (isWifiConn.equals("all_type")) {
                        Application.connectedType = ConnectivityManager.TYPE_WIFI;
                    } else {
                        Application.connectedType = ConnectivityManager.TYPE_MOBILE;
                    }
                }
            } else {
                Application.connectedType = -1;
//                ViewUtils.showToast("网络已断开");
            }
        }
    }

    /**
     * 判断Network是否开启(包括移动网络和wifi),并且链接成功
     *
     * @return
     */
    public static boolean isNetworkSuccess(Context mContext) {
        return (isNetworkEnabled(mContext) && isNetworkConnected(mContext));
    }

    /**
     * 判断Network是否开启(包括移动网络和wifi)
     *
     * @return
     */
    public static boolean isNetworkEnabled(Context mContext) {
        return (isNetEnabled(mContext) || isWIFIEnabled(mContext));
    }

    /**
     * 判断Network是否连接成功(包括移动网络和wifi)
     *
     * @return
     */
    public static boolean isNetworkConnected(Context mContext) {
        return (isWifiContected(mContext) || isNetContected(mContext));
    }

    /**
     * 判断移动网络是否开启
     *
     * @return
     */
    public static boolean isNetEnabled(Context context) {
        boolean enable = false;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            if (telephonyManager.getNetworkType() != TelephonyManager.NETWORK_TYPE_UNKNOWN) {
                enable = true;
                Log.i(Thread.currentThread().getName(), "isNetEnabled");
            }
        }
        return enable;
    }

    /**
     * 判断wifi是否开启
     */
    public static boolean isWIFIEnabled(Context context) {
        boolean enable = false;
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            enable = true;
            Log.i(Thread.currentThread().getName(), "isWifiEnabled");
        }

        Log.i(Thread.currentThread().getName(), "isWifiDisabled");
        return enable;
    }

    /**
     * 判断移动网络是否连接成功！
     *
     * @param context
     * @return
     */
    public static boolean isNetContected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetworkInfo.isConnected()) {

            Log.i(Thread.currentThread().getName(), "isNetContected");
            return true;
        }
        Log.i(Thread.currentThread().getName(), "isNetDisconnected");
        return false;

    }

    /**
     * 判断wifi是否连接成功
     *
     * @param context
     * @return
     */
    public static boolean isWifiContected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetworkInfo.isConnected()) {
            Log.i(Thread.currentThread().getName(), "isWifiContected");
            return true;
        }
        Log.i(Thread.currentThread().getName(), "isWifiDisconnected");
        return false;

    }

}
