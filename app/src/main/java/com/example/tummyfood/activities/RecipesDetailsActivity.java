package com.example.tummyfood.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tummyfood.R;
import com.example.tummyfood.helpers.ImageCache;
import com.example.tummyfood.helpers.ServerUrls;
import com.example.tummyfood.helpers.UserLikes;
import com.example.tummyfood.helpers.UserLogin;
import com.google.android.flexbox.FlexboxLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RecipesDetailsActivity extends AppCompatActivity {

    private ImageView detailsImage;
    private TextView likesText;
    private int id;
    private RequestQueue queue;
    private FlexboxLayout flex, flex1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        LinearLayout likesLayout = findViewById(R.id.likesLayout);
        ImageView likesImage = findViewById(R.id.likeImage);
        likesText = findViewById(R.id.likesTextView);

        detailsImage = findViewById(R.id.detailsImage);

        flex = findViewById(R.id.flexLayout);
        flex1 = findViewById(R.id.flexLayout1);

        queue = Volley.newRequestQueue(this);

        id = getIntent().getIntExtra("id", 0);
        getRecipeDetails(id);

        if (UserLikes.DoesLike(id)) {
            likesImage.setImageResource(R.drawable.like_enabled_recipe_image);
        }

        if (!UserLogin.isLoggedIn) {
            likesLayout.setEnabled(false);
        }

        Animation bounceAnim = AnimationUtils.loadAnimation(this, R.anim.bounce2);

        likesLayout.setOnClickListener(view -> {
            likesLayout.startAnimation(bounceAnim);
            StringRequest request;
            likesLayout.setEnabled(false);
            if (UserLikes.DoesLike(id)) {
                request = new StringRequest(1, ServerUrls.userLikes, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        UserLikes.removeLike(id);
                        likesImage.setImageResource(R.drawable.like_recipe_image);
                        likesText.setText((Integer.parseInt(likesText.getText().toString()) - 1) + "");
                        likesLayout.setEnabled(true);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        likesLayout.setEnabled(true);
                    }
                }) {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("userID", UserLogin.CurrentLoginID + "");
                        params.put("recipeID", id + "");
                        params.put("type", "remove");
                        return params;
                    }
                };
            } else {
                request = new StringRequest(1, ServerUrls.userLikes, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        UserLikes.addLike(id);
                        likesImage.setImageResource(R.drawable.like_enabled_recipe_image);
                        likesText.setText((Integer.parseInt(likesText.getText().toString()) + 1) + "");
                        likesLayout.setEnabled(true);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        likesLayout.setEnabled(true);
                    }
                }) {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("userID", UserLogin.CurrentLoginID + "");
                        params.put("recipeID", id + "");
                        params.put("type", "add");
                        return params;
                    }
                };
            }
            queue.add(request);
        });

        String t = getIntent().getStringExtra("title");
        TextView title = findViewById(R.id.recipeTitleTv);
        title.setText(t);

        detailsImage.setImageBitmap(ImageCache.GetImage(id));
    }

    public void getRecipeDetails(int id) {
        JsonArrayRequest request = new JsonArrayRequest(ServerUrls.getRecipeDetails(id), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject row = response.getJSONObject(0);
                    String ingredients = row.getString("ingredients");
                    String preparations = row.getString("preparation");
                    int totalLikes = row.getInt("totallikes");
                    likesText.setText(totalLikes + "");

                    String[] ingredientArray = ingredients.split("\n");

                    for(String ingredient : ingredientArray) {
                        TextView tv = new TextView(RecipesDetailsActivity.this);
                        tv.setText(ingredient);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.
                                LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(5, 5, 5, 5);
                        tv.setLayoutParams(lp);
                        tv.setPadding(30, 30,30,30);
                        tv.setTextColor(Color.BLACK);
                        tv.setBackground(getResources().getDrawable(R.drawable.flexradius));
                        flex.addView(tv);
                    }

                    String[] preparationArray = preparations.split("\n");

                    for(String prep : preparationArray){
                        TextView tv = new TextView(RecipesDetailsActivity.this);
                        tv.setText(prep);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.
                                LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(5, 5, 5, 5);
                        tv.setLayoutParams(lp);
                        tv.setPadding(30, 30,30,30);
                        tv.setTextColor(Color.BLACK);
                        tv.setBackground(getResources().getDrawable(R.drawable.flexradius));
                        tv.setGravity(Gravity.CENTER);
                        flex1.addView(tv);
                    }

                } catch (Exception e) {
                    Toast.makeText(RecipesDetailsActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RecipesDetailsActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
    }
}