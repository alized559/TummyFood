package com.example.tummyfood;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddRecipeActivity extends AppCompatActivity {

    private RequestQueue queue;

    EditText recipeTitle, recipePrepTime, recipeIngredients, recipePreparation;

    Button createRecipe;

    String currentCategory = "Main Dish";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        currentCategory = getIntent().getStringExtra("currentCategory");

        createRecipe = findViewById(R.id.recipeCreateButton);

        recipeTitle = findViewById(R.id.titleInputText);
        recipePrepTime = findViewById(R.id.prepTimeInputText);
        recipeIngredients= findViewById(R.id.ingredientsInputText);
        recipePreparation = findViewById(R.id.preparationInputText);

        queue = Volley.newRequestQueue(this);

        createRecipe.setOnClickListener(view -> {
            //Get The Image And Upload
            String title = recipeTitle.getText().toString();
            String prepTime = recipePrepTime.getText().toString();
            String ingredients = recipeIngredients.getText().toString();
            String preparation = recipePreparation.getText().toString();
            if(title.isEmpty() || prepTime.isEmpty() || ingredients.isEmpty() || preparation.isEmpty()){
                Toast.makeText(AddRecipeActivity.this, "You must fill in all info!", Toast.LENGTH_LONG).show();
                return;
            }
            mGetContent.launch("image/*");
        });
    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
        new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri uri) {
                try {
                    Bitmap image = MediaStore.Images.Media.getBitmap(AddRecipeActivity.this.getContentResolver(), uri);//Retrieve the image from files

                    String title = recipeTitle.getText().toString();
                    String prepTime = recipePrepTime.getText().toString();
                    String ingredients = recipeIngredients.getText().toString().replace("\n", "-nline-");//For New Lines
                    String preparation = recipePreparation.getText().toString().replace("\n", "-nline-");

                    createRecipe.setEnabled(false);

                    StringRequest request = new StringRequest(1, ServerUrls.CreateRecipe, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(AddRecipeActivity.this, response, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            image.compress(Bitmap.CompressFormat.JPEG,50,bos);
                            byte[] bb = bos.toByteArray();
                            String imager = Base64.encodeToString(bb, Base64.DEFAULT);

                            Map<String, String> params = new HashMap<>();
                            params.put("title", title);
                            params.put("type", currentCategory);
                            params.put("prepTime", prepTime);
                            params.put("ingredients", ingredients);
                            params.put("preparation", preparation);
                            params.put("file", imager);
                            return params;
                        }
                    };
                    queue.add(request);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

}