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

                // firebase database
                SpecDTO specDTO = new SpecDTO();
                specDTO.id = id;
                specDTO.title = title;
                specDTO.desc = desc;
                specDTO.category = categoryInput;
                dbRef.child("spec").push().setValue(specDTO);
                finish();
        }
    }

}