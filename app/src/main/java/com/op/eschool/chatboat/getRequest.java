package com.op.eschool.chatboat;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

public class getRequest {
    private String APIkey = "[API Key]";
    private String brainID = "[Brain id]";
    private String reply;
    private char[] illegalChars = {'#', '<', '>', '$', '+', '%', '!', '`', '&',
            '*', '\'', '\"', '|', '{', '}', '/', '\\', ':', '@'};

    public getRequest(Context context){
    }

    public interface VolleyResponseListener {
        void onError(String message);

        void onResponse(String reply);
    }

    private String formatMessage(String message){

        message = message.replace(' ', '-');
        for(char illegalChar : illegalChars){
            message = message.replace(illegalChar, '-');
        }
        return message;
    }

    public void getResponse(String message, final VolleyResponseListener volleyResponseListener){
        message = formatMessage(message);
        String url = "http://api.brainshop.ai/get?bid=" + brainID+ "&key=" + APIkey + "&uid=1&msg=" + message;
        Log.d("URL", url);
        volleyResponseListener.onResponse("reply here >>>>") ;
    }

}
