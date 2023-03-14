package com.des.cozawelfare;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.graphics.drawable.ColorDrawable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;


public class MainActivity2 extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Button help, admin;
    private ImageButton childCare, coreWelfare, evangelism, medics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        getSupportActionBar().setDisplayShowTitleEnabled(false);


        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF2D0D67));

        help = (Button) findViewById(R.id.requestHelp);
        admin = (Button) findViewById(R.id.admin);
        childCare = (ImageButton) findViewById(R.id.childCareButton);
        coreWelfare = (ImageButton) findViewById(R.id.coreWelfareButton);
        evangelism = (ImageButton) findViewById(R.id.evangelismButton);
        medics = (ImageButton) findViewById(R.id.medicsButton);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.nav_exit).setOnMenuItemClickListener(menuItem -> {
            new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Closing App").setMessage("Are you sure you want to close this app?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity2.this.finish();
                            System.exit(0);
                            // on below line we are exiting our activity
                            System.exit(0);
                            Toast.makeText(MainActivity2.this, "App closed",Toast.LENGTH_SHORT).show();
                        }
                    }).setNegativeButton("No", null).show();

            return true;
        });

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent int1 = new Intent(MainActivity2.this,RequestHelp.class);
                startActivity(int1);
            }
        });
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent int2 = new Intent(MainActivity2.this,SignIn2.class);
                startActivity(int2);
            }
        });
        childCare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent int3 = new Intent(MainActivity2.this,SignIn1.class);
                startActivity(int3);
            }
        });
        coreWelfare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent int4 = new Intent(MainActivity2.this,SignIn1.class);
                startActivity(int4);
            }
        });
        evangelism.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent int5 = new Intent(MainActivity2.this,SignIn1.class);
                startActivity(int5);
            }
        });
        medics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent int6 = new Intent(MainActivity2.this,SignIn1.class);
                startActivity(int6);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing App").setMessage("Are you sure you want to close this app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity2.this.finish();
                        // on below line we are exiting our activity
                        System.exit(0);
                        Toast.makeText(MainActivity2.this, "App closed",Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("No", null).show();
    }

}