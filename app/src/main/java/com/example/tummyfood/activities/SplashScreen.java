package com.example.tummyfood.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tummyfood.R;

public class SplashScreen extends AppCompatActivity {

    public static final long TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Animation top = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        Animation bottom = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        TextView t1 = findViewById(R.id.tv);
        TextView t2 = findViewById(R.id.tv1);

        ImageView image = findViewById(R.id.splashImage);

        t1.setAnimation(bottom);
        t2.setAnimation(bottom);
        image.setAnimation(top);

        new Handler().postDelayed(() -> {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }, TIME);
    }
}