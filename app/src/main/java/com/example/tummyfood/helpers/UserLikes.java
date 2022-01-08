package com.example.tummyfood.helpers;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserLikes {

    private static ArrayList<Integer> likedRecipes = new ArrayList<Integer>();

    public static void addLike(int RecipeID) {
        if (!likedRecipes.contains(RecipeID)) {
            likedRecipes.add(RecipeID);
        }
    }

    public static void removeLike(int RecipeID) {
        if (likedRecipes.contains(RecipeID)) {
            likedRecipes.remove((Object)RecipeID);
        }
    }

    public static boolean DoesLike(int RecipeID){
        return likedRecipes.contains(RecipeID);
    }

    public static void UpdateLikes(Context context) {
        likedRecipes.clear();
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(ServerUrls.getUserLikes(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject row = response.getJSONObject(i);
                        int id = row.getInt("recipeID");
                        if(!likedRecipes.contains(id)){
                            likedRecipes.add(id);
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
        likedRecipes.clear();
    }

}
