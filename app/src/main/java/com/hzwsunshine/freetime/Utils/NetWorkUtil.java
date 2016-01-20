package com.hzwsunshine.freetime.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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
}
