package com.puulapp.ethio_cargo.mainUi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.puulapp.ethio_cargo.R;
import com.puulapp.ethio_cargo.adapters.NotListAdapter;
import com.puulapp.ethio_cargo.spacecraft.NotSpacecraft;
import com.puulapp.ethio_cargo.adapters.TrackListAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.security.AccessController.getContext;

public class Notification extends AppCompatActivity {

    private TextView not_back_btn;
    private RecyclerView not_rv;

    private NotListAdapter listAdapter;
    DatabaseReference mDatabaseRefNot;
    List<NotSpacecraft> list;
    private SpinKitView not_spin_kit;
    private TextView not_message;
    private int position;

    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        initialization();
        viewAction();
    }


    private void viewAction() {
        not_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initialization() {
        not_back_btn = findViewById(R.id.not_back_btn);
        not_spin_kit = findViewById(R.id.not_spin_kit);
        not_message = findViewById(R.id.not_message);
        not_rv = findViewById(R.id.not_rv);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        not_rv.setLayoutManager(linearLayoutManager);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabaseRefNot = FirebaseDatabase.getInstance().getReference()
                .child("customer")
                .child("notifications").child(currentUserId);

        mDatabaseRefNot.keepSynced(true);
        list = new ArrayList<>();


        reload();

    }

    private void reload() {
        list.clear();
        mDatabaseRefNot.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snap, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                not_message.setVisibility(View.GONE);
                not_spin_kit.setVisibility(View.GONE);
                String notification = "";
                Log.d("snap_key", snap.getKey());

                if (snap.child("message").exists()) {
                    notification = snap.child("message").getValue().toString();
                }

                NotSpacecraft appSpace = new NotSpacecraft(notification, snap.getKey());
                list.add(appSpace);


                listAdapter = new NotListAdapter(list, Notification.this);
                not_rv.setAdapter(listAdapter);
                listAdapter.notifyDataSetChanged();

            }


            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {
                reload();
            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

            }

        });

    }



}