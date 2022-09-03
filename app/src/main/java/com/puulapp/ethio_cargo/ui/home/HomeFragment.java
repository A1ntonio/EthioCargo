package com.puulapp.ethio_cargo.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.puulapp.ethio_cargo.R;
import com.puulapp.ethio_cargo.databinding.FragmentHomeBinding;
import com.puulapp.ethio_cargo.mainUi.Book;
import com.puulapp.ethio_cargo.mainUi.CheckFlight;
import com.puulapp.ethio_cargo.mainUi.Loadability;
import com.puulapp.ethio_cargo.mainUi.Manage;
import com.puulapp.ethio_cargo.mainUi.Notification;
import com.puulapp.ethio_cargo.mainUi.Track;
import com.puulapp.ethio_cargo.ui.feedback.FeedbackFragment;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private AppCompatButton bookbtn, checkbtn, managebtn, trackbtn, notificationbtn, loadabilitybtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        bookbtn = root.findViewById(R.id.bookbtn);
        checkbtn = root.findViewById(R.id.checkbtn);
        managebtn = root.findViewById(R.id.managebtn);
        trackbtn = root.findViewById(R.id.trackbtn);
        notificationbtn = root.findViewById(R.id.notificationbtn);
        loadabilitybtn = root.findViewById(R.id.loadabilitybtn);

        viewAction();

        return root;
    }

    private void viewAction() {
        bookbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Book.class));
            }
        });
        checkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), CheckFlight.class));
            }
        });
        managebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Manage.class));
            }
        });
        trackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Track.class));
            }
        });
        notificationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Notification.class));
            }
        });
        loadabilitybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Loadability.class));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}