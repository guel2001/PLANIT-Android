package com.example.projectgit.Login;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.projectgit.Activity.GalleryActivity;
import com.example.projectgit.CameraActivity.CameraActivity;
import com.example.projectgit.Constellation.SetConstellation;

import com.example.projectgit.Data.Memberinfo;
import com.example.projectgit.Frag.FragmentHome;
import com.example.projectgit.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;



public class MemberinitActivity extends AppCompatActivity {
    private static final String TAG = "MemberInitActivity";
    private ImageView profileImageView;
    private String profilePath;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_init);

        profileImageView = findViewById(R.id.profileimageView2);
        profileImageView.setOnClickListener(onClickListener);

        findViewById(R.id.checkButton2).setOnClickListener(onClickListener);
        findViewById(R.id.gallery2).setOnClickListener(onClickListener);
        findViewById(R.id.picture2).setOnClickListener(onClickListener);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();   //현재 액티비티 종료
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode) {
            case 0: {
                if (resultCode == Activity.RESULT_OK) {
                    profilePath = intent.getStringExtra("profilePath");
                    Glide.with(this).load(profilePath).centerCrop().override(500).into(profileImageView);

                }
                break;
            }
        }
    }

    View.OnClickListener onClickListener = v -> {
        switch (v.getId()) {
            case R.id.checkButton2:
                profileUpdate();
                break;
            case R.id.profileimageView2:
                CardView cardView = findViewById(R.id.buttonsCardView2);
                if (cardView.getVisibility() == View.VISIBLE) {
                    cardView.setVisibility(View.GONE);
                } else {
                    cardView.setVisibility(View.VISIBLE);
                }

                break;

            case R.id.picture2:
                myStartActivity(CameraActivity.class);
                break;
            case R.id.gallery2:

                if (ContextCompat.checkSelfPermission(MemberinitActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MemberinitActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            1);
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MemberinitActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    } else {
                        startToast("권한을 허용해 주세요.");
                    }
                } else {
                    myStartActivity(GalleryActivity.class);
                }
                break;
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    myStartActivity(GalleryActivity.class);
                } else {
                    startToast("권한을 허용해 주세요.");
                }
        }
    }



    private void profileUpdate() {
       final String name = ((EditText) findViewById(R.id.nameEditText2)).getText().toString();
       final String phoneNumber = ((EditText) findViewById(R.id.phoneNumberEditText2)).getText().toString();
       final String address = ((EditText) findViewById(R.id.edit_addr)).getText().toString();

        if (name.length() > 0 && phoneNumber.length() > 9 ) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

           user = FirebaseAuth.getInstance().getCurrentUser();
            final StorageReference mountainImagesRef = storageRef.child("users/"+user.getUid()+"/profileImage.jpg");


            if(profilePath == null){
                Memberinfo memberinfo = new Memberinfo(name, phoneNumber, 1, FragmentHome.finished_cons, user.getEmail(), user.getUid(),false,0,address,true,true);
                //Memberinfo memberinfo = new Memberinfo(name, phoneNumber, 1, FragmentHome.finished_cons,user.getEmail(),user.getUid(),address,true);

                uploader(memberinfo);

            }else {
                try {
                    InputStream stream = new FileInputStream(new File(profilePath));

                    UploadTask uploadTask = mountainImagesRef.putStream(stream);
                    uploadTask.continueWithTask(task -> {
                        if (!task.isSuccessful()) {

                            throw task.getException();

                        }
                        // Continue with the task to get the download URL
                        return mountainImagesRef.getDownloadUrl();
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {

                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                Memberinfo memberinfo = new Memberinfo(name, phoneNumber, 1,downloadUri.toString(),FragmentHome.finished_cons,user.getEmail(),user.getUid(),false,0,address,true,true);

                                uploader(memberinfo);

                            } else {
                                startToast("회원정보를 보내는데 실패했습니다.");
                            }
                        }
                    });
                } catch (FileNotFoundException e) {
                    Log.e("로그", "에러 : " + e.toString());
                }
            }
        } else {
            startToast("회원정보를 입력해주세요.");
        }
    }

    private void startToast(String msg){
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
    }

    private void uploader( Memberinfo memberinfo){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(user.getUid()).set(memberinfo)
                .addOnSuccessListener((OnSuccessListener) (aVoid) -> {

                    startToast("회원정보 등록을 성공하였습니다.");
                    SharedPreferences pref = getSharedPreferences("mine",MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("login", "true");
                    editor.commit();

                    myStartActivity(SetConstellation.class);
                })
                .addOnFailureListener((e) -> {
                    startToast("회원정보 등록에 실패하였습니다.");
                    Log.w(TAG, "Error writing document", e);
                });


    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivityForResult(intent,0);

    }



}

