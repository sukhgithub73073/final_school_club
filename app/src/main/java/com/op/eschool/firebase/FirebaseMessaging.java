package com.op.eschool.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.op.eschool.R;
import com.op.eschool.util.FLog;

public class FirebaseMessaging extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        try {
            FLog.w("onMessageeReceived", "onMessageReceived :");
            FLog.w("onMessageeReceived", "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            FLog.w("onMessageReceived", "Message data payload: " + remoteMessage.getData());
            String json = remoteMessage.getData().get("notification_json") ;
//            showNotification(notificationModel);
        }
        }catch (Exception e){e.printStackTrace();}

    }

//
//    void showNotification(NotificationModel notificationModel){
//
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class) ;
//        intent.putExtra("notification_data" , new Gson().toJson(notificationModel)) ;
//
//
//        showNotification(notificationModel.getTitle() , notificationModel.getDescription() , intent)  ;
//
//
//
//    }

    public void showNotification( String title, String body, Intent intent) {
        NotificationManager notificationManager = (NotificationManager)
                getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-" + System.currentTimeMillis() ;
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                .setContentTitle(title)
                .setContentText(body);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setAutoCancel(true);
        notificationManager.notify(notificationId, mBuilder.build());
    }

}
