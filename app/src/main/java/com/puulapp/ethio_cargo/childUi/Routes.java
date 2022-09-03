package com.puulapp.ethio_cargo.childUi;

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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.puulapp.ethio_cargo.R;
import com.puulapp.ethio_cargo.adapters.RoutesListAdapter;
import com.puulapp.ethio_cargo.spacecraft.BookSpacecraft;
import com.puulapp.ethio_cargo.spacecraft.RoutesListSpacecraft;

import java.util.ArrayList;
import java.util.List;

public class Routes extends AppCompatActivity {

    private TextView routes_back_btn;
    private RecyclerView route_rv;
    private RoutesListAdapter listAdapter;
    DatabaseReference mDatabaseRefRoute;
    List<RoutesListSpacecraft> list;
    List<BookSpacecraft> bookList;
    private SpinKitView spin_kit;
    private TextView message_home_tv;

    boolean loaded = false;
    ValueEventListener ve;

    String currentUserId;
    String origin, destination, date, weight, pieces, length, width, height, volume, total_volume, agent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_route);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            origin = bundle.getString("origin");
            destination = bundle.getString("destination");
            date = bundle.getString("date");
            weight = bundle.getString("weight");
            pieces = bundle.getString("pieces");
            length = bundle.getString("length");
            width = bundle.getString("width");
            height = bundle.getString("height");
            volume = bundle.getString("volume");
            total_volume = bundle.getString("total_volume");
            agent = bundle.getString("agent");

        }

        initialization();
        viewAction();

    }

    private void viewAction() {
        routes_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initialization() {
        spin_kit = findViewById(R.id.spin_kit_ap);
        routes_back_btn = findViewById(R.id.routes_back_btn);
        message_home_tv = findViewById(R.id.message_home_tv);
        route_rv = findViewById(R.id.route_rv);
        route_rv.setLayoutManager(new LinearLayoutManager(this));

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabaseRefRoute = FirebaseDatabase.getInstance().getReference()
                .child("administration")
                .child("routes");
        mDatabaseRefRoute.keepSynced(true);
        list = new ArrayList<>();
        bookList = new ArrayList<>();

        BookSpacecraft spacecraft = new BookSpacecraft(origin, destination, date, weight, pieces, length, width, height, volume, total_volume, agent);
        bookList.add(spacecraft);

        reload();
    }

    private void reload() {
        list.clear();
        mDatabaseRefRoute.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snap, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                    message_home_tv.setVisibility(View.GONE);
                    spin_kit.setVisibility(View.GONE);
                    String appoint_key = snap.getKey();
                    String _date = "";
                    String flight_no = "";
                    String aircraft_type = "";
                    String _origin = "";
                    String _destination = "";

                    if (snap.child("flight_no").exists()) {
                        flight_no = snap.child("flight_no").getValue().toString();
                    }if (snap.child("aircraft_type").exists()) {
                        aircraft_type = snap.child("aircraft_type").getValue().toString();
                    }if (snap.child("origin").exists()) {
                        _origin = snap.child("origin").getValue().toString();
                    }if (snap.child("destination").exists()) {
                        _destination = snap.child("destination").getValue().toString();
                    }if (snap.child("date").exists()) {
                        _date = snap.child("date").getValue().toString();
                    }

                    if (origin.equals(_origin) && destination.equals(_destination) && date.equals(_date)) {
                        RoutesListSpacecraft appSpace = new RoutesListSpacecraft(flight_no, aircraft_type, _origin, _destination, date, appoint_key);
                        list.add(appSpace);

                    }

                listAdapter = new RoutesListAdapter(list, Routes.this, bookList);
                route_rv.setAdapter(listAdapter);
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