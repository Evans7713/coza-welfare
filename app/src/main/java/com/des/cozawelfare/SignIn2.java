package com.des.cozawelfare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class SignIn2 extends AppCompatActivity {
    private static String date;
    private Button loginBt;
    private EditText userEdT,passwordEdT,dateEdt;
    private DatePickerDialog picker;
    public static String getDate() {
        return date;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in2);
        getSupportActionBar().setTitle("Sign In");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF737070));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        userEdT = (EditText)findViewById(R.id.user);
        passwordEdT = (EditText)findViewById(R.id.password);
        loginBt = (Button)findViewById(R.id.login);
        dateEdt = findViewById(R.id.date);
        dateEdt.setInputType(InputType.TYPE_NULL);

        dateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(SignIn2.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                dateEdt.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = dateEdt.getText().toString();
                if (userEdT.getText().toString().trim().equals("admin") &&
                        passwordEdT.getText().toString().equals("1999")) {
                    if (TextUtils.isEmpty(date)) {
                        dateEdt.setError("Please select date");
                    }
                    else{Intent intent = new Intent(SignIn2.this, MembersRecords.class);
                        startActivity(intent);
                        passwordEdT.setText("");
                        userEdT.setText("");
                        dateEdt.setText("");
                    }

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Wrong Credentials", Toast.LENGTH_SHORT).show();
                }
            }


        });


    }

    @Override
    public void onBackPressed() {
        finish();
    }
    // this event will enable the back
    // function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}