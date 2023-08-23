package com.op.eschool.util.network_util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;

public class NetworkConnectivityUtil {

    private Context context;
    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback networkCallback;

    public NetworkConnectivityUtil(Context context) {
        this.context = context.getApplicationContext();
        this.connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public void startNetworkMonitoring(ConnectivityManager.NetworkCallback networkCallback) {
        this.networkCallback = networkCallback;

        if (connectivityManager != null) {
            NetworkRequest networkRequest = new NetworkRequest.Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
                    .build();

            connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
        }
    }

    public void stopNetworkMonitoring() {
        if (connectivityManager != null && networkCallback != null) {
            connectivityManager.unregisterNetworkCallback(networkCallback);
            networkCallback = null;
        }
    }
}
