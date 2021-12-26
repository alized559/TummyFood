package com.example.tummyfood;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class RecipesDetailsActivity extends AppCompatActivity {

    private TextView ingredientsDetails;
    private TextView preparationDetails;
    private ImageView detailsImage;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        ingredientsDetails = findViewById(R.id.ingredientsDetails);
        preparationDetails = findViewById(R.id.preparationDetails);
        detailsImage = findViewById(R.id.detailsImage);

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
                    String preparation = "";
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject row = response.getJSONObject(i);
                        ingredients = row.getString("ingredients");
                        preparation = row.getString("preparation");
                    }
                    String newIngredients = "★ ";
                    for (int i = 0; i < ingredients.length(); i++) {
                        if (ingredients.charAt(i) == '\n') {
                            newIngredients += ingredients.charAt(i) + "\n" + "★ ";
                        } else {
                            newIngredients += ingredients.charAt(i);
                        }
                    }

                    String newPreparation = "★ ";
                    for (int i = 0; i < preparation.length(); i++) {
                        if (preparation.charAt(i) == '\n') {
                            newPreparation += preparation.charAt(i) + "\n" + "★ ";
                        } else {
                            newPreparation += preparation.charAt(i);
                        }
                    }

                    ingredientsDetails.setText(newIngredients);
                    preparationDetails.setText(newPreparation);
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