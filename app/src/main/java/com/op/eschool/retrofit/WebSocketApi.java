package com.op.eschool.retrofit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface WebSocketApi {
    @GET
    Call<WebSocket> connectWebSocket(@Url String url) ;

}
