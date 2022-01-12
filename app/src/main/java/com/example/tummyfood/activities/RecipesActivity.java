package com.example.tummyfood.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tummyfood.helpers.ImageCache;
import com.example.tummyfood.R;
import com.example.tummyfood.helpers.UserLogin;
import com.example.tummyfood.models.RecipeDataModel;
import com.example.tummyfood.adapters.RecipeListAdapter;
import com.example.tummyfood.helpers.ServerUrls;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
            if(UserLogin.isLoggedIn){
                Intent i = new Intent(RecipesActivity.this,AddRecipeActivity.class);
                i.putExtra("currentCategory", currentCategory);
                startActivity(i);
            }else {
                UserLogin.Login(RecipesActivity.this, false);
            }
        });

        queue = Volley.newRequestQueue(this);

        GetCurrentRecipes(currentCategory);

        recipeListAdapter = new RecipeListAdapter(this, R.layout.recipelistitem, CurrentRecipes);
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
        recipesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { //list is my listView

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int pos, long id) {//When the category is held
                if(UserLogin.isAdmin) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RecipesActivity.this);
                    String recipeTitle = CurrentRecipes.get(pos).getName();
                    builder.setTitle("Delete " + CurrentRecipes.get(pos).getName());
                    builder.setMessage("Are you sure you want to delete this recipe?");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            StringRequest request = new StringRequest(1, ServerUrls.deleteRecipe, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    CurrentRecipes.remove(pos);
                                    recipeListAdapter.notifyDataSetChanged();
                                    Toast.makeText(RecipesActivity.this, response, Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }) {
                                @Nullable
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("recipeID", CurrentRecipes.get(pos).getId() + "");
                                    return params;
                                }
                            };
                            queue.add(request);
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Do Nothing
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
                return true;
            }
        });

        TextView title = findViewById(R.id.main_recipe_tv);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.jump);
        title.startAnimation(anim);
        title.setText(currentCategory);

    }

    public void GetCurrentRecipes(String category){
        CurrentRecipes.clear();
        JsonArrayRequest request = new JsonArrayRequest(ServerUrls.getRecipes(category), new Response.Listener<JSONArray>() {
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
                            ImageRequest request = new ImageRequest(ServerUrls.getImage(id), new Response.Listener<Bitmap>() {
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
                Toast.makeText(RecipesActivity.this, "No Recipes Found", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
    }

}