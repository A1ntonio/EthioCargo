package com.puulapp.ethio_cargo.childUi;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.puulapp.ethio_cargo.R;
import com.puulapp.ethio_cargo.mainUi.Book;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class ViewMore extends AppCompatActivity {

    private TextView origin_spinner, destination_spinner, agent_spinner;
    private TextView date_field, weight_field, pieces_field, length_field, width_field, height_field, volume_field, total_volume_field;

    private TextView view_more_back, deleteBook;
    private ProgressBar deleting_progressBar;
    private TextView name_field, address_field, city_field, state_field, phone_field, zipcode_field;
    private TextView country_spinner, price_class_spinner, handling_code_spinner, commodity_code_spinner;

    private DatabaseReference db;

    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_more);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            key = bundle.getString("key");
        }

        initializeView();
        viewAction();
    }


    private void viewAction() {

        deleteBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleting_progressBar.setVisibility(View.VISIBLE);
                db.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ViewMore.this, "Deleted", Toast.LENGTH_SHORT).show();
                            deleting_progressBar.setVisibility(View.GONE);
                            finish();
                        } else {
                            deleting_progressBar.setVisibility(View.GONE);
                            Toast.makeText(ViewMore.this, "Not Deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        view_more_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initializeView() {

        deleting_progressBar = findViewById(R.id.deleting_progressBar);
        deleteBook = findViewById(R.id.deleteBook);
        view_more_back = findViewById(R.id.view_more_back);
        origin_spinner = findViewById(R.id.origin_spinner);
        destination_spinner = findViewById(R.id.destination_spinner);
        agent_spinner = findViewById(R.id.agent_spinner);
        date_field = findViewById(R.id.date_field);
        weight_field = findViewById(R.id.weight_field);
        pieces_field = findViewById(R.id.pieces_field);
        length_field = findViewById(R.id.length_field);
        width_field = findViewById(R.id.width_field);
        height_field = findViewById(R.id.height_field);
        volume_field = findViewById(R.id.volume_field);
        total_volume_field = findViewById(R.id.total_volume_field);

        name_field = findViewById(R.id.name_field);
        address_field = findViewById(R.id.address_field);
        city_field = findViewById(R.id.city_field);
        state_field = findViewById(R.id.state_field);
        phone_field = findViewById(R.id.phone_field);
        zipcode_field = findViewById(R.id.zipcode_field);
        country_spinner = findViewById(R.id.country_spinner);
        price_class_spinner = findViewById(R.id.price_class_spinner);
        handling_code_spinner = findViewById(R.id.handling_code_spinner);
        commodity_code_spinner = findViewById(R.id.commodity_code_spinner);

        retrieveDataFromFirebase();

    }

    private void retrieveDataFromFirebase() {
        db = FirebaseDatabase.getInstance().getReference().child("customer").child("manage").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(key);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.child("origin").exists()){
                    origin_spinner.setText(snapshot.child("origin").getValue().toString());
                }
                if (snapshot.child("destination").exists()){
                    destination_spinner.setText(snapshot.child("destination").getValue().toString());
                }
                if (snapshot.child("date").exists()){
                    date_field.setText(snapshot.child("date").getValue().toString());
                }
                if (snapshot.child("weight").exists()){
                    weight_field.setText(snapshot.child("weight").getValue().toString());
                }
                if (snapshot.child("pieces").exists()){
                    pieces_field.setText(snapshot.child("pieces").getValue().toString());
                }
                if (snapshot.child("length").exists()){
                    length_field.setText(snapshot.child("length").getValue().toString());
                }
                if (snapshot.child("width").exists()){
                    width_field.setText(snapshot.child("width").getValue().toString());
                }
                if (snapshot.child("height").exists()){
                    height_field.setText(snapshot.child("height").getValue().toString());
                }
                if (snapshot.child("volume").exists()){
                    volume_field.setText(snapshot.child("volume").getValue().toString());
                }
                if (snapshot.child("total_volume").exists()){
                    total_volume_field.setText(snapshot.child("total_volume").getValue().toString());
                }
                if (snapshot.child("agent").exists()){
                    agent_spinner.setText(snapshot.child("agent").getValue().toString());
                }
                if (snapshot.child("consignee_name").exists()){
                    name_field.setText(snapshot.child("consignee_name").getValue().toString());
                }
                if (snapshot.child("consignee_address").exists()){
                    address_field.setText(snapshot.child("consignee_address").getValue().toString());
                }
                if (snapshot.child("consignee_country").exists()){
                    country_spinner.setText(snapshot.child("consignee_country").getValue().toString());
                }
                if (snapshot.child("consignee_city").exists()){
                    city_field.setText(snapshot.child("consignee_city").getValue().toString());
                }
                if (snapshot.child("consignee_state").exists()){
                    state_field.setText(snapshot.child("consignee_state").getValue().toString());
                }
                if (snapshot.child("consignee_phone").exists()){
                    phone_field.setText(snapshot.child("consignee_phone").getValue().toString());
                }
                if (snapshot.child("consignee_zip").exists()){
                    zipcode_field.setText(snapshot.child("consignee_zip").getValue().toString());
                }
                if (snapshot.child("price").exists()){
                    price_class_spinner.setText(snapshot.child("price").getValue().toString());
                }
                if (snapshot.child("handling_code").exists()){
                    handling_code_spinner.setText(snapshot.child("handling_code").getValue().toString());
                }
                if (snapshot.child("commodity_code").exists()){
                    commodity_code_spinner.setText(snapshot.child("commodity_code").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

}