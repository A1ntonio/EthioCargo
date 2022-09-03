package com.puulapp.ethio_cargo.ui.account;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.puulapp.ethio_cargo.R;
import com.puulapp.ethio_cargo.databinding.FragmentAccountBinding;
import com.puulapp.ethio_cargo.welcomeUi.Signin;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;
    private ImageView user_image;
    private TextView user_name, user_address, user_email, user_phone, change_password;
    private Button logout_btn;
    private ProgressBar image_progressBar;

    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mAuth;
    private String currentUserId, profile_image = "";
    private StorageReference mStorageRef;
    private Uri imageUriProfile;

    String phone = "Tap here...", full_name = "Name", email = "Tap here...", address = "City, State";

    private static final int GALLERY_REQUEST_PROFILE = 2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initializeView(root);

        actionView();

        retrieveData();

        return root;
    }

    private void retrieveData() {
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("customer").child("account").child(currentUserId);
        mDatabaseRef.keepSynced(true);
        mStorageRef = FirebaseStorage.getInstance().getReference().child("user_image");

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if (snapshot.child("u_phone").exists()){
                    phone = snapshot.child("u_phone").getValue().toString();
                }
                if (snapshot.child("u_name").exists()){
                    full_name = snapshot.child("u_name").getValue().toString();
                }
                if (snapshot.child("u_photo").exists()){
                    profile_image = snapshot.child("u_photo").getValue().toString();
                }
                if (snapshot.child("u_address").exists()){
                    address = snapshot.child("u_address").getValue().toString();
                }
                if (snapshot.child("u_email").exists()){
                    email = snapshot.child("u_email").getValue().toString();
                }

                if (!profile_image.equals("")){

                    Picasso.get().load(profile_image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(Objects.requireNonNull(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_account_circle_24, null))).into(user_image, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(profile_image).placeholder(Objects.requireNonNull(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_account_circle_24, null))).into(user_image);
                        }
                    });
                }
                user_name.setText(full_name);
                user_phone.setText(phone);
                user_email.setText(email);
                user_address.setText(address);

            }


            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void actionView() {
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), Signin.class));
                getActivity().finishAffinity();
                getActivity().finish();
            }
        });

        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myPhotoUpdate();
            }
        });

        user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAccount("Change Name", "Enter your name : ", "Abebe", InputType.TYPE_CLASS_TEXT, "Name", "u_name");
            }
        });
        user_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAccount("Change City/State", "Enter your city and state here : ", "Addis Ababa, Ethiopia", InputType.TYPE_CLASS_TEXT, "City and State", "u_address");
            }
        });
        user_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAccount("Change Phone", "Enter your phone number : ", "0987654321", InputType.TYPE_CLASS_PHONE, "Phone number", "u_phone");
            }
        });
        user_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ChangeEmail.class));
            }
        });
        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ChangePassword.class));
            }
        });
    }

    private void updateAccount(String title, String message, String hint, int input_type, String required, String db_name) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(message);

        final EditText input = new EditText(getContext());
        input.setInputType(input_type);
        input.setHint(hint);
        builder.setView(input);
        builder.setPositiveButton("Save", (dialog, which) -> {
            if(input.getText().toString().isEmpty()){
                Toast.makeText(getContext(), required + " required.", Toast.LENGTH_SHORT).show();
            }else if(input_type == InputType.TYPE_CLASS_PHONE){
                if ((input.getText().toString().length() == 10)) {
                    if ((input.getText().toString().charAt(0) == '0')) {
                        if (((input.getText().toString().charAt(1) == '9'))){
                            mDatabaseRef.child(db_name).setValue("+251" + Integer.parseInt(input.getText().toString())).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(getContext(), required + " edited!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getContext(), required + "Invalid Phone Number. Start with 09---", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), required + "Invalid Phone Number. Start with 09---", Toast.LENGTH_SHORT).show();
                    }
            } else {
                    Toast.makeText(getContext(), required + "Invalid Phone Number. Length should be 10.", Toast.LENGTH_SHORT).show();
                }

            } else {
                mDatabaseRef.child(db_name).setValue(input.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getContext(), required + " edited!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();

    }

    private void myPhotoUpdate() {
        Intent intent = CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .getIntent(getContext());
        startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && data != null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                imageUriProfile = result.getUri();
                profile_image = imageUriProfile.toString();

                Picasso.get().load(imageUriProfile)
                        .placeholder(Objects.requireNonNull(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_account_circle_24, null)))
                        .into(user_image);
                uploadFile();
            }
        }
    }

    private void uploadFile() {
        image_progressBar.setVisibility(View.VISIBLE);
        if (imageUriProfile != null){
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUriProfile));
            UploadTask uploadTask = fileReference.putFile(imageUriProfile);
            uploadTask.addOnSuccessListener(taskSnapshot -> uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();

                }
                // Continue with the task to get the download URL
                return fileReference.getDownloadUrl();

            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String thumb_download_url = task.getResult().toString();
                    mDatabaseRef.child("u_photo").setValue(thumb_download_url);
                    image_progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                } else {
                    image_progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Not Uploaded", Toast.LENGTH_SHORT).show();
                }
            })).addOnFailureListener(e -> {
                image_progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {

                }
            });

        }else {
            Toast.makeText(getContext(), "No file selected", Toast.LENGTH_SHORT).show();
        }

    }

    private String getFileExtension(Uri uri){
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void initializeView(View root) {
        image_progressBar = root.findViewById(R.id.image_progressBar);
        user_image = root.findViewById(R.id.user_image);
        change_password = root.findViewById(R.id.change_password);
        user_name = root.findViewById(R.id.user_name);
        user_address = root.findViewById(R.id.user_address);
        user_email = root.findViewById(R.id.user_email);
        user_phone = root.findViewById(R.id.user_phone);
        logout_btn = root.findViewById(R.id.logout_btn);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}