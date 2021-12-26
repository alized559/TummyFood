package com.example.tummyfood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CategoriesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        ListView categoriesList = findViewById(R.id.categoriesList);

        ArrayList<CategoriesDataModel> list = new ArrayList<>();
        list.add(new CategoriesDataModel(R.drawable.main_dish, "Main Dishes"));
        list.add(new CategoriesDataModel(R.drawable.apetizers, "Appetizers"));
        list.add(new CategoriesDataModel(R.drawable.dessert, "Desserts"));
        list.add(new CategoriesDataModel(R.drawable.pizza, "Pizza"));
        list.add(new CategoriesDataModel(R.drawable.pasta, "Pasta"));

        CategoriesListAdapter adapter = new CategoriesListAdapter(this, R.layout.categorieslistitem,
                list);
        categoriesList.setAdapter(adapter);

        categoriesList.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(this, RecipesDetailsActivity.class);
            intent.putExtra("currentCategory", list.get(i).getTitle());
            startActivity(intent);
        });

        TextView title = findViewById(R.id.chooseCategoryTv);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        title.startAnimation(anim);
    }
}