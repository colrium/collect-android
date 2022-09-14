package com.colrium.collect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.colrium.collect.databinding.ActivitySplashScreenBinding;
import com.google.gson.Gson;

import com.colrium.collect.config.Constants;
import com.colrium.collect.data.local.database.AppDatabase;
import com.colrium.collect.data.local.model.SessionWithAccessTokenAndUser;
import com.colrium.collect.fragments.auth.OnAuthListener;
import com.colrium.collect.utility.AppPreferences;

public class SplashActivity extends AppCompatActivity {
    private static final String LOG_TAG = SplashActivity.class.getSimpleName();
    private ActivitySplashScreenBinding binding;
    AppPreferences appPreferences;
    AppDatabase appDatabase;
    App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        app = ((App)getApplication());
        appPreferences = app.getAppPreferences();
        appDatabase = app.getAppDatabase();
        start();

    }

    public void start(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                String sessionId = appPreferences.getString(Constants.PREF_USER_SESSIONID, null);
                app.setSessionId(sessionId);

            }
        }, 250);
    }
}