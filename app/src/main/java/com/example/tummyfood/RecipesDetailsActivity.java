package com.example.tummyfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecipesDetailsActivity extends AppCompatActivity {

    private ImageView detailsImage;
    private ArrayList<String> prep;
    private RequestQueue queue;
    private FlexboxLayout flex, flex1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        detailsImage = findViewById(R.id.detailsImage);

        flex = findViewById(R.id.flexLayout);
        flex1 = findViewById(R.id.flexLayout1);

        queue = Volley.newRequestQueue(this);

        getRecipeDetails();

        detailsImage.setImageResource(R.drawable.recipe_20);
    }

    public void getRecipeDetails() {
        JsonArrayRequest request = new JsonArrayRequest(ServerUrls.getRecipeDetails(1), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    String ingredients = "";
                    String preparations = "";
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject row = response.getJSONObject(i);
                        ingredients = row.getString("ingredients");
                        preparations = row.getString("preparation");
                    }

                    String newIngredients = "";
                    for (int i = 0; i < ingredients.length(); i++) {
                        if (ingredients.charAt(i) == '\n' || i == ingredients.length() - 1) {
                            TextView tv = new TextView(RecipesDetailsActivity.this);
                            tv.setText(newIngredients);
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.
                                    LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            lp.setMargins(5, 5, 5, 5);
                            tv.setLayoutParams(lp);
                            tv.setPadding(30, 30,30,30);
                            tv.setTextColor(Color.BLACK);
                            tv.setBackground(getResources().getDrawable(R.drawable.flexradius));
                            flex.addView(tv);
                            newIngredients = "";
                        } else {
                            newIngredients += ingredients.charAt(i);
                        }
                    }

                    String newPreparation = "";
                    for (int i = 0; i < preparations.length(); i++) {
                        if (preparations.charAt(i) == '\n' || i == preparations.length() - 1) {
                            TextView tv = new TextView(RecipesDetailsActivity.this);
                            tv.setText(newPreparation);
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.
                                    LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            lp.setMargins(5, 5, 5, 5);
                            tv.setLayoutParams(lp);
                            tv.setPadding(30, 30,30,30);
                            tv.setTextColor(Color.BLACK);
                            tv.setBackground(getResources().getDrawable(R.drawable.flex1radius));
                            tv.setGravity(Gravity.CENTER);
                            flex1.addView(tv);
                            newPreparation = "";
                        } else {
                            newPreparation += preparations.charAt(i);
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(RecipesDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RecipesDetailsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
    }
}