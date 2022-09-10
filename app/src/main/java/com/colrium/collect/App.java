package com.colrium.collect;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.multidex.MultiDexApplication;

import com.google.gson.Gson;

import java.util.Map;

import com.colrium.collect.config.Constants;
import com.colrium.collect.data.local.database.AppDatabase;
import com.colrium.collect.data.local.database.AppExecutors;
import com.colrium.collect.data.local.model.SessionWithAccessTokenAndUser;
import com.colrium.collect.data.remote.api.ApiClient;
import com.colrium.collect.ui.AuthActivity;
import com.colrium.collect.ui.MainActivity;
import com.colrium.collect.ui.auth.OnAuthListener;
import com.colrium.collect.utility.AppPreferences;

public class App extends MultiDexApplication implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String LOG_TAG = App.class.getSimpleName();
    private OnAuthListener onAuthListener;
    private AppPreferences appPreferences;
    private ApiClient apiClient;
    private AppDatabase appDatabase;
    private MediatorLiveData<Boolean> darkModeEnabled = new MediatorLiveData<>();
    private MediatorLiveData<String> SESSION_ID = new MediatorLiveData<>();
    private MediatorLiveData<SessionWithAccessTokenAndUser> session = new MediatorLiveData<>();

    @Override
    public void onCreate() {
        super.onCreate();
        appPreferences = AppPreferences.getInstance(getApplicationContext());
        appDatabase = AppDatabase.getInstance(getApplicationContext());
        apiClient = ApiClient.getInstance();
        int nightModePreference = appPreferences.getInt(Constants.PREF_NIGHT_MODE_ENABLED, AppCompatDelegate.MODE_NIGHT_UNSPECIFIED);
        Log.w(LOG_TAG, "nightModePreference"+nightModePreference);
        darkModeEnabled.setValue(nightModePreference != AppCompatDelegate.MODE_NIGHT_NO && isDeviceNightModeOn());
        if (nightModePreference != AppCompatDelegate.MODE_NIGHT_UNSPECIFIED || nightModePreference != AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) {
            AppCompatDelegate.setDefaultNightMode(nightModePreference);
        }
        SESSION_ID.observeForever(new Observer<String>() {
            @Override
            public void onChanged(String sessionId) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(LOG_TAG, "sessionId "+sessionId);
                        if (sessionId == null || sessionId.isEmpty()){
                            session.postValue(null);
                            return;
                        }
                        SessionWithAccessTokenAndUser newSession = appDatabase.sessionDao().getByIdWithAccessTokenAndUser(sessionId);
                        session.postValue(newSession);
                        Gson gson = new Gson();
                        Log.d(LOG_TAG, "session id changed newSession "+gson.toJson(newSession));
                    }
                });
            }
        });


        initOnAuthListener();
    }

    public OnAuthListener getOnAuthListener(){
        return onAuthListener;
    }
    public AppPreferences getAppPreferences(){
        return appPreferences;
    }
    public AppDatabase getAppDatabase(){
        return appDatabase;
    }
    public ApiClient getApiClient(){
        return apiClient;
    }
    public void setSession(@Nullable SessionWithAccessTokenAndUser sessionWithAccessTokenAndUser) {
        session.postValue(sessionWithAccessTokenAndUser);
    }
    public LiveData<SessionWithAccessTokenAndUser> getSession() {
        return session;
    }
    private LiveData<SessionWithAccessTokenAndUser> getCurrentActiveSession(){
        MediatorLiveData<SessionWithAccessTokenAndUser> currentActiveSession = new MediatorLiveData<>();
        appDatabase.sessionDao().getActiveSessionWithAccessTokenAndUser().observeForever(new Observer<SessionWithAccessTokenAndUser>() {
            @Override
            public void onChanged(@Nullable SessionWithAccessTokenAndUser sessionWithAccessTokenAndUser) {
                currentActiveSession.postValue(sessionWithAccessTokenAndUser);
            }
        });
        return currentActiveSession;
    }

    public void setSessionId(String s){
        SESSION_ID.postValue(s);
    }

    public void setActivity(Class clazz){
        if(AppCompatActivity.class.isAssignableFrom(clazz)){
            Intent intent = new Intent(getApplicationContext(), clazz);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        };
    }

    private void initOnAuthListener() {
        onAuthListener = new OnAuthListener() {
            @Override
            public void onLogin(SessionWithAccessTokenAndUser session) {
                appPreferences.putString(Constants.PREF_HTTP_AUTHORIZATION_HEADER, session.accessToken.getTokenType() +" "+session.accessToken.getToken());
                appPreferences.putString(Constants.PREF_USER_SESSIONID, session.session.getId());
                appPreferences.putBoolean(Constants.PREF_USER_AUTHENTICATED, true);
                SESSION_ID.postValue(session.session.getId());
                setActivity(MainActivity.class);
            }

            @Override
            public void onLogout() {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        appDatabase.sessionDao().deactivateAll();
                    }
                });
                setNightMode(AppCompatDelegate.MODE_NIGHT_UNSPECIFIED);
                appPreferences.putBoolean(Constants.PREF_USER_AUTHENTICATED, false);
                appPreferences.putString(Constants.PREF_USER_SESSIONID, "");
                SESSION_ID.postValue(null);
                setActivity(AuthActivity.class);
            }
        };
    }

    private boolean isDeviceNightModeOn() {
        return  (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)  == Configuration.UI_MODE_NIGHT_YES;
    }
    private int nightModePreference() {
        return appPreferences.getInt(Constants.PREF_NIGHT_MODE_ENABLED, AppCompatDelegate.MODE_NIGHT_UNSPECIFIED);
    }
    private boolean isNightModeEnabled() {
        int nightMode = nightModePreference();
        return  nightMode == AppCompatDelegate.MODE_NIGHT_UNSPECIFIED || nightMode == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM? isDeviceNightModeOn() : nightMode == AppCompatDelegate.MODE_NIGHT_YES;
    }

    private void setNightMode(int nightMode) {
        int nightModePref = nightModePreference();
        appPreferences.putInt(Constants.PREF_NIGHT_MODE_ENABLED, nightMode);
        darkModeEnabled.setValue((nightMode != AppCompatDelegate.MODE_NIGHT_NO && nightMode != AppCompatDelegate.MODE_NIGHT_YES && isDeviceNightModeOn()) || nightMode == AppCompatDelegate.MODE_NIGHT_YES);
        if (nightMode != nightModePref){
            AppCompatDelegate.setDefaultNightMode(nightMode);
        }
    }
    public LiveData<Boolean> darkModeEnabled(){
        return darkModeEnabled;
    }
    public void toggleNightMode() {
        setNightMode(isNightModeEnabled()? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(LOG_TAG, "onSharedPreferenceChanged key: "+key);
        if (key == Constants.PREF_HTTP_AUTHORIZATION_HEADER){
            Map<String, Object> apiClientHeaders = apiClient.getRequestHeaders();
            apiClientHeaders.put("Authorization", sharedPreferences.getString(Constants.PREF_HTTP_AUTHORIZATION_HEADER, null));
            apiClient = apiClient.setRequestHeaders(apiClientHeaders);
        }
        else if (key == Constants.PREF_USER_SESSIONID) {
            setSessionId(sharedPreferences.getString(Constants.PREF_USER_SESSIONID, null));
        }
        else if (key == Constants.PREF_NIGHT_MODE_ENABLED) {
            int darkModePref = sharedPreferences.getInt(Constants.PREF_NIGHT_MODE_ENABLED, AppCompatDelegate.MODE_NIGHT_UNSPECIFIED);
            darkModeEnabled.postValue(darkModePref == AppCompatDelegate.MODE_NIGHT_YES || ((darkModePref == AppCompatDelegate.MODE_NIGHT_UNSPECIFIED || darkModePref == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) && isDeviceNightModeOn()));
        }
    }
}
