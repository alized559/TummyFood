package com.example.tummyfood.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tummyfood.R;
import com.example.tummyfood.helpers.ServerUrls;
import com.example.tummyfood.helpers.UserLikes;
import com.example.tummyfood.helpers.UserLogin;

import java.util.HashMap;
import java.util.Map;

public class UserAccountActivity extends AppCompatActivity {

    boolean isLoggingIn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        TextView repeatPasswordTextView = findViewById(R.id.repeatPasswordTextView);

        EditText usernameText = findViewById(R.id.usernameInputText);

        EditText passwordText = findViewById(R.id.passwordInputText);

        EditText repeatPasswordText = findViewById(R.id.repeatPasswordInputText);

        Button loginButton = findViewById(R.id.loginButton);

        TextView errorMessage = findViewById(R.id.errorMessageTextView);
        TextView switchAccount = findViewById(R.id.switchAccountText);

        boolean returnToUser = getIntent().getBooleanExtra("returnToUserPage", true);

        errorMessage.setVisibility(View.INVISIBLE);
        repeatPasswordText.setVisibility(View.GONE);
        repeatPasswordTextView.setVisibility(View.GONE);

        Animation bounceAnim = AnimationUtils.loadAnimation(this, R.anim.bounce2);

        switchAccount.setOnClickListener(view -> {
            if (isLoggingIn) {
                isLoggingIn = false;
                repeatPasswordText.setVisibility(View.VISIBLE);
                repeatPasswordTextView.setVisibility(View.VISIBLE);
                switchAccount.setText("Already Have An Account? Login.");
                loginButton.setText("Register");
            } else {
                isLoggingIn = true;
                repeatPasswordText.setVisibility(View.GONE);
                repeatPasswordTextView.setVisibility(View.GONE);
                switchAccount.setText("Don't Have An Account? Register.");
                loginButton.setText("Login");
            }
            errorMessage.clearAnimation();
            errorMessage.setVisibility(View.INVISIBLE);
        });


        loginButton.setOnClickListener(view -> {
            String username = usernameText.getText().toString();
            String password = passwordText.getText().toString();
            if (isLoggingIn) {
                if (username.length() < 4 || password.length() < 6) {
                    Toast.makeText(UserAccountActivity.this, "Input Fields Invalid Length (MIN 6)", Toast.LENGTH_LONG).show();
                } else {
                    errorMessage.clearAnimation();
                    errorMessage.setVisibility(View.INVISIBLE);
                    loginButton.setText("Please Wait");
                    loginButton.setEnabled(false);
                    RequestQueue queue = Volley.newRequestQueue(UserAccountActivity.this);
                    StringRequest request = new StringRequest(1, ServerUrls.authenticate, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.contains("success")) {
                                UserLogin.isLoggedIn = true;
                                UserLogin.CurrentLoginID = Integer.parseInt(response.replace("success_", ""));
                                UserLogin.CurrentLoginUsername = username;
                                UserLogin.UpdateAccount(username, password);
                                UserLikes.UpdateLikes(UserAccountActivity.this);
                                if (returnToUser) {
                                    Intent intent = new Intent(UserAccountActivity.this, UserPageActivity.class);
                                    startActivity(intent);
                                }
                                finish();
                            } else if (response.equals("error")) {
                                UserLogin.isLoggedIn = false;
                                UserLogin.CurrentLoginUsername = "Anonymous";
                                UserLogin.CurrentLoginID = -1;
                                UserLikes.ResetLikes();
                                loginButton.setText("Login");
                                loginButton.setEnabled(true);
                                errorMessage.setText("Username Or Password Incorrect!");
                                errorMessage.setVisibility(View.VISIBLE);
                                errorMessage.startAnimation(bounceAnim);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            UserLogin.isLoggedIn = false;
                            UserLogin.CurrentLoginUsername = "Anonymous";
                            UserLogin.CurrentLoginID = -1;
                            UserLikes.ResetLikes();
                            loginButton.setText("Login");
                            loginButton.setEnabled(true);
                            errorMessage.setText("Database Error!");
                            errorMessage.setVisibility(View.VISIBLE);
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
            } else {
                String repeatPassword = repeatPasswordText.getText().toString();
                if(username.length() < 4 || password.length() < 6 || repeatPassword.length() < 6){
                    Toast.makeText(UserAccountActivity.this, "Input Fields Invalid Length (MIN 6)", Toast.LENGTH_LONG).show();
                }else {
                    if(!password.equals(repeatPassword)){
                        Toast.makeText(UserAccountActivity.this, "Passwords Must Match!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    errorMessage.clearAnimation();
                    errorMessage.setVisibility(View.INVISIBLE);
                    loginButton.setText("Please Wait");
                    loginButton.setEnabled(false);
                    RequestQueue queue = Volley.newRequestQueue(UserAccountActivity.this);
                    StringRequest request = new StringRequest(1, ServerUrls.authenticate, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.contains("success")) {
                                UserLogin.isLoggedIn = true;
                                UserLogin.CurrentLoginID = Integer.parseInt(response.replace("success_", ""));
                                UserLogin.CurrentLoginUsername = username;
                                UserLogin.UpdateAccount(username, password);
                                UserLikes.UpdateLikes(UserAccountActivity.this);
                                if (returnToUser) {
                                    Intent intent = new Intent(UserAccountActivity.this, UserPageActivity.class);
                                    startActivity(intent);
                                }
                                finish();
                            } else if(response.equals("error")) {
                                errorMessage.setText("Username Already Exists!");
                                errorMessage.setVisibility(View.VISIBLE);
                                errorMessage.startAnimation(bounceAnim);
                                loginButton.setText("Register");
                                loginButton.setEnabled(true);
                                UserLogin.isLoggedIn = false;
                                UserLogin.CurrentLoginUsername = "Anonymous";
                                UserLogin.CurrentLoginID = -1;
                                UserLikes.ResetLikes();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            errorMessage.setText("Database Error!");
                            errorMessage.setVisibility(View.VISIBLE);
                            errorMessage.startAnimation(bounceAnim);
                            loginButton.setText("Register");
                            loginButton.setEnabled(true);
                            UserLogin.isLoggedIn = false;
                            UserLogin.CurrentLoginUsername = "Anonymous";
                            UserLogin.CurrentLoginID = -1;
                            UserLikes.ResetLikes();
                        }
                    }) {
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("username", username);
                            params.put("password", password);
                            params.put("login", "false");
                            return params;
                        }
                    };
                    queue.add(request);
                }
            }
        });


    }
}