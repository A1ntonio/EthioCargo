package com.puulapp.ethio_cargo.ui.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.puulapp.ethio_cargo.R;
import com.puulapp.ethio_cargo.welcomeUi.ForgotPassword;

import java.util.Objects;

public class ChangeEmail extends AppCompatActivity {

    private EditText email_address, password, new_email_address;
    private Button changeBtn;
    private ProgressBar progressBarEmailChange;
    private TextView forgotTextBtnEmailChange;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mAuth;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_email);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("customer").child("account").child(currentUserId);

        email_address = findViewById(R.id.oldEmail);

        password = findViewById(R.id.pass_mail);

        new_email_address  = findViewById(R.id.newEmail);

        changeBtn = findViewById(R.id.changeBtn);

        progressBarEmailChange = findViewById(R.id.progressBarEmailChange);
        progressBarEmailChange.setVisibility(View.GONE);

        forgotTextBtnEmailChange = findViewById(R.id.forgotTextBtnEmailChange);

        forgotTextBtnEmailChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChangeEmail.this, ForgotPassword.class));
                finish();
            }
        });

        changeBtn.setOnClickListener(v -> {
            String old_email = email_address.getText().toString().trim();
            String pass = password.getText().toString().trim();
            String new_email = new_email_address.getText().toString().trim();

            if (old_email.isEmpty()){
                email_address.setError("Old Email Address Required");
                email_address.requestFocus();
            } else if (pass.isEmpty()){
                password.setError("Password is required");
                password.requestFocus();
            } else if (new_email.isEmpty()){
                new_email_address.setError("New Email Address Required");
                new_email_address.requestFocus();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(new_email).matches()){
                email_address.setError("Please enter a valid email address");
                email_address.requestFocus();
            } else {
                if (isInternetConnected()){
                    progressBarEmailChange.setVisibility(View.VISIBLE);
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    // Get auth credentials from the user for re-authentication
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(old_email, pass); // Current Login Credentials \\
                    // Prompt the user to re-provide their sign-in credentials
                    assert user != null;
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(ChangeEmail.this, "User re-authenticated", Toast.LENGTH_SHORT).show();
                                        //Now change your email address \\
                                        //----------------Code for Changing Email Address----------\\

                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        assert user != null;
                                        user.updateEmail(new_email)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        progressBarEmailChange.setVisibility(View.GONE);
                                                        if (task.isSuccessful()) {
                                                            mDatabaseRef.child("u_email").setValue(new_email);
                                                            finish();
                                                            Toast.makeText(ChangeEmail.this, "User email address changed", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(ChangeEmail.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                        //----------------------------------------------------------\\
                                    } else {
                                        progressBarEmailChange.setVisibility(View.GONE);
                                        Toast.makeText(ChangeEmail.this, "User re-authentication problem", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                } else {
                    checkInternetConnection();
                }

            }
        });
        if (!isInternetConnected()) {
            checkInternetConnection();
        }
    }

    private void checkInternetConnection() {

            View view = findViewById(R.id.changeEmailLayout);
            Snackbar.make(this, view, "No Internet connection. Please enable internet data or wifi.", 6000)
                    .setAction("Setting", v -> startActivityForResult(new Intent(Settings.ACTION_SETTINGS), 0))
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                    .show();


    }

    private boolean isInternetConnected(){
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }


}