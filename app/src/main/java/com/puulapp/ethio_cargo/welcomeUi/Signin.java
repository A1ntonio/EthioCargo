package com.puulapp.ethio_cargo.welcomeUi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.puulapp.ethio_cargo.HomePageActivity;
import com.puulapp.ethio_cargo.R;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class Signin extends AppCompatActivity {

    private EditText signinemail, signinpassword;
    private TextView signinback, signinsignupbtn, forgot;
    private CircularProgressButton loginBtn;
    private DatabaseReference mDatabase;
    private ConstraintLayout loginConLayout;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressBar signinprogressBar;
    private Button googlesignin;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN =123;

    private StorageReference mStorageRef;

    private DatabaseReference user_account;

    String sent_name, sent_email;
    Uri sent_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        createRequest();

        // view initialization
        initialize();

        // view click methods
        clickMethods();

        mAuthListener = firebaseAuth -> {};

        if (!checkInternetConnection()){
            showSnackBar();
        }

    }

    private void showSnackBar() {
        View view = findViewById(R.id.loginConLayout);
        Snackbar.make(this, view, "No Internet connection. Please enable internet data or wifi.", 6000)
                .setAction("Setting", v -> startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0))
                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                .show();
    }

    private boolean checkInternetConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

    }

    private void sendVerificationEmail(String email, String pass) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        user.sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // email sent

                        Toast.makeText(Signin.this, "email sent", Toast.LENGTH_LONG).show();
                        FirebaseAuth.getInstance().signOut();
                        // after email is sent just logout the user and finish this activity
                        Intent intent = new Intent(Signin.this, EmailVerification.class);
                        intent.putExtra("email", email);
                        intent.putExtra("pass", pass);
                        startActivity(intent);
                    }
                    else
                    {
                        // email not sent, so display message and restart the activity or do whatever you wish to do

                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(Signin.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();

                        //restart this activity
                        overridePendingTransition(0, 0);
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());

                    }
                });
    }

    private void createRequest() {
//        signInRequest = BeginSignInRequest.builder()
//                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                        .setSupported(true)
//                        // Your server's client ID, not your Android client ID.
//                        .setServerClientId(getString(R.string.default_web_client_id))
//                        // Only show accounts previously used to sign in.
//                        .setFilterByAuthorizedAccounts(true)
//                        .build())
//                .build();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }


    private void clickMethods() {
        signinback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        loginBtn.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(loginConLayout.getWindowToken(), 0);
            startLogin();
        });
        signinsignupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signin.this, Signup.class);
                startActivity(intent);
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signin.this, ForgotPassword.class);
                startActivity(intent);
            }
        });
        googlesignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signingoogle();
            }
        });
    }

    private void startLogin() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        loginBtn.startAnimation();

        String email = signinemail.getText().toString().trim();
        String pass = signinpassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)){
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            loginBtn.revertAnimation();
            Toast.makeText(this, "Fields are empty", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                if (!task.isSuccessful()){
                    loginBtn.revertAnimation();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(Signin.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    checkIfEmailVerified(email, pass);
                }
            });
        }
    }

    private void checkIfEmailVerified(String email, String pass) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        if (user.isEmailVerified())
        {
            // user is verified, so you can finish this activity or send user to activity which you want.
            loginBtn.revertAnimation();
            startActivity(new Intent(Signin.this, HomePageActivity.class));
            finish();
        }
        else
        {
            loginBtn.revertAnimation();
            Toast.makeText(Signin.this, "Email is not verified!", Toast.LENGTH_LONG).show();

            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
//            FirebaseAuth.getInstance().signOut();
//            overridePendingTransition(0, 0);
//            finish();
//            overridePendingTransition(0, 0);
//            startActivity(getIntent());

            sendVerificationEmail(email, pass);

        }

    }


    private void initialize() {
        googlesignin = findViewById(R.id.googlesignin);
        forgot = findViewById(R.id.forgot);
        signinprogressBar = findViewById(R.id.signinprogressBar);
        signinback = findViewById(R.id.signinback);
        signinsignupbtn = findViewById(R.id.signinsignupbtn);
        signinpassword = findViewById(R.id.signinpassword);
        signinemail = findViewById(R.id.signinemail);
        loginBtn = findViewById(R.id.signinbtn);

        mAuth = FirebaseAuth.getInstance();

        mStorageRef = FirebaseStorage.getInstance().getReference().child("user_image");
        loginConLayout = findViewById(R.id.loginConLayout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        signinprogressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent intent = new Intent(Signin.this, HomePageActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                signinprogressBar.setVisibility(View.VISIBLE);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(Signin.this, "ic catch" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        sent_email = account.getEmail();
        sent_name = account.getDisplayName();
        sent_url = account.getPhotoUrl();

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(Signin.this, HomePageActivity.class));
                            finish();

                            user_account = FirebaseDatabase.getInstance().getReference("customer")
                                    .child("account")
                                    .child(user.getUid());

                            UserSpacecraft userSpacecraft = new UserSpacecraft(sent_name, sent_email);

                            user_account.setValue(userSpacecraft)
                                    .addOnCompleteListener(task1 -> {

                                        if (task1.isSuccessful()){
                                            user_account.child("time").setValue(ServerValue.TIMESTAMP);
                                            uploadFile();
                                        }
                                        else {
                                            signinprogressBar.setVisibility(View.GONE);
                                            Toast.makeText(Signin.this, "Failed!", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    }
                });
    }

    private void signingoogle(){
        Intent signingoogleIntenet = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signingoogleIntenet, RC_SIGN_IN);
    }

    private void uploadFile() {

        if (sent_url != null){
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(sent_url));
            UploadTask uploadTask = fileReference.putFile(sent_url);
            uploadTask.addOnSuccessListener(taskSnapshot -> uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();

                }
                // Continue with the task to get the download URL
                return fileReference.getDownloadUrl();

            }).addOnCompleteListener(task -> {
                signinprogressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    String thumb_download_url = task.getResult().toString();
                    user_account.child("u_photo").setValue(thumb_download_url);
                    finish();
                    Toast.makeText(Signin.this, "Registration Successful!", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(Signin.this, "Not Uploaded", Toast.LENGTH_SHORT).show();
                }
            })).addOnFailureListener(e -> {
                signinprogressBar.setVisibility(View.GONE);
                finish();
                Toast.makeText(Signin.this, "No Image!", Toast.LENGTH_SHORT).show();
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {

                }
            });

        }else {
            Toast.makeText(Signin.this, "No file selected", Toast.LENGTH_SHORT).show();
        }

    }

    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

}