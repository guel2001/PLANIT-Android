package com.example.projectgit.Activity;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
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
import com.example.projectgit.CameraActivity.CameraActivity;
import com.example.projectgit.R;
import com.example.projectgit.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
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

public class MyProfileModify extends AppCompatActivity {
    private static final String TAG = "MemberInitActivity";
    private ImageView profileImageView;
    private String profilePath;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_modify);

        profileImageView = findViewById(R.id.profileimageView2);
        profileImageView.setOnClickListener(onClickListener);

        findViewById(R.id.checkButton2).setOnClickListener(onClickListener);
        findViewById(R.id.gallery2).setOnClickListener(onClickListener);
        findViewById(R.id.picture2).setOnClickListener(onClickListener);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0: {
                if (resultCode == Activity.RESULT_OK) {
                    profilePath = data.getStringExtra("profilePath");
                    Glide.with(this).load(profilePath).centerCrop().override(500).into(profileImageView);

                }
                break;
            }
        }
    }

    View.OnClickListener onClickListener = v -> {
        switch (v.getId()) {
            case R.id.checkButton2:
                profileModify();
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

                if (ContextCompat.checkSelfPermission(MyProfileModify.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MyProfileModify.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            1);
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MyProfileModify.this,
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



    private void profileModify() {
        final String name = ((EditText) findViewById(R.id.nameEditText2)).getText().toString();
        final String phoneNumber = ((EditText) findViewById(R.id.phoneNumberEditText2)).getText().toString();
        final String address = ((EditText) findViewById(R.id.editaddr)).getText().toString();
        if (name.length() > 0 && phoneNumber.length() >9 ) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

            user = FirebaseAuth.getInstance().getCurrentUser();
            final StorageReference mountainImagesRef = storageRef.child("users/"+user.getUid()+"/profileImage.jpg");

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            if(profilePath ==null){
                db.collection("users").document(user.getUid()).update("name", name);
                db.collection("users").document(user.getUid()).update("phoneNumber", phoneNumber);
                db.collection("users").document(user.getUid()).update("address", address);
                startToast("회원정보 수정완료");
                myStartActivity(MainActivity.class);
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
                                String uri = downloadUri.toString();
                                db.collection("users").document(user.getUid()).update("name", name);
                                db.collection("users").document(user.getUid()).update("phoneNumber", phoneNumber);
                                db.collection("users").document(user.getUid()).update("photoUrl", uri);
                                db.collection("users").document(user.getUid()).update("address", address);
                                startToast("회원정보 수정완료");
                                myStartActivity(MainActivity.class);
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


    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivityForResult(intent,0);

    }
}
