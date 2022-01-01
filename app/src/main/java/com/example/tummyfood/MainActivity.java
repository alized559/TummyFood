package com.example.tummyfood;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RequestQueue queue;

    public static Bitmap image;

    public static ArrayList<RecipeDataModel> TrendingRecipes = new ArrayList<>();

    private RecipeListAdapter recipeListAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView allRecipesBtn = findViewById(R.id.all_recipes_image);
        ListView trendingListView = findViewById(R.id.trending_recipes_list);


        allRecipesBtn.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, CategoriesActivity.class);
            startActivity(i);
        });

        queue = Volley.newRequestQueue(this);

        GetTrendingRecipes();

        recipeListAdapter = new RecipeListAdapter(this, R.layout.recipelistitem, TrendingRecipes, queue);
        trendingListView.setAdapter(recipeListAdapter);

        trendingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RecipeDataModel model = recipeListAdapter.getItem(i);
                Intent intent = new Intent(MainActivity.this, RecipesDetailsActivity.class);
                intent.putExtra("id", model.getId());
                intent.putExtra("title", model.getName());
                image = model.getImage();
                startActivity(intent);
            }
        });

        TextView title = findViewById(R.id.main_trending_tv);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        title.startAnimation(anim);

    }

    public void GetTrendingRecipes(){
        TrendingRecipes.clear();
        JsonArrayRequest request = new JsonArrayRequest(ServerUrls.GetRecipe, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0;i < response.length();i++) {
                        JSONObject row = response.getJSONObject(i);
                        int id = row.getInt("id");
                        String name = row.getString("title");
                        String type = row.getString("type");
                        int time = row.getInt("time");
                        String image = row.getString("image");
                        if(image.equalsIgnoreCase("none")){
                            ImageRequest request = new ImageRequest(ServerUrls.GetImage(id), new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap response) {
                                    RecipeDataModel model = new RecipeDataModel(id, name, type, time, image);
                                    model.setImage(response);
                                    TrendingRecipes.add(model);
                                    recipeListAdapter.notifyDataSetChanged();
                                }
                            }, 0, 0, ImageView.ScaleType.FIT_CENTER, Bitmap.Config.ARGB_8888,
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            queue.add(request);
                        }else {
                            TrendingRecipes.add(new RecipeDataModel(id, name, type, time, image));
                            recipeListAdapter.notifyDataSetChanged();
                        }
                    }

                } catch (Exception ex) {
                    Toast.makeText(MainActivity.this, "No Trending Recipes Found", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                Log.e("Loading Error", error.toString());
            }
        });

        queue.add(request);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();//This is used to skip the splash screen once you go back and close the application
    }
}