// Amanda Villarreal
// Created May 12, 2021
// GeckTrack.app
// MainActivity.java
//-------------------------------------------------------------------------------------------------

// PACKAGES AND IMPORTS

package com.example.gecktrack;
import android.os.Bundle;
import android.view.MenuItem;
import com.example.gecktrack.ui.dashboard.MyGecksFragment;
import com.example.gecktrack.ui.home.HomeFragment;
import com.example.gecktrack.ui.notifications.CalendarFragment;
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
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navigationView, navController);

        // set home page as default fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment()).commit();
    }

}