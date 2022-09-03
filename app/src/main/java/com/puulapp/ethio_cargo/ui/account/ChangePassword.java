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

public class ChangePassword extends AppCompatActivity {

    private EditText email_address, password, new_password, confi_password;
    private Button changePassBtn;
    private ProgressBar progressBarPassChange;
    private TextView forgotTextBtnPassChange, pass_pwrd_strength;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mAuth;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("customer").child("account").child(currentUserId);

        email_address = findViewById(R.id.email_for_pass_change);

        password = findViewById(R.id.pass_pass);

        new_password  = findViewById(R.id.new_pass);

        confi_password  = findViewById(R.id.conf_new_pass);

        changePassBtn = findViewById(R.id.changePassBtn);

        pass_pwrd_strength = findViewById(R.id.pass_pwrd_strength);

        progressBarPassChange = findViewById(R.id.progressBarPassChange);
        progressBarPassChange.setVisibility(View.GONE);

        forgotTextBtnPassChange = findViewById(R.id.forgotTextBtnPassChange);

        forgotTextBtnPassChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChangePassword.this, ForgotPassword.class));
                finish();
            }
        });

        changePassBtn.setOnClickListener(v -> {
            String email = email_address.getText().toString().trim();
            String old_pass = password.getText().toString().trim();
            String new_pass = new_password.getText().toString().trim();
            String con_pass = confi_password.getText().toString().trim();

            if (email.isEmpty()){
                email_address.setError("Email Address Required");
                email_address.requestFocus();
            } else if (old_pass.isEmpty()){
                password.setError("Password is required");
                password.requestFocus();
            } else if (new_pass.isEmpty()){
                new_password.setError("New Password Required");
                new_password.requestFocus();
            } else if(new_password.getText().toString().length() < 6){
                pass_pwrd_strength.setVisibility(View.VISIBLE);
                pass_pwrd_strength.setText("The password length should be greater than 6 and contain at least 1 digit and 1 char.");
                new_password.requestFocus();
            } else {
                boolean containsDigit = false;
                boolean containsLetter = false;
                String s = new_password.getText().toString();
                for (char c : s.toCharArray()){
                    if (containsDigit = Character.isDigit(c)){
                        break;
                    }
                }
                for (char c : s.toCharArray()){
                    if (containsLetter = Character.isLetter(c)){
                        break;
                    }
                }

                if (containsDigit && containsLetter){

                    pass_pwrd_strength.setVisibility(View.GONE);

                    if (!new_pass.equals(con_pass)) {
                        confi_password.setError("Password not match!!");
                        confi_password.requestFocus();
                    } else {
                        if (isInternetConnected()){
                            progressBarPassChange.setVisibility(View.VISIBLE);
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            // Get auth credentials from the user for re-authentication
                            AuthCredential credential = EmailAuthProvider
                                    .getCredential(email, old_pass); // Current Login Credentials \\
                            // Prompt the user to re-provide their sign-in credentials
                            assert user != null;
                            user.reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                //Now change your Password \\
                                                //----------------Code for Changing Password----------\\

                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                assert user != null;
                                                user.updatePassword(new_pass)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                progressBarPassChange.setVisibility(View.GONE);
                                                                if (task.isSuccessful()) {
                                                                    finish();
                                                                    Toast.makeText(ChangePassword.this, "Password changed", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    Toast.makeText(ChangePassword.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                //----------------------------------------------------------\\
                                            } else {
                                                progressBarPassChange.setVisibility(View.GONE);
                                                Toast.makeText(ChangePassword.this, "User re-authentication problem", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                        } else {
                            checkInternetConnection();
                        }

                    }


                }else {
                    pass_pwrd_strength.setVisibility(View.VISIBLE);
                    pass_pwrd_strength.setText("The password length should be greater than 6 and contain at least 1 digit and 1 char.");
                    new_password.requestFocus();
                    new_password.setError("Should contain at least one letter and one digit");
                }

            }
        });
        if (!isInternetConnected()) {
            checkInternetConnection();
        }

    }

    private void checkInternetConnection() {

        View view = findViewById(R.id.changePassLayout);
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