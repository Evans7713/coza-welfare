package com.des.cozawelfare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class UpdateChild extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private String name, surname, age, gender, phone_no, card_no, attendant, campus, date,imageUrl;
    private Bitmap bitmap;
    private Uri uri;
    private Spinner ageSpinner, genderSpinner;
    private TextView dateTv,campusTv,attendantTv;
    private EditText nameEdT,family_nameEdT,phoneEdT,cardEdT;
    private Button camera_open_id, update;
    private ImageView click_image_id;
    private CheckBox clockIn, clockOut;
    private String clockInState = "";
    private String clockOutState = "";
    private String[] genders = {"Boy", "Girl"};
    private String[] ages = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18"};
    private static final int pic_id = 123;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private int c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_child);

        Children children = (Children) getIntent().getSerializableExtra("child");

        progressDialog = new ProgressDialog(this);

        getSupportActionBar().setTitle("Update Child");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF737070));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // calling this activity's function to
        // use ActionBar utility methods
        ActionBar actionBar = getSupportActionBar();

        // methods to display the icon in the ActionBar
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl(children.getImageUrl());

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Children");

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
        phoneEdT = findViewById(R.id.family_phone);
        cardEdT = findViewById(R.id.card_no);
        update = findViewById(R.id.childRegistration);
        clockIn = findViewById(R.id.clockIn);
        clockOut = findViewById(R.id.clockOut);


        if(children.getClockInState() != null){
            clockIn.setChecked(true);
        }
        if(children.getClockOutState() != null){
            clockOut.setChecked(true);
        }


        // on below line we are initializing adapter for our spinner
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genders);

        // on below line we are setting drop down view resource for our adapter.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // on below line we are setting adapter for spinner.
        genderSpinner.setAdapter(adapter);

        // on below line we are adding click listener for our spinner
        genderSpinner.setOnItemSelectedListener(this);

        // on below line we are creating a variable to which we have to set our spinner item selected.
        String selection = children.getGender();

        // on below line we are getting the position of the item by the item name in our adapter.
        int spinnerPosition = adapter.getPosition(selection);

        // on below line we are setting selection for our spinner to spinner position.
        genderSpinner.setSelection(spinnerPosition);

        // on below line we are initializing adapter for our spinner
        ArrayAdapter<CharSequence> ageAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ages);

        // on below line we are setting drop down view resource for our adapter.
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // on below line we are setting adapter for spinner.
        ageSpinner.setAdapter(ageAdapter);

        // on below line we are adding click listener for our spinner
        ageSpinner.setOnItemSelectedListener(this);

        // on below line we are creating a variable to which we have to set our spinner item selected.
        String ageSelection = children.getAge();

        // on below line we are getting the position of the item by the item name in our adapter.
        int ageSpinnerPosition = ageAdapter.getPosition(ageSelection);

        // on below line we are setting selection for our spinner to spinner position.
        ageSpinner.setSelection(ageSpinnerPosition);

        camera_open_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = 1;
                        // Create the camera_intent ACTION_IMAGE_CAPTURE it will open the camera for capture the image
                        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // Start the activity with camera_intent, and request pic id
                        startActivityForResult(camera_intent, pic_id);

            }
        });

        ageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                // Get Selected gender from the list
                age = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // can leave this empty
            }
        });
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

        nameEdT.setText(children.getName());
        family_nameEdT.setText(children.getSurname());
        phoneEdT.setText(children.getPhone_no());
        cardEdT.setText(children.getCard_no());
        dateTv.setText(children.getDate());
        attendantTv.setText(children.getAttendant());
        campusTv.setText(children.getCampus());
        //Picasso.get().load(children.getImageUrl()).into(click_image_id);
        Glide.with(UpdateChild.this).load(children.getImageUrl()).into(click_image_id);



        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameEdT.getText().toString();
                surname = family_nameEdT.getText().toString();
                phone_no = phoneEdT.getText().toString();
                card_no = cardEdT.getText().toString();
                attendant = attendantTv.getText().toString();
                date = dateTv.getText().toString();
                campus = campusTv.getText().toString();
                if (clockIn.isChecked()) {
                    clockInState = clockInState + "Clocked In";
                }

                if (clockOut.isChecked()) {
                    clockOutState = clockOutState + "Clocked Out";
                }

                // validating the text fields if empty or not.
                if (TextUtils.isEmpty(name)) {
                    nameEdT.setError("Please enter Child Name");
                } else if (TextUtils.isEmpty(surname)) {
                    family_nameEdT.setError("Please enter Family Name");
                }  else {
                    if (c==1){
                        uploadImage(uri);
                    }
                    else {
                        // calling a method to update our course.
                        // we are passing our object class, course name,
                        // course description and course duration from our edittext field.
                        Children children = (Children) getIntent().getSerializableExtra("child");
                        imageUrl = children.getImageUrl();
                        updateChildren(children,name, surname, age,gender,phone_no,card_no,attendant, campus, date, clockInState, clockOutState, imageUrl);
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
                bitmap = (Bitmap) data.getExtras().get("data");
                click_image_id.setImageBitmap(bitmap);
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
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Failed to upload", Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Snackbar.make(findViewById(android.R.id.content), "Image uploaded", Snackbar.LENGTH_LONG).show();
                            // creating a collection reference
                            // for our Firebase Firetore database.
                            imageUrl = uri.toString();
                            progressDialog.dismiss();
                            Snackbar.make(findViewById(android.R.id.content), "Image uploaded", Snackbar.LENGTH_LONG).show();
                            Children children = (Children) getIntent().getSerializableExtra("child");
                            updateChildren(children,name, surname, age,gender,phone_no,card_no,attendant, campus, date, clockInState, clockOutState, imageUrl);
                            //click_image_id.setImageURI(null);

                        }
                    });

                }
            }).addOnProgressListener(
                    new OnProgressListener<UploadTask.TaskSnapshot>() {

                        // Progress Listener for loading
                        // percentage on the dialog box
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress
                                    = (100.0
                                    * taskSnapshot.getBytesTransferred()
                                    / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploading " + (int) progress + "%");
                        }
                    });


    }
    private void updateChildren(Children children, String name, String surname, String age,String gender,String phone_no,String card_no,String attendant,String campus,String date,String clockInState,String clockOutState,String imageUrl) {
        // inside this method we are passing our updated values
        // inside our object class and later on we
        // will pass our whole object to firebase Firestore.
        Children updatedChild = new Children(name, surname, age,gender,phone_no,card_no,attendant, campus, date, clockInState, clockOutState, imageUrl);

        // after passing data to object class we are
        // sending it to firebase with specific document id.
        // below line is use to get the collection of our Firebase Firestore.
        databaseReference.
                // below line is use toset the id of
                // document where we have to perform
                // update operation.
                        child(children.getId()).

                // after setting our document id we are
                // passing our whole object class to it.
                        setValue(updatedChild).

                // after passing our object class we are
                // calling a method for on success listener.
                        addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // on successful completion of this process
                        // we are displaying the toast message.
                        Toast.makeText(UpdateChild.this, "Child has been updated..", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            // inside on failure method we are
            // displaying a failure message.
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateChild.this, "Failed to update the data..", Toast.LENGTH_SHORT).show();
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
                        .setTitle("Deleting Child").setMessage("Are you sure you want to delete this child?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        // calling method to delete the course.
                                        Children children = (Children) getIntent().getSerializableExtra("child");
                                        deleteChild(children);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(UpdateChild.this, "Unable to delete child",Toast.LENGTH_SHORT).show();
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
    private void deleteChild(Children children) {

        // below line is for getting the collection
        // where we are storing our courses.
        databaseReference.
                // after that we are getting the document
                // which we have to delete.
                        child(children.getId()).

                // after passing the document id we are calling
                // delete method to delete this document.
                        setValue(null).
                // after deleting call on complete listener
                // method to delete this data.
                        addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // inside on complete method we are checking
                        // if the task is success or not.
                        if (task.isSuccessful()) {
                            // this method is called when the task is success
                            // after deleting we are starting our MainActivity.
                            Toast.makeText(UpdateChild.this, "Child has been deleted from Database.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            // if the delete operation is failed
                            // we are displaying a toast message.
                            Toast.makeText(UpdateChild.this, "Failed to delete the child. ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    @Override
    public void onBackPressed() {
        UpdateChild.this.finish();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}