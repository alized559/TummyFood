package com.example.tummyfood;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class RecipesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        String currentCategory = getIntent().getStringExtra("currentCategory");
    }
}