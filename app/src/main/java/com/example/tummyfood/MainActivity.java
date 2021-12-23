package com.example.tummyfood;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView allRecipiesBtn = findViewById(R.id.all_recipies_image);

        allRecipiesBtn.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, CategoriesActivity.class);
            startActivity(i);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}