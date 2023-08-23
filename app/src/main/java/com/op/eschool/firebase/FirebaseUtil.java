package com.op.eschool.firebase;


import com.op.eschool.util.FLog;

public class FirebaseUtil {

    public  static void  subcribeTopic(String topic){
        try {
            com.google.firebase.messaging.FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnCompleteListener(d->{
                FLog.e("subcribeTopic" , ">>>>>>>" + topic) ;
            }) ;

        }catch (Exception e){

        }


    }
    public  static void  unSubcribeTopic(String topic){
       try {
           com.google.firebase.messaging.FirebaseMessaging.getInstance().unsubscribeFromTopic(topic).addOnCompleteListener(d->{
               FLog.e("unSubcribeTopic" , ">>>>>>>" + topic) ;
           }) ;

       }catch (Exception E){

       }


    }
}


