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

import java.net.URISyntaxException;
import java.util.Map;

import com.colrium.collect.config.Constants;
import com.colrium.collect.data.local.database.AppDatabase;
import com.colrium.collect.data.local.database.AppExecutors;
import com.colrium.collect.data.local.model.SessionWithAccessTokenAndUser;
import com.colrium.collect.data.remote.api.ApiClient;
import com.colrium.collect.data.remote.api.WebSocketClient;
import com.colrium.collect.fragments.auth.OnAuthListener;
import com.colrium.collect.utility.AppPreferences;

import io.socket.client.IO;
import io.socket.client.Socket;

public class App extends MultiDexApplication implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String LOG_TAG = App.class.getSimpleName();
    private OnAuthListener onAuthListener;
    private AppPreferences appPreferences;
    private ApiClient apiClient;
    private AppDatabase appDatabase;
    private WebSocketClient webSocketClient;
    private MediatorLiveData<String> SESSION_ID = new MediatorLiveData<>();
    private MediatorLiveData<SessionWithAccessTokenAndUser> session = new MediatorLiveData<>();
    private Class acitivityClass = MainActivity.class;

    @Override
    public void onCreate() {
        super.onCreate();
        appPreferences = AppPreferences.getInstance(getApplicationContext());
        appPreferences.getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        appDatabase = AppDatabase.getInstance(getApplicationContext());
        apiClient = ApiClient.getInstance();
        webSocketClient = WebSocketClient.getInstance();
        try {
            webSocketClient.getSocket().connect();
            Log.d(LOG_TAG, "webSocketClient.getSocket().connected() "+webSocketClient.getSocket().connected());
        } catch (URISyntaxException e) {}

//        AppCompatDelegate.setDefaultNightMode(nightModePreference());

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
                    }
                });
            }
        });
        session.observeForever(new Observer<SessionWithAccessTokenAndUser>() {
            @Override
            public void onChanged(SessionWithAccessTokenAndUser sessionWithAccessTokenAndUser) {
                Log.d(LOG_TAG, "session onChanged"+sessionWithAccessTokenAndUser);
                if (sessionWithAccessTokenAndUser == null){
                    acitivityClass = AuthActivity.class;
                }
                else {
                    acitivityClass = MainActivity.class;
                }
                sessionAwareAppActivity();
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
    public WebSocketClient getWebSocketClient(){
        return webSocketClient;
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

    public void setSessionId(String sessionId){
        SESSION_ID.postValue(sessionId);
        if (sessionId==null){
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    appDatabase.sessionDao().deactivateAll();
                }
            });
            setNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            appPreferences.putBoolean(Constants.PREF_USER_AUTHENTICATED, false);
            appPreferences.putString(Constants.PREF_USER_SESSIONID, null);
        }
    }


    public void sessionAwareAppActivity(){
        if(acitivityClass != null && AppCompatActivity.class.isAssignableFrom(acitivityClass)){
            Intent intent = new Intent(getApplicationContext(), acitivityClass);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        };
    }

    private void initOnAuthListener() {
        onAuthListener = new OnAuthListener() {
            @Override
            public void onLogin(SessionWithAccessTokenAndUser sessionWithAccessTokenAndUser) {
//                setSessionId(sessionWithAccessTokenAndUser.session.getId());
            }

            @Override
            public void onLogout() {
                setSessionId(null);
            }
        };
    }

    private boolean isDeviceNightModeOn() {
        Log.d(LOG_TAG, "uiMode "+ getResources().getConfiguration().uiMode);
        Log.d(LOG_TAG, "UI_MODE_NIGHT_MASK "+ Configuration.UI_MODE_NIGHT_MASK);
        return  (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)  == Configuration.UI_MODE_NIGHT_YES;
    }
    private int nightModePreference() {
        return appPreferences.getInt(Constants.PREF_NIGHT_MODE_ENABLED, AppCompatDelegate.MODE_NIGHT_NO);
    }
    public boolean isNightModeOn() {
        int nightMode = nightModePreference();
        return  nightMode == AppCompatDelegate.MODE_NIGHT_UNSPECIFIED || nightMode == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM || nightMode == AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY? isDeviceNightModeOn() : nightMode == AppCompatDelegate.MODE_NIGHT_YES;
    }

    public void setNightMode(int nightMode) {
        int nightModePref = nightModePreference();
        if (nightMode != nightModePref){
            Log.d(LOG_TAG, "nightMode: "+nightMode+" nightModePref: "+nightModePref);
            appPreferences.putInt(Constants.PREF_NIGHT_MODE_ENABLED, nightMode);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key == Constants.PREF_HTTP_AUTHORIZATION_HEADER){
            Map<String, Object> apiClientHeaders = apiClient.getRequestHeaders();
            apiClientHeaders.put("Authorization", appPreferences.getString(Constants.PREF_HTTP_AUTHORIZATION_HEADER, null));
            apiClient = apiClient.setRequestHeaders(apiClientHeaders);
        }
        else if (key == Constants.PREF_USER_SESSIONID) {
            Log.d(LOG_TAG, Constants.PREF_USER_SESSIONID+" "+sharedPreferences.getString(Constants.PREF_USER_SESSIONID, null));
            setSessionId(appPreferences.getString(Constants.PREF_USER_SESSIONID, null));
        }
    }
}
