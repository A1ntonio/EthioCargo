package com.puulapp.ethio_cargo.mainUi;

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
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.puulapp.ethio_cargo.R;
import com.puulapp.ethio_cargo.childUi.Flights;

import java.util.Calendar;

public class CheckFlight extends AppCompatActivity {

    private TextView check_back_btn;
    private Spinner from_spinner, to_spinner;
    private AppCompatButton check_search_btn;
    private EditText departure_date_field;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_flight);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        initializeView();
        viewAction();
    }

    private void viewAction() {
        check_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        check_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (from_spinner.getSelectedItemId() != 0 && to_spinner.getSelectedItemId() != 0 && !date.isEmpty()) {
                    Intent intent = new Intent(CheckFlight.this, Flights.class);
                    intent.putExtra("from", from_spinner.getSelectedItem().toString());
                    intent.putExtra("to", to_spinner.getSelectedItem().toString());
                    intent.putExtra("date", date);
                    startActivity(intent);
                } else {
                    Toast.makeText(CheckFlight.this, "Check required fields.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        departure_date_field.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                openDatePicker();
            }
        });
    }

    private void initializeView() {
        check_back_btn = findViewById(R.id.check_back_btn);
        from_spinner = findViewById(R.id.from_spinner);
        to_spinner = findViewById(R.id.to_spinner);
        check_search_btn = findViewById(R.id.check_search_btn);
        departure_date_field = findViewById(R.id.departure_date_field);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void openDatePicker() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.date_picker, null);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        alertInitialization(view, day);

        builder.setView(view);

        builder.setPositiveButton("OK", (dialog, which) -> {
            departure_date_field.setText(date);

            dialog.dismiss();
        });
        builder.setNegativeButton("Cancel", (dialog, which) ->{
            dialog.dismiss();
        });
        builder.show();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void alertInitialization(View view , int day) {

        final CalendarView calendarView = view.findViewById(R.id.calendarView);


        calendarView.setOnDateChangeListener((view12, year, month, dayOfMonth) -> {
            date = dayOfMonth + "/" + month + "/" + year;
        });

    }
}