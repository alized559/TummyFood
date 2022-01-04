package com.example.tummyfood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecipesActivity extends AppCompatActivity {

    private RequestQueue queue;

    public static ArrayList<RecipeDataModel> CurrentRecipes = new ArrayList<>();

    private RecipeListAdapter recipeListAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        ImageView addRecipesBtn = findViewById(R.id.add_recipes_image);
        ListView recipesListView = findViewById(R.id.recipes_list);

        String currentCategory = getIntent().getStringExtra("currentCategory");

        addRecipesBtn.setOnClickListener(view -> {
            Intent i = new Intent(RecipesActivity.this,AddRecipeActivity.class);
            startActivity(i);
        });

        queue = Volley.newRequestQueue(this);

        GetCurrentRecipes(currentCategory);

        recipeListAdapter = new RecipeListAdapter(this, R.layout.recipelistitem, CurrentRecipes, queue);
        recipesListView.setAdapter(recipeListAdapter);

        recipesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RecipeDataModel model = recipeListAdapter.getItem(i);
                Intent intent = new Intent(RecipesActivity.this, RecipesDetailsActivity.class);
                intent.putExtra("id", model.getId());
                intent.putExtra("title", model.getName());
                startActivity(intent);
            }
        });

        TextView title = findViewById(R.id.main_recipe_tv);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        title.startAnimation(anim);
        title.setText(currentCategory);

    }

    public void GetCurrentRecipes(String category){
        CurrentRecipes.clear();
        JsonArrayRequest request = new JsonArrayRequest(ServerUrls.GetRecipes(category), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0;i < response.length();i++) {
                        JSONObject row = response.getJSONObject(i);
                        int id = row.getInt("id");
                        String name = row.getString("title");
                        String type = row.getString("type");
                        int time = row.getInt("time");
                        if(ImageCache.GetImage(id) == null){
                            ImageRequest request = new ImageRequest(ServerUrls.GetImage(id), new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap response) {
                                    RecipeDataModel model = new RecipeDataModel(id, name, type, time);
                                    model.setImage(response);
                                    ImageCache.SetImage(id, response);
                                    CurrentRecipes.add(model);
                                    recipeListAdapter.notifyDataSetChanged();
                                }
                            }, 0, 0, ImageView.ScaleType.FIT_CENTER, Bitmap.Config.ARGB_8888,
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(RecipesActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            queue.add(request);
                        }else {
                            RecipeDataModel model = new RecipeDataModel(id, name, type, time);
                            model.setImage(ImageCache.GetImage(id));
                            CurrentRecipes.add(model);
                            recipeListAdapter.notifyDataSetChanged();
                        }

                    }

                } catch (Exception ex) {
                    Toast.makeText(RecipesActivity.this, "No Recipes Found", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RecipesActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                Log.e("Loading Error", error.toString());
            }
        });

        queue.add(request);
    }

}