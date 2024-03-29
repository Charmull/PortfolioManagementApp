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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SpecList extends AppCompatActivity {

    // firebase db
    private FirebaseDatabase database;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    // listView layout
    private LinearLayout listView;

    // back button
    private Button backBtn;

    // intent
    String category;

    private TextView title;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spec_list);

        Intent intent = getIntent();
        category = intent.getStringExtra("category");

        listView = (LinearLayout) findViewById(R.id.listView);
        listView.removeAllViews();
        backBtn = (Button) findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        title = (TextView) findViewById(R.id.title);

        switch (category) {
            case "certificate":
                title.setText("자격증 스펙 리스트");
                break;
            case "degree":
                title.setText("학위 스펙 리스트");
                break;
            case "volunteer":
                title.setText("봉사활동 스펙 리스트");
                break;
            case "grade":
                title.setText("학점 스펙 리스트");
                break;
            case "award":
                title.setText("입상 스펙 리스트");
                break;
            case "etc":
                title.setText("대외활동 스펙 리스트");
                break;
        }

        createList();
    }

    private void createList() {
        dbRef.child(category).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listView.removeAllViews();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    SpecDTO specDTO = ds.getValue(SpecDTO.class);
                    createSpec(specDTO);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // getting failed
            }
        });
    }

    private void createSpec(SpecDTO specDTO) {
        Drawable border = getResources().getDrawable(R.drawable.memo_border);
        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setBackground(border);
        // 레이아웃
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        param.leftMargin = 20;

        TextView title = new TextView(getApplicationContext());
        TextView desc = new TextView(getApplicationContext());
        title.setText(specDTO.title);
        title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
        title.setTypeface(null, Typeface.BOLD);
        title.setPadding(40, 40, 40, 40);
        title.setTextColor(Color.parseColor("#000000"));
        desc.setText(specDTO.desc);
        desc.setTextColor(Color.parseColor("#000000"));

        title.setLayoutParams(param);
        desc.setLayoutParams(param);
        layout.addView(title);
        layout.addView(desc);
        listView.addView(layout);

        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SpecDetail.class);
                intent.putExtra("specDTO_Id", String.valueOf(specDTO.id));
                intent.putExtra("specDTO_category", specDTO.category);
                startActivity(intent);
            }
        });
    }
}