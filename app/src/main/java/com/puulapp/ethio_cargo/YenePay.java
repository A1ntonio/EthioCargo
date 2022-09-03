package com.puulapp.ethio_cargo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.puulapp.ethio_cargo.com.yenepaySDK.PaymentOrderManager;
import com.puulapp.ethio_cargo.com.yenepaySDK.PaymentResponse;
import com.puulapp.ethio_cargo.com.yenepaySDK.errors.InvalidPaymentException;
import com.puulapp.ethio_cargo.com.yenepaySDK.model.OrderedItem;
import com.puulapp.ethio_cargo.com.yenepaySDK.YenePayPaymentActivity;
import com.puulapp.ethio_cargo.mainUi.Manage;
import com.puulapp.ethio_cargo.spacecraft.SendbookSpacecraft;

import java.util.Date;

public class YenePay extends YenePayPaymentActivity {

    private static final String TAG = "yenepay";
    private String amount = "200";
    String origin, destination, date, weight, pieces, length, width, height, volume, total_volume, agent, key
            , consignee_name, consignee_address, consignee_country, consignee_city, consignee_state, consignee_phone
            , consignee_zip, price, handling_code, commodity_code;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            amount = bundle.getString("value");
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
        }

        try {
            checkout();
        } catch (InvalidPaymentException e) {
            e.printStackTrace();
        }
    }

    private void checkout() throws InvalidPaymentException {

        Toast.makeText(this, amount, Toast.LENGTH_SHORT).show();

        PaymentOrderManager paymentMgr = new PaymentOrderManager(
                "13959",
                "2437");

        paymentMgr.setPaymentProcess(PaymentOrderManager.PROCESS_EXPRESS);
        paymentMgr.setReturnUrl("com.ethiocargo.yenepay:/payment2redirect");
        //If you want to move to production just omit this line
        paymentMgr.setUseSandboxEnabled(true);
        //This will disable shopping cart mode to enable set it true.
        paymentMgr.setShoppingCartMode(false);
        try {
            paymentMgr.addItem(new OrderedItem("01", "Gold", 1, Double.parseDouble(amount)));
            paymentMgr.startCheckout(this);
        } catch (InvalidPaymentException e) {
            Log.e(TAG, "checkoutWithBrowser: ", e);
        }

        //If you want to validate manually

        //Payment validation
        PaymentOrderManager.PaymentValidationResult result = paymentMgr.validate();

        //To check result
        if(!result.isValid){
            throw new InvalidPaymentException(result.toString());
        }
    }
    @Override
    public void onPaymentResponseArrived(PaymentResponse response) {
        //Handle Payment response
        if(response.isPaymentCompleted()){
            long date_time = new Date().getTime();
            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("customer").child("manage").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(date_time));
            SendbookSpacecraft spacecraft = new SendbookSpacecraft(origin, destination, date, weight, pieces, length, width, height, volume
                    , total_volume, agent, key
                    , consignee_name, consignee_address, consignee_country, consignee_city, consignee_state, consignee_phone
                    , consignee_zip, price, handling_code, commodity_code, "Pending...", "Pending...");

            db.setValue(spacecraft).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(YenePay.this, "Sent!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(YenePay.this, Manage.class));
                    finish();
                } else {
                    Toast.makeText(YenePay.this, "Not sent!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Payment Incomplete!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPaymentResponseError(String error) {
        //Handle payment request error.
        Toast.makeText(this, "payment request error", Toast.LENGTH_LONG).show();
    }


}
