package com.example.portfoliomanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Objects;

public class BriefProfileUpdate extends AppCompatActivity {

    private String strDate;
    private int year;
    private int month;
    private int day;
    private EditText profileDesc;
    private TextView date;

    private Double id;
    private String profileDate;

    // firebase db
    private FirebaseDatabase database;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brief_profile_update);

        Intent intent = getIntent();
        // id
        String profileDTOId = intent.getStringExtra("profileDTO_Id");
        id = Double.parseDouble(profileDTOId);
        // date
        profileDate = intent.getStringExtra("profileDTO_date");

        profileDesc = (EditText) findViewById(R.id.profileDesc);
        date = (TextView) findViewById(R.id.date);

        dbRef.child("profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ProfileDTO profileDTO = ds.getValue(ProfileDTO.class);

                    if (Objects.equals(profileDTO.id, id)) {
                        strDate = String.valueOf(profileDTO.date);
                        date.setText(strDate);
                        profileDesc.setText(String.valueOf(profileDTO.desc));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // getting failed
            }
        });
    }

    private void updateDate() {
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

    public void updateProfileHandler(View v) {
        switch (v.getId()) {
            case R.id.updateProfileBtn:
                String desc = profileDesc.getText().toString();

                ProfileDTO profileDTO = new ProfileDTO();
                profileDTO.id = id;
                profileDTO.date = strDate;
                profileDTO.desc = desc;

                final boolean[] flag = {false};
                dbRef.child("profile").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ProfileDTO temp = ds.getValue(ProfileDTO.class);

                            if (Objects.equals(temp.id, id) && flag[0] == false) {
                                dbRef.child("profile").child(ds.getKey()).setValue(profileDTO);
                                flag[0] = true;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // getting failed
                    }
                });
                finish();
                break;
        }
    }
}