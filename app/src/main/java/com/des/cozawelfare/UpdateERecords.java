package com.des.cozawelfare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UpdateERecords extends AppCompatActivity {
    private String name,surname,age,gender,marital_status,phone_no,email,location,help,request_status,date,imageUrl;
    private TextView nameTv,surnameTv,ageTv,genderTv,marital_statusTv,phone_noTv,emailTv,locationTv,requestTv,dateTv;
    private ImageView photoIv;
    private Spinner requestStatusSpinner;
    private Button updateBtn;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_erecords);
        getSupportActionBar().setTitle("Update E-Records");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF737070));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        Request request = (Request) getIntent().getSerializableExtra("request");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Online Request");

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl(request.getImageUrl());

        dateTv = findViewById(R.id.date);
        nameTv = findViewById(R.id.name);
        surnameTv = findViewById(R.id.surname);
        ageTv = findViewById(R.id.age);
        genderTv = findViewById(R.id.gender);
        marital_statusTv = findViewById(R.id.marital_status);
        phone_noTv = findViewById(R.id.phone_no);
        emailTv = findViewById(R.id.email);
        locationTv = findViewById(R.id.location);
        requestTv = findViewById(R.id.request);
        photoIv = findViewById(R.id.photo);
        requestStatusSpinner = findViewById(R.id.request_status);
        updateBtn = findViewById(R.id.update);

        dateTv.setText(request.getDate());
        nameTv.setText(request.getName());
        surnameTv.setText(request.getSurname());
        ageTv.setText(request.getAge());
        genderTv.setText(request.getGender());
        marital_statusTv.setText(request.getMarital_status());
        phone_noTv.setText(request.getPhone_no());
        emailTv.setText(request.getEmail());
        locationTv.setText(request.getLocation());
        requestTv.setText(request.getHelp());
        Glide.with(UpdateERecords.this).load(request.getImageUrl()).into(photoIv);

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

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameTv.getText().toString();
                surname = surnameTv.getText().toString();
                age = ageTv.getText().toString();
                gender = genderTv.getText().toString();
                marital_status = marital_statusTv.getText().toString();
                email = emailTv.getText().toString();
                phone_no = phone_noTv.getText().toString();
                location = locationTv.getText().toString();
                help = requestTv.getText().toString();
                date = dateTv.getText().toString();

                Request request = (Request) getIntent().getSerializableExtra("request");
                imageUrl = request.getImageUrl();
                updateERequest(request,date,name,surname,gender,age,marital_status,phone_no,email,location,help,request_status,imageUrl);
            }
        });

    }
    private void updateERequest(Request request, String date, String name, String surname ,String gender,String age,String marital_status,String phone_no,String email,String location,String help,String request_status,String imageUrl) {
        // inside this method we are passing our updated values
        // inside our object class and later on we
        // will pass our whole object to firebase Firestore.
        Request updatedrequest = new Request(date,name,surname,gender,age,marital_status,phone_no,email,location,help,request_status,imageUrl);

        databaseReference.child(request.getId()).setValue(updatedrequest)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // on successful completion of this process
                        // we are displaying the toast message.
                        Toast.makeText(UpdateERecords.this, "Case has been updated..", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateERecords.this, "Failed to update the case..", Toast.LENGTH_SHORT).show();
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
                        .setTitle("Deleting Case...").setMessage("Are you sure you want to delete this case?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        // calling method to delete the course.
                                        Request request = (Request) getIntent().getSerializableExtra("request");
                                        deleteECase(request);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(UpdateERecords.this, "Unable to delete case",Toast.LENGTH_SHORT).show();
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
    private void deleteECase(Request request) {
        // below line is for getting the collection
        // where we are storing our courses.
        databaseReference.child(request.getId()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // this method is called when the task is success
                    // after deleting we are starting our MainActivity.
                    Toast.makeText(UpdateERecords.this, "Case has been deleted from Database.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // if the delete operation is failed
                    // we are displaying a toast message.
                    Toast.makeText(UpdateERecords.this, "Failed to delete the case. ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        UpdateERecords.this.finish();
    }

}
