package com.des.cozawelfare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Contact extends AppCompatActivity {
    private String name, surname, gender, birthday, phone_no, email,marital_status, date, imageUrl;
    private String selectGender;
    private String selectMaritalStatus;
    private EditText nameEdT, family_nameEdT, birthdayEdT, phone_noEdT, emailEdT;
    private TextView dateTv;
    private Button captureImage,register;
    private ImageView capturedImage;
    private Spinner genderSpinner, marital_statusSpinner;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private static final int pic_id = 123;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private StorageTask uploadTask;
    private ProgressDialog progressDialog;
    private Bitmap bitmap;
    private Uri uri;

    private Members members;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        getSupportActionBar().setTitle("Add Member");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF737070));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("Members Data");

        // initializing our object
        // class variable.
        members = new Members();

        progressDialog = new ProgressDialog(this);
        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        nameEdT = findViewById(R.id.case_name);
        family_nameEdT = findViewById(R.id.familyName);
        phone_noEdT = findViewById(R.id.family_phone);
        emailEdT = findViewById(R.id.email);
        birthdayEdT = findViewById(R.id.birthday);
        genderSpinner = findViewById(R.id.genderSpinner);
        marital_statusSpinner = findViewById(R.id.maritalStatusSpinner);
        capturedImage = findViewById(R.id.photo);
        captureImage = findViewById(R.id.captureImage);
        register = findViewById(R.id.registration);
        dateTv = findViewById(R.id.date);
        dateTv.setText(SignIn2.getDate());

        // Camera_open button is for open the camera and add the setOnClickListener in this button
        captureImage.setOnClickListener(v -> {
            // Create the camera_intent ACTION_IMAGE_CAPTURE it will open the camera for capture the image
            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Start the activity with camera_intent, and request pic id
            startActivityForResult(camera_intent, pic_id);

        });
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                // Get Selected gender from the list
                gender = parent.getItemAtPosition(position).toString();
                selectGender = parent.getItemAtPosition(0).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // can leave this empty
            }
        });
        marital_statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                // Get Selected gender from the list
                marital_status = parent.getItemAtPosition(position).toString();
                selectMaritalStatus = parent.getItemAtPosition(0).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // can leave this empty
            }
        });

        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // getting data from edittext fields.
                name = nameEdT.getText().toString();
                surname = family_nameEdT.getText().toString();
                phone_no = phone_noEdT.getText().toString();
                birthday = birthdayEdT.getText().toString();
                email = emailEdT.getText().toString();
                date = SignIn2.getDate();
                // validating the text fields if empty or not.
                if (TextUtils.isEmpty(name)) {
                    nameEdT.setError("Please enter name");
                }else if (TextUtils.isEmpty(surname)) {
                    family_nameEdT.setError("Please enter surname");
                }
                else if (TextUtils.isEmpty(email)) {
                    emailEdT.setError("Please enter email");
                }
                else if (TextUtils.isEmpty(birthday)) {
                    birthdayEdT.setError("Please enter bithday");
                } else if (TextUtils.isEmpty(phone_no)) {
                    phone_noEdT.setError("Please enter phone no");
                } else if (gender == selectGender){
                    Toast.makeText(Contact.this, "Please select Gender", Toast.LENGTH_SHORT).show();
                }
                else if (marital_status == selectMaritalStatus){
                    Toast.makeText(Contact.this, "Please select Marital Status", Toast.LENGTH_SHORT).show();
                }
                else if (uri == null){
                    Toast.makeText(Contact.this, "Please capture Image", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (uploadTask != null && uploadTask.isInProgress()) {
                        Toast.makeText(Contact.this,"Upload in progress",Toast.LENGTH_SHORT).show();

                    } else {
                        uploadImage(uri);
                    }

                }
            }
        });
    }
    // This method will help to retrieve the image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Match the request 'pic id with requestCode
        if (requestCode == pic_id && resultCode == RESULT_OK) {
            //BitMap is data structure of image file which store the image in memory
            bitmap = (Bitmap) data.getExtras().get("data");
            // Set the image in imageview for display
            capturedImage.setImageBitmap(bitmap);
            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            uri = getImageUri(getApplicationContext(), bitmap);

        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    // UploadImage method
    private void uploadImage(Uri uri) {
        progressDialog.setMessage("Uploading image");
        progressDialog.show();
        final String randomKey = UUID.randomUUID().toString();
        StorageReference childRef = storageReference.child("Members Images/" + randomKey);

        // Register observers to listen for when the download is done or if it fails
        uploadTask = childRef.putFile(uri).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(getApplicationContext(),"Failed to upload image",Toast.LENGTH_LONG).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Snackbar.make(findViewById(android.R.id.content),"Image uploaded", Snackbar.LENGTH_LONG).show();
                        // creating a collection reference
                        // for our Firebase Firetore database.
                        imageUrl = uri.toString();
                        progressDialog.dismiss();
                        // calling method to add data to Firebase Firestore.
                        //addDataToFirestore(date,name,surname,gender,birthday,marital_status,phone_no,email,imageUrl);
                        addDataToFirebase(date,name,surname,gender,birthday,marital_status,phone_no,email,imageUrl);
                        capturedImage.setImageBitmap(null);
                    }
                });

            }
        }).addOnProgressListener(
                new OnProgressListener<UploadTask.TaskSnapshot>() {

                    // Progress Listener for loading
                    // percentage on the dialog box
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot)
                    {
                        double progress
                                = (100.0
                                * taskSnapshot.getBytesTransferred()
                                / taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage("Uploading " + (int)progress + "%");
                    }
                });

    }
    private void addDataToFirebase(String date, String name, String surname,String gender,String birthday,String marital_status,String phone_no,String email,String imageUrl) {
        // below 3 lines of code is used to set
        // data in our object class.
        members.setDate(date);
        members.setName(name);
        members.setSurname(surname);
        members.setGender(gender);
        members.setBirthday(birthday);
        members.setMarital_status(marital_status);
        members.setPhone_no(phone_no);
        members.setEmail(email);
        members.setImageUrl(imageUrl);

        databaseReference.push().setValue(members).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                // after adding this data we are showing toast message.
                Toast.makeText(Contact.this, "data added", Toast.LENGTH_SHORT).show();
                nameEdT.setText("");
                family_nameEdT.setText("");
                birthdayEdT.setText("");
                phone_noEdT.setText("");
                emailEdT.setText("");
                genderSpinner.setSelection(0);
                marital_statusSpinner.setSelection(0);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // we are displaying a failure toast message.
                Toast.makeText(Contact.this, "Failed to add member \n" + e, Toast.LENGTH_SHORT).show();
            }
        });

    }
    // method to inflate the options menu when
    // the user opens the menu for the first time

    // methods to control the operations that will
    // happen when user clicks on the action buttons
    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {

        switch (item.getItemId()){

            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        finish();
    }

}