package com.colrium.collect.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.google.android.material.navigation.NavigationView;

import com.colrium.collect.R;
import com.colrium.collect.config.Constants;
import com.colrium.collect.data.remote.api.ApiClient;
import com.colrium.collect.databinding.ActivityAuthBinding;
import com.colrium.collect.databinding.ActivityMainBinding;
import com.colrium.collect.utility.AppPreferences;

public class AuthActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String LOG_TAG = AuthActivity.class.getSimpleName();
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityAuthBinding binding;
    AppPreferences appPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appPreferences = AppPreferences.getInstance(getApplicationContext());
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarAuth.toolbar);
        mAppBarConfiguration = new AppBarConfiguration.Builder()
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_auth);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.auth, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_auth);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key == Constants.PREF_USER_AUTHENTICATED || key == Constants.PREF_USER_SESSIONID){
            String sessionID = sharedPreferences.getString(Constants.PREF_USER_SESSIONID, "");
            boolean isAuthenticated = sharedPreferences.getBoolean(Constants.PREF_USER_AUTHENTICATED, false);
            Log.d(LOG_TAG, sessionID+" "+isAuthenticated);
            if (isAuthenticated && !sessionID.isEmpty()) {
                Intent i = new Intent(AuthActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }
    }
}