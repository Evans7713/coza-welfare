package com.des.cozawelfare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PatientsRecords extends AppCompatActivity {
    // creating variables for our recycler view,
    // array list, adapter, firebase firestore
    // and our progress bar.
    private RecyclerView patientRV;
    private ArrayList<Patient> patientArrayList;
    private PatientsRVAdapter patientsRVAdapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients_records);
        getSupportActionBar().setTitle("Patients Records");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF737070));
        // initializing our variables.
        floatingActionButton = findViewById(R.id.floatingActionButton);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Patients");


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Medics.class));
            }
        });
        fillPatientsList();

    }
    private void fillPatientsList() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                patientArrayList = new ArrayList<>();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Patient patient = dataSnapshot.getValue(Patient.class);
                    patient.setId(dataSnapshot.getKey());
                    patientArrayList.add(patient);
                }
                patientRV = findViewById(R.id.idRVPatients);

                patientRV.setHasFixedSize(true);

                patientsRVAdapter = new PatientsRVAdapter(patientArrayList, PatientsRecords.this);

                patientRV.setLayoutManager(new LinearLayoutManager(PatientsRecords.this));

                patientRV.setAdapter(patientsRVAdapter);

                patientsRVAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.action_bar,menu);
        MenuItem menuItem=menu.findItem(R.id.search);
        SearchView searchView=(SearchView)menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                patientsRVAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    // methods to control the operations that will
    // happen when user clicks on the action buttons
    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {

        switch (item.getItemId()){
            case R.id.logout:
                this.finish();
                SignIn1.setCampusSpinner(SignIn1.getCampusSpinner());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Logging Out").setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PatientsRecords.this.finish();
                        SignIn1.setCampusSpinner(SignIn1.getCampusSpinner());
                    }
                }).setNegativeButton("No", null).show();
    }
}