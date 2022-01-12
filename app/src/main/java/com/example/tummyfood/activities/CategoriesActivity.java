package com.example.tummyfood.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.tummyfood.helpers.ImageCache;
import com.example.tummyfood.helpers.ServerUrls;
import com.example.tummyfood.models.CategoriesDataModel;
import com.example.tummyfood.adapters.CategoriesListAdapter;
import com.example.tummyfood.R;
import com.example.tummyfood.models.RecipeDataModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoriesActivity extends AppCompatActivity {

    private ArrayList<CategoriesDataModel> list;
    private CategoriesListAdapter adapter;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        ListView categoriesList = findViewById(R.id.categoriesList);

        list = new ArrayList<>();

        adapter = new CategoriesListAdapter(this, R.layout.categorieslistitem,
                list);

//        list.add(new CategoriesDataModel(R.drawable.main_dish, "Main Dish"));
//        list.add(new CategoriesDataModel(R.drawable.apetizers, "Appetizer"));
//        list.add(new CategoriesDataModel(R.drawable.pizza, "Pizza"));
//        list.add(new CategoriesDataModel(R.drawable.pasta, "Pasta"));
//        list.add(new CategoriesDataModel(R.drawable.dessert, "Dessert"));

        queue = Volley.newRequestQueue(this);

        categoriesList.setAdapter(adapter);

        categoriesList.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(this, RecipesActivity.class);
            intent.putExtra("currentCategory", list.get(i).getTitle());
            startActivity(intent);
        });

        getCategories();

        TextView title = findViewById(R.id.chooseCategoryTv);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        title.startAnimation(anim);
    }

    public void getCategories() {
        list.clear();
        JsonArrayRequest request = new JsonArrayRequest(ServerUrls.getCategories, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject row = response.getJSONObject(i);
                        int id = row.getInt("id");
                        String title = row.getString("name");
                        ImageRequest request = new ImageRequest(ServerUrls.getCategoryImage(id), new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap response) {
                                CategoriesDataModel model = new CategoriesDataModel(id, title);
                                model.setImage(response);
                                list.add(model);
                                adapter.notifyDataSetChanged();
                            }
                        }, 0, 0, ImageView.ScaleType.FIT_CENTER, Bitmap.Config.ARGB_8888,
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(CategoriesActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                        queue.add(request);
                    }

                } catch (Exception ex) {
                    Toast.makeText(CategoriesActivity.this, "Database error!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CategoriesActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
    }
}