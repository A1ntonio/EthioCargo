package com.puulapp.ethio_cargo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.puulapp.ethio_cargo.databinding.ActivityHomePageBinding;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class HomePageActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomePageBinding binding;

    private TextView u_name, u_header_address;
    private ImageView u_header_image;

    private DatabaseReference mDatabaseRefFeedback;
    private FirebaseAuth mAuth;
    private String currentUserId;
    String name = "ETHIO-CARGO";
    String address = "ETHIOPIA";
    String profile_image = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        mDatabaseRefFeedback = FirebaseDatabase.getInstance().getReference().child("customer").child("account").child(currentUserId);

        setSupportActionBar(binding.appBarHomePage.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_account, R.id.nav_about, R.id.nav_feed, R.id.nav_share, R.id.nav_help)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home_page);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        mDatabaseRefFeedback.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if (snapshot.child("u_name").exists()) {
                    name = snapshot.child("u_name").getValue().toString();
                }
                if (snapshot.child("u_address").exists()) {
                    address = snapshot.child("u_address").getValue().toString();
                }
                if (snapshot.child("u_photo").exists()){
                    profile_image = snapshot.child("u_photo").getValue().toString();
                }

                u_header_address = findViewById(R.id.u_header_address);
                u_header_image = findViewById(R.id.u_header_image);
                u_name = findViewById(R.id.u_header_name);

                if (u_name != null || u_header_address != null){
                    u_name.setText(name);
                    u_header_address.setText(address);

                    if (!profile_image.equals("")){

                        Picasso.get().load(profile_image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(Objects.requireNonNull(ResourcesCompat.getDrawable(getResources(), R.drawable.profile, null))).into(u_header_image, new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError(Exception e) {
                                Picasso.get().load(profile_image).placeholder(Objects.requireNonNull(ResourcesCompat.getDrawable(getResources(), R.drawable.profile, null))).into(u_header_image);
                            }
                        });
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home_page);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}