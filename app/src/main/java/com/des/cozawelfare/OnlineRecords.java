package com.des.cozawelfare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class OnlineRecords extends AppCompatActivity {
    private RecyclerView requestRV;
    private ArrayList<Request> requestArrayList;
    private OnlineCaseRVAdapter onlineCaseRVAdapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_records);
        getSupportActionBar().setTitle("E-Records");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF737070));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // initializing our variable for firebase
        // firestore and getting its instance.
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Online Request");

        // creating our new array list

        fillOnlineCaseList();
    }

    private void fillOnlineCaseList () {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestArrayList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Request request = dataSnapshot.getValue(Request.class);
                    request.setId(dataSnapshot.getKey());
                    requestArrayList.add(request);
                }
                requestRV = findViewById(R.id.idRVERecords);

                requestRV.setHasFixedSize(true);
                requestRV.setLayoutManager(new LinearLayoutManager(OnlineRecords.this));

                // adding our array list to our recycler view adapter class.
                onlineCaseRVAdapter = new OnlineCaseRVAdapter(requestArrayList, OnlineRecords.this);

                // setting adapter to our recycler view.
                requestRV.setAdapter(onlineCaseRVAdapter);

                onlineCaseRVAdapter.notifyDataSetChanged();

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
                onlineCaseRVAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }
    // methods to control the operations that will
    // happen when user clicks on the action buttons
    @Override
    public boolean onOptionsItemSelected (@NonNull MenuItem item ){

        switch (item.getItemId()) {
            case R.id.logout:
                this.finish();
                return true;
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed () {
        finish();
    }


}