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

public class Medics extends AppCompatActivity {
    private Button register;
    private String name,age,gender,phone_no,complain,diagnosis,treatment,remarks,attendant,campus,date,selectGender;
    private Spinner genderSpinner;
    private EditText nameEdT,ageEdT,phone_noEdT,complainEdT,diagnosisEdT,treatmentEdT,remarksEdT;
    private TextView dateTv,campusTv,attendantTv;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medics);
        getSupportActionBar().setTitle("Patient");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF737070));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("Patients");

        patient = new Patient();

        register = findViewById(R.id.registration);
        genderSpinner = findViewById(R.id.genderSpinner);
        nameEdT = findViewById(R.id.name);
        ageEdT = findViewById(R.id.age);
        phone_noEdT = findViewById(R.id.phone);
        complainEdT = findViewById(R.id.complain);
        diagnosisEdT = findViewById(R.id.diagnosis);
        treatmentEdT = findViewById(R.id.treatment);
        remarksEdT = findViewById(R.id.remarks);
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
        // adding on click listener for button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // getting data from edittext fields.
                name = nameEdT.getText().toString();
                age = ageEdT.getText().toString();
                phone_no = phone_noEdT.getText().toString();
                complain = complainEdT.getText().toString();
                diagnosis = diagnosisEdT.getText().toString();
                treatment = treatmentEdT.getText().toString();
                remarks = remarksEdT.getText().toString();
                attendant = attendantTv.getText().toString();
                campus = campusTv.getText().toString();
                date = dateTv.getText().toString();

                // validating the text fields if empty or not.
                if (TextUtils.isEmpty(name)) {
                    nameEdT.setError("Please enter Name");
                } else if (TextUtils.isEmpty(phone_no)) {
                    phone_noEdT.setError("Please enter Phone No");
                }else if (gender == selectGender){
                    Toast.makeText(Medics.this, "Please select Gender", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(age)) {
                    ageEdT.setError("Please enter Age");
                } else if (TextUtils.isEmpty(complain)) {
                    complainEdT.setError("Please enter Complain");
                }
                else if (TextUtils.isEmpty(diagnosis)) {
                    diagnosisEdT.setError("Please enter Diagnosis");
                }
                else if (TextUtils.isEmpty(treatment)) {
                    treatmentEdT.setError("Please enter Treatment");
                }
                else if (TextUtils.isEmpty(remarks)) {
                    remarksEdT.setError("Please enter Remarks");
                }
                else {
                    // calling method to add data to Firebase Firestore.
                    addDataToFirebase(name,age,gender,phone_no,complain,diagnosis,treatment,remarks,attendant,campus,date);
                }
            }
        });
    }
    private void addDataToFirebase(String name,String age,String gender,String phone_no,String complain,String diagnosis,String treatment,String remarks,String attendant,String campus,String date) {


        // adding our data to our courses object class.
        Patient patient = new Patient(name,age,gender,phone_no,complain,diagnosis,treatment,remarks,attendant,campus,date);
        patient.setDate(date);
        patient.setName(name);
        patient.setTreatment(treatment);
        patient.setGender(gender);
        patient.setAge(age);
        patient.setDiagnosis(diagnosis);
        patient.setComplain(complain);
        patient.setPhone_no(phone_no);
        patient.setRemarks(remarks);
        patient.setAttendant(attendant);
        patient.setCampus(campus);

        databaseReference.push().setValue(patient).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                // after adding this data we are showing toast message.
                Toast.makeText(Medics.this, "Patient added", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // we are displaying a failure toast message.
                Toast.makeText(Medics.this, "Failed to add patient \n" + e, Toast.LENGTH_SHORT).show();
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