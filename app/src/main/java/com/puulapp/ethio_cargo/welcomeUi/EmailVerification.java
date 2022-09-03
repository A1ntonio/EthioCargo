package com.puulapp.ethio_cargo.welcomeUi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.puulapp.ethio_cargo.R;

import java.util.Objects;

public class EmailVerification extends AppCompatActivity {

    private Button emailVerifyBtn;

    private String sent_email, sent_pass, sent_name;

    private ProgressBar progressBarEmail;

    private TextView waitUntil, resendBtn;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        Bundle email = getIntent().getExtras();
        Bundle pass = getIntent().getExtras();
        Bundle name = getIntent().getExtras();
        if(email!=null && pass != null) {
            sent_name = name.getString("name");
            sent_email = email.getString("email");
            sent_pass = pass.getString("pass");
        }

        if (!checkInternetConnection()){
            showSnackBar();
        }

        mAuth = FirebaseAuth.getInstance();

        initialize();

        actionMethod();
    }

    private void showSnackBar() {
        View view = findViewById(R.id.emailCoLayout);
        Snackbar.make(this, view, "No Internet connection. Please enable internet data or wifi.", 6000)
                .setAction("Setting", v -> startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0))
                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                .show();
    }

    private void actionMethod() {
        emailVerifyBtn.setOnClickListener(v -> {
            progressBarEmail.setVisibility(View.VISIBLE);
            if (checkInternetConnection()){
                authenticate();
            } else {
                showSnackBar();
            }

        });

        resendBtn.setOnClickListener(v -> {
            if (checkInternetConnection()) {
                sendEmailAgain();
            } else {
                showSnackBar();
            }

            progressBarEmail.setVisibility(View.VISIBLE);
        });
    }

    private void sendEmailAgain() {
        mAuth.signInWithEmailAndPassword(sent_email, sent_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()){
                    progressBarEmail.setVisibility(View.GONE);
                    emailVerifyBtn.setEnabled(true);
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(EmailVerification.this, "A problem!", Toast.LENGTH_LONG).show();
                } else {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    assert user != null;
                    user.sendEmailVerification()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    FirebaseAuth.getInstance().signOut();
                                    progressBarEmail.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        // email sent
                                        Toast.makeText(EmailVerification.this, "New email sent", Toast.LENGTH_LONG).show();
                                        countDownTimer();

                                    }
                                    else
                                    {
                                        // email not sent, so display message and restart the activity or do whatever you wish to do
                                        Toast.makeText(EmailVerification.this, "Please try again", Toast.LENGTH_LONG).show();
                                        FirebaseAuth.getInstance().signOut();
                                    }
                                }
                            });
                }
            }
        });
    }


    private void initialize() {
        emailVerifyBtn = findViewById(R.id.emailVerfiyBtn);
        progressBarEmail = findViewById(R.id.progressBarEmail);
        waitUntil = findViewById(R.id.waitUntil);
        resendBtn = findViewById(R.id.resendBtn);
        progressBarEmail.setVisibility(View.GONE);

        countDownTimer();
    }

    private void countDownTimer() {
        resendBtn.setVisibility(View.GONE);
        waitUntil.setVisibility(View.VISIBLE);
        new CountDownTimer(20000, 1000) {

            public void onTick(long millisUntilFinished) {
                waitUntil.setText("Please wait " + millisUntilFinished / 1000 + " second before trying again.");
            }

            public void onFinish() {
                resendBtn.setVisibility(View.VISIBLE);
                waitUntil.setVisibility(View.GONE);
            }
        }.start();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        progressBarEmail.setVisibility(View.VISIBLE);
        emailVerifyBtn.setEnabled(false);
        authenticate();
    }

    private void authenticate() {
        mAuth.signInWithEmailAndPassword(sent_email, sent_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull  Task<AuthResult> task) {

                if (!task.isSuccessful()){
                    progressBarEmail.setVisibility(View.GONE);
                    emailVerifyBtn.setEnabled(true);
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(EmailVerification.this, "A problem!", Toast.LENGTH_LONG).show();
                } else {
                    checkIfEmailVerified();
                }
            }
        });
    }

    private void checkIfEmailVerified() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        if (user.isEmailVerified())
        {
            // user is verified, so you can finish this activity or send user to activity which you want.

            DatabaseReference account = FirebaseDatabase.getInstance().getReference("customer")
                    .child("account")
                    .child(user.getUid());

            UserSpacecraft userSpacecraft = new UserSpacecraft(sent_name, sent_email);

            account.setValue(userSpacecraft)
                    .addOnCompleteListener(task1 -> {
                        progressBarEmail.setVisibility(View.GONE);
                        if (task1.isSuccessful()){
                            account.child("time").setValue(ServerValue.TIMESTAMP);
                            Toast.makeText(EmailVerification.this, "Email verified!", Toast.LENGTH_SHORT).show();

                            FirebaseAuth.getInstance().signOut();
                            finish();
                            Toast.makeText(EmailVerification.this, "Registration successful!!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(EmailVerification.this, "Failed!", Toast.LENGTH_LONG).show();
                        }
                    });

            }
        else
        {
            progressBarEmail.setVisibility(View.GONE);
            emailVerifyBtn.setEnabled(true);
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(EmailVerification.this, "Email is not verified!", Toast.LENGTH_SHORT).show();

            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.


        }

    }

    private boolean checkInternetConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

}