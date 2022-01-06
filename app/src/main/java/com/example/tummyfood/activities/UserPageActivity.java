package com.example.tummyfood.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tummyfood.R;
import com.example.tummyfood.helpers.UserLogin;

public class UserPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        TextView title = findViewById(R.id.username_page_title);

        title.setText("Welcome " + UserLogin.CurrentLoginUsername);

        Button logoutButton = findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(view -> {
            UserLogin.Logout();
            UserLogin.Login(UserPageActivity.this, true);
            finish();
        });

    }
}