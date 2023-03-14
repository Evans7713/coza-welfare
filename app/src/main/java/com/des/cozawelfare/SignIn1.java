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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class SignIn1 extends AppCompatActivity {

    private static String campus,date,attendant;
    private static String selectCampus;
    private DatePickerDialog picker;
    private Button loginBt;
    private EditText userEdT,passwordEdT,attendantEdT,dateEdT;
    private static Spinner campusSpinner;
    public static Spinner getCampusSpinner(){return campusSpinner;}
    public static void setCampusSpinner(Spinner campusSpinner){
       campusSpinner.setSelection(0);
    }
    public static String getCampus() {
        return campus;
    }
    public static String getAttendant() {
        return attendant;
    }
    public static String getDate() {
        return date;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in1);

        getSupportActionBar().setTitle("Sign In");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF737070));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userEdT = (EditText)findViewById(R.id.user);
        passwordEdT = (EditText)findViewById(R.id.password);
        loginBt = (Button)findViewById(R.id.login);
        attendantEdT = (EditText)findViewById(R.id.attendant);
        dateEdT = (EditText)findViewById(R.id.date);
        campusSpinner = (Spinner) findViewById(R.id.campusSpinner);
        dateEdT.setInputType(InputType.TYPE_NULL);

        dateEdT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(SignIn1.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                dateEdT.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        campusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                // Get Selected gender from the list
                campus = parent.getItemAtPosition(position).toString().trim();
                selectCampus = parent.getItemAtPosition(0).toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // can leave this empty112
            }
        });


        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attendant = attendantEdT.getText().toString().trim();
                date = dateEdT.getText().toString().trim();
                if(userEdT.getText().toString().trim().equals("childcare") &&
                        passwordEdT.getText().toString().equals("1999") && campus != selectCampus) {
                    // display it as Toast to the user
                    if (TextUtils.isEmpty(attendant)) {
                        attendantEdT.setError("Please enter your Name as attendant");
                    } else if (TextUtils.isEmpty(date)) {
                        dateEdT.setError("Please enter date");
                    }else {
                    Intent intent1 = new Intent(SignIn1.this, Records.class);
                    startActivity(intent1);

                    passwordEdT.setText("");
                    userEdT.setText("");
                    attendantEdT.setText("");
                    dateEdT.setText("");
                    }

                } else if(userEdT.getText().toString().trim().equals("corewelfare") &&
                        passwordEdT.getText().toString().equals("1999") && campus != selectCampus){
                    // display it as Toast to the user
                    if (TextUtils.isEmpty(attendant)) {
                        attendantEdT.setError("Please enter your Name as attendant");
                    } else if (TextUtils.isEmpty(date)) {
                        dateEdT.setError("Please enter date");
                    }else {
                        Intent intent2 = new Intent(SignIn1.this, CaseRecords.class);
                        startActivity(intent2);

                        passwordEdT.setText("");
                        userEdT.setText("");
                        attendantEdT.setText("");
                        dateEdT.setText("");
                    }

                } else if(userEdT.getText().toString().trim().equals("evangelism") &&
                        passwordEdT.getText().toString().equals("1999") && campus != selectCampus){
                    // display it as Toast to the user
                    if (TextUtils.isEmpty(attendant)) {
                        attendantEdT.setError("Please enter your Name as attendant");
                    } else if (TextUtils.isEmpty(date)) {
                        dateEdT.setError("Please enter date");
                    }else {
                        Intent intent3 = new Intent(SignIn1.this, FirstTimerRecords.class);
                        startActivity(intent3);

                        passwordEdT.setText("");
                        userEdT.setText("");
                        attendantEdT.setText("");
                        dateEdT.setText("");
                    }

                } else if(userEdT.getText().toString().trim().equals("medics") &&
                        passwordEdT.getText().toString().equals("1999")&& campus != selectCampus){
                    // display it as Toast to the user
                    if (TextUtils.isEmpty(attendant)) {
                        attendantEdT.setError("Please enter your Name as attendant");
                    } else if (TextUtils.isEmpty(date)) {
                        dateEdT.setError("Please enter date");
                    }else {
                        Intent intent4 = new Intent(SignIn1.this, PatientsRecords.class);
                        startActivity(intent4);

                        passwordEdT.setText("");
                        userEdT.setText("");
                        attendantEdT.setText("");
                        dateEdT.setText("");
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter correct credentials and fill all fields",Toast.LENGTH_SHORT).show();
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