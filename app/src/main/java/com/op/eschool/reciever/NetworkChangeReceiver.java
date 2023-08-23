package com.op.eschool.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.op.eschool.util.FLog;
import com.op.eschool.util.network_util.NetworkConnectivityCallback;
import com.op.eschool.util.network_util.NetworkConnectivityUtil;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        FLog.w("NetworkChangeReceiver", "onReceive");
        NetworkConnectivityUtil networkConnectivityUtil = new NetworkConnectivityUtil(context);
        NetworkConnectivityCallback networkCallback = new NetworkConnectivityCallback(context);
        networkConnectivityUtil.startNetworkMonitoring(networkCallback);

    }
}