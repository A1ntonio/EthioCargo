package com.puulapp.ethio_cargo;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.database.FirebaseDatabase;
import com.puulapp.ethio_cargo.com.yenepaySDK.PaymentOrderManager;
import com.puulapp.ethio_cargo.com.yenepaySDK.PaymentResponse;
import com.puulapp.ethio_cargo.com.yenepaySDK.model.YenePayConfiguration;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class EthioCargo extends Application {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(false);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

        startService(new Intent(EthioCargo.this, MyService.class));

        PendingIntent completionIntent = PendingIntent.getActivity(getApplicationContext(),
                PaymentOrderManager.YENEPAY_CHECKOUT_REQ_CODE,
                new Intent(getApplicationContext(), PaymentResponse.class), 0);
        PendingIntent cancelationIntent = PendingIntent.getActivity(getApplicationContext(),
                PaymentOrderManager.YENEPAY_CHECKOUT_REQ_CODE,
                new Intent(getApplicationContext(), PaymentResponse.class), 0);
        YenePayConfiguration.setDefaultInstance(new YenePayConfiguration.Builder(getApplicationContext())
                .setGlobalCompletionIntent(completionIntent)
                .setGlobalCancelIntent(cancelationIntent)
                .build());

    }


}
