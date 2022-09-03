package com.puulapp.ethio_cargo.mainUi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.puulapp.ethio_cargo.R;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class Loadability extends AppCompatActivity {

    private TextView load_back_btn;
    private Spinner load_spinner;
    private EditText load_height, load_width, load_length;
    private AppCompatButton check_load_btn;
    DatabaseReference mDatabaseRef;
    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadability);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        initialization();
        viewAction();
    }

    private void viewAction() {
        load_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        check_load_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        String aircraft = load_spinner.getSelectedItem().toString();
                        if (snapshot.child(aircraft).exists()){
                            if (snapshot.child(aircraft).child("volume").exists()){
                                String volume = snapshot.child(aircraft).child("volume").getValue().toString();
                                int required_volume = Integer.parseInt(volume);
                                int height = Integer.parseInt(load_height.getText().toString());
                                int width = Integer.parseInt(load_width.getText().toString());
                                int length = Integer.parseInt(load_length.getText().toString());
                                int cargo_volume = height * width * length;
                                if (cargo_volume < required_volume) {
                                    dialogBox("Yes, it is loadable on " + aircraft);
                                } else {
                                    dialogBox("NO, it is not loadable on " + aircraft);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void dialogBox(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.custome_load_check, null);

        TextView check_load = view.findViewById(R.id.load_message);

        check_load.setText(s);
        builder.setView(view);

        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.setNegativeButton("Cancel", (dialog, which) ->{
            dialog.dismiss();
        });
        builder.show();
    }

    private void initialization() {
        load_back_btn = findViewById(R.id.load_back_btn);
        load_spinner = findViewById(R.id.load_spinner);
        load_height = findViewById(R.id.load_height);
        load_width = findViewById(R.id.load_width);
        load_length = findViewById(R.id.load_length);
        check_load_btn = findViewById(R.id.check_load_btn);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference()
                .child("administration")
                .child("loadability");

    }

}