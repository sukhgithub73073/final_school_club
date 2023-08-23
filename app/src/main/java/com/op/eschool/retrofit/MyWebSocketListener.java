package com.op.eschool.retrofit;

import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MyWebSocketListener extends WebSocketListener {
    @Override
    public void onOpen(WebSocket webSocket, okhttp3.Response response) {
        super.onOpen(webSocket, response);
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        // Received a text message from the WebSocket server
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        super.onMessage(webSocket, bytes);
        // Received a binary message from the WebSocket server
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        super.onClosing(webSocket, code, reason);
        // WebSocket connection is closing
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
        // WebSocket connection is closed
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
        super.onFailure(webSocket, t, response);
        // Failed to establish the WebSocket connection or other error
    }
}
