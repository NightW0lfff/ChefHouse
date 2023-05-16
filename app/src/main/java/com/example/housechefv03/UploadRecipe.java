package com.example.housechefv03;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.AbstractCollection;
import java.util.Objects;

public class UploadRecipe extends AppCompatActivity {

    ImageView uploadImage;
    Button saveButton;
    EditText uploadTitle, uploadDescription, uploadIngredient, uploadInstruction;
    String imageURL;
    Uri imageUri;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_recipe);

        uploadImage = findViewById(R.id.uploadImg);
        uploadDescription = findViewById(R.id.uploadDescription);
        uploadTitle = findViewById(R.id.uploadTitle);
        uploadIngredient = findViewById(R.id.uploadIngredient);
        uploadInstruction = findViewById(R.id.uploadInstruction);
        saveButton = findViewById(R.id.btnSave);


        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                imageUri = data.getData();
                                uploadImage.setImageURI(imageUri);
                            }
                        } else {
                            Toast.makeText(UploadRecipe.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }


    public void saveData(){
        if (imageUri == null) {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
            return;
        }

        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("Android Images")
                .child(imageUri.getLastPathSegment());

        AlertDialog.Builder builder = new AlertDialog.Builder(UploadRecipe.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri urlImage) {
                        imageURL = urlImage.toString();
                        uploadData();
                        dialog.dismiss();
                    }
                });
            }
        });

    }

    public void uploadData(){
        String title = uploadTitle.getText().toString();
        String description = uploadDescription.getText().toString();
        String ingredient = uploadIngredient.getText().toString();
        String instruction = uploadInstruction.getText().toString();

        DataClass dataClass = new DataClass(title, description, ingredient, instruction, imageURL);

        FirebaseDatabase.getInstance().getReference("Recipes").child(title)
                .setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(UploadRecipe.this, "Saved!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UploadRecipe.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}