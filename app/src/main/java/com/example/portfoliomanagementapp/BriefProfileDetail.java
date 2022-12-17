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

public class BriefProfileDetail extends AppCompatActivity {

    private String strDate;
    private int year;
    private int month;
    private int day;
    private TextView profileDesc;
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
        setContentView(R.layout.activity_brief_profile_detail);

        profileDesc = (TextView) findViewById(R.id.profileDesc);
        date = (TextView) findViewById(R.id.date);

        Intent intent = getIntent();
        // id
        String profileDTOId = intent.getStringExtra("profileDTO_Id");
        id = Double.parseDouble(profileDTOId);
        // category
        profileDate = intent.getStringExtra("profileDTO_date");

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


    public void backBtnHandler(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                finish();
        }
    }

    public void delProfileHandler(View v) {
        switch (v.getId()) {
            case R.id.delProfileBtn:
                dbRef.child("profile").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ProfileDTO profileDTO = ds.getValue(ProfileDTO.class);

                            if (Objects.equals(profileDTO.id, id)) {
                                dbRef.child("profile").child(ds.getKey()).removeValue();
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

    public void updateProfileHandler(View v) {
        switch (v.getId()) {
            case R.id.updateProfileBtn:
                Intent intent = new Intent(getApplicationContext(), BriefProfileUpdate.class);
                intent.putExtra("profileDTO_Id", String.valueOf(id));
                intent.putExtra("profileDTO_date", profileDate);
                startActivity(intent);
        }
    }
}