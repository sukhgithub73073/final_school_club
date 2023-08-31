package com.op.eschool.util.network_util;

import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_FAILED;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import com.op.eschool.activities.LoginActivity;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.interfaces.NoInternetinterface;
import com.op.eschool.services.StaffRegisterService;
import com.op.eschool.services.StudentRegisterService;
import com.op.eschool.util.FLog;
import com.op.eschool.util.Utility;

public class NetworkConnectivityCallback extends ConnectivityManager.NetworkCallback {

    private static final String TAG = "NetworkCallback";
    Context context ;
    Dialog dialog ;
    public NetworkConnectivityCallback(Context context) {
        this.context = context ;
    }
    @Override
    public void onAvailable(Network network) {
        super.onAvailable(network);
        FLog.w(TAG, "Network is available");
        if (dialog != null){
            dialog.dismiss();
            dialog=null ;
            Intent intent = new Intent(this.context, StudentRegisterService.class) ;
            this.context.startService(intent);
            Intent serviceIntent = new Intent(this.context, StaffRegisterService.class) ;
            this.context.startService(serviceIntent);
        }
    }

    @Override
    public void onLost(Network network) {
        super.onLost(network);
        FLog.w(TAG, "Network is lost");
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {

                Utility.noInternetDialog("Try Again", ANIMATED_DAILOG_TYPE_FAILED, new BaseActivity().activity, "LOST_NETWORK", new NoInternetinterface() {
                    @Override
                    public void noNetrok(Dialog d) {
                        if (dialog==null){
                            dialog = d ;
                            dialog.show();
                        }
                    }

                    @Override
                    public void onDialogDismiss() {
                        if (dialog != null){
                            dialog.dismiss();
                            dialog=null ;
                        }


                    }
                }) ;
            }
        });




    }
    @Override
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
        // Network capabilities changed (e.g., switching from mobile data to Wi-Fi or vice versa)
        if (networkCapabilities != null) {
            boolean hasInternet = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            FLog.w(TAG, "Network capabilities changed. Has internet: " + hasInternet);
        }
    }
}
