package com.colrium.collect.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.colrium.collect.App;
import com.colrium.collect.R;
import com.colrium.collect.config.Constants;
import com.colrium.collect.data.local.database.AppDatabase;
import com.colrium.collect.data.local.database.AppExecutors;
import com.colrium.collect.data.local.model.SessionWithAccessTokenAndUser;
import com.colrium.collect.databinding.ActivityMainBinding;
import com.colrium.collect.utility.AppPreferences;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
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
        SwitchCompat nightModeSwith = menuItem.getActionView().findViewById(R.id.switchCompat);
        navHeaderMain = navigationView.getHeaderView(0);
        TextView accountName = navHeaderMain.findViewById(R.id.accountName);
        TextView accountDescription = navHeaderMain.findViewById(R.id.accountDescription);
        ImageView accountAvatar = navHeaderMain.findViewById(R.id.accountAvatar);
        if (nightModeSwith != null){
            nightModeSwith.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    app.toggleNightMode();
                }
            });
        }
        app.darkModeEnabled().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean darkModeOn) {
                if (nightModeSwith != null)
                    nightModeSwith.setChecked(darkModeOn);
            }
        });
        app.getSession().observe(this, new Observer<SessionWithAccessTokenAndUser>() {
            @Override
            public void onChanged(SessionWithAccessTokenAndUser sessionWithAccessTokenAndUser) {
                if (accountName != null && sessionWithAccessTokenAndUser.user != null)
                    accountName.setText(sessionWithAccessTokenAndUser.user.name());
                if (accountDescription != null && sessionWithAccessTokenAndUser.user != null)
                    accountDescription.setText(sessionWithAccessTokenAndUser.user.getEmailAddress());
                if (accountAvatar != null && sessionWithAccessTokenAndUser.user != null && sessionWithAccessTokenAndUser.user.getAvatarId() != null){
                    Glide.with(MainActivity.this).load(Constants.API_URL+"/download/"+sessionWithAccessTokenAndUser.user.getAvatarId()).into(accountAvatar);
                }
                else if (accountAvatar != null){
                    Glide.with(getApplicationContext()).load(Constants.API_URL+"/download/62e16932c871af005f5c8834").into(accountAvatar);
                }
            }
        });
        appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph())
                        .setOpenableLayout(drawer)
                        .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public void toggleNightMode(){
        app.toggleNightMode();
        drawer.close();
    }
    public void navToHomeFragment(){
        MenuItem activeMenuItem = navigationView.getCheckedItem();
        if (activeMenuItem.getItemId() != R.id.nav_home)
            navigationView.setCheckedItem(R.id.nav_home);
        if (activeMenuItem.getItemId() == R.id.nav_settings)
            navController.navigate(R.id.navSettingsToHome);
        drawer.close();
    }
    public void navToSettingsFragment(){
        MenuItem activeMenuItem = navigationView.getCheckedItem();
        if (activeMenuItem.getItemId() != R.id.nav_settings)
            navigationView.setCheckedItem(R.id.nav_settings);
        if (activeMenuItem.getItemId() == R.id.nav_home)
            navController.navigate(R.id.navHomeToSettings);

        drawer.close();
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
        switch (item.getItemId()) {
            case R.id.darkModeToggle:
                app.toggleNightMode();
                return true;
            case R.id.nav_home:
                navToHomeFragment();
                return true;
            case R.id.nav_settings:
                Log.e(LOG_TAG, "nav_settings clicked");
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
}