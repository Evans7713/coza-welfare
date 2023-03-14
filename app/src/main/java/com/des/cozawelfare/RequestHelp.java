package com.des.cozawelfare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.UUID;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ImageView;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;


public class RequestHelp extends AppCompatActivity {

    // creating variables
    private String date, name, surname, gender, age, marital_status, phone_no, email, location, help,request_status,imageUrl;
    private String selectGender;
    private String selectMaritalStatus;
    private DatePickerDialog picker;
    private Spinner genderSpinner,maritalStatusSpinner,requestStatusSpinner;
    private ImageView photo;
    private Button selectImage, submitBtn;
    private EditText nameEdt, surnameEdt, ageEdt, phoneEdt, emailEdt, locationEdt, requestEdt, dateEdt;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Uri uri;
    private Bitmap bitmap;
    private static final int pic_id = 123;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private StorageTask uploadTask;
    private ProgressDialog progressDialog;

    private Request request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_help);

        getSupportActionBar().setTitle("Online Request");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF737070));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("Online Request");

        // initializing our object
        // class variable.
        request = new Request();
        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        progressDialog = new ProgressDialog(this);

        // initializing all our variables.
        genderSpinner = findViewById(R.id.genderSpinner);
        maritalStatusSpinner = findViewById(R.id.maritalStatusSpinner);
        requestStatusSpinner = findViewById(R.id.request_status);
        photo = findViewById(R.id.photo);
        nameEdt = findViewById(R.id.name);
        surnameEdt = findViewById(R.id.surname);
        ageEdt = findViewById(R.id.age);
        phoneEdt = findViewById(R.id.phone);
        emailEdt = findViewById(R.id.email);
        locationEdt = findViewById(R.id.location);
        requestEdt = findViewById(R.id.request);
        selectImage = findViewById(R.id.selectImage);
        submitBtn = findViewById(R.id.submit);
        dateEdt = findViewById(R.id.date);
        dateEdt.setInputType(InputType.TYPE_NULL);

        dateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(RequestHelp.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                dateEdt.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        // Camera_open button is for open the camera and add the setOnClickListener in this button
        selectImage.setOnClickListener(v -> {
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

        maritalStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
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
        requestStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                // Get Selected gender from the list
                request_status = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // can leave this empty
            }
        });

        // below line is to add on click listener for our add case button.
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestStatusSpinner.setVisibility(View.VISIBLE);
                // below line is to get data from all edit text fields.
                date = dateEdt.getText().toString();
                name = nameEdt.getText().toString();
                surname =surnameEdt.getText().toString();
                age = ageEdt.getText().toString();
                phone_no = phoneEdt.getText().toString();
                email = emailEdt.getText().toString();
                location = locationEdt.getText().toString();
                help = requestEdt.getText().toString();

                // validating the text fields if empty or not.
                if (TextUtils.isEmpty(name)) {
                    nameEdt.setError("Please enter First name");
                }
                else if (TextUtils.isEmpty(surname)) {
                    surnameEdt.setError("Please enter Surname");
                }else if (TextUtils.isEmpty(age)) {
                    ageEdt.setError("Please enter age");
                } else if (TextUtils.isEmpty(phone_no)) {
                    phoneEdt.setError("Please enter phone no");
                }
                else if (TextUtils.isEmpty(date)) {
                    dateEdt.setError("Please enter date");
                }
                else if (TextUtils.isEmpty(email)) {
                    emailEdt.setError("Please enter email");
                }
                else if (TextUtils.isEmpty(location)) {
                    locationEdt.setError("Please enter location");
                }
                else if (TextUtils.isEmpty(help)) {
                    requestEdt.setError("Please enter request");
                }
                else if (gender == selectGender){
                    Toast.makeText(RequestHelp.this, "Please select Gender", Toast.LENGTH_SHORT).show();
                }
                else if (marital_status == selectMaritalStatus){
                    Toast.makeText(RequestHelp.this, "Please select Marital Status", Toast.LENGTH_SHORT).show();
                }
                else if (uri == null){
                    Toast.makeText(RequestHelp.this, "Please select Image", Toast.LENGTH_SHORT).show();
                }
                    else {
                    if(uploadTask != null && uploadTask.isInProgress()){
                        Toast.makeText(RequestHelp.this,"Upload in progress",Toast.LENGTH_SHORT).show();

                    }
                    else {
                        uploadImage();

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
            photo.setImageBitmap(bitmap);
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
    private void uploadImage() {
        progressDialog.setMessage("Uploading image");
        progressDialog.show();
        final String randomKey = UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child("Online Images/" + randomKey);
        // Register observers to listen for when the download is done or if it fails
        uploadTask = riversRef.putFile(uri).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Failed to upload",Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageUrl = uri.toString();
                        progressDialog.dismiss();
                        // calling method to add data to Firebase Firestore.
                        //addDataToFirestore(date,name,surname,age,gender,marital_status,phone_no,email,location,help,imageUrl);
                        addDataToFirebase(date,name,surname,age,gender,marital_status,phone_no,email,location,help,imageUrl);
                        Snackbar.make(findViewById(android.R.id.content),"Image uploaded", Snackbar.LENGTH_LONG).show();
                        photo.setImageURI(null);
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
    @Override
    public void onBackPressed() {
        RequestHelp.this.finish();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void addDataToFirebase(String date,String name,String surname,String age,String gender,String marital_status,String phone_no,String email,String location,String help, String imageUrl) {
        // below 3 lines of code is used to set
        // data in our object class.
        request.setDate(date);
        request.setName(name);
        request.setSurname(surname);
        request.setAge(age);
        request.setGender(gender);
        request.setMarital_status(marital_status);
        request.setPhone_no(phone_no);
        request.setEmail(email);
        request.setLocation(location);
        request.setHelp(help);
        request.setRequest_status(request_status);
        request.setImageUrl(imageUrl);

        databaseReference.push().setValue(request).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                // after adding this data we are showing toast message.
                Toast.makeText(RequestHelp.this, "Request submitted", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // we are displaying a failure toast message.
                requestStatusSpinner.setVisibility(View.INVISIBLE);
                Toast.makeText(RequestHelp.this, "Failed to submit request \n" + e, Toast.LENGTH_SHORT).show();
            }
        });

    }


}