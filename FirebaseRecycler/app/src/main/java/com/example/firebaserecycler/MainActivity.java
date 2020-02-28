package com.example.firebaserecycler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private Spinner year, major;
    private Button Add,Display;
    private ImageView imageView;
    Uri imageUri;
    Integer PICK_IMAGE = 0, REQUEST_CAMERA = 1;
    private EditText editTextname;
    private DatabaseReference reference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextname = findViewById(R.id.name);
        year = findViewById(R.id.year);
        imageView = findViewById(R.id.img);
        major = findViewById(R.id.major);
        Add = findViewById(R.id.Add);
        Display = findViewById(R.id.viewList);
        reference = FirebaseDatabase.getInstance().getReference("Register");
        storageReference = FirebaseStorage.getInstance().getReference("Register");
        Display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Main2Activity.class));
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextname.getText().toString().isEmpty()) {
                    editTextname.setError("Enter Name");
                    return;
                } else {
                    if (imageUri != null) {
                        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                        progressDialog.setTitle("Uploading.....");
                        progressDialog.show();
                        StorageReference Sreferences = storageReference.child("Register/" + UUID.randomUUID().toString());
                        Sreferences.putFile(imageUri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        progressDialog.dismiss();
                                        //taskSnapshot.getMetadata().getReference().getDownloadUrl().toString()
                                        String image = taskSnapshot.getDownloadUrl().toString();
                                        Toast.makeText(MainActivity.this, "Upload", Toast.LENGTH_LONG).show();
                                        String id = reference.push().getKey();
                                        User data = new User(id, editTextname.getText().toString().trim(),image, year.getSelectedItem().toString(), major.getSelectedItem().toString());
                                        reference.child(id).setValue(data);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_LONG).show();
                            }
                        })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                        progressDialog.setMessage("Uploaded" + (int) progress + "%");
                                    }
                                });
                    }
                    else {
                        Toast.makeText(MainActivity.this,"Add Image First",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void openGallery() {
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")) {
                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camera, REQUEST_CAMERA);
                } else if (items[i].equals("Gallery")) {
                    Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(gallery, PICK_IMAGE);
                } else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bundle bundle = data.getExtras();
                final Bitmap bmp = (Bitmap) bundle.get("data");
                imageView.setImageBitmap(bmp);

            } else if (requestCode == PICK_IMAGE) {
                imageUri = data.getData();
                imageView.setImageURI(imageUri);
            }
        }
    }
}
