package com.example.tummyfood.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tummyfood.activities.UserPageActivity;
import com.example.tummyfood.activities.UserAccountActivity;

import java.util.HashMap;
import java.util.Map;

public class UserLogin {

    static SharedPreferences prefs = null;

    public static boolean IsLoggedIn = false;
    public static String CurrentLoginUsername = "Anonymous";
    public static int CurrentLoginID = -1;

    public static void Login(Context context, boolean OpenUserPage){
        if(IsLoggedIn){
            Intent intent = new Intent(context, UserPageActivity.class);
            context.startActivity(intent);
            return;
        }
        if(prefs == null) {
            prefs = context.getSharedPreferences("user_settings", context.MODE_PRIVATE);
        }
        String username = prefs.getString("username", null);
        String password = prefs.getString("password", null);

        if(username == null || password == null || username.isEmpty() || password.isEmpty()){
            Intent intent = new Intent(context, UserAccountActivity.class);
            intent.putExtra("returnToUserPage", OpenUserPage);
            context.startActivity(intent);
        }else {
            SendLoginRequest(username, password, context, OpenUserPage);
        }
    }

    public static void SendLoginRequest(String username, String password, Context context, boolean OpenUserPage){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(1, ServerUrls.Authenticate, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("success")) {
                    IsLoggedIn = true;
                    CurrentLoginUsername = username;
                    CurrentLoginID = Integer.parseInt(response.replace("success_", ""));
                    Toast.makeText(context, "Logged In Successfully!", Toast.LENGTH_SHORT).show();
                    if(OpenUserPage){
                        Intent intent = new Intent(context, UserPageActivity.class);
                        context.startActivity(intent);
                    }
                }else {
                    IsLoggedIn = false;
                    CurrentLoginUsername = "Anonymous";
                    CurrentLoginID = -1;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                IsLoggedIn = false;
                CurrentLoginUsername = "Anonymous";
                CurrentLoginID = -1;
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                params.put("login", "true");
                return params;
            }
        };
        queue.add(request);
    }

    public static void UpdateAccount(String username, String password){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("username", username);
        editor.putString("password", password);

        editor.commit();//Apply changes
    }

    public static void Logout(){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("username", null);
        editor.putString("password", null);
        IsLoggedIn = false;
        CurrentLoginUsername = "Anonymous";
        CurrentLoginID = -1;
        editor.commit();//Apply changes
    }

}
