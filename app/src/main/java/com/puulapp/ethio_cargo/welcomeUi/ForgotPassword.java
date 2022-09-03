package com.puulapp.ethio_cargo.welcomeUi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.puulapp.ethio_cargo.R;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;


public class ForgotPassword extends AppCompatActivity {

    private CircularProgressButton nextBtn_forgot;
    private EditText email_forgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        email_forgot = findViewById(R.id.email_forgot);
        nextBtn_forgot = findViewById(R.id.nextBtn_forgot);

        if (!checkInternetConnection()){
            showSnackBar();
        }

        nextBtn_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_forgot.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    email_forgot.setError("Enter a valid email");
                    email_forgot.requestFocus();
                } else if (email.isEmpty()){
                    email_forgot.setError("Email is required");
                    email_forgot.requestFocus();
                } else {
                    if (checkInternetConnection()){
                        nextBtn_forgot.startAnimation();
                        FirebaseAuth auth = FirebaseAuth.getInstance();

                        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                nextBtn_forgot.revertAnimation();
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotPassword.this, "Check email to reset your password!", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(ForgotPassword.this, "Fail to send reset password email!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        showSnackBar();
                    }

                }
            }
        });
    }//End of onCreate methode

    //Show snack bar for displaying no internet connection message
    private void showSnackBar() {
        View view = findViewById(R.id.forgotConLayout);
        Snackbar.make(this, view, "No Internet connection. Please enable internet data or wifi.", 6000)
                .setAction("Setting", v -> startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0))
                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                .show();
    } //End of snack bar

    //Check internet connection before changing password
    private boolean checkInternetConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

    } //End of check internet connection
} //End of class forgot password