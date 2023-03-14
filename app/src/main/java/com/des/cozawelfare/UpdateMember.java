package com.des.cozawelfare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UpdateMember extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private String name, surname, gender, birthday, phone_no, email,marital_status, date,imageUrl;
    private EditText nameEdT, family_nameEdT, birthdayEdT, phone_noEdT, emailEdT,dateEdt;
    private Button captureImage,register;
    private ImageView capturedImage;
    private Spinner genderSpinner, marital_statusSpinner;
    DatePickerDialog picker;
    private String[] genders = {"Male", "Female"};
    private String[] maritalStatuses = {"Single","Married","Seperated","Divorced"};
    // creating a variable for firebasefirestore.
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    // Define the pic id
    private static final int pic_id = 123;
    // instance for firebase storage and StorageReference
    private FirebaseStorage storage;
    private StorageReference storageReference;
    // Uri indicates, where the image will be picked from
    private Uri uri;
    private Bitmap bitmap;
    private ProgressDialog progressDialog;
    private int count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_member);
        //Data data= (Data) getIntent().getSerializableExtra("data");
        getSupportActionBar().setTitle("Update Member");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF737070));
        // calling this activity's function to
        // use ActionBar utility methods
        ActionBar actionBar = getSupportActionBar();
        // methods to display the icon in the ActionBar
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        progressDialog = new ProgressDialog(this);

        Members members = (Members) getIntent().getSerializableExtra("member");

        // getting our instance from Firebase Firestore.

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Members Data");

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl(members.getImageUrl());

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
        dateEdt = findViewById(R.id.date);
        dateEdt.setInputType(InputType.TYPE_NULL);

        // display the string into textView
        dateEdt.setText(members.getDate());
        nameEdT.setText(members.getName());
        family_nameEdT.setText(members.getSurname());
        birthdayEdT.setText(members.getBirthday());
        phone_noEdT.setText(members.getPhone_no());
        emailEdT.setText(members.getEmail());
        //Picasso.get().load(members.getImageUrl()).into(capturedImage);
        Glide.with(UpdateMember.this).load(members.getImageUrl()).into(capturedImage);

        dateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(UpdateMember.this,
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
        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = 1;
                if (count == 1) {
                    // Create the camera_intent ACTION_IMAGE_CAPTURE it will open the camera for capture the image
                    Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Start the activity with camera_intent, and request pic id
                    startActivityForResult(camera_intent, pic_id);
                }
            }

        });
        // on below line we are initializing adapter for our spinner
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genders);

        // on below line we are setting drop down view resource for our adapter.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // on below line we are setting adapter for spinner.
        genderSpinner.setAdapter(adapter);

        // on below line we are adding click listener for our spinner
        genderSpinner.setOnItemSelectedListener(this);

        // on below line we are creating a variable to which we have to set our spinner item selected.
        String selection = gender;

        // on below line we are getting the position of the item by the item name in our adapter.
        int spinnerPosition = adapter.getPosition(selection);

        // on below line we are setting selection for our spinner to spinner position.
        genderSpinner.setSelection(spinnerPosition);

        // on below line we are initializing adapter for our spinner
        ArrayAdapter<CharSequence> adapterMarital = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, maritalStatuses);

        // on below line we are setting drop down view resource for our adapter.
        adapterMarital.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // on below line we are setting adapter for spinner.
        marital_statusSpinner.setAdapter(adapterMarital);

        // on below line we are adding click listener for our spinner
        marital_statusSpinner.setOnItemSelectedListener(this);

        // on below line we are creating a variable to which we have to set our spinner item selected.
        String selectionMarital = marital_status;

        // on below line we are getting the position of the item by the item name in our adapter.
        int maritalSpinnerPosition = adapterMarital.getPosition(selectionMarital);

        // on below line we are setting selection for our spinner to spinner position.
        marital_statusSpinner.setSelection(maritalSpinnerPosition);

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                // Get Selected gender from the list
                gender = parent.getItemAtPosition(position).toString();

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
                date = dateEdt.getText().toString();
                // validating the text fields if empty or not.
                if (TextUtils.isEmpty(name)) {
                    nameEdT.setError("Please enter name");
                } else if (TextUtils.isEmpty(surname)) {
                    family_nameEdT.setError("Please enter surname");
                } else if (TextUtils.isEmpty(email)) {
                    emailEdT.setError("Please enter email");
                } else if (TextUtils.isEmpty(date)) {
                    dateEdt.setError("Please enter date");
                } else if (TextUtils.isEmpty(birthday)) {
                    birthdayEdT.setError("Please enter bithday");
                } else if (TextUtils.isEmpty(phone_no)) {
                    phone_noEdT.setError("Please enter phone no");
                }
                else {
                    if (count==1) {
                        uploadImage(uri);
                    }
                    else {
                        Members members = (Members) getIntent().getSerializableExtra("member");
                        imageUrl = members.getImageUrl();
                        updateMembers(members,date,name,surname,gender,birthday,marital_status,phone_no,email,imageUrl);
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
        progressDialog.setMessage("Image uploading...");
        progressDialog.show();
        storageReference.putFile(uri).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(getApplicationContext(),"Failed to upload",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Snackbar.make(findViewById(android.R.id.content),"Image uploaded", Snackbar.LENGTH_LONG).show();
                        // creating a collection reference
                        // for our Firebase Firetore database.
                        imageUrl = uri.toString();
                        progressDialog.dismiss();
                        Snackbar.make(findViewById(android.R.id.content), "Image uploaded", Snackbar.LENGTH_LONG).show();
                        Members members = (Members) getIntent().getSerializableExtra("member");
                        updateMembers(members,date,name,surname,gender,birthday,marital_status,phone_no,email,imageUrl);

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
                        progressDialog.setMessage("Uploaded " + (int)progress + "%");
                    }
                });

    }
    private void updateMembers(Members members, String date, String name, String surname ,String gender,String birthday,String marital_status,String phone_no,String email,String imageUrl) {
        // inside this method we are passing our updated values
        // inside our object class and later on we
        // will pass our whole object to firebase Firestore.
        Members updatedmember = new Members(date,name,surname,gender,birthday,marital_status,phone_no,email,imageUrl);

        databaseReference.child(members.getId()).setValue(updatedmember)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // on successful completion of this process
                        // we are displaying the toast message.
                        Toast.makeText(UpdateMember.this, "Member has been updated..", Toast.LENGTH_SHORT).show();

                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateMember.this, "Failed to update the data..", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    // method to inflate the options menu when
    // the user opens the menu for the first time
    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {

        getMenuInflater().inflate(R.menu.overflow_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // methods to control the operations that will
    // happen when user clicks on the action buttons
    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {

        switch (item.getItemId()){

            case R.id.delete:

                new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Deleting Member...").setMessage("Are you sure you want to delete this member?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        // calling method to delete the course.
                                        Members members = (Members) getIntent().getSerializableExtra("member");
                                        deleteMember(members);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(UpdateMember.this, "Unable to delete member",Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }).setNegativeButton("No", null).show();

                break;
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void deleteMember(Members members) {
        // below line is for getting the collection
        // where we are storing our courses.
        databaseReference.child(members.getId()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // this method is called when the task is success
                    // after deleting we are starting our MainActivity.
                    Toast.makeText(UpdateMember.this, "Member has been deleted from Database.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // if the delete operation is failed
                    // we are displaying a toast message.
                    Toast.makeText(UpdateMember.this, "Failed to delete the member. ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        UpdateMember.this.finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}