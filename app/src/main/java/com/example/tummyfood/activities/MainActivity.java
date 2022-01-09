package com.example.tummyfood.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
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
import com.example.tummyfood.R;
import com.example.tummyfood.helpers.UserLogin;
import com.example.tummyfood.models.RecipeDataModel;
import com.example.tummyfood.adapters.RecipeListAdapter;
import com.example.tummyfood.helpers.ServerUrls;

import org.json.JSONArray;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RequestQueue queue;

    public static ArrayList<RecipeDataModel> TrendingRecipes = new ArrayList<>();

    private RecipeListAdapter recipeListAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView allRecipesBtn = findViewById(R.id.all_recipes_image);
        ListView trendingListView = findViewById(R.id.trending_recipes_list);

        ImageView userAccountBtn = findViewById(R.id.user_account_image);

        allRecipesBtn.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, CategoriesActivity.class);
            startActivity(i);
        });

        userAccountBtn.setOnClickListener(view -> {
            UserLogin.Login(MainActivity.this, true);
        });

        //Attempt Automated Login
        UserLogin.AttemptLogin(this);

        queue = Volley.newRequestQueue(this);

        GetTrendingRecipes();

        recipeListAdapter = new RecipeListAdapter(this, R.layout.recipelistitem, TrendingRecipes);
        trendingListView.setAdapter(recipeListAdapter);

        trendingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RecipeDataModel model = recipeListAdapter.getItem(i);
                Intent intent = new Intent(MainActivity.this, RecipesDetailsActivity.class);
                intent.putExtra("id", model.getId());
                intent.putExtra("title", model.getName());
                startActivity(intent);
            }
        });

        TextView title = findViewById(R.id.main_trending_tv);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.pulse);
        title.startAnimation(anim);

    }

    public void GetTrendingRecipes() {
        TrendingRecipes.clear();
        JsonArrayRequest request = new JsonArrayRequest(ServerUrls.getTrendingRecipes(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject row = response.getJSONObject(i);
                        int id = row.getInt("id");
                        String name = row.getString("title");
                        String type = row.getString("type");
                        int time = row.getInt("time");
                        if(ImageCache.GetImage(id) == null) {
                            ImageRequest request = new ImageRequest(ServerUrls.getImage(id), new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap response) {
                                    RecipeDataModel model = new RecipeDataModel(id, name, type, time);
                                    model.setImage(response);
                                    ImageCache.SetImage(id, response);
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
                        } else {
                            RecipeDataModel model = new RecipeDataModel(id, name, type, time);
                            model.setImage(ImageCache.GetImage(id));
                            TrendingRecipes.add(model);
                            recipeListAdapter.notifyDataSetChanged();
                        }
                    }

                } catch (Exception ex) {
                    Toast.makeText(MainActivity.this, "Database error!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
    }
}