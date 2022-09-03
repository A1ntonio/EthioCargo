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
import android.widget.TimePicker;

import com.puulapp.ethio_cargo.R;
import com.puulapp.ethio_cargo.childUi.Routes;

import java.util.Calendar;

public class Book extends AppCompatActivity {

    private Spinner origin_spinner, destination_spinner, agent_spinner;
    private EditText date_field, weight_field, pieces_field, length_field, width_field, height_field;
    private AppCompatButton find_route_btn;
    private TextView book_back_btn, volume_field, total_volume_field;

    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        initializeView();
        viewAction();

    }

    private void viewAction() {
        date_field.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                openDatePicker();
            }
        });
        volume_field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (length_field.getText() != null && weight_field.getText() != null && height_field.getText() != null) {
                    int length = Integer.parseInt(length_field.getText().toString());
                    int width = Integer.parseInt(weight_field.getText().toString());
                    int height = Integer.parseInt(height_field.getText().toString());
                    int volume = length * width * height;
                    volume_field.setText(String.valueOf(volume));
                }
            }
        });
        total_volume_field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (length_field.getText() != null && weight_field.getText() != null && height_field.getText() != null) {
                    int length = Integer.parseInt(length_field.getText().toString());
                    int width = Integer.parseInt(weight_field.getText().toString());
                    int height = Integer.parseInt(height_field.getText().toString());
                    int volume = length * width * height;
                    total_volume_field.setText(String.valueOf(volume));
                }
            }
        });
        find_route_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (origin_spinner.getSelectedItemId() != 0 && destination_spinner.getSelectedItemId() != 0 &&
                        !date.isEmpty() && !weight_field.getText().toString().isEmpty() && !pieces_field.getText().toString().isEmpty() &&
                !length_field.getText().toString().isEmpty() && !width_field.getText().toString().isEmpty() &&
                !height_field.getText().toString().isEmpty() && !volume_field.getText().toString().isEmpty() &&
                !total_volume_field.getText().toString().isEmpty() && agent_spinner.getSelectedItemId() != 0) {
                    Intent intent = new Intent(Book.this, Routes.class);
                    intent.putExtra("origin", origin_spinner.getSelectedItem().toString());
                    intent.putExtra("destination", destination_spinner.getSelectedItem().toString());
                    intent.putExtra("date", date);
                    intent.putExtra("weight", weight_field.getText().toString());
                    intent.putExtra("pieces", pieces_field.getText().toString());
                    intent.putExtra("length", length_field.getText().toString());
                    intent.putExtra("width", width_field.getText().toString());
                    intent.putExtra("height", height_field.getText().toString());
                    intent.putExtra("volume", volume_field.getText().toString());
                    intent.putExtra("total_volume", total_volume_field.getText().toString());
                    intent.putExtra("agent", agent_spinner.getSelectedItem().toString());
                    startActivity(intent);
                }
            }

        });
        book_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initializeView() {

        book_back_btn = findViewById(R.id.book_back_btn);
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
        find_route_btn = findViewById(R.id.find_route_btn);

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
            date_field.setText(date);

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



//notification
//forgot password textview
//book error
//phone number validation
//feedback email invisible