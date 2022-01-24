/*
SplashScreen.java
Description: Starts the application on the splashscreen until app is fully loaded
 */

package com.example.gecktrack.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;

import com.example.gecktrack.MainActivity;

public class SplashScreen extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        startActivity(new Intent(this, MainActivity.class));
    }
}
