package com.des.cozawelfare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.UUID;

public class UpdateCase extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private String name,surname,age,gender,phone_no,attendant,campus,date, marital_status, no_of_children,years_in_coza,bfc,evangelism,request,request_status,born_again,address,remarks,imageUrl;
    private Bitmap bitmap;
    private Uri uri;
    private Spinner maritalStatusSpinner, genderSpinner,evangelismSpinner,bfcSpinner,requestStatusSpinner,bornAgainSpinner;
    private TextView dateTv,campusTv,attendantTv;
    private EditText case_nameEdT,case_surnameEdT,case_ageEdT,case_phone_noEdT,case_no_of_childrenEdT,case_years_in_cozaEdT,case_requestEdT,case_addressEdT,case_remarksEdT;
    private Button camera_open_id, register;
    private ImageView click_image_id;
    private String[] genders = {"Male", "Female"};
    private String[] maritalStatuses = {"Single","Married","Seperated","Divorced"};
    private String[] bornAgainStatus = {"Yes","No"};
    private String[] bfcs = {"Yes","No"};
    private String[] evagelismStatus = {"Yes","No"};
    private String[] requestStatuses = {"Pending","Sorted","Rejected"};
    // Define the pic id
    private static final int pic_id = 123;
    // instance for firebase storage and StorageReference
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private int c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_case);
        getSupportActionBar().setTitle("Case Update");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF737070));

        Cases cases = (Cases) getIntent().getSerializableExtra("cases");

        progressDialog = new ProgressDialog(this);
        // getting our instance
        // from Firebase Firestore.
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Cases");
        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl(cases.getImageUrl());
        // calling this activity's function to
        // use ActionBar utility methods
        ActionBar actionBar = getSupportActionBar();
        // methods to display the icon in the ActionBar
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        camera_open_id = findViewById(R.id.camera_button);
        click_image_id = findViewById(R.id.click_image);
        genderSpinner = findViewById(R.id.genderSpinner);
        maritalStatusSpinner = findViewById(R.id.maritalStatusSpinner);
        evangelismSpinner = findViewById(R.id.evangelismSpinner);
        bfcSpinner = findViewById(R.id.bfcSpinner);
        requestStatusSpinner = findViewById(R.id.requestStatusSpinner);
        bornAgainSpinner = findViewById(R.id.bornAgainSpinner);
        case_ageEdT = findViewById(R.id.age);
        dateTv = findViewById(R.id.dateTV);
        campusTv = findViewById(R.id.campusTV);
        attendantTv = findViewById(R.id.attendantTV);
        case_nameEdT = findViewById(R.id.case_name);
        case_surnameEdT = findViewById(R.id.case_surname);
        case_no_of_childrenEdT = findViewById(R.id.noOfChildren);
        case_phone_noEdT = findViewById(R.id.phone);
        case_years_in_cozaEdT = findViewById(R.id.duration);
        case_requestEdT = findViewById(R.id.request);
        case_addressEdT = findViewById(R.id.address);
        case_remarksEdT = findViewById(R.id.remarks);
        register = findViewById(R.id.caseRegistration);

        // on below line we are initializing adapter for our spinner
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genders);

        // on below line we are setting drop down view resource for our adapter.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // on below line we are setting adapter for spinner.
        genderSpinner.setAdapter(adapter);

        // on below line we are adding click listener for our spinner
        genderSpinner.setOnItemSelectedListener(this);

        // on below line we are creating a variable to which we have to set our spinner item selected.
        String selection = cases.getGender();

        // on below line we are getting the position of the item by the item name in our adapter.
        int spinnerPosition = adapter.getPosition(selection);

        // on below line we are setting selection for our spinner to spinner position.
        genderSpinner.setSelection(spinnerPosition);

        // on below line we are initializing adapter for our spinner
        ArrayAdapter<CharSequence> adapterMarital = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, maritalStatuses);

        // on below line we are setting drop down view resource for our adapter.
        adapterMarital.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // on below line we are setting adapter for spinner.
        maritalStatusSpinner.setAdapter(adapterMarital);

        // on below line we are adding click listener for our spinner
        maritalStatusSpinner.setOnItemSelectedListener(this);

        // on below line we are creating a variable to which we have to set our spinner item selected.
        String selectionMarital = cases.getMarital_Status();

        // on below line we are getting the position of the item by the item name in our adapter.
        int maritalSpinnerPosition = adapterMarital.getPosition(selectionMarital);

        // on below line we are setting selection for our spinner to spinner position.
        maritalStatusSpinner.setSelection(maritalSpinnerPosition);

        // on below line we are initializing adapter for our spinner
        ArrayAdapter<CharSequence> bornAgainAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bornAgainStatus);

        // on below line we are setting drop down view resource for our adapter.
        bornAgainAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // on below line we are setting adapter for spinner.
        bornAgainSpinner.setAdapter(bornAgainAdapter);

        // on below line we are adding click listener for our spinner
        bornAgainSpinner.setOnItemSelectedListener(this);

        // on below line we are creating a variable to which we have to set our spinner item selected.
        String bornAgainSelection = cases.getBorn_Again();

        // on below line we are getting the position of the item by the item name in our adapter.
        int bAspinnerPosition = bornAgainAdapter.getPosition(bornAgainSelection);

        // on below line we are setting selection for our spinner to spinner position.
        bornAgainSpinner.setSelection(bAspinnerPosition);

        // on below line we are initializing adapter for our spinner
        ArrayAdapter<CharSequence> bfcAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bfcs);

        // on below line we are setting drop down view resource for our adapter.
        bfcAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // on below line we are setting adapter for spinner.
        bfcSpinner.setAdapter(bfcAdapter);

        // on below line we are adding click listener for our spinner
        bfcSpinner.setOnItemSelectedListener(this);

        // on below line we are creating a variable to which we have to set our spinner item selected.
        String bfcSelection = cases.getBFC();

        // on below line we are getting the position of the item by the item name in our adapter.
        int bfcSpinnerPosition = bfcAdapter.getPosition(bfcSelection);

        // on below line we are setting selection for our spinner to spinner position.
        bfcSpinner.setSelection(bfcSpinnerPosition);

        // on below line we are initializing adapter for our spinner
        ArrayAdapter<CharSequence> eAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, evagelismStatus);

        // on below line we are setting drop down view resource for our adapter.
        eAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // on below line we are setting adapter for spinner.
        evangelismSpinner.setAdapter(eAdapter);

        // on below line we are adding click listener for our spinner
        evangelismSpinner.setOnItemSelectedListener(this);

        // on below line we are creating a variable to which we have to set our spinner item selected.
        String eSelection = cases.getEvangelism();

        // on below line we are getting the position of the item by the item name in our adapter.
        int eSpinnerPosition = eAdapter.getPosition(eSelection);

        // on below line we are setting selection for our spinner to spinner position.
        evangelismSpinner.setSelection(eSpinnerPosition);

        // on below line we are initializing adapter for our spinner
        ArrayAdapter<CharSequence> rAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, requestStatuses);

        // on below line we are setting drop down view resource for our adapter.
        rAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // on below line we are setting adapter for spinner.
        requestStatusSpinner.setAdapter(rAdapter);

        // on below line we are adding click listener for our spinner
        requestStatusSpinner.setOnItemSelectedListener(this);

        // on below line we are creating a variable to which we have to set our spinner item selected.
        String rSelection = cases.getRequest_Status();

        // on below line we are getting the position of the item by the item name in our adapter.
        int rSpinnerPosition = rAdapter.getPosition(rSelection);

        // on below line we are setting selection for our spinner to spinner position.
        requestStatusSpinner.setSelection(rSpinnerPosition);

        camera_open_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = 1;
                if (c==1){
                    // Create the camera_intent ACTION_IMAGE_CAPTURE it will open the camera for capture the image
                    Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Start the activity with camera_intent, and request pic id
                    startActivityForResult(camera_intent, pic_id);
                }

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
        maritalStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
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
        bfcSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                // Get Selected gender from the list
                bfc = parent.getItemAtPosition(position).toString();

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
        dateTv.setText(cases.getDate());
        attendantTv.setText(cases.getAttendant());
        campusTv.setText(cases.getCampus());
        case_addressEdT.setText(cases.getAddress());
        case_nameEdT.setText(cases.getName());
        case_surnameEdT.setText(cases.getSurname());
        case_ageEdT.setText(cases.getAge());
        case_no_of_childrenEdT.setText(cases.getNo_of_Children());
        case_years_in_cozaEdT.setText(cases.getYears_in_COZA());
        case_requestEdT.setText(cases.getRequest());
        case_remarksEdT.setText(cases.getRemarks());
        case_phone_noEdT.setText(cases.getPhone_no());
        //Picasso.get().load(cases.getImageUrl()).into(click_image_id);
        Glide.with(UpdateCase.this).load(cases.getImageUrl()).into(click_image_id);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // getting data from edittext fields.
                name = case_nameEdT.getText().toString();
                surname = case_surnameEdT.getText().toString();
                no_of_children = case_no_of_childrenEdT.getText().toString();
                phone_no = case_phone_noEdT.getText().toString();
                date = dateTv.getText().toString();
                attendant = attendantTv.getText().toString();
                campus = campusTv.getText().toString();
                age = case_ageEdT.getText().toString();
                years_in_coza = case_years_in_cozaEdT.getText().toString();
                request = case_requestEdT.getText().toString();
                address = case_addressEdT.getText().toString();
                remarks = case_remarksEdT.getText().toString();
                // display it as Toast to the user
                if (TextUtils.isEmpty(name)) {
                    case_nameEdT.setError("Please enter Name");
                }
                else if (TextUtils.isEmpty(surname)) {
                    case_surnameEdT.setError("Please enter Age");
                }else if (TextUtils.isEmpty(age)) {
                    case_ageEdT.setError("Please enter Age");
                } else if (TextUtils.isEmpty(phone_no)) {
                    case_phone_noEdT.setError("Please enter Phone No");
                }else if (TextUtils.isEmpty(no_of_children)) {
                    case_no_of_childrenEdT.setError("Please enter No of Children");
                }
                else if (TextUtils.isEmpty(years_in_coza)) {
                    case_years_in_cozaEdT.setError("Please enter years spent in COZA");
                }
                else if (TextUtils.isEmpty(request)) {
                    case_requestEdT.setError("Please enter Request");
                }
                else if (TextUtils.isEmpty(address)) {
                    case_addressEdT.setError("Please enter Address");
                }
                else if (TextUtils.isEmpty(remarks)) {
                    case_remarksEdT.setError("Please enter Remarks");
                }
                //else if (uri == null){
                  //  Toast.makeText(UpdateCase.this, "Please capture Image", Toast.LENGTH_SHORT).show();
                //}
                else {
                    if (c==1){
                        uploadImage(uri);
                    }
                    else {
                        // calling a method to update our course.
                        // we are passing our object class, course name,
                        // course description and course duration from our edittext field.
                        Cases cases = (Cases) getIntent().getSerializableExtra("cases");
                        imageUrl = cases.getImageUrl();
                        updateCases(cases, name, surname,age, gender, marital_status, no_of_children, phone_no, years_in_coza, born_again, bfc, evangelism, request, request_status, address, remarks, attendant, campus, date, imageUrl);
                    }

                }

            }
        });

    }
    private void updateCases(Cases cases, String name, String surname, String age, String gender, String marital_status, String no_of_children, String phone_no, String years_in_coza, String born_again, String bfc, String evangelism, String request, String request_status, String address, String remarks,String attendant, String campus, String date,String imageUrl) {
        // inside this method we are passing our updated values
        // inside our object class and later on we
        // will pass our whole object to firebase Firestore.
        Cases updatedCase = new Cases(name, surname,age, gender,marital_status,no_of_children, phone_no, years_in_coza,born_again,bfc,evangelism,request,request_status,address,remarks,attendant, campus, date,imageUrl);

        databaseReference.child(cases.getId()).setValue(updatedCase)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // on successful completion of this process
                        // we are displaying the toast message.
                        Toast.makeText(UpdateCase.this, "Case has been updated..", Toast.LENGTH_SHORT).show();

                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateCase.this, "Failed to update the case..", Toast.LENGTH_SHORT).show();
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
        progressDialog.setMessage("Image uploading...");
        progressDialog.show();

        storageReference.putFile(uri).addOnFailureListener(new OnFailureListener() {
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
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Snackbar.make(findViewById(android.R.id.content),"Image uploaded", Snackbar.LENGTH_LONG).show();
                        // creating a collection reference
                        // for our Firebase Firetore database.
                        imageUrl = uri.toString();
                        progressDialog.dismiss();
                        Cases cases = (Cases) getIntent().getSerializableExtra("cases");
                        updateCases(cases, name, surname,age, gender, marital_status, no_of_children, phone_no, years_in_coza, born_again, bfc, evangelism, request, request_status, address, remarks, attendant, campus, date, imageUrl);
                        Snackbar.make(findViewById(android.R.id.content), "Image uploaded", Snackbar.LENGTH_LONG).show();
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
                        .setTitle("Deleting Case").setMessage("Are you sure you want to delete this Case?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        // calling method to delete the course.
                                        Cases cases = (Cases) getIntent().getSerializableExtra("cases");
                                        deleteCase(cases);


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(UpdateCase.this, "Unable to delete case",Toast.LENGTH_SHORT).show();
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
    private void deleteCase(Cases cases) {

        // below line is for getting the collection
        // where we are storing our courses.
        databaseReference.
                // after that we are getting the document
                // which we have to delete.
                        child(cases.getId()).

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
                            Toast.makeText(UpdateCase.this, "Case has been deleted from Database.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            // if the delete operation is failed
                            // we are displaying a toast message.
                            Toast.makeText(UpdateCase.this, "Failed to delete Case. ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    @Override
    public void onBackPressed() {
        UpdateCase.this.finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
