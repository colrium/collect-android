package com.colrium.collect;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.colrium.collect.data.remote.api.ApiClient;
import com.colrium.collect.databinding.ActivityMainBinding;
import com.colrium.collect.utility.AppPreferences;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.colrium.collect.config.Constants;
import com.colrium.collect.data.local.model.SessionWithAccessTokenAndUser;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener, NavController.OnDestinationChangedListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private App app;
    private SessionWithAccessTokenAndUser session;
    private ActionBarDrawerToggle drawerToggle;
    private NavController navController;
    private View navHeaderMain;
    private SwitchCompat drawerMenuNightModeSwith;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (App) getApplication();
        session = app.getSession().getValue();


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        toolbar = binding.appBarMain.toolbar;
        setSupportActionBar(toolbar);
        drawer = binding.drawerLayout;
        navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        navigationView.setNavigationItemSelectedListener(this);

        drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open,  R.string.drawer_close);;

        // Setup toggle to display hamburger icon with nice animation
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();

        // Tie DrawerLayout events to the ActionBarToggle
        drawer.addDrawerListener(drawerToggle);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.darkModeToggle);
        drawerMenuNightModeSwith = menuItem.getActionView().findViewById(R.id.switchCompat);
        navHeaderMain = navigationView.getHeaderView(0);
        TextView accountName = navHeaderMain.findViewById(R.id.accountName);
        TextView accountDescription = navHeaderMain.findViewById(R.id.accountDescription);
        ImageView ivAccountAvatar = navHeaderMain.findViewById(R.id.accountAvatar);
        if (drawerMenuNightModeSwith != null){
            drawerMenuNightModeSwith.setChecked(app.getAppPreferences().getInt(Constants.PREF_NIGHT_MODE_ENABLED, AppCompatDelegate.MODE_NIGHT_NO) == AppCompatDelegate.MODE_NIGHT_YES);
            drawerMenuNightModeSwith.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toggleNightMode();
                }
            });
            /*drawerMenuNightModeSwith.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
//                    toggleNightMode();
                }
            });*/
        }
        app.getAppPreferences().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        app.getSession().observe(this, new Observer<SessionWithAccessTokenAndUser>() {
            @Override
            public void onChanged(SessionWithAccessTokenAndUser sessionWithAccessTokenAndUser) {
                if (accountName != null && sessionWithAccessTokenAndUser.user != null)
                    accountName.setText(sessionWithAccessTokenAndUser.user.name());
                if (accountDescription != null && sessionWithAccessTokenAndUser.user != null)
                    accountDescription.setText(sessionWithAccessTokenAndUser.user.getEmailAddress());
                if (ivAccountAvatar != null && sessionWithAccessTokenAndUser.user != null && sessionWithAccessTokenAndUser.user.getAvatarId() != null){
                    Glide.with(MainActivity.this).load(ApiClient.fileUrl(sessionWithAccessTokenAndUser.user.getAvatarId())).error(R.drawable.ic_face).placeholder(R.drawable.ic_face).into(ivAccountAvatar);
                }
                else if (ivAccountAvatar != null){
                    Glide.with(getApplicationContext()).load(ApiClient.fileUrl("62e16932c871af005f5c8834")).error(R.drawable.ic_face).placeholder(R.drawable.ic_face).into(ivAccountAvatar);
                }
            }
        });
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph())
                        .setOpenableLayout(drawer)
                        .build();
        navController.addOnDestinationChangedListener(this);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public void recreate(){
        if (android.os.Build.VERSION.SDK_INT >= 11){
            super.recreate();
        }
        else{
            startActivity(getIntent());
            finish();
        }
    }
    public void reload(){
        /*if (Build.VERSION.SDK_INT >= 11) {
            recreate();
        } else {
            Intent intent = getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            overridePendingTransition(0, 0);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }*/
        startActivity(getIntent());
        finish();
    }
    public void toggleNightMode(){
        drawer.close();
        int nightMode = app.getAppPreferences().getInt(Constants.PREF_NIGHT_MODE_ENABLED, AppCompatDelegate.MODE_NIGHT_NO) == AppCompatDelegate.MODE_NIGHT_YES? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES;
        app.getAppPreferences().putInt(Constants.PREF_NIGHT_MODE_ENABLED, nightMode);
        Log.d(LOG_TAG, "nightMode "+nightMode);
    }
    public void navToHomeFragment(){
        MenuItem activeMenuItem = navigationView.getCheckedItem();
        if (activeMenuItem.getItemId() != R.id.nav_home)
            navigationView.setCheckedItem(R.id.nav_home);
        navController.navigate(R.id.homeFragment);
    }
    public void navToSettingsFragment(){
        MenuItem activeMenuItem = navigationView.getCheckedItem();
        if (activeMenuItem.getItemId() != R.id.nav_settings)
            navigationView.setCheckedItem(R.id.nav_settings);
        navController.navigate(R.id.settingsFragment);
    }

    public void logout(){
        app.getOnAuthListener().onLogout();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Check if item is drawer
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_main_action_logout:
                logout();
                return true;
            case R.id.menu_main_action_settings:
                navToSettingsFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.e(LOG_TAG, "onNavigationItemSelected clicked:"+item.getItemId());
        drawer.closeDrawer(GravityCompat.START);
        switch (item.getItemId()) {
            case R.id.darkModeToggle:
                toggleNightMode();
                return true;
            case R.id.nav_home:
                navToHomeFragment();
                return true;
            case R.id.nav_settings:
                navToSettingsFragment();
                return true;
            default:
                return false;
        }



    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key == Constants.PREF_NIGHT_MODE_ENABLED) {
            int nightMode = sharedPreferences.getInt(Constants.PREF_NIGHT_MODE_ENABLED, AppCompatDelegate.MODE_NIGHT_NO);
            if (drawerMenuNightModeSwith != null)
                drawerMenuNightModeSwith.setChecked(nightMode != AppCompatDelegate.MODE_NIGHT_NO);
            AppCompatDelegate.setDefaultNightMode(nightMode);
            reload();
        }
    }
    @Override
    public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
        MenuItem activeMenuItem = navigationView.getCheckedItem();
        if (navController.getCurrentDestination().getId() == R.id.settingsFragment && activeMenuItem.getItemId() != R.id.nav_settings)
            navigationView.setCheckedItem(R.id.nav_settings);
        if (navController.getCurrentDestination().getId() == R.id.homeFragment && activeMenuItem.getItemId() != R.id.nav_home)
            navigationView.setCheckedItem(R.id.nav_home);
    }
}