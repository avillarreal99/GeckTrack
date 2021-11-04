// Amanda Villarreal
// Created May 12, 2021
// GeckTrack.app
// MainActivity.java
//-------------------------------------------------------------------------------------------------

// PACKAGES AND IMPORTS
package com.example.gecktrack;
import android.os.Bundle;
import android.view.MenuItem;
import com.example.gecktrack.ui.dashboard.DashboardFragment;
import com.example.gecktrack.ui.home.HomeFragment;
import com.example.gecktrack.ui.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

//-------------------------------------------------------------------------------------------------

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder
                (R.id.navigation_home, R.id.navigation_my_gecks, R.id.navigation_schedule).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // intialize listeners
        navigationView.setOnNavigationItemSelectedListener(bottomNavListener);
        // initializeImageSlider();

        // set home page as default fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment()).commit();
    }


    // listener for when the user selects another fragment (switches pages)
    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavListener =
            new BottomNavigationView.OnNavigationItemSelectedListener()
            {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item)
                {
                    Fragment selectedFragment = null;

                    // switch case to determine the selected fragment
                    switch(item.getItemId())
                    {
                        case R.id.navigation_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.navigation_my_gecks:
                            selectedFragment = new DashboardFragment();
                            break;
                        case R.id.navigation_schedule:
                            selectedFragment = new NotificationsFragment();
                            break;
                    }
                    // set current fragment to the selected fragment
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                                                selectedFragment).commit();
                    return true;
                }
            };
}