package com.puulapp.ethio_cargo.childUi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
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
import com.puulapp.ethio_cargo.adapters.FlightsListAdapter;
import com.puulapp.ethio_cargo.spacecraft.FlightsSpacecraft;

import java.util.ArrayList;
import java.util.List;

public class Flights extends AppCompatActivity {

    private TextView flight_back_btn;
    private RecyclerView flight_rv;

    private FlightsListAdapter listAdapter;
    DatabaseReference mDatabaseRefFlights;
    List<FlightsSpacecraft> list;
    private SpinKitView spin_kit;
    private TextView message_home_tv;

    String from, to, date;
    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flights);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            from = bundle.getString("from");
            to = bundle.getString("to");
            date = bundle.getString("date");
        }

        initialization();
        viewAction();

    }

    private void viewAction() {
        flight_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initialization() {
        flight_back_btn = findViewById(R.id.flight_back_btn);
        spin_kit = findViewById(R.id.spin_kit_);
        message_home_tv = findViewById(R.id.message_home_);
        flight_rv = findViewById(R.id.flight_rv);
        flight_rv.setLayoutManager(new LinearLayoutManager(this));

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabaseRefFlights = FirebaseDatabase.getInstance().getReference()
                .child("administration")
                .child("routes");
        mDatabaseRefFlights.keepSynced(true);
        list = new ArrayList<>();

        reload();
    }

    private void reload() {
        list.clear();
        mDatabaseRefFlights.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snap, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                    message_home_tv.setVisibility(View.GONE);
                    spin_kit.setVisibility(View.GONE);
                    String _from = "";
                    String _to = "";
                    String _aircraft_type = "";
                    String _flight_no = "";
                    String _date = "";
                    Log.d("id_key", snap.getKey());

                    if (snap.child("flight_no").exists()) {
                        _flight_no = snap.child("flight_no").getValue().toString();
                    }if (snap.child("aircraft_type").exists()) {
                        _aircraft_type = snap.child("aircraft_type").getValue().toString();
                    }if (snap.child("origin").exists()) {
                        _from = snap.child("origin").getValue().toString();
                    }if (snap.child("destination").exists()) {
                        _to = snap.child("destination").getValue().toString();
                    }if (snap.child("date").exists()) {
                        _date = snap.child("date").getValue().toString();
                    }

                    Log.d("origin", from + ", " + _from);
                    Log.d("dest", to + ", " + _to);
                    Log.d("date_", date + ", " + _date);

                    if (from.equals(_from) && to.equals(_to) && date.equals(_date)) {
                        FlightsSpacecraft appSpace = new FlightsSpacecraft(_date, _aircraft_type, _flight_no, _from, _to);
                        list.add(appSpace);
                    }

                listAdapter = new FlightsListAdapter(list, Flights.this);

                flight_rv.setAdapter(listAdapter);

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