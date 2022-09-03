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
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInCredential;
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

import static androidx.fragment.app.FragmentManager.TAG;

public class Signup extends AppCompatActivity {

    private EditText signupemail, signuppassword, signupname;
    private TextView signupback, signupsigninbtn, pass_str;
    private FirebaseAuth mAuth;
    private ConstraintLayout signupLayout;
    private CircularProgressButton signupbtn;
    private ProgressBar signupprogressBar;
    private Button googlesignup;
    BeginSignInRequest signInRequest;
//    private BeginSignInRequest signInRequest;
    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    private boolean showOneTapUI = true;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN =123;

    private StorageReference mStorageRef;

    private DatabaseReference user_account;

    String sent_name, sent_email;
    Uri sent_url;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        createRequest();

        // view initialization
        initialize();

        // view click methods
        clickMethods();

        if (!checkInternetConnection()){
            showSnackBar();
        }

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

    private void showSnackBar() {
        View view = findViewById(R.id.signupLayout);
        Snackbar.make(this, view, "No Internet connection. Please enable internet data or wifi.", 6000)
                .setAction("Setting", v -> startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0))
                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                .show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkInternetConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

    }

    private void clickMethods() {
        signupback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(signupLayout.getWindowToken(), 0);
                signUpUser();
            }
        });
        signupsigninbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signup.this, Signin.class);
                startActivity(intent);
                finish();
            }
        });
        googlesignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signingoogle();
            }
        });
    }

    private void initialize() {
        googlesignup = findViewById(R.id.googlesignup);
        signupprogressBar = findViewById(R.id.signupprogressBar);
        signupLayout = findViewById(R.id.signupLayout);
        signupback = findViewById(R.id.signupback);
        signupsigninbtn = findViewById(R.id.signupsignin);
        signupname = findViewById(R.id.signupname);
        signuppassword = findViewById(R.id.signuppassword);
        signupemail = findViewById(R.id.signupemail);
        signupbtn = (CircularProgressButton) findViewById(R.id.signupbtn);
        pass_str = findViewById(R.id.pass_str);
        pass_str.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();

        mStorageRef = FirebaseStorage.getInstance().getReference().child("user_image");
    }

    private void signUpUser() {

        String name = signupname.getText().toString().trim();
        String email_address = signupemail.getText().toString().trim();
        String pass = signuppassword.getText().toString().trim();

        if (name.isEmpty()){
            signupname.setError("First name is required!!");
            signupname.requestFocus();
        } else if (email_address.isEmpty()){
            signupemail.setError("Email is required!!");
            signupemail.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email_address).matches()){
            signupemail.setError("Enter a valid email");
            signupemail.requestFocus();
        } else if (signuppassword.getText().toString().isEmpty()){
            pass_str.setVisibility(View.VISIBLE);
            pass_str.setText("Password length should be greater than 6.");
            signuppassword.requestFocus();
        } else if(signuppassword.getText().toString().length() < 6){
            pass_str.setVisibility(View.VISIBLE);
            pass_str.setText("Password length should be greater than 6.");
            signuppassword.requestFocus();
        } else {
            boolean containsDigit = false;
            boolean containsLetter = false;
            String s = signuppassword.getText().toString();
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

                pass_str.setVisibility(View.GONE);

                signupbtn.startAnimation();

                    mAuth.createUserWithEmailAndPassword(email_address, pass).addOnCompleteListener(task -> {

                        if (task.isSuccessful()){
                            sendVerificationEmail(email_address, pass, name);
                        }else {
                            signupbtn.revertAnimation();
                            Toast.makeText(Signup.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

            }else {
                pass_str.setVisibility(View.VISIBLE);
                pass_str.setText("Password length should be greater than 6 and should contain letter and digit.");
                signuppassword.requestFocus();
                signuppassword.setError("Should contain at least one letter and one digit");
            }

        }

    }

    private void sendVerificationEmail(String email, String pass, String name) {
        signupbtn.startAnimation();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        user.sendEmailVerification()
                .addOnCompleteListener(task -> {
                    signupbtn.revertAnimation();
                    if (task.isSuccessful()) {
                        // email sent

                        Toast.makeText(Signup.this, "Email sent", Toast.LENGTH_LONG).show();
                        FirebaseAuth.getInstance().signOut();
                        // after email is sent just logout the user and finish this activity
                        Intent intent = new Intent(Signup.this, EmailVerification.class);
                        intent.putExtra("email", email);
                        intent.putExtra("pass", pass);
                        intent.putExtra("name", name);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        // email not sent, so display message and restart the activity or do whatever you wish to do

                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(Signup.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();

                        //restart this activity
                        overridePendingTransition(0, 0);
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        signupbtn.dispose();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                signupprogressBar.setVisibility(View.VISIBLE);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(Signup.this, "ic catch" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                            startActivity(new Intent(Signup.this, HomePageActivity.class));

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
                                            signupprogressBar.setVisibility(View.GONE);
                                            Toast.makeText(Signup.this, "Failed!", Toast.LENGTH_LONG).show();
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
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "");
            UploadTask uploadTask = fileReference.putFile(sent_url);
            uploadTask.addOnSuccessListener(taskSnapshot -> uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();

                }
                // Continue with the task to get the download URL
                return fileReference.getDownloadUrl();

            }).addOnCompleteListener(task -> {
                signupprogressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    String thumb_download_url = task.getResult().toString();
                    user_account.child("u_photo").setValue(thumb_download_url);
                    finish();
                    Toast.makeText(Signup.this, "Registration Successful!", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(Signup.this, "Not Uploaded", Toast.LENGTH_SHORT).show();
                }
            })).addOnFailureListener(e -> {
                signupprogressBar.setVisibility(View.GONE);
                finish();
                Toast.makeText(Signup.this, "No Image!", Toast.LENGTH_SHORT).show();
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {

                }
            });

        }else {
            Toast.makeText(Signup.this, "No file selected", Toast.LENGTH_SHORT).show();
        }

    }

    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
}