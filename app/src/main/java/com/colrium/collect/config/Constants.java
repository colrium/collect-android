package com.colrium.collect.config;


import com.colrium.collect.BuildConfig;

public class Constants {
    public static String ENVIRONMENT = BuildConfig.DEBUG? "development" : "production";
    public static String API_URL_DEV = "https://api.preview.realfield.io";
    public static String API_URL_PROD = "https://api.realfield.io";
    public static String API_URL = BuildConfig.DEBUG? API_URL_DEV : API_URL_PROD;
    public static String DEFAULT_USERNAME = "colrium@gmail.com";
    public static String DEFAULT_PASSWORD = "&teCsZ7dWvGPYH";
    public static String DEFAULT_GRANT_TYPE = "password";
    public static String CLIENT_ID = "P4lEX1fw5guj8KRtBJUhEOYMybTFlftb4BCCC7fcpzxJu3hrwU";
    public static String CLIENT_SECRET = "aCaEPdO0u0rZ1mwAzwKjwYZb6OE4RFTUrCdHKuhPPnMXsB6jsXJgvrwxtl3ZGevDZrnyNS663NNnuyfEGJayK9LwPl3WF8XoKhscCXAMdOlY7w3YfOpVLzxkKsMa";
    public static final String DATABASE_NAME = "rf-collect-db";
    public static final String PREF_USER_AUTHENTICATED = "auth-user-authenticated";
    public static final String PREF_USER_SESSIONID = "auth-user-session-id";
    public static final String PREF_HTTP_AUTHORIZATION_HEADER = "http-authorization-header";
    public static final String PREF_NIGHT_MODE_ENABLED = "night-mode-enabled";
}
