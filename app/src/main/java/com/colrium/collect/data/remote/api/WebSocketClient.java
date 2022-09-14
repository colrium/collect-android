package com.colrium.collect.data.remote.api;

import android.util.Log;

import com.colrium.collect.config.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class WebSocketClient {
    private static final String LOG_TAG = WebSocketClient.class.getSimpleName();
    private static Socket socket;
    private static WebSocketClient webSocketClient;
    private String authorizationToken;
    private Boolean isSocketAuthorized = false;
    private WebSocketClient(){
        try {
            socket = createIOSocket();
        } catch (URISyntaxException e) {}
    }
    private Socket createIOSocket() throws URISyntaxException {
        Socket ioSocket = IO.socket(Constants.API_URL);
        ioSocket.on("connect", onConnect());
        ioSocket.on("authorization-required", onAuthorizationRequired());
        ioSocket.on("unauthorized", onAuthorizationRequired());
        ioSocket.on("authorized", onAuthorized());

        Log.d(LOG_TAG, "ioSocket.connected() "+ioSocket.connected());
        return ioSocket;
    }
    public static WebSocketClient getInstance(){
        if (webSocketClient == null){
            webSocketClient = new WebSocketClient();
        }
        return webSocketClient;
    }
    public static WebSocketClient getInstance(String authorizationToken){
        if (webSocketClient == null || webSocketClient.getAuthorizationToken() != authorizationToken){
            webSocketClient = new WebSocketClient().setAuthorizationToken(authorizationToken);
        }
        return webSocketClient;
    }
    private WebSocketClient authorizeWithToken() throws JSONException {
        if (socket != null){
            JSONObject data = new JSONObject();
            data.put("token", this.authorizationToken);
            socket.emit("authorization", data);
        }
        return this;
    }
    private Emitter.Listener onAuthorized() {
        return new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                JSONObject data = (JSONObject) args[0];

                Log.d(LOG_TAG, "Socket Authorized Event data: "+data.toString());
            }
        };
    }
    private Emitter.Listener onConnect() {
        return new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                JSONObject data = (JSONObject) args[0];

                Log.d(LOG_TAG, "Socket connect Event data: "+data.toString());
                try {
                    authorizeWithToken();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    private Emitter.Listener onAuthorizationFailed() {
        return new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                JSONObject data = (JSONObject) args[0];

                Log.d(LOG_TAG, "Socket authorization failed Event data: "+data.toString());
            }
        };
    }
    private Emitter.Listener onAuthorizationRequired() {
        String token = this.authorizationToken;
        return new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                JSONObject data = (JSONObject) args[0];
                Log.d(LOG_TAG, "Socket Unauthorized Event data: "+data.toString());
                try {
                    JSONArray use = data.getJSONArray("use");
                    if(use != null && use.getString(0) == "token"){
                        authorizeWithToken();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    public Socket getSocket() throws URISyntaxException{
        if (socket == null){
            socket = createIOSocket();
        }
        return socket;
    }

    public WebSocketClient setAuthorizationToken(String authorizationToken){
        this.authorizationToken = authorizationToken;
        return this;
    }

    public String getAuthorizationToken(){
        return this.authorizationToken;
    }



}
