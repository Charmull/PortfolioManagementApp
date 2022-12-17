package com.example.portfoliomanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class SpecDetail extends AppCompatActivity {

    // 사진 관련
    private final int GALLERY_CODE = 10;
    private FirebaseStorage storage;
    Uri imgFile;

    // firebase db
    private FirebaseDatabase database;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    private ImageView specImg;
    private TextView category;
    private TextView specTitle;
    private TextView specDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spec_detail);

        Intent intent = getIntent();
        String specDTOId = intent.getStringExtra("specDTO_Id");
        Double id = Double.parseDouble(specDTOId);

        // 스펙 이미지 세팅
        specImg = (ImageView) findViewById(R.id.specImg);

//        // firebase storage
//        StorageReference storageRef = storage.getReference();
//        StorageReference riversRef = storageRef.child("spec/"+id);
//        if (riversRef == null) {
//            Toast.makeText(SpecDetail.this, "없졍", Toast.LENGTH_LONG).show();
//        }
//        else {
//            StorageReference submitImg = storageRef.child("spec/"+id+"/img.png");
//            submitImg.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                @Override
//                public void onSuccess(Uri uri) {
//                    Glide.with
//                }
//            })
//        }


        // 스펙 정보 세팅
        category = (TextView) findViewById(R.id.category);
        specTitle = (TextView) findViewById(R.id.specTitle);
        specDesc = (TextView) findViewById(R.id.specDesc);

        dbRef.child("certificate").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    SpecDTO specDTO = ds.getValue(SpecDTO.class);

                    if (Objects.equals(specDTO.id, id)) {
                        switch (specDTO.category) {
                            case "certificate":
                                category.setText("자격증");
                                specTitle.setText(specDTO.title);
                                specDesc.setText(specDTO.desc);
                                break;
                            case "degree":
                                category.setText("학위");
                                specTitle.setText(specDTO.title);
                                specDesc.setText(specDTO.desc);
                                break;
                            case "volunteer":
                                category.setText("봉사활동");
                                specTitle.setText(specDTO.title);
                                specDesc.setText(specDTO.desc);
                                break;
                            case "grade":
                                category.setText("학점");
                                specTitle.setText(specDTO.title);
                                specDesc.setText(specDTO.desc);
                                break;
                            case "award":
                                category.setText("입상");
                                specTitle.setText(specDTO.title);
                                specDesc.setText(specDTO.desc);
                                break;
                            case "etc":
                                category.setText("대외활동");
                                specTitle.setText(specDTO.title);
                                specDesc.setText(specDTO.desc);
                                break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // getting failed
            }
        });
    }
}