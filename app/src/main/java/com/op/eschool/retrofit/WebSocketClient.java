package com.op.eschool.retrofit;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebSocketClient {

    private static final String BASE_URL = "wss://schoolclub.in:3001/ClassSettingScoket";
    private static WebSocketApi webSocketApi;

    public static WebSocketApi getWebSocketApi() {
        if (webSocketApi == null) {


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
            OkHttpClient client = clientBuilder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build();
            client.hostnameVerifier();


//            OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
//                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            webSocketApi = retrofit.create(WebSocketApi.class);
        }

        return webSocketApi;
    }
}
