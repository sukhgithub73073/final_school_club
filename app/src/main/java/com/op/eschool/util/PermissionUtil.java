package com.op.eschool.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;

public class PermissionUtil {
    public static String[] storge_permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storge_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA
    };
    public static String[] permissions() {
        String[] p;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storge_permissions_33;
        } else {
            p = storge_permissions;
        }
        return p;
    }
    public static boolean checkPermisiion(Context context , String permission) {
        int res = context.checkCallingOrSelfPermission(permission);
        return res == PackageManager.PERMISSION_GRANTED ;
    }
    public static void requestPermission(Activity activity, String permission[] , int code){
        FLog.w("requestPermission" ,"permission>>>>>>>>>>" + new Gson().toJson(permission)) ;
        ActivityCompat.requestPermissions(activity,
                permissions(),
                1);

        // ActivityCompat.requestPermissions(activity , permission ,code);
    }
}
