package com.example.tummyfood;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class CategoriesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        ListView categoriesList = findViewById(R.id.categoriesList);

        ArrayList<CategoriesDataModel> list = new ArrayList<>();
        list.add(new CategoriesDataModel(R.drawable.apetizers, "Apetizers"));
        list.add(new CategoriesDataModel(R.drawable.main_dish, "Main Dishes"));
        list.add(new CategoriesDataModel(R.drawable.dessert, "Desserts"));

        CategoriesListAdapter adapter = new CategoriesListAdapter(this, R.layout.categorieslistitem,
                list);
        categoriesList.setAdapter(adapter);
    }
}