package com.des.cozawelfare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateFirstTimer extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    // variables for storing our data.
    private String name,age,gender,phone_no,membership,address,remarks,attendant,campus,date;
    private Button register;
    private EditText nameEdT,ageEdT,phone_noEdT,addressEdT,remarksEdT;
    private Spinner genderSpinner,membershipSpinner;
    private TextView dateTv,campusTv,attendantTv;
    // on below line we are creating a variable for our list of data to be displayed in spinner.
    private String[] genders = {"Male", "Female"};
    private String[] membershipStatuses = {"Yes","No"};

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_first_timer);
        getSupportActionBar().setTitle("Update First Timer");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF737070));
        FirstTimer firstTimer = (FirstTimer) getIntent().getSerializableExtra("firstTimer");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("First Timers");
        // calling this activity's function to
        // use ActionBar utility methods
        ActionBar actionBar = getSupportActionBar();

        // methods to display the icon in the ActionBar
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        // initializing our edittext and buttons
        nameEdT = findViewById(R.id.name);
        ageEdT = findViewById(R.id.age);
        genderSpinner = findViewById(R.id.genderSpinner);
        phone_noEdT = findViewById(R.id.phone);
        membershipSpinner = findViewById(R.id.membershipSpinner);
        addressEdT = findViewById(R.id.address);
        remarksEdT = findViewById(R.id.remarks);
        register = findViewById(R.id.registration);
        dateTv = findViewById(R.id.dateTV);
        campusTv = findViewById(R.id.campusTV);
        attendantTv = findViewById(R.id.attendantTV);
        

        // on below line we are initializing adapter for our spinner
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genders);

        // on below line we are setting drop down view resource for our adapter.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // on below line we are setting adapter for spinner.
        genderSpinner.setAdapter(adapter);

        // on below line we are adding click listener for our spinner
        genderSpinner.setOnItemSelectedListener(this);

        // on below line we are creating a variable to which we have to set our spinner item selected.
        String selection = firstTimer.getGender();

        // on below line we are getting the position of the item by the item name in our adapter.
        int spinnerPosition = adapter.getPosition(selection);

        // on below line we are setting selection for our spinner to spinner position.
        genderSpinner.setSelection(spinnerPosition);

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

        // on below line we are initializing adapter for our spinner
        ArrayAdapter<CharSequence> mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, membershipStatuses);

        // on below line we are setting drop down view resource for our adapter.
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // on below line we are setting adapter for spinner.
        membershipSpinner.setAdapter(mAdapter);

        // on below line we are adding click listener for our spinner
        membershipSpinner.setOnItemSelectedListener(this);

        // on below line we are creating a variable to which we have to set our spinner item selected.
        String mSelection = firstTimer.getGender();

        // on below line we are getting the position of the item by the item name in our adapter.
        int mSpinnerPosition = mAdapter.getPosition(mSelection);

        // on below line we are setting selection for our spinner to spinner position.
        membershipSpinner.setSelection(mSpinnerPosition);


        membershipSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                // Get Selected gender from the list
                membership = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // can leave this empty
            }
        });
        dateTv.setText(firstTimer.getDate());
        attendantTv.setText(firstTimer.getAttendant());
        campusTv.setText(firstTimer.getCampus());
        nameEdT.setText(firstTimer.getName());
        phone_noEdT.setText(firstTimer.getPhone_no());
        ageEdT.setText(firstTimer.getAddress());
        addressEdT.setText(firstTimer.getAddress());
        remarksEdT.setText(firstTimer.getRemarks());


        // adding on click listener for button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // getting data from edittext fields.
                name = nameEdT.getText().toString();
                age = ageEdT.getText().toString();
                phone_no = phone_noEdT.getText().toString();
                address = addressEdT.getText().toString();
                remarks = remarksEdT.getText().toString();
                attendant = attendantTv.getText().toString();
                campus = campusTv.getText().toString();
                date = dateTv.getText().toString();

                // validating the text fields if empty or not.
                if (TextUtils.isEmpty(name)) {
                    nameEdT.setError("Please enter Name");
                } else if (TextUtils.isEmpty(address)) {
                    addressEdT.setError("Please enter address");
                }else if (TextUtils.isEmpty(phone_no)) {
                    phone_noEdT.setError("Please enter Phone no");
                }

                else if (TextUtils.isEmpty(remarks)) {
                    remarksEdT.setError("Please enter Phone no");
                }
                else if (TextUtils.isEmpty(age)) {
                    ageEdT.setError("Please enter Age");
                } else {
                    // calling a method to update our course.
                    // we are passing our object class, course name,
                    // course description and course duration from our edittext field.
                    updateFirstTimer(firstTimer, name,age,gender,phone_no,membership,address,remarks,attendant,campus,date);

                }
            }
        });
    }
    private void updateFirstTimer(FirstTimer firstTimer, String name,String age,String gender,String phone_no,String membership,String address,String remarks,String attendant,String campus,String date) {
        // inside this method we are passing our updated values
        // inside our object class and later on we
        // will pass our whole object to firebase Firestore.
        FirstTimer updatedFirstTimer = new FirstTimer(name,age,gender,phone_no,membership,address,remarks,attendant,campus,date);

        databaseReference.child(firstTimer.getId()).setValue(updatedFirstTimer)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // on successful completion of this process
                        // we are displaying the toast message.
                        Toast.makeText(UpdateFirstTimer.this, "Firstimer has been updated..", Toast.LENGTH_SHORT).show();

                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateFirstTimer.this, "Failed to update firsttimer..", Toast.LENGTH_SHORT).show();
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
                        .setTitle("Deleting First Timer").setMessage("Are you sure you want to delete this First timer?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirstTimer firstTimer = (FirstTimer) getIntent().getSerializableExtra("firstTimer");
                                deleteFirstTimer(firstTimer);

                            }
                        }).setNegativeButton("No", null).show();

                break;
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void deleteFirstTimer(FirstTimer firstTimer) {

        // below line is for getting the collection
        // where we are storing our courses.
        databaseReference.
                // after that we are getting the document
                // which we have to delete.
                        child(firstTimer.getId()).

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
                            Toast.makeText(UpdateFirstTimer.this, "First Timer has been deleted from Database.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            // if the delete operation is failed
                            // we are displaying a toast message.
                            Toast.makeText(UpdateFirstTimer.this, "Failed to delete First Timer. ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    @Override
    public void onBackPressed() {
        UpdateFirstTimer.this.finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}