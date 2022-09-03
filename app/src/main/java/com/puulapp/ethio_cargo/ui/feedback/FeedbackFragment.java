package com.puulapp.ethio_cargo.ui.feedback;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.puulapp.ethio_cargo.FeedbackSpacecraft;
import com.puulapp.ethio_cargo.R;

import java.util.Objects;

public class FeedbackFragment extends Fragment {

    private EditText your_email;
    private Button feed_send_btn;
    private MultiAutoCompleteTextView your_feedbck;
    private DatabaseReference mDatabaseRefFeedback;
    private FirebaseAuth mAuth;
    private String currentUserId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_feedback, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        mDatabaseRefFeedback = FirebaseDatabase.getInstance().getReference().child("administration").child("feedback").child(currentUserId);

        your_feedbck = root.findViewById(R.id.your_feedbck);
        your_email = root.findViewById(R.id.your_email);
        feed_send_btn = root.findViewById(R.id.feed_send_btn);


        feed_send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String feedback_message = your_feedbck.getText().toString();
                String feedback_email = your_email.getText().toString();
                if (!feedback_message.isEmpty()) {

                    FeedbackSpacecraft feedbackSpacecraft = new FeedbackSpacecraft(feedback_message, feedback_email);
                    mDatabaseRefFeedback.push().setValue(feedbackSpacecraft).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Feedback sent! Thank you for your feedback/suggestion.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Input field can't be empty.", Toast.LENGTH_LONG).show();
                }
            }
        });

        return root;
    }
}