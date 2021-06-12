package com.example.projectgit.Activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectgit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ReminderActivity extends AppCompatActivity {
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String onlineUserID;
    private String Memo;


    EditText mMemoEdit = null;
    // TextFileManager mTextFileManager = new TextFileManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        mMemoEdit = (EditText) findViewById(R.id.memo_edit);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        onlineUserID = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference();

        Button load = findViewById(R.id.load_btn);
        Button save = findViewById(R.id.save_btn);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String MemoData = mMemoEdit.getText().toString();
                reference.child("Reminder").child(onlineUserID).child("Memo").setValue(MemoData);
                mMemoEdit.setText("");
            }
        });

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child("Reminder").child(onlineUserID).child("Memo").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                        else {
                            mMemoEdit.setText(String.valueOf(task.getResult().getValue()));
                        }
                    }
                });
            }
        });
    }
}