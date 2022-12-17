package com.example.portfoliomanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
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

    // intent id, category
    private Double id;
    private String specDTOCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spec_detail);

        Intent intent = getIntent();
        // spec id
        String specDTOId = intent.getStringExtra("specDTO_Id");
        id = Double.parseDouble(specDTOId);
        // spec category
        specDTOCategory = intent.getStringExtra("specDTO_category");

        // 스펙 이미지 세팅
        specImg = (ImageView) findViewById(R.id.specImg);

        // firebase storage
        StorageReference storageRef = storage.getInstance().getReference();
        StorageReference riversRef = storageRef.child("spec/"+id+"/img.png");
        if (riversRef == null) {
            Toast.makeText(SpecDetail.this, "해당 스펙에 대한 이미지가 사라졌습니다.", Toast.LENGTH_LONG).show();
        }
        else {
            StorageReference submitImg = storageRef.child("spec/"+id+"/img.png");
            submitImg.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(SpecDetail.this).load(uri).into(specImg);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SpecDetail.this, "failure", Toast.LENGTH_LONG).show();
                }
            });
        }


        // 스펙 정보 세팅
        category = (TextView) findViewById(R.id.category);
        specTitle = (TextView) findViewById(R.id.specTitle);
        specDesc = (TextView) findViewById(R.id.specDesc);

        dbRef.child(specDTOCategory).addValueEventListener(new ValueEventListener() {
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

    public void delSpecBtnHandler(View view) {
        // firebase storage
        StorageReference storageRef = storage.getInstance().getReference();
        StorageReference riversRef = storageRef.child("spec/"+id+"/img.png");
        if (riversRef == null) {
//            Toast.makeText(SpecDetail.this, "해당 스펙에 대한 이미지가 사라졌습니다.", Toast.LENGTH_LONG).show();
        }
        else {
            StorageReference submitImg = storageRef.child("spec/"+id+"/img.png");
            submitImg.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        }

        // firebase db
        dbRef.child(specDTOCategory).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    SpecDTO specDTO = ds.getValue(SpecDTO.class);

                    if (Objects.equals(specDTO.id, id)) {
                        dbRef.child(specDTOCategory).child(ds.getKey()).removeValue();
                    }
            }
        }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // getting failed
            }
        });

        final boolean[] flag = {false};

        // category따라 경로
        switch (specDTOCategory) {
            case "certificate":
                dbRef.child("certificate_prev").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue(Integer.class) != null && flag[0] == false) {
                            int num = (int) snapshot.getValue(Integer.class);
                            num = num - 1;
                            dbRef.child(specDTOCategory + "_prev").setValue(num);
                            flag[0] = true;
                        }
                        else if (flag[0] == false) {
                            dbRef.child(specDTOCategory + "_prev").setValue(0);
                            flag[0] = true;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // getting failed
                    }
                });
                break;
            case "degree":
                dbRef.child(specDTOCategory).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            SpecDTO specDTO = ds.getValue(SpecDTO.class);
                            Toast.makeText(SpecDetail.this, "수정정", Toast.LENGTH_LONG).show();

                            dbRef.child("degree_prev").setValue(specDTO.title);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // getting failed
                    }
                });
                break;
            case "volunteer":
                dbRef.child(specDTOCategory).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int num = 0;
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            SpecDTO specDTO = ds.getValue(SpecDTO.class);

                            num = num + Integer.parseInt(specDTO.desc);
                            dbRef.child("volunteer_prev").setValue(num);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // getting failed
                    }
                });
                break;
            case "grade":
                dbRef.child(specDTOCategory).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Double num = 0.0;
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            SpecDTO specDTO = ds.getValue(SpecDTO.class);

                            num = Double.parseDouble(specDTO.desc);
                            dbRef.child("grade_prev").setValue(num);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // getting failed
                    }
                });
                break;
            case "award":
                dbRef.child("award_prev").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue(Integer.class) != null && flag[0] == false) {
                            int num = (int) snapshot.getValue(Integer.class);
                            num = num - 1;
                            dbRef.child(specDTOCategory + "_prev").setValue(num);
                            flag[0] = true;
                        }
                        else if (flag[0] == false) {
                            dbRef.child(specDTOCategory + "_prev").setValue(0);
                            flag[0] = true;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // getting failed
                    }
                });
                break;
            case "etc":
                dbRef.child("etc_prev").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue(Integer.class) != null && flag[0] == false) {
                            int num = (int) snapshot.getValue(Integer.class);
                            num = num - 1;
                            dbRef.child(specDTOCategory + "_prev").setValue(num);
                            flag[0] = true;
                        }
                        else if (flag[0] == false) {
                            dbRef.child(specDTOCategory + "_prev").setValue(0);
                            flag[0] = true;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // getting failed
                    }
                });
                break;
        }

        finish();
    }

    public void backBtnHandler(View view) {
        finish();
    }
}