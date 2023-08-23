package com.op.eschool.firebase;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.op.eschool.util.CommonDB;
import com.op.eschool.util.FLog;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        try {
            FLog.w("FCM_token", ">>>>>>"  +token);
            CommonDB commonDB = new CommonDB(getApplicationContext()) ;
            commonDB.putString("FCM_TOKEN" , token) ;
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
