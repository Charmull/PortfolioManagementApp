package com.example.portfoliomanagementapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class SpecUpload extends AppCompatActivity {

    // 사진 관련
    private final int GALLERY_CODE = 10;
    ImageView specImg;
    private FirebaseStorage storage;
    Uri imgFile;

    // 카테고리 관련
    RadioGroup radioGroup;
    private String categoryInput;

    // 스펙 제목 관련
    EditText specTitle;

    // 스펙 설명 관련
    EditText specDesc;

    // firebase db
    private FirebaseDatabase database;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spec_upload);

        // 사진 관련
        specImg = (ImageView) findViewById(R.id.specImg);
        specImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.specImg:
                        loadAlbum();
                        break;
                }
            }
        });
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        // 카테고리 관련
        radioGroup = (RadioGroup) findViewById(R.id.selectCategory);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.selectCertificate:
                        categoryInput = "certificate";
                        break;
                    case R.id.selectDegree:
                        categoryInput = "degree";
                        break;
                    case R.id.selectVolunteer:
                        categoryInput = "volunteer";
                        break;
                    case R.id.selectGrade:
                        categoryInput = "grade";
                        break;
                    case R.id.selectAward:
                        categoryInput = "award";
                        break;
                    case R.id.selectEtc:
                        categoryInput = "etc";
                        break;
                }
            }
        });

        // 스펙 제목 관련
        specTitle = (EditText) findViewById(R.id.specTitle);

        // 스펙 설명 관련
        specDesc = (EditText) findViewById(R.id.specDesc);
    }

    private void loadAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE) {
            imgFile = data.getData();

            try {
                InputStream in = getContentResolver().openInputStream(data.getData());
                Bitmap specBit = BitmapFactory.decodeStream(in);
                in.close();
                specImg.setImageBitmap(specBit);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addSpecBtnHandler(View v) {
        switch (v.getId()) {
            case R.id.addSpecBtn:
                String title = specTitle.getText().toString();
                String desc = specDesc.getText().toString();
                double id = Math.random();

                // firebase storage
                StorageReference storageRef = storage.getReference();
                StorageReference riversRef = storageRef.child("spec/"+id+"/img.png");
                UploadTask uploadTask = riversRef.putFile(imgFile);


                // firebase Realtime Database에 정보 저장하기
                SpecDTO specDTO = new SpecDTO();
                specDTO.id = id;
                specDTO.title = title;
                specDTO.desc = desc;
                specDTO.category = categoryInput;

                final boolean[] flag = {false};
                // category따라 경로
                switch (categoryInput) {
                    case "certificate":
                        dbRef.child("certificate").push().setValue(specDTO);
                        Toast.makeText(SpecUpload.this, "specDTO 넣음!", Toast.LENGTH_LONG).show();

                        dbRef.child("certificate_prev").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.getValue(Integer.class) != null && flag[0] == false) {
                                    int num = (int) snapshot.getValue(Integer.class);
                                    num = num + 1;
                                    dbRef.child("certificate_prev").setValue(num);
                                    flag[0] = true;
                                    Toast.makeText(SpecUpload.this, "num 읽어와서 넣음!", Toast.LENGTH_LONG).show();
                                }
                                else if (flag[0] == false) {
                                    dbRef.child("certificate_prev").setValue(1);
                                    flag[0] = true;
                                    Toast.makeText(SpecUpload.this, "넣음!", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // getting failed
                            }
                        });
                        break;
                    case "degree":
                        dbRef.child("degree").push().setValue(specDTO);

                        dbRef.child("degree_prev").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.getValue(Integer.class) != null && flag[0] == false) {
                                    int num = (int) snapshot.getValue(Integer.class);
                                    num = num + 1;
                                    dbRef.child("degree_prev").setValue(num);
                                    flag[0] = true;
                                }
                                else if (flag[0] == false) {
                                    dbRef.child("degree_prev").setValue(1);
                                    flag[0] = true;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // getting failed
                            }
                        });
                        break;
                    case "volunteer":
                        dbRef.child("volunteer").push().setValue(specDTO);

                        dbRef.child("volunteer_prev").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.getValue(Integer.class) != null && flag[0] == false) {
                                    int num = (int) snapshot.getValue(Integer.class);
                                    num = num + 1;
                                    dbRef.child("volunteer_prev").setValue(num);
                                    flag[0] = true;
                                }
                                else if (flag[0] == false) {
                                    dbRef.child("volunteer_prev").setValue(1);
                                    flag[0] = true;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // getting failed
                            }
                        });
                        break;
                    case "grade":
                        dbRef.child("grade").push().setValue(specDTO);

                        dbRef.child("grade_prev").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.getValue(Integer.class) != null && flag[0] == false) {
                                    int num = (int) snapshot.getValue(Integer.class);
                                    num = num + 1;
                                    dbRef.child("grade_prev").setValue(num);
                                    flag[0] = true;
                                }
                                else if (flag[0] == false) {
                                    dbRef.child("grade_prev").setValue(1);
                                    flag[0] = true;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // getting failed
                            }
                        });
                        break;
                    case "award":
                        dbRef.child("award").push().setValue(specDTO);

                        dbRef.child("award_prev").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.getValue(Integer.class) != null && flag[0] == false) {
                                    int num = (int) snapshot.getValue(Integer.class);
                                    num = num + 1;
                                    dbRef.child("award_prev").setValue(num);
                                    flag[0] = true;
                                }
                                else if (flag[0] == false) {
                                    dbRef.child("award_prev").setValue(1);
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
                        dbRef.child("etc").push().setValue(specDTO);

                        dbRef.child("etc_prev").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.getValue(Integer.class) != null && flag[0] == false) {
                                    int num = (int) snapshot.getValue(Integer.class);
                                    num = num + 1;
                                    dbRef.child("etc_prev").setValue(num);
                                    flag[0] = true;
                                }
                                else if (flag[0] == false) {
                                    dbRef.child("etc_prev").setValue(1);
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
//                dbRef.child("spec").push().setValue(specDTO);
                finish();
        }
    }

}