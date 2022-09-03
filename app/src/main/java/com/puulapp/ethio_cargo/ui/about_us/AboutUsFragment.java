package com.puulapp.ethio_cargo.ui.about_us;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.puulapp.ethio_cargo.R;
import com.puulapp.ethio_cargo.databinding.FragmentAboutBinding;

public class AboutUsFragment extends Fragment {

    private FragmentAboutBinding binding;

    private AppCompatButton about1, about2, about3, about4;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAboutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initializeView(root);
        viewAction();

        return root;
    }

    private void viewAction() {

        about1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://cargo.ethiopianairlines.com/about-us/gceo-message"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        about2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://cargo.ethiopianairlines.com/about-us/vision"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        about3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://cargo.ethiopianairlines.com/about-us/addis-ababa-hub"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        about4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://cargo.ethiopianairlines.com/about-us/corporate-social-responsibility"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        }


    private void initializeView(View root) {

        about1 = root.findViewById(R.id.about1);
        about2 = root.findViewById(R.id.about2);
        about3 = root.findViewById(R.id.about3);
        about4 = root.findViewById(R.id.about4);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}