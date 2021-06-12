package com.example.projectgit.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectgit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class SettingActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    boolean i;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        CheckBox checkBox_open = findViewById(R.id.open_chk);
        CheckBox checkBox_close = findViewById(R.id.close_chk);

        CheckBox checkBox_on = findViewById(R.id.on_chk);
        CheckBox checkBox_off = findViewById(R.id.off_chk);




        checkBox_open.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    checkBox_close.setChecked(false);
                }
            }
        });

        checkBox_close.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    checkBox_open.setChecked(false);

                }
            }
        });

        checkBox_on.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    checkBox_off.setChecked(false);
                }
            }
        });

        checkBox_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    checkBox_on.setChecked(false);

                }
            }
        });


        checkBox_open.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : process the click event.
                db.collection("users").document(user.getUid()).update("space_show", true);

            }
        }) ;

        checkBox_close.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : process the click event.
                db.collection("users").document(user.getUid()).update("space_show", false);

            }
        }) ;

        checkBox_on.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : process the click event.
                db.collection("users").document(user.getUid()).update("lockscreen", true);
                Toast.makeText(SettingActivity.this, "어플 재시작시 적용됩니다.", Toast.LENGTH_SHORT).show();
            }
        }) ;

        checkBox_off.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : process the click event.
                db.collection("users").document(user.getUid()).update("lockscreen", false);
                Toast.makeText(SettingActivity.this, "어플 재시작시 적용됩니다.", Toast.LENGTH_SHORT).show();
            }
        }) ;


        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
          documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            if(document.getData().get("space_show").toString().equals("false")){
                                        checkBox_close.setChecked(true);
                            }else {
                                        checkBox_open.setChecked(true);
                            }


                            }
                        }
                    }
                }
            }
        );

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                              if(document.getData().get("lockscreen").toString().equals("false")){

                                checkBox_off.setChecked(true);

                            }else {

                                checkBox_on.setChecked(true);

                            }
                        }
                    }
                }
            }
        });

    }
}
