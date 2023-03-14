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
import android.widget.SearchView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MembersRecords extends AppCompatActivity {
    private RecyclerView membersRV;
    private ArrayList<Members> membersArrayList;
    private MembersRVAdapter membersRVAdapter;
    private FloatingActionButton floatingActionButton;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members_records);
        getSupportActionBar().setTitle("Members Records");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF737070));
        // initializing our variables.
        floatingActionButton = findViewById(R.id.floatingActionButton);
        // initializing our variable for firebase
        // firestore and getting its instance.
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Members Data");
        // creating our new array list
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Contact.class));
            }
        });
        fillMembersList();

    }
    private void fillMembersList() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                membersArrayList = new ArrayList<>();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Members members = dataSnapshot.getValue(Members.class);
                    members.setId(dataSnapshot.getKey());
                    membersArrayList.add(members);
                }
                membersRV = findViewById(R.id.idRVMRecords);

                membersRV.setHasFixedSize(true);

                membersRVAdapter = new MembersRVAdapter(membersArrayList, MembersRecords.this);

                membersRV.setLayoutManager(new LinearLayoutManager(MembersRecords.this));

                membersRV.setAdapter(membersRVAdapter);

                membersRVAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.contact_bar,menu);
        MenuItem menuItem=menu.findItem(R.id.search);
        SearchView searchView=(SearchView)menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                membersRVAdapter.getFilter().filter(s);
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
            case R.id.eRecords:
                Toast.makeText(this, "E-Records Clicked", Toast.LENGTH_SHORT).show();
                // opening a new activity on button click
                Intent e = new Intent(MembersRecords.this,OnlineRecords.class);
                startActivity(e);
                break;
            case R.id.logout:
                this.finish();
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
                        MembersRecords.this.finish();
                    }
                }).setNegativeButton("No", null).show();
    }

}