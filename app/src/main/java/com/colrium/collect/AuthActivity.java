package com.colrium.collect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.colrium.collect.config.Constants;
import com.colrium.collect.databinding.ActivityAuthBinding;
import com.colrium.collect.utility.AppPreferences;
import com.google.android.material.navigation.NavigationView;

public class AuthActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener, NavController.OnDestinationChangedListener {

    private static final String LOG_TAG = AuthActivity.class.getSimpleName();
    private ActivityAuthBinding binding;

    AppPreferences appPreferences;

    private AppBarConfiguration appBarConfiguration;
    private NavController navController;
    private FragmentManager fragmentManager;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appPreferences = AppPreferences.getInstance(getApplicationContext());
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        toolbar = binding.appBarAuth.toolbar;
        toolbar.setNavigationIcon(getDrawable(R.drawable.ic_logo_monochrome));
        setSupportActionBar(toolbar);

        /*appBarConfiguration = new AppBarConfiguration.Builder()
                .build();*/
        //
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_auth);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        navController.addOnDestinationChangedListener(this);
        //
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.auth, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
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

    @Override
    public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
        Boolean isCurrentEntryFragment = navController.getCurrentDestination().getId() == R.id.loginFragment;
        if (getSupportActionBar() != null){
            if (isCurrentEntryFragment){
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setHomeButtonEnabled(false);
            }
            else {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);
            }
        }
    }
}