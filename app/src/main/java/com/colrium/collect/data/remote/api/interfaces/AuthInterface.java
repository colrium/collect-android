package com.colrium.collect.data.remote.api.interfaces;

import com.colrium.collect.data.remote.auth.login.Credentials;
import com.colrium.collect.data.remote.auth.login.LoginResponse;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthInterface {
    @POST("auth/login")
    Call<LoginResponse> login(@Body RequestBody credentials);
}
