package com.puulapp.ethio_cargo.welcomeUi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.puulapp.ethio_cargo.HomePageActivity;
import com.puulapp.ethio_cargo.R;

public class Welcome extends AppCompatActivity {

    private AppCompatButton welsigninbtn;
    private TextView welsignupbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        welsigninbtn = findViewById(R.id.welsigninbtn);
        welsignupbtn = findViewById(R.id.welsignupbtn);

        welsignupbtn.setOnClickListener(view -> {
            Intent intent = new Intent(Welcome.this, Signup.class);
            startActivity(intent);
        });
        welsigninbtn.setOnClickListener(view -> {
            Intent intent = new Intent(Welcome.this, Signin.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent intent = new Intent(Welcome.this, HomePageActivity.class);
            startActivity(intent);
            finish();
        }
    }
}