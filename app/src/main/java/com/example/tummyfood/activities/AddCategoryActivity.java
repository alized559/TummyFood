package com.example.tummyfood.activities;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tummyfood.R;
import com.example.tummyfood.helpers.ServerUrls;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddCategoryActivity extends AppCompatActivity {

    EditText categoryTitle;

    Button createCategory;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        createCategory = findViewById(R.id.categoryCreateButton);

        categoryTitle = findViewById(R.id.categoryTitleInputText);

        queue = Volley.newRequestQueue(this);

        createCategory.setOnClickListener(view -> {
            //Get The Image And Upload
            String title = categoryTitle.getText().toString();
            if (title.isEmpty() && title.length() < 4) {
                Toast.makeText(AddCategoryActivity.this, "You must fill in all info!", Toast.LENGTH_LONG).show();
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
                    Bitmap image = MediaStore.Images.Media.getBitmap(AddCategoryActivity.this.getContentResolver(), uri);//Retrieve the image from files

                    String title = categoryTitle.getText().toString();
                    createCategory.setEnabled(false);

                    StringRequest request = new StringRequest(1, ServerUrls.createCategory, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(AddCategoryActivity.this, response, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            createCategory.setEnabled(true);
                            Toast.makeText(AddCategoryActivity.this, "Error, Couldn't Add Category!", Toast.LENGTH_SHORT).show();
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