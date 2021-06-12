package com.example.projectgit.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.projectgit.Activity.OtherSpaceActivity;
import com.example.projectgit.Data.Memberinfo;

import com.example.projectgit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MainViewHolder> {
    public static ArrayList<Memberinfo> mDataset;
    private Activity activity;
    private FirebaseUser user;
    private static final String TAG = "UserListAdapter";
    private FirebaseAuth mAuth;
    public static int pos;
    public static String UserName;
    public static String UID;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button btnAdd;

    public class MainViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;


        MainViewHolder(CardView v) {
            super(v);
            cardView = v;

        }


    }


    public UserListAdapter(Activity activity, ArrayList<Memberinfo> myDataset) {
        this.mDataset = myDataset;
        this.activity = activity;
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }

    @NonNull
    @Override
    public UserListAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.row_users, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(cardView);


        return mainViewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull final MainViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        ImageView photoImageVIew = cardView.findViewById(R.id.photoImageVIew);
        TextView nameTextView = cardView.findViewById(R.id.nameTextView);
        TextView emailTextView = cardView.findViewById(R.id.emailTextView);
        TextView uidText = cardView.findViewById(R.id.uidText);
        btnAdd = cardView.findViewById(R.id.otherspaceView);

        Memberinfo userInfo = mDataset.get(position);

        if (mDataset.get(position).getPhotoUrl() != null) {
            Glide.with(activity).load(mDataset.get(position).getPhotoUrl()).centerCrop().override(500).into(photoImageVIew);
        }
        nameTextView.setText(userInfo.getName());
        emailTextView.setText(userInfo.getEmail());
        uidText.setText(userInfo.getUid());



        /*


        */
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("포지션션값: " + position);
                System.out.println("이름: " + mDataset.get(position).getName());
                System.out.println("uid: " + mDataset.get(position).getUid());
                UserName = mDataset.get(position).getName();
                UID = mDataset.get(position).getUid();
                DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(mDataset.get(position).getUid().toString());
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null) {
                                if (document.exists()) {
                                 if(document.getData().get("space_show").toString().equals("false")){

                                     Toast.makeText(activity, "비공개입니다.", Toast.LENGTH_SHORT).show();
                                 }else {
                                     Intent intent = new Intent(v.getContext(), OtherSpaceActivity.class);
                                     v.getContext().startActivity(intent);
                                 }
                                } else {
                                    Log.d(TAG, "No such document"); Log.d(TAG, "No such document");
                                }
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

            }
        });





    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}

