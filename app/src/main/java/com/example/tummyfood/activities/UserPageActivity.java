package com.example.tummyfood.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
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
import com.example.tummyfood.R;
import com.example.tummyfood.adapters.RecipeListAdapter;
import com.example.tummyfood.helpers.ImageCache;
import com.example.tummyfood.helpers.ServerUrls;
import com.example.tummyfood.helpers.UserLogin;
import com.example.tummyfood.models.RecipeDataModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserPageActivity extends AppCompatActivity {

    private RequestQueue queue;

    public static ArrayList<RecipeDataModel> LikedRecipes = new ArrayList<>();

    private RecipeListAdapter recipeListAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        ListView likedRecipesListView = findViewById(R.id.liked_recipes_listview);

        TextView title = findViewById(R.id.username_page_title);

        StringBuilder sb = new StringBuilder(UserLogin.CurrentLoginUsername);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));//To capitalize first letter

        title.setText(sb.toString());

        ImageView logoutButton = findViewById(R.id.user_logout_button);

        logoutButton.setOnClickListener(view -> {
            UserLogin.Logout();
            UserLogin.Login(UserPageActivity.this, true);
            finish();
        });

        queue = Volley.newRequestQueue(this);

        GetLikedRecipes();

        recipeListAdapter = new RecipeListAdapter(this, R.layout.recipelistitem, LikedRecipes);
        likedRecipesListView.setAdapter(recipeListAdapter);

        likedRecipesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RecipeDataModel model = recipeListAdapter.getItem(i);
                Intent intent = new Intent(UserPageActivity.this, RecipesDetailsActivity.class);
                intent.putExtra("id", model.getId());
                intent.putExtra("title", model.getName());
                startActivity(intent);
            }
        });

    }

    public void GetLikedRecipes() {
        LikedRecipes.clear();
        JsonArrayRequest request = new JsonArrayRequest(ServerUrls.getAllLikes(UserLogin.CurrentLoginID), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0;i < response.length();i++) {
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
                                    LikedRecipes.add(model);
                                    recipeListAdapter.notifyDataSetChanged();
                                }
                            }, 0, 0, ImageView.ScaleType.FIT_CENTER, Bitmap.Config.ARGB_8888,
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(UserPageActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            queue.add(request);
                        }else {
                            RecipeDataModel model = new RecipeDataModel(id, name, type, time);
                            model.setImage(ImageCache.GetImage(id));
                            LikedRecipes.add(model);
                            recipeListAdapter.notifyDataSetChanged();
                        }
                    }

                } catch (Exception ex) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(request);
    }

}