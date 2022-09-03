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
import com.puulapp.ethio_cargo.adapters.TrackListAdapter;
import com.puulapp.ethio_cargo.spacecraft.TrackSpacecraft;

import java.util.ArrayList;
import java.util.List;

public class Track extends AppCompatActivity {

    private TextView track_back_btn;
    private RecyclerView track_rv;

    private TrackListAdapter listAdapter;
    DatabaseReference mDatabaseRefTrack;
    List<TrackSpacecraft> list;
    private SpinKitView track_spin_kit;
    private TextView track_message;

    String from, to, date;
    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        initialization();
        viewAction();
    }


    private void viewAction() {
        track_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initialization() {
        track_back_btn = findViewById(R.id.track_back_btn);
        track_spin_kit = findViewById(R.id.track_spin_kit);
        track_message = findViewById(R.id.track_message);
        track_rv = findViewById(R.id.track_rv);
        track_rv.setLayoutManager(new LinearLayoutManager(this));

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabaseRefTrack = FirebaseDatabase.getInstance().getReference()
                .child("customer")
                .child("manage").child(currentUserId);
        mDatabaseRefTrack.keepSynced(true);
        list = new ArrayList<>();

        reload();
    }

    private void reload() {
        list.clear();
        mDatabaseRefTrack.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snap, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                    track_message.setVisibility(View.GONE);
                    track_spin_kit.setVisibility(View.GONE);
                    String _from = "";
                    String _to = "";
                    String _status = "";
                    String _date = "";
                    String _book_key = "";

                    if (snap.child("action").exists()) {
                        _status = snap.child("action").getValue().toString();
                    }if (snap.child("origin").exists()) {
                        _from = snap.child("origin").getValue().toString();
                    }if (snap.child("destination").exists()) {
                        _to = snap.child("destination").getValue().toString();
                    }if (snap.child("date").exists()) {
                        _date = snap.child("date").getValue().toString();
                    }if (snap.child("key").exists()) {
                        _book_key = snap.child("key").getValue().toString();
                    }

                        TrackSpacecraft appSpace = new TrackSpacecraft(_from, _to, _status, _date, _book_key);
                        list.add(appSpace);

                listAdapter = new TrackListAdapter(list, Track.this);
                listAdapter.notifyDataSetChanged();
                track_rv.setAdapter(listAdapter);

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