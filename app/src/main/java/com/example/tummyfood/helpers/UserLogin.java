package com.example.tummyfood.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
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

    public static boolean isLoggedIn = false, isAdmin = false;
    public static String CurrentLoginUsername = "Anonymous";
    public static int CurrentLoginID = -1;

    public static void Login(Context context, boolean OpenUserPage) {
        if (isLoggedIn) {
            Intent intent = new Intent(context, UserPageActivity.class);
            context.startActivity(intent);
            return;
        }
        if (prefs == null) {
            prefs = context.getSharedPreferences("user_settings", context.MODE_PRIVATE);
        }
        String username = prefs.getString("username", null);
        String password = prefs.getString("password", null);

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            Intent intent = new Intent(context, UserAccountActivity.class);
            intent.putExtra("returnToUserPage", OpenUserPage);
            context.startActivity(intent);
        } else {
            SendLoginRequest(username, password, context, OpenUserPage);
        }
    }

    public static void AttemptLogin(Context context) {
        if (!isLoggedIn) {
            if (prefs == null) {
                prefs = context.getSharedPreferences("user_settings", context.MODE_PRIVATE);
            }
            String username = prefs.getString("username", null);
            String password = prefs.getString("password", null);

            if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
                //Login Failed
            } else {
                SendLoginRequest(username, password, context, false);
            }
        }
    }

    public static void SendLoginRequest(String username, String password, Context context, boolean OpenUserPage) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(1, ServerUrls.authenticate, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("success")) {
                    UpdateLoginState(true, username, response);
                    Toast.makeText(context, "Logged In Successfully!", Toast.LENGTH_SHORT).show();
                    UserLikes.UpdateLikes(context);
                    if (OpenUserPage) {
                        Intent intent = new Intent(context, UserPageActivity.class);
                        context.startActivity(intent);
                    }
                } else {
                    UpdateLoginState(false, "Anonymous", -1, false);
                    UserLikes.ResetLikes();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                UpdateLoginState(false, "Anonymous", -1, false);
                UserLikes.ResetLikes();
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

    public static void UpdateLoginState(boolean loggedIn, String username, int loginID, boolean admin){
        isLoggedIn = loggedIn;
        CurrentLoginUsername = username;
        CurrentLoginID = loginID;
        isAdmin = admin;
    }

    public static void UpdateLoginState(boolean loggedIn, String username, String response){
        isLoggedIn = loggedIn;
        CurrentLoginUsername = username;
        String loginData[] = response.replace("success_", "").split("_");
        int loginID = Integer.parseInt(loginData[0]);
        boolean admin = loginData[1].equalsIgnoreCase("1") ? true : false;
        CurrentLoginID = loginID;
        isAdmin = admin;
    }

    public static void UpdateAccount(String username, String password) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("username", username);
        editor.putString("password", password);

        editor.commit();//Apply changes
    }

    public static void Logout() {
        UserLikes.ResetLikes();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("username", null);
        editor.putString("password", null);
        UpdateLoginState(false, "Anonymous", -1, false);
        editor.commit();//Apply changes
    }

}
