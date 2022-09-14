package com.colrium.collect.config;


import com.colrium.collect.BuildConfig;

public class Constants {
    public static String ENVIRONMENT = BuildConfig.DEBUG? "development" : "production";
    public static String API_URL_DEV = "http://192.168.0.2:8081";
    public static String API_URL_PROD = "https://api.realfield.io";
    public static String API_URL = BuildConfig.DEBUG? API_URL_DEV : API_URL_PROD;
    public static String DEFAULT_USERNAME = "colrium@gmail.com";
    public static String DEFAULT_PASSWORD = "&teCsZ7dWvGPYH";
    public static String DEFAULT_GRANT_TYPE = "password";
    public static String CLIENT_ID = "5e777c88d4b6f406b85c024a";
    public static String CLIENT_SECRET = "Gmoru5UJvK9yQKt3xKyTvpktxNJ69f4JeendInvljGWEAENAT31q8qaaTEJzsob9sqA5RKLE4aKUsg1oIlMEYdrLklfSK0Dxb8LQUagzb1tdu6TIJ2wihaTBfngr";
    public static final String DATABASE_NAME = "rf-collect-db";
    public static final String PREF_USER_AUTHENTICATED = "auth-user-authenticated";
    public static final String PREF_USER_SESSIONID = "auth-user-session-id";
    public static final String PREF_HTTP_AUTHORIZATION_HEADER = "http-authorization-header";
    public static final String PREF_NIGHT_MODE_ENABLED = "night-mode-enabled";
}
