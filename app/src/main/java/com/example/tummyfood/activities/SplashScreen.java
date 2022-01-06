package com.example.tummyfood.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.tummyfood.R;
import com.example.tummyfood.activities.MainActivity;

public class SplashScreen extends AppCompatActivity {

    public static final long TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(() -> {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }, TIME);
    }
}