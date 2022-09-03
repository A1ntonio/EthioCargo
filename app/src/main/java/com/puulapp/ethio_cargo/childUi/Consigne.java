package com.puulapp.ethio_cargo.childUi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.puulapp.ethio_cargo.R;

public class Consigne extends AppCompatActivity {

    private TextView consigne_back_btn;
    private EditText name_field, address_field, city_field, state_field, phone_field, zipcode_field;
    private Spinner country_spinner, price_class_spinner, handling_code_spinner, commodity_code_spinner;
    private AppCompatButton consignee_book_btn;


    String origin, destination, date, weight, pieces, length, width, height, volume, total_volume, agent, key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consigne);
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
        }

        initialization();
        viewAction();

    }

    private void viewAction() {
        consigne_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        consignee_book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String consignee_name = name_field.getText().toString();
                String address = address_field.getText().toString();
                String country = country_spinner.getSelectedItem().toString();
                String city = city_field.getText().toString();
                String state = state_field.getText().toString();
                String phone = phone_field.getText().toString();
                String zip_code = zipcode_field.getText().toString();
                String price = price_class_spinner.getSelectedItem().toString();
                String handling_code = handling_code_spinner.getSelectedItem().toString();
                String commodity_code = commodity_code_spinner.getSelectedItem().toString();

                if (consignee_name.isEmpty() && address.isEmpty() && country_spinner.getSelectedItemId() == 0
                && city.isEmpty() && state.isEmpty() && phone.isEmpty() && zip_code.isEmpty() && price_class_spinner.getSelectedItemId() == 0
                && handling_code_spinner.getSelectedItemId() == 0 && commodity_code_spinner.getSelectedItemId() == 0) {
                    Toast.makeText(Consigne.this, "Please check if you fulfilled all fields.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(Consigne.this, Payment.class);
                    intent.putExtra("origin", origin);
                    intent.putExtra("destination", destination);
                    intent.putExtra("date", date);
                    intent.putExtra("weight", weight);
                    intent.putExtra("pieces", pieces);
                    intent.putExtra("length", length);
                    intent.putExtra("width", width);
                    intent.putExtra("height", height);
                    intent.putExtra("volume", volume);
                    intent.putExtra("total_volume", total_volume);
                    intent.putExtra("agent", agent);
                    intent.putExtra("key", key);
                    intent.putExtra("consignee_name", consignee_name);
                    intent.putExtra("consignee_address", address);
                    intent.putExtra("consignee_country", country);
                    intent.putExtra("consignee_city", city);
                    intent.putExtra("consignee_state", state);
                    intent.putExtra("consignee_phone", phone);
                    intent.putExtra("consignee_zip", zip_code);
                    intent.putExtra("price", price);
                    intent.putExtra("handling_code", handling_code);
                    intent.putExtra("commodity_code", commodity_code);
                    intent.putExtra("price_class", price_class_spinner.getSelectedItemId());
                    startActivity(intent);
                }
            }
        });
    }

    private void initialization() {
        consigne_back_btn = findViewById(R.id.consigne_back_btn);
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
        consignee_book_btn = findViewById(R.id.consignee_book_btn);
    }
}