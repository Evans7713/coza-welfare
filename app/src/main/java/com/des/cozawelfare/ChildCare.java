package com.des.cozawelfare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class ChildCare extends AppCompatActivity {
    private String name, surname, age, gender, phone_no, card_no, attendant, campus, date,imageUrl;
    private String selectAge, selectGender;
    private Bitmap bitmap;
    private Uri uri;
    private Spinner ageSpinner, genderSpinner;
    private TextView dateTv,campusTv,attendantTv;
    private EditText nameEdT,family_nameEdT,phone_noEdT,card_noEdT;
    private Button camera_open_id, register;
    private ImageView click_image_id;
    private CheckBox clockIn, clockOut;
    private String clockInState;
    private String clockOutState;
    private static final int pic_id = 123;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private StorageTask uploadTask;
    private ProgressDialog progressDialog;

    private Children children;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_care);
        getSupportActionBar().setTitle("Child Registration");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF737070));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // calling this activity's function to
        // use ActionBar utility methods
        ActionBar actionBar = getSupportActionBar();
        // methods to display the icon in the ActionBar
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Children");

        progressDialog = new ProgressDialog(this);

        children = new Children();

        camera_open_id = findViewById(R.id.camera_button);
        click_image_id = findViewById(R.id.click_image);
        genderSpinner = findViewById(R.id.genderSpinner);
        ageSpinner = findViewById(R.id.ageSpinner);
        dateTv = findViewById(R.id.dateTV);
        dateTv.setText(SignIn1.getDate());
        campusTv = findViewById(R.id.campusTV);
        campusTv.setText(SignIn1.getCampus());
        attendantTv = findViewById(R.id.attendantTV);
        attendantTv.setText(SignIn1.getAttendant());
        nameEdT = findViewById(R.id.child_name);
        family_nameEdT = findViewById(R.id.familyName);
        phone_noEdT = findViewById(R.id.family_phone);
        card_noEdT = findViewById(R.id.card_no);
        register = findViewById(R.id.childRegistration);
        clockIn = findViewById(R.id.clockIn);
        clockOut = findViewById(R.id.clockOut);

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
        ageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                // Get Selected gender from the list
                age = parent.getItemAtPosition(position).toString();
                selectAge = parent.getItemAtPosition(0).toString();
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
                card_no = card_noEdT.getText().toString();
                date = dateTv.getText().toString();
                attendant = attendantTv.getText().toString();
                campus = campusTv.getText().toString();
                if (clockIn.isChecked()) {
                    clockInState = "Clocked In";
                }
                if (clockOut.isChecked()) {
                    clockOutState = "Clocked Out";
                }
                // display it as Toast to the user
                if (TextUtils.isEmpty(name)) {
                    nameEdT.setError("Please enter Child's Name");
                } else if (TextUtils.isEmpty(surname)) {
                    family_nameEdT.setError("Please enter Family's Name");
                }else if (TextUtils.isEmpty(phone_no)) {
                    phone_noEdT.setError("Please enter Phone no");
                }else if (TextUtils.isEmpty(card_no)) {
                    card_noEdT.setError("Please enter Card no");
                }
                else if (gender == selectGender){
                    Toast.makeText(ChildCare.this, "Please select Gender", Toast.LENGTH_SHORT).show();
                }
                else if (age == selectAge){
                    Toast.makeText(ChildCare.this, "Please select Age", Toast.LENGTH_SHORT).show();
                }
                else if (clockInState == null){
                    Toast.makeText(ChildCare.this, "Please clock child in ", Toast.LENGTH_SHORT).show();
                }
                else if (uri == null){
                    Toast.makeText(ChildCare.this, "Please capture child", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(uploadTask != null && uploadTask.isInProgress()){
                        Toast.makeText(ChildCare.this,"Upload in progress",Toast.LENGTH_SHORT).show();

                    }
                    else {
                        uploadImage(uri);

                    }

                }

            }
        });
    }
    private void addDataToFirebase(String name, String surname, String age,String gender,String phone_no,String card_no,String attendant, String campus, String date, String clockInState, String clockOutState,String imageUrl) {
        // below 3 lines of code is used to set
        // data in our object class.
        children.setDate(date);
        children.setName(name);
        children.setSurname(surname);
        children.setGender(gender);
        children.setAge(age);
        children.setPhone_no(phone_no);
        children.setCard_no(card_no);
        children.setAttendant(attendant);
        children.setCampus(campus);
        children.setClockInState(clockInState);
        children.setClockOutState(clockOutState);
        children.setImageUrl(imageUrl);

        databaseReference.push().setValue(children).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                // after adding this data we are showing toast message.
                Toast.makeText(ChildCare.this, "Child added", Toast.LENGTH_SHORT).show();
                nameEdT.setText("");
                family_nameEdT.setText("");
                ageSpinner.setSelection(0);
                genderSpinner.setSelection(0);
                phone_noEdT.setText("");
                card_noEdT.setText("");
                clockIn.setChecked(false);
                click_image_id.setImageBitmap(null);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // we are displaying a failure toast message.
                Toast.makeText(ChildCare.this, "Failed to add child \n" + e, Toast.LENGTH_SHORT).show();
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
        StorageReference childRef = storageReference.child("Children Images/" + randomKey);

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
                        addDataToFirebase(name, surname, age,gender,phone_no,card_no,attendant, campus, date, clockInState, clockOutState,imageUrl);
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
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}