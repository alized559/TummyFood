package com.example.tummyfood.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tummyfood.helpers.ImageCache;
import com.example.tummyfood.helpers.ServerUrls;
import com.example.tummyfood.helpers.UserLikes;
import com.example.tummyfood.helpers.UserLogin;
import com.example.tummyfood.models.CategoriesDataModel;
import com.example.tummyfood.adapters.CategoriesListAdapter;
import com.example.tummyfood.R;
import com.example.tummyfood.models.RecipeDataModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

        queue = Volley.newRequestQueue(this);

        categoriesList.setAdapter(adapter);

        categoriesList.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(this, RecipesActivity.class);
            intent.putExtra("currentCategory", list.get(i).getTitle());
            startActivity(intent);
        });

        getCategories();

        ImageView addCategory = findViewById(R.id.add_categories_image);

        addCategory.setOnClickListener(view -> {
            if(UserLogin.isLoggedIn){
                Intent i = new Intent(CategoriesActivity.this,AddCategoryActivity.class);
                startActivity(i);
            }
        });

        if(!UserLogin.isAdmin){
            addCategory.setVisibility(View.GONE);
        }else {
            categoriesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { //list is my listView

                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               final int pos, long id) {//When the category is held
                    AlertDialog.Builder builder = new AlertDialog.Builder(CategoriesActivity.this);
                    String categoryTitle = list.get(pos).getTitle();
                    builder.setTitle("Delete " + list.get(pos).getTitle());
                    builder.setMessage("Are you sure you want to delete this category?");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            StringRequest request = new StringRequest(1, ServerUrls.deleteCategory, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    list.remove(pos);
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(CategoriesActivity.this, response, Toast.LENGTH_SHORT).show();
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
                                    params.put("categoryID", list.get(pos).getId() + "");
                                    params.put("name", categoryTitle);
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
                    return true;
                }
            });
        }

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

                        if(ImageCache.GetCategoriesImage(id) == null){
                            ImageRequest request = new ImageRequest(ServerUrls.getCategoryImage(id), new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap response) {
                                    CategoriesDataModel model = new CategoriesDataModel(id, title);
                                    model.setImage(response);
                                    ImageCache.SetCategoriesImage(id, response);
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
                        }else {
                            CategoriesDataModel model = new CategoriesDataModel(id, title);
                            model.setImage(ImageCache.GetCategoriesImage(id));
                            list.add(model);
                            adapter.notifyDataSetChanged();
                        }
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