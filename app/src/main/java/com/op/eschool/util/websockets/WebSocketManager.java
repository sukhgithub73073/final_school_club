package com.op.eschool.util.websockets;

import static com.op.eschool.base.BaseActivity.activity;

import com.google.gson.Gson;
import com.op.eschool.interfaces.WebSocketInterface;
import com.op.eschool.util.FLog;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class WebSocketManager {
    private static final int RECONNECT_INTERVAL = 5000;
    private boolean isConnected = false;

    private static final String WS_URL = "wss://schoolclub.in:3001/ClassSettingScoket";
    WebSocketInterface socketInterface ;
    private WebSocket webSocket;
    public void startWebSocket() {
        if (!isConnected) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        TrustManager[] trustAllCertificates = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }
            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }
        }};
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCertificates, new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            clientBuilder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCertificates[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        clientBuilder.hostnameVerifier((hostname, session) -> true);
        OkHttpClient client = clientBuilder.build();
        client.hostnameVerifier();
        Request request = new Request.Builder().url(WS_URL).build();
        WebSocketListener listener = new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, okhttp3.Response response) {
                isConnected = true;
                FLog.w("WebSocketManager"  , "onOpen" ) ;
            }
            @Override
            public void onMessage(WebSocket webSocket, String text) {
                try {
                    text= text.replace("'" ,"\"") ;
                    text= text.replace("staus" ,"status");
                    text= text.replace("[{\"type\": \"ClassGrpAdded\"}," ,"[");
                    text= text.replace("[{\"type\": \"ClassGrpAdded\"}]" ,"[]");
                    text= text.replace("\\xa0","") ;
                    FLog.w("WebSocketManager"  , "RESPONSE>>>" + text ) ;
                    String finalText = text;
                    activity.runOnUiThread(()->{
                        if (socketInterface!=null){socketInterface.onResponse(finalText) ;}
                    });
                }catch (Exception e){e.printStackTrace();}
            }
            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                FLog.w("WebSocketManager"  , "onClosed" + reason ) ;
                isConnected = false;
                //reconnect();
            }
            @Override
            public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
                try {
                    if (socketInterface!=null){socketInterface.onResponse("FAILED") ;}
                    isConnected = false;
                    //reconnect();
                    FLog.w("WebSocketManager"  , "onFailure" + t.getMessage()) ;
                }catch (Exception e){e.printStackTrace();}
            }
        };
        webSocket = client.newWebSocket(request, listener);
        }
    }
    public void sendMessage(String message , WebSocketInterface socketInterface) {
        FLog.w("WebSocketManager"  , "sendMessage" + message ) ;
        this.socketInterface = socketInterface ;
        if (webSocket != null) {
            webSocket.send(message);
        }else{
            startWebSocket() ;
        }
    }
    private void reconnect() {
        new Thread(() -> {
            try {
                Thread.sleep(RECONNECT_INTERVAL);
                if (!isConnected) {
                    startWebSocket();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}