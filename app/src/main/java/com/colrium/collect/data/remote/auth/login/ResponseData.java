package com.colrium.collect.data.remote.auth.login;

import com.google.gson.annotations.SerializedName;

import com.colrium.collect.data.local.model.AccessToken;
import com.colrium.collect.data.local.model.User;


public class ResponseData {
    @SerializedName("profile")
    private User user;

    @SerializedName("access_token")
    private AccessToken accessToken;

    public ResponseData setUser(User profile){
        this.user = profile;
        return this;
    }

    public User getUser(){
        return user;
    }

    public ResponseData setAccessToken(AccessToken accessToken){
        this.accessToken = accessToken;
        return this;
    }

    public AccessToken getAccessToken(){
        return accessToken;
    }
}
