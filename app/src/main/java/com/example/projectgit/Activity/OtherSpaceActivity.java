package com.example.projectgit.Activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.projectgit.R;
import com.example.projectgit.Adapter.UserListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.panxw.android.imageindicator.ImageIndicatorView;

import java.util.ArrayList;


public class OtherSpaceActivity extends AppCompatActivity {
    private static final String TAG = "SpaceActivity";
    private ImageIndicatorView imageIndicatorView;
    public ArrayList<String> Other_finished_cons_code = new ArrayList<>(); //메인에 저장된 배열

    @Override
    protected void onResume() {
        super.onResume();
        // fragmentHome.updateFragmentHomeViewPagerView();

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_space);


      //  ImageView spaceView=findViewById(R.id.spaceView);
        ImageView cons1 = findViewById(R.id.cons1); //-> 변수명은 별자리코드와같음
        ImageView cons2  = findViewById(R.id.cons2);
        ImageView cons3  = findViewById(R.id.cons3);
        ImageView cons4  = findViewById(R.id.cons4);
        ImageView cons5 = findViewById(R.id.cons5);
        ImageView cons6  = findViewById(R.id.cons6);
        ImageView cons7  = findViewById(R.id.cons7);
        ImageView cons8  = findViewById(R.id.cons8);
        ImageView cons9  = findViewById(R.id.cons9);
        ImageView cons10 = findViewById(R.id.cons10);
        ImageView cons11 = findViewById(R.id.cons11);
        ImageView cons12 = findViewById(R.id.cons12);
        TextView textView = findViewById(R.id.spaceName);

        Glide.with(this).load(R.raw.aquariusgif).into(cons1);
        Glide.with(this).load(R.raw.ariesgif).into(cons2);
        Glide.with(this).load(R.raw.cancergif).into(cons3);
        Glide.with(this).load(R.raw.capricorngif).into(cons4);
        Glide.with(this).load(R.raw.geminigif).into(cons5);
        Glide.with(this).load(R.raw.libragif).into(cons7);
        Glide.with(this).load(R.raw.piscesgif).into(cons8);
        Glide.with(this).load(R.raw.taurusgif).into(cons11);

        textView.setText(UserListAdapter.UserName+" 's");
        ImageView fireview = findViewById(R.id.fireView);
        Glide.with(this).load(R.raw.bonfire_unscreen).into(fireview);

        DocumentReference documentReference = FirebaseFirestore.getInstance().
                collection("users").document(UserListAdapter.UID.toString());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Other_finished_cons_code.clear();
                    if (document != null) {
                        if (document.exists()) {
                         Other_finished_cons_code.add(document.getData().get("finished_cons").toString());

                            for (int i = 0; i < 12; i++) { //별자리 총 개수
                                for (int j = 0; j < Other_finished_cons_code.size(); j++) {
                                    if (Other_finished_cons_code.get(j).contains(String.valueOf(i))) { //새로만든조건문

                                        if (i == 0) {
                                            cons1.setVisibility(View.VISIBLE);
                                        }
                                        if (i == 1) {
                                            cons2.setVisibility(View.VISIBLE);
                                        }
                                        if (i == 2) {
                                            cons3.setVisibility(View.VISIBLE);
                                        }
                                        if (i == 3) {
                                            cons4.setVisibility(View.VISIBLE);
                                        }
                                        if (i == 4) {
                                            cons5.setVisibility(View.VISIBLE);
                                        }
                                        if (i == 5) {
                                            cons6.setVisibility(View.VISIBLE);
                                        }
                                        if (i == 6) {
                                            cons7.setVisibility(View.VISIBLE);
                                        }
                                        if (i == 7) {
                                            cons8.setVisibility(View.VISIBLE);
                                        }
                                        if (i == 8) {
                                            cons9.setVisibility(View.VISIBLE);
                                        }
                                        if (i == 9) {
                                            cons10.setVisibility(View.VISIBLE);
                                        }
                                        if (i == 10) {
                                            cons11.setVisibility(View.VISIBLE);
                                        }
                                        if (i == 11) {
                                            cons12.setVisibility(View.VISIBLE);
                                        }

                                    }
                                }

                        }

                            }

                        }else{
                            Log.d(TAG, "No such document"); Log.d(TAG, "No such document");
                        }
                    }

            }
        }
        );

        this.imageIndicatorView = (ImageIndicatorView) findViewById(R.id.guide_indicate_view);
        final Integer[] resArray = new Integer[] {R.drawable.night_sky1,R.drawable.background_space2,R.drawable.background_space3,R.drawable.background_space4};
        imageIndicatorView.setupLayoutByDrawable(resArray);
        imageIndicatorView.setIndicateStyle(ImageIndicatorView.INDICATE_USERGUIDE_STYLE);
        imageIndicatorView.show();
    }

}

