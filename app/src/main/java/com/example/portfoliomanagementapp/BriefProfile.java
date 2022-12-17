package com.example.portfoliomanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BriefProfile extends AppCompatActivity {

    private Button addBriefProfile;

    // listView layout
    private LinearLayout listView;

    // firebase db
    private FirebaseDatabase database;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brief_profile);

        listView = (LinearLayout) findViewById(R.id.listView);
        listView.removeAllViews();

        createList();
    }

    private void createList() {
        dbRef.child("profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listView.removeAllViews();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ProfileDTO profileDTO = ds.getValue(ProfileDTO.class);
                    createSpec(profileDTO);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // getting failed
            }
        });
    }

    private void createSpec(ProfileDTO profileDTO) {
        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.HORIZONTAL);
        // 레이아웃
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        param.leftMargin = 20;

        TextView date = new TextView(getApplicationContext());
        TextView desc = new TextView(getApplicationContext());
        date.setText(profileDTO.date);
        date.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        date.setTypeface(null, Typeface.BOLD);
        date.setPadding(20, 20, 20, 20);
        date.setTextColor(Color.parseColor("#000000"));
        desc.setText(profileDTO.desc);
        desc.setTextColor(Color.parseColor("#000000"));

        date.setLayoutParams(param);
        desc.setLayoutParams(param);
        layout.addView(date);
        layout.addView(desc);
        listView.addView(layout);

        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BriefProfileDetail.class);
                intent.putExtra("profileDTO_Id", String.valueOf(profileDTO.id));
                intent.putExtra("profileDTO_date", profileDTO.date);
                startActivity(intent);
            }
        });
    }

    public void addBriefProfileHandler(View v) {
        switch (v.getId()) {
            case R.id.addProfileBtn:
                Intent intent = new Intent(getApplicationContext(), AddBriefProfile.class);
                startActivity(intent);
        }
    }

    public void backBtnHandler(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                finish();
        }
    }
}