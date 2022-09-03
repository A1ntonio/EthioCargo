package com.puulapp.ethio_cargo.mainUi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.puulapp.ethio_cargo.R;
import com.puulapp.ethio_cargo.adapters.ManageListAdapter;
import com.puulapp.ethio_cargo.spacecraft.ManageSpacecraft;

import java.util.ArrayList;
import java.util.List;

public class Manage extends AppCompatActivity {

    private TextView manage_back_btn;
    private RecyclerView manage_rv;

    private ManageListAdapter listAdapter;
    DatabaseReference mDatabaseRefManage;
    List<ManageSpacecraft> list;
    private SpinKitView spin_kit;
    private TextView message_home_tv;

    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        initialization();
        viewAction();

    }

    private void viewAction() {
        manage_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initialization() {
        manage_back_btn = findViewById(R.id.manage_back_btn);
        spin_kit = findViewById(R.id._spin_kit);
        message_home_tv = findViewById(R.id._message_home);
        manage_rv = findViewById(R.id.manage_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        manage_rv.setLayoutManager(linearLayoutManager);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabaseRefManage = FirebaseDatabase.getInstance().getReference()
                .child("customer")
                .child("manage")
                .child(currentUserId);
        mDatabaseRefManage.keepSynced(true);
        list = new ArrayList<>();

        reload();
    }

    private void reload() {
        list.clear();
        mDatabaseRefManage.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snap, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                    message_home_tv.setVisibility(View.GONE);
                    spin_kit.setVisibility(View.GONE);
                    String _from = "";
                    String _to = "";
                    String _status = "";
                    String _action = "";
                    String _date = "";

                    if (snap.child("status").exists()) {
                        _status = snap.child("status").getValue().toString();
                    }if (snap.child("action").exists()) {
                        _action = snap.child("action").getValue().toString();
                    }if (snap.child("origin").exists()) {
                        _from = snap.child("origin").getValue().toString();
                    }if (snap.child("destination").exists()) {
                        _to = snap.child("destination").getValue().toString();
                    }if (snap.child("date").exists()) {
                        _date = snap.child("date").getValue().toString();
                    }

                        ManageSpacecraft appSpace = new ManageSpacecraft(_from, _to, _status, _action, _date, snap.getKey());
                        list.add(appSpace);

                listAdapter = new ManageListAdapter(list, Manage.this);
                listAdapter.notifyDataSetChanged();
                manage_rv.setAdapter(listAdapter);

            }


            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                reload();
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