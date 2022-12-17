package com.example.portfoliomanagementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddBriefProfile extends AppCompatActivity {

    private String strDate;
    private int year = 2022;
    private int month = 5;
    private int day = 17;
    private EditText profileDesc;

    // firebase db
    private FirebaseDatabase database;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_brief_profile);

        profileDesc = (EditText) findViewById(R.id.profileDesc);
    }

    private void updateDate() {
        TextView date = (TextView) findViewById(R.id.date);
        date.setText(String.format("%d-%d-%d", year, month, day));
    }

    public void mOnClick(View v) {
        Calendar c = Calendar.getInstance();
        switch (v.getId()) {
            case R.id.btnSelectDate:
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(this, mDateSetListener, year, month, day).show();
                break;
        }
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int y, int m, int dayOfMonth) {
            strDate = String.format("%d-%d-%d", y, m+1, dayOfMonth);
            year = y;
            month = m + 1;
            day = dayOfMonth;
            updateDate();
        }
    };

    public void addProfileHandler(View v) {
        switch (v.getId()) {
            case R.id.addProfileBtn:
                String desc = profileDesc.getText().toString();
                double id = Math.random();

                ProfileDTO profileDTO = new ProfileDTO();
                profileDTO.id = id;
                profileDTO.date = strDate;
                profileDTO.desc = desc;

                dbRef.child("profile").push().setValue(profileDTO);
                finish();
                break;
        }
    }
}