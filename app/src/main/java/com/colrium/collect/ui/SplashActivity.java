package com.colrium.collect.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;

import com.colrium.collect.App;
import com.colrium.collect.config.Constants;
import com.colrium.collect.data.local.database.AppDatabase;
import com.colrium.collect.data.local.model.SessionWithAccessTokenAndUser;
import com.colrium.collect.databinding.ActivitySplashScreenBinding;
import com.colrium.collect.ui.auth.OnAuthListener;
import com.colrium.collect.utility.AppPreferences;

public class SplashActivity extends AppCompatActivity {
    private static final String LOG_TAG = SplashActivity.class.getSimpleName();
    private boolean mAppBarConfiguration;
    private ActivitySplashScreenBinding binding;
    private MediatorLiveData<SessionWithAccessTokenAndUser> sessionWithAccessTokenAndUserMediatorLiveData = new MediatorLiveData<>();
    AppPreferences appPreferences;
    OnAuthListener onAuthListener;
    AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        App app = ((App)getApplication());
        appPreferences = app.getAppPreferences();
        onAuthListener = app.getOnAuthListener();
        appDatabase = app.getAppDatabase();
        app.getSession().observe(this, new Observer<SessionWithAccessTokenAndUser>() {
            @Override
            public void onChanged(SessionWithAccessTokenAndUser session) {
                Gson gson = new Gson();
                Log.d(LOG_TAG, "session id changed newSession "+gson.toJson(session));
                if (session == null){
                    app.setActivity(AuthActivity.class);
                }
                else {
                    app.setActivity(MainActivity.class);
                }
                finish();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                app.setSessionId(appPreferences.getString(Constants.PREF_USER_SESSIONID, null));
            }
        }, 250);

    }
}