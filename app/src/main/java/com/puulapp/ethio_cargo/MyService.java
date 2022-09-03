package com.puulapp.ethio_cargo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.puulapp.ethio_cargo.alarm.AlarmHandler;

import java.util.Date;
import java.util.Random;

public class MyService extends Service {
    private String image, name;
    String message1 = "Your cargo is loaded to aircraft!";
    String message2 = "Your cargo book request accepted!";
    String message3 = "Your cargo book request rejected!";
    String message4 = "Your cargo is in journey!";
    String message5 = "Your cargo is delivered successfully!";
    String message6 = "Your cargo book request is pending...";
    DatabaseReference mDatabaseRefStatus;

    String currentUserId;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        startForeground();

        return super.onStartCommand(intent, flags, startId);
    }

    private void startForeground(){

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            mDatabaseRefStatus = FirebaseDatabase.getInstance().getReference().child("customer")
                    .child("manage")
                    .child(currentUserId);
            checkCargoStatus();
        }

    }




private void checkCargoStatus() {
        mDatabaseRefStatus.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                if (snapshot.child("status").exists()) {
                    String status = snapshot.child("status").getValue().toString();

                    if (status.equals("Pending...")){
                        notifyStatus(message6);
                    } else if (status.equals("Accepted")){
                        notifyStatus(message2);
                    } else if (status.equals("Rejected")){
                        notifyStatus(message3);
                    }
                }
                if (snapshot.child("action").exists()) {
                    String action = snapshot.child("action").getValue().toString();
                    if (action.equals("Loaded")){
                        notifyStatus(message1);
                    } else if (action.equals("In Journey")){
                        notifyStatus(message4);
                    } else if (action.equals("Delivered")){
                        notifyStatus(message5);
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

            }
        });
    }

    private void notifyStatus(String message6) {

        long date = new Date().getTime();
        FirebaseDatabase.getInstance().getReference().child("customer").child("notifications").child(currentUserId)
                .child(String.valueOf(date)).child("message").setValue(message6);

        Random random = new Random();
        int id = random.nextInt(9999);
        new AlarmHandler(getApplicationContext()).notification(message6, id);
    }

}