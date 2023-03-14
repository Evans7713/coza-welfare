package com.des.cozawelfare;

import androidx.annotation.NonNull;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Evangelism extends AppCompatActivity {

    // variables for storing our data.
    private String name,age,gender,phone_no,membership,address,remarks,attendant,campus,date,selectGender,membershipStatus;
    private Button register;
    private EditText nameEdT,ageEdT,phone_noEdT,addressEdT,remarksEdT;
    private Spinner genderSpinner,membershipSpinner;
    private TextView dateTv,campusTv,attendantTv;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    FirstTimer firstTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evangelism);
        getSupportActionBar().setTitle("First Timer");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF737070));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("First Timers");

        firstTimer = new FirstTimer();

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
        dateTv.setText(SignIn1.getDate());
        campusTv = findViewById(R.id.campusTV);
        campusTv.setText(SignIn1.getCampus());
        attendantTv = findViewById(R.id.attendantTV);
        attendantTv.setText(SignIn1.getAttendant());

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

        membershipSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                    // Get Selected gender from the list
                    membership = parent.getItemAtPosition(position).toString();
                    membershipStatus = parent.getItemAtPosition(0).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent)
                {
                    // can leave this empty
                }
            });


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
                else if (gender == selectGender){
                    Toast.makeText(Evangelism.this, "Please select Gender", Toast.LENGTH_SHORT).show();
                }
                else if (membership == membershipStatus){
                    Toast.makeText(Evangelism.this, "Please select Membership Status", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(remarks)) {
                    remarksEdT.setError("Please enter Phone no");
                }
                else if (TextUtils.isEmpty(age)) {
                    ageEdT.setError("Please enter Age");
                } else {
                    // calling method to add data to Firebase Firestore.
                    addDataToFirebase(name,age,gender,phone_no,membership,address,remarks,attendant,campus,date);
                }
            }
        });
    }
    private void addDataToFirebase(String name,String age,String gender,String phone_no,String membership,String address,String remarks,String attendant,String campus,String date) {


        // adding our data to our courses object class.
        FirstTimer firstTimer = new FirstTimer(name,age,gender,phone_no,membership,address,remarks,attendant,campus,date);

        firstTimer.setDate(date);
        firstTimer.setName(name);
        firstTimer.setCampus(campus);
        firstTimer.setGender(gender);
        firstTimer.setAge(age);
        firstTimer.setAddress(address);
        firstTimer.setPhone_no(phone_no);
        firstTimer.setRemarks(remarks);
        firstTimer.setAttendant(attendant);

        databaseReference.push().setValue(firstTimer).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                // after adding this data we are showing toast message.
                Toast.makeText(Evangelism.this, "First-Timer added", Toast.LENGTH_SHORT).show();

                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // we are displaying a failure toast message.
                Toast.makeText(Evangelism.this, "Failed to add first-timer \n" + e, Toast.LENGTH_SHORT).show();
            }
        });

    }
    // method to inflate the options menu when
    // the user opens the menu for the first time
    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {

        getMenuInflater().inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // methods to control the operations that will
    // happen when user clicks on the action buttons

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