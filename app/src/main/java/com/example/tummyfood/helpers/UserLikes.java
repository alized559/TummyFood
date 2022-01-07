package com.example.tummyfood.helpers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tummyfood.activities.MainActivity;
import com.example.tummyfood.activities.UserPageActivity;
import com.example.tummyfood.models.RecipeDataModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserLikes {

    private static ArrayList<Integer> LikedRecipes = new ArrayList<Integer>();

    public static void AddLike(int RecipeID){
        if(!LikedRecipes.contains(RecipeID)){
            LikedRecipes.add(RecipeID);
        }
    }

    public static void RemoveLike(int RecipeID){
        if(LikedRecipes.contains(RecipeID)){
            LikedRecipes.remove((Object)RecipeID);
        }
    }

    public static boolean DoesLike(int RecipeID){
        return LikedRecipes.contains(RecipeID);
    }

    public static void UpdateLikes(Context context){
        LikedRecipes.clear();
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(ServerUrls.GetUserLikes(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0;i < response.length();i++) {
                        JSONObject row = response.getJSONObject(i);
                        int id = row.getInt("recipeID");
                        if(!LikedRecipes.contains(id)){
                            LikedRecipes.add(id);
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

    public static void ResetLikes(){
        LikedRecipes.clear();
    }

}
