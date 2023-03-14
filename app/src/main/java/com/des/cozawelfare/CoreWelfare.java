package com.des.cozawelfare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.UUID;

public class CoreWelfare extends AppCompatActivity {
    private String name,surname,age,gender,phone_no, marital_status, no_of_children,years_in_coza,bfc,evangelism,request,request_status,born_again,address,remarks,attendant,campus,date,imageUrl;
    private String selectGender,training,maritalStatus,evangelismStatus,requestStatus,bornAgain;
    private Bitmap bitmap;
    private Uri uri;
    private Spinner maritalStatusSpinner, genderSpinner,evangelismSpinner,bfcSpinner,requestStatusSpinner,bornAgainSpinner;
    private TextView dateTv,campusTv,attendantTv;
    private EditText nameEdT,surnameEdt,ageEdT,phone_noEdT,no_of_childrenEdT,years_in_cozaEdT,requestEdT,addressEdT,remarksEdT;
    private Button camera_open_id, register;
    private ImageView click_image_id;
    private static final int pic_id = 123;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private StorageTask uploadTask;
    private ProgressDialog progressDialog;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Cases cases;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core_welfare);
        getSupportActionBar().setTitle("Case");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF737070));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("Cases");
        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        cases = new Cases();

        progressDialog = new ProgressDialog(this);

        camera_open_id = findViewById(R.id.camera_button);
        click_image_id = findViewById(R.id.click_image);
        genderSpinner = findViewById(R.id.case_genderSpinner);
        maritalStatusSpinner = findViewById(R.id.case_maritalStatusSpinner);
        evangelismSpinner = findViewById(R.id.case_evangelismSpinner);
        bfcSpinner = findViewById(R.id.case_bfcSpinner);
        requestStatusSpinner = findViewById(R.id.case_requestStatusSpinner);
        bornAgainSpinner = findViewById(R.id.case_bornAgainSpinner);
        ageEdT = findViewById(R.id.case_age);
        dateTv = findViewById(R.id.case_dateTV);
        dateTv.setText(SignIn1.getDate());
        campusTv = findViewById(R.id.case_campusTV);
        campusTv.setText(SignIn1.getCampus());
        attendantTv = findViewById(R.id.case_attendantTV);
        attendantTv.setText(SignIn1.getAttendant());
        nameEdT = findViewById(R.id.case_name);
        surnameEdt = findViewById(R.id.case_surname);
        no_of_childrenEdT = findViewById(R.id.case_noOfChildren);
        phone_noEdT = findViewById(R.id.case_phone);
        years_in_cozaEdT = findViewById(R.id.case_duration);
        requestEdT = findViewById(R.id.case_request);
        addressEdT = findViewById(R.id.case_address);
        remarksEdT = findViewById(R.id.case_remarks);
        register = findViewById(R.id.caseRegistration);

        // Camera_open button is for open the camera and add the setOnClickListener in this button
        camera_open_id.setOnClickListener(v -> {
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
                maritalStatus = parent.getItemAtPosition(0).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // can leave this empty
            }
        });
        bfcSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                // Get Selected gender from the list
                bfc = parent.getItemAtPosition(position).toString();
                training = parent.getItemAtPosition(0).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // can leave this empty
            }
        });
        bornAgainSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                // Get Selected gender from the list
                born_again = parent.getItemAtPosition(position).toString();
                bornAgain = parent.getItemAtPosition(0).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // can leave this empty
            }
        });
        evangelismSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                // Get Selected gender from the list
                evangelism = parent.getItemAtPosition(position).toString();
                evangelismStatus = parent.getItemAtPosition(0).toString();
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
                requestStatus = parent.getItemAtPosition(0).toString();
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
                surname = surnameEdt.getText().toString();
                no_of_children = no_of_childrenEdT.getText().toString();
                phone_no = phone_noEdT.getText().toString();
                date = dateTv.getText().toString();
                attendant = attendantTv.getText().toString();
                campus = campusTv.getText().toString();
                age = ageEdT.getText().toString();
                years_in_coza = years_in_cozaEdT.getText().toString();
                request = requestEdT.getText().toString();
                address = addressEdT.getText().toString();
                remarks = remarksEdT.getText().toString();
                // display it as Toast to the user
                if (TextUtils.isEmpty(name)) {
                    nameEdT.setError("Please enter Name");
                }else if (TextUtils.isEmpty(surname)) {
                    surnameEdt.setError("Please enter Age");
                }
                else if (TextUtils.isEmpty(age)) {
                    ageEdT.setError("Please enter Age");
                } else if (TextUtils.isEmpty(phone_no)) {
                    phone_noEdT.setError("Please enter Phone No");
                }else if (TextUtils.isEmpty(no_of_children)) {
                    no_of_childrenEdT.setError("Please enter No of Children");
                }
                else if (TextUtils.isEmpty(years_in_coza)) {
                    years_in_cozaEdT.setError("Please enter years spent in COZA");
                }
                else if (TextUtils.isEmpty(request)) {
                    requestEdT.setError("Please enter Request");
                }
                else if (TextUtils.isEmpty(address)) {
                    addressEdT.setError("Please enter Address");
                }
                else if (TextUtils.isEmpty(remarks)) {
                    remarksEdT.setError("Please enter Remarks");
                }
                else if (gender == selectGender){
                    Toast.makeText(CoreWelfare.this, "Please select Gender", Toast.LENGTH_SHORT).show();
                }
                else if (marital_status == maritalStatus){
                    Toast.makeText(CoreWelfare.this, "Please select Marital Status", Toast.LENGTH_SHORT).show();
                }
                else if (bfc == training){
                    Toast.makeText(CoreWelfare.this, "Please select Believer's class", Toast.LENGTH_SHORT).show();
                }
                else if (request_status == requestStatus){
                    Toast.makeText(CoreWelfare.this, "Please select Request Status", Toast.LENGTH_SHORT).show();
                }
                else if (born_again == bornAgain){
                    Toast.makeText(CoreWelfare.this, "Please select Born Again status", Toast.LENGTH_SHORT).show();
                }
                else if (evangelism == evangelismStatus){
                    Toast.makeText(CoreWelfare.this, "Please select Evangelism", Toast.LENGTH_SHORT).show();
                }
                else if (uri == null){
                    Toast.makeText(CoreWelfare.this, "Please capture Image", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(uploadTask != null && uploadTask.isInProgress()){
                        Toast.makeText(CoreWelfare.this,"Upload in progress",Toast.LENGTH_SHORT).show();

                    }
                    else {
                        uploadImage(uri);

                    }

                }

            }
        });

    }
    // This method will help to retrieve the image
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Match the request 'pic id with requestCode
        if (requestCode == pic_id && resultCode == RESULT_OK) {
            // BitMap is data structure of image file which store the image in memory
            bitmap = (Bitmap) data.getExtras().get("data");
            // Set the image in imageview for display
            click_image_id.setImageBitmap(bitmap);


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
        StorageReference childRef = storageReference.child("Cases Images/" + randomKey);

// Register observers to listen for when the download is done or if it fails
        uploadTask = childRef.putFile(uri).addOnFailureListener(new OnFailureListener() {
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
                childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Snackbar.make(findViewById(android.R.id.content),"Image uploaded", Snackbar.LENGTH_LONG).show();
                        // creating a collection reference
                        // for our Firebase Firetore database.
                        imageUrl = uri.toString();
                        progressDialog.dismiss();
                        // calling method to add data to Firebase Firestore.
                        addDataToFirebase(name,surname,age,gender,phone_no,marital_status,no_of_children,years_in_coza,born_again,bfc,evangelism,request,request_status,address,remarks,attendant,campus,date,imageUrl);
                        Snackbar.make(findViewById(android.R.id.content),"Image uploaded", Snackbar.LENGTH_LONG).show();
                        click_image_id.setImageURI(null);
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
    private void addDataToFirebase(String name,String surname,String age,String gender,String phone_no,String marital_status,String no_of_children,String years_in_coza,String born_again,String bfc,String evangelism,String request,String request_status,String address,String remarks,String attendant,String campus,String date,String imageUrl) {
        // below 3 lines of code is used to set
        // data in our object class.
        cases.setDate(date);
        cases.setName(name);
        cases.setSurname(surname);
        cases.setGender(gender);
        cases.setAge(age);
        cases.setMarital_Status(marital_status);
        cases.setPhone_no(phone_no);
        cases.setNo_of_Children(no_of_children);
        cases.setYears_in_COZA(years_in_coza);
        cases.setEvangelism(evangelism);
        cases.setBFC(bfc);
        cases.setBorn_Again(born_again);
        cases.setRequest(request);
        cases.setRequest_Status(request_status);
        cases.setAddress(address);
        cases.setCampus(campus);
        cases.setAttendant(attendant);
        cases.setRemarks(remarks);
        cases.setImageUrl(imageUrl);


        databaseReference.push().setValue(cases).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                // after adding this data we are showing toast message.
                Toast.makeText(CoreWelfare.this, "Case added", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // we are displaying a failure toast message.
                Toast.makeText(CoreWelfare.this, "Failed to add case \n" + e, Toast.LENGTH_SHORT).show();
            }
        });

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
    @Override
    public void onBackPressed() {
        finish();
    }

}