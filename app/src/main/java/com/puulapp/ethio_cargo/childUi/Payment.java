package com.puulapp.ethio_cargo.childUi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.puulapp.ethio_cargo.R;
import com.puulapp.ethio_cargo.YenePay;
import com.puulapp.ethio_cargo.mainUi.Manage;
import com.puulapp.ethio_cargo.spacecraft.SendbookSpacecraft;

import java.util.Date;

public class Payment extends AppCompatActivity {

    private TextView amountField;
    private Button free_trial_button, yenepayBtn;
    private ProgressBar paying_progressBar;
    private final float amount = 0;

    private String currentUserId, sent_email, sent_pass;
    String origin, destination, date, weight, pieces, length, width, height, volume, total_volume, agent, key
            , consignee_name, consignee_address, consignee_country, consignee_city, consignee_state, consignee_phone
            , consignee_zip, price, handling_code, commodity_code;
    int price_class;
    double cost_per_kg = 0, cargo_dimension = 0 , total_price = 0;
    private static final double ESTIMATED_KG_PER_CBM = 167;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
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
            key = bundle.getString("key");
            consignee_name = bundle.getString("consignee_name");
            consignee_address = bundle.getString("consignee_address");
            consignee_country = bundle.getString("consignee_country");
            consignee_city = bundle.getString("consignee_city");
            consignee_state = bundle.getString("consignee_state");
            consignee_phone = bundle.getString("consignee_phone");
            consignee_zip = bundle.getString("consignee_zip");
            price = bundle.getString("price");
            handling_code = bundle.getString("handling_code");
            commodity_code = bundle.getString("commodity_code");
            price_class = bundle.getInt("price_class");

        }

        mAuth = FirebaseAuth.getInstance();

        initialization();
        actionMethods();

        calculatePrice();

    }

    private void calculatePrice() {
        if (price_class == 1) {
            cost_per_kg = 2.5;
        } else if (price_class == 2) {
            cost_per_kg = 3.0;
        } else if (price_class == 3) {
            cost_per_kg = 3.5;
        } else if (price_class == 4) {
            cost_per_kg = 4.0;
        } else {
            cost_per_kg = 5.0;
        }
        //change total_volume in cm3 to m3 by dividing by 1000000
        cargo_dimension = (Double.parseDouble(total_volume) / 1000000);

        double gross_weight = Double.parseDouble(weight);

        //chargeable_weight is cargo_dimension in m3 multiply by estimated kg/m3   (m3 * kg/m3 = kg)
        double chargeable_weight = cargo_dimension * ESTIMATED_KG_PER_CBM;

        //(kg * cost/kg = cost)
        if (chargeable_weight > gross_weight) {
            total_price = chargeable_weight * cost_per_kg;
        } else {
            total_price = gross_weight * cost_per_kg;
        }

        amountField.setText(String.valueOf(total_price));

    }

    private void actionMethods() {
        yenepayBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Payment.this, YenePay.class);
            if (total_price != 0) {
                intent.putExtra("value", String.valueOf(total_price));
            }
                startActivity(intent);
        });
        free_trial_button.setOnClickListener(v -> {
            paying_progressBar.setVisibility(View.VISIBLE);
            long date_time = new Date().getTime();
            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("customer").child("manage").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(date_time));
            SendbookSpacecraft spacecraft = new SendbookSpacecraft(origin, destination, date, weight, pieces, length, width, height, volume
            , total_volume, agent, key
                    , consignee_name, consignee_address, consignee_country, consignee_city, consignee_state, consignee_phone
                    , consignee_zip, price, handling_code, commodity_code, "Pending...", "Pending...");

            db.setValue(spacecraft).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(Payment.this, "Sent!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Payment.this, Manage.class));
                    paying_progressBar.setVisibility(View.GONE);
                    finish();
                } else {
                    paying_progressBar.setVisibility(View.GONE);
                    Toast.makeText(Payment.this, "Not sent!", Toast.LENGTH_SHORT).show();
                }
            });

        });
    }

    private void initialization() {
        paying_progressBar = findViewById(R.id.paying_progressBar);
        amountField = findViewById(R.id.amountField);
        free_trial_button = findViewById(R.id.free_trial_button);
        yenepayBtn = findViewById(R.id.yenepayBtn);
    }

}