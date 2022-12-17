package com.example.portfoliomanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    // firebase db
    private FirebaseDatabase database;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    // certificate
    private TextView certificateInfo;
    private ImageView certificateImg;

    // degree
    private TextView degreeInfo;
    private ImageView degreeImg;

    // volunteer
    private TextView volunteerInfo;
    private ImageView volunteerImg;

    // grade
    private TextView gradeInfo;
    private ImageView gradeImg;

    // award
    private TextView awardInfo;
    private ImageView awardImg;

    // etc
    private TextView etcInfo;
    private ImageView etcImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        certificateInfo = (TextView) findViewById(R.id.certificate_info);
        degreeInfo = (TextView) findViewById(R.id.degree_info);
        volunteerInfo = (TextView) findViewById(R.id.volunteer_info);
        gradeInfo = (TextView) findViewById(R.id.grade_info);
        awardInfo = (TextView) findViewById(R.id.award_info);
        etcInfo = (TextView) findViewById(R.id.etc_info);
        certificateImg = (ImageView) findViewById(R.id.certificate);
        degreeImg = (ImageView) findViewById(R.id.degree);
        volunteerImg = (ImageView) findViewById(R.id.volunteer);
        gradeImg = (ImageView) findViewById(R.id.grade);
        awardImg = (ImageView) findViewById(R.id.award);
        etcImg = (ImageView) findViewById(R.id.etc);

        // certificateInfo setting
        dbRef.child("certificate_prev").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(Integer.class) != null) {
                    int num = (int) snapshot.getValue(Integer.class);
                    certificateInfo.setText(String.valueOf(num) + "개 보유");
                }
                else {
                    int num = 0;
                    certificateInfo.setText(String.valueOf(num) + "개 보유");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // getting failed
            }
        });

        // degreeInfo setting
        dbRef.child("degree_prev").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(String.class) != null) {
                    String value = snapshot.getValue(String.class);
                    degreeInfo.setText(value);
                }
                else {
                    degreeInfo.setText("등록 학위 없음");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // getting failed
            }
        });

        // volunteerInfo setting
        dbRef.child("volunteer_prev").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(Integer.class) != null) {
                    int time = (int) snapshot.getValue(Integer.class);
                    volunteerInfo.setText(String.valueOf(time) + "시간");
                }
                else {
                    volunteerInfo.setText("0시간");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // getting failed
            }
        });

        // gradeInfo setting
        dbRef.child("grade_prev").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(Double.class) != null) {
                    Double value = (Double) snapshot.getValue(Double.class);
                    gradeInfo.setText("직전학기 " + String.valueOf(value) + "/4.5");
                }
                else {
                    gradeInfo.setText("직전학기 0.0/4.5");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // getting failed
            }
        });

        // awardInfo setting
        dbRef.child("award_prev").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(Integer.class) != null) {
                    int time = (int) snapshot.getValue(Integer.class);
                    awardInfo.setText(String.valueOf(time) + "회");
                }
                else {
                    awardInfo.setText("0회");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // getting failed
            }
        });

        // etcInfo setting
        dbRef.child("etc_prev").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(Integer.class) != null) {
                    int time = (int) snapshot.getValue(Integer.class);
                    etcInfo.setText(String.valueOf(time) + "회 참여");
                }
                else {
                    etcInfo.setText("0회 참여");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // getting failed
            }
        });


        // certificateImg onClick
        certificateImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SpecList.class);
                intent.putExtra("category", "certificate");
                startActivity(intent);
            }
        });

        // degreeImg onClick
        degreeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SpecList.class);
                intent.putExtra("category", "degree");
                startActivity(intent);
            }
        });

        // volunteerImg onClick
        volunteerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SpecList.class);
                intent.putExtra("category", "volunteer");
                startActivity(intent);
            }
        });

        // gradeImg onClick
        gradeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SpecList.class);
                intent.putExtra("category", "grade");
                startActivity(intent);
            }
        });

        // awardImg onClick
        awardImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SpecList.class);
                intent.putExtra("category", "award");
                startActivity(intent);
            }
        });

        // etcImg onClick
        etcImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SpecList.class);
                intent.putExtra("category", "etc");
                startActivity(intent);
            }
        });
    }

    public void addSpecHandler(View v) {
        switch (v.getId()) {
            case R.id.addSpec:
                Intent intent = new Intent(getApplicationContext(), SpecUpload.class);
                startActivity(intent);
        }
    }

    public void briefProfileHandler(View v) {
        switch (v.getId()) {
            case R.id.briefProfile:
                Intent intent = new Intent(getApplicationContext(), BriefProfile.class);
                startActivity(intent);
        }
    }
}