package com.puulapp.ethio_cargo.ui.share;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.puulapp.ethio_cargo.R;

public class ShareFragment extends Fragment {

    private Button share_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_share, container, false);

        share_btn = root.findViewById(R.id.share_btn);

        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share();
            }
        });

        return root;
    }
    private void share() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareBody = "Download and use this app to book into ethiopian airline cargo! \n https://play.google.com/store/apps/details?id=com.puulapp.ethio_cargo";
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Ethio-Cargo");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

}