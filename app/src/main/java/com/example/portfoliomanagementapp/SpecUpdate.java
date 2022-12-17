package com.example.portfoliomanagementapp;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

public class SpecUpdate extends AppCompatActivity {

    // 사진 관련
    private final int GALLERY_CODE = 10;
    private FirebaseStorage storage;
    ImageView specImg;
    Uri imgFile;

    // firebase db
    private FirebaseDatabase database;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    // intent id, category
    private Double id;
    private String specDTOCategory;

    // 카테고리 관련
    RadioGroup radioGroup;
    private String categoryInput;
    private RadioButton selectCertificate;
    private RadioButton selectDegree;
    private RadioButton selectVolunteer;
    private RadioButton selectGrade;
    private RadioButton selectAward;
    private RadioButton selectEtc;

    // 스펙 제목 관련
    EditText specTitle;

    // 스펙 설명 관련
    EditText specDesc;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spec_update);

        Intent intent = getIntent();
        // spec id
        String specDTOId = intent.getStringExtra("specDTO_Id");
        id = Double.parseDouble(specDTOId);
        // spec category
        specDTOCategory = intent.getStringExtra("specDTO_category");

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

        // firebase storage
        StorageReference storageRef = storage.getInstance().getReference();
        StorageReference riversRef = storageRef.child("spec/"+id+"/img.png");
        if (riversRef == null) {
            Toast.makeText(SpecUpdate.this, "해당 스펙에 대한 이미지가 사라졌습니다.", Toast.LENGTH_LONG).show();
        }
        else {
            StorageReference submitImg = storageRef.child("spec/"+id+"/img.png");
            submitImg.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(SpecUpdate.this).load(uri).into(specImg);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SpecUpdate.this, "failure", Toast.LENGTH_LONG).show();
                }
            });
        }

        // 카테고리 관련
        radioGroup = (RadioGroup) findViewById(R.id.selectCategory);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
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
        switch (specDTOCategory) {
            case "certificate":
                selectCertificate = (RadioButton) findViewById(R.id.selectCertificate);
                selectCertificate.setChecked(true);
                categoryInput = "certificate";
                break;
            case "degree":
                selectDegree = (RadioButton) findViewById(R.id.selectDegree);
                selectDegree.setChecked(true);
                categoryInput = "degree";
                break;
            case "volunteer":
                selectVolunteer = (RadioButton) findViewById(R.id.selectVolunteer);
                selectVolunteer.setChecked(true);
                categoryInput = "volunteer";
                break;
            case "grade":
                selectGrade = (RadioButton) findViewById(R.id.selectGrade);
                selectGrade.setChecked(true);
                categoryInput = "grade";
                break;
            case "award":
                selectAward = (RadioButton) findViewById(R.id.selectAward);
                selectAward.setChecked(true);
                categoryInput = "award";
                break;
            case "etc":
                selectEtc = (RadioButton) findViewById(R.id.selectEtc);
                selectEtc.setChecked(true);
                categoryInput = "etc";
                break;
        }

        // 스펙 제목 관련
        specTitle = (EditText) findViewById(R.id.specTitle);

        // 스펙 설명 관련
        specDesc = (EditText) findViewById(R.id.specDesc);

        // 스펙 정보 세팅
        dbRef.child(specDTOCategory).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    SpecDTO specDTO = ds.getValue(SpecDTO.class);

                    if (Objects.equals(specDTO.id, id)) {
                        specTitle.setText(specDTO.title);
                        specDesc.setText(specDTO.desc);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // getting failed
            }
        });
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

    public void updateSpecBtnHandler(View v) {
        String title = specTitle.getText().toString();
        String desc = specDesc.getText().toString();

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
                dbRef.child("certificate").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            SpecDTO temp = ds.getValue(SpecDTO.class);

                            if (Objects.equals(temp.id, id)) {
                                dbRef.child(specDTOCategory).child(ds.getKey()).setValue(specDTO);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // getting failed
                    }
                });
                break;
            case "degree":
                dbRef.child("degree").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            SpecDTO temp = ds.getValue(SpecDTO.class);

                            if (Objects.equals(temp.id, id)) {
                                dbRef.child(specDTOCategory).child(ds.getKey()).setValue(specDTO);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // getting failed
                    }
                });

                dbRef.child("degree_prev").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue(String.class) != null && flag[0] == false) {
                            String value = specDTO.title;
                            dbRef.child("degree_prev").setValue(value);
                            flag[0] = true;
                        }
                        else if (flag[0] == false) {
                            String value = specDTO.title;
                            dbRef.child("degree_prev").setValue(value);
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
                dbRef.child("volunteer").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            SpecDTO temp = ds.getValue(SpecDTO.class);

                            if (Objects.equals(temp.id, id)) {
                                dbRef.child(specDTOCategory).child(ds.getKey()).setValue(specDTO);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // getting failed
                    }
                });

                dbRef.child("volunteer_prev").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue(Integer.class) != null && flag[0] == false) {
                            int num = (int) snapshot.getValue(Integer.class);
                            int addTime = Integer.parseInt(specDTO.desc);
                            num = num + addTime;
                            dbRef.child("volunteer_prev").setValue(num);
                            flag[0] = true;
                        }
                        else if (flag[0] == false) {
                            int time = Integer.parseInt(specDTO.desc);
                            dbRef.child("volunteer_prev").setValue(time);
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
                dbRef.child("grade").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            SpecDTO temp = ds.getValue(SpecDTO.class);

                            if (Objects.equals(temp.id, id)) {
                                dbRef.child(specDTOCategory).child(ds.getKey()).setValue(specDTO);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // getting failed
                    }
                });

                dbRef.child("grade_prev").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue(Double.class) != null && flag[0] == false) {
//                                    Double num = (Double) snapshot.getValue(Double.class);
                            Double gradeValue = Double.parseDouble(specDTO.desc);
                            dbRef.child("grade_prev").setValue(gradeValue);
                            flag[0] = true;
                        }
                        else if (flag[0] == false) {
                            Double gradeValue = Double.parseDouble(specDTO.desc);
                            dbRef.child("grade_prev").setValue(gradeValue);
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
                dbRef.child("award").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            SpecDTO temp = ds.getValue(SpecDTO.class);

                            if (Objects.equals(temp.id, id)) {
                                dbRef.child(specDTOCategory).child(ds.getKey()).setValue(specDTO);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // getting failed
                    }
                });
                break;
            case "etc":
                dbRef.child("etc").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            SpecDTO temp = ds.getValue(SpecDTO.class);

                            if (Objects.equals(temp.id, id)) {
                                dbRef.child(specDTOCategory).child(ds.getKey()).setValue(specDTO);
                            }
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