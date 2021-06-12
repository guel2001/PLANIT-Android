package com.example.projectgit.Frag;

import android.content.Intent;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.projectgit.Constellation.SetConstellation;
import com.example.projectgit.Login.LoginActivity;

import com.example.projectgit.Activity.MyProfileModify;
import com.example.projectgit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.content.Context.MODE_PRIVATE;
import static com.example.projectgit.Frag.FragmentHome.mediaPlayer;
import static com.example.projectgit.main.MainActivity.finished_cons_code;
import static com.example.projectgit.main.MainActivity.sta_al_model;
import static com.example.projectgit.main.MainActivity.sta_hmap_model;


public class FragmentMyInfo extends Fragment {


    private static final String TAG = " FragmentMyInfo";
    private FirebaseUser user;
    public FragmentMyInfo() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_myinfo, container, false);
        final TextView nameTextView = view.findViewById(R.id.memberName);
        final TextView phoneNumberTextView = view.findViewById(R.id.memberPhone);
        final ImageView imageView = view.findViewById(R.id.profileView);
        final TextView EmailText = view.findViewById(R.id.EmailView);
        final TextView addrText = view.findViewById(R.id.addrtextView);
        Button logoutButton = view.findViewById(R.id.logoutButton2);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sta_hmap_model.clear();
                finished_cons_code.clear();
                sta_al_model.clear();
                SetConstellation.al_const.clear();
                try {
                    mediaPlayer.pause();
                }catch(NullPointerException e){}
                SharedPreferences pref = getActivity().getSharedPreferences("mine",MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });


      Button modifyButton = view.findViewById(R.id.modifyButton);

      modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyProfileModify.class);
                startActivity(intent);
            }
        });

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference rootRef = firebaseStorage.getReference();

        StorageReference imgRef = rootRef.child("users/"+user.getUid()+"/profileImage.jpg");

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() { //imgRef 자체가 객체.
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(getActivity()).load(uri).into(imageView);                            //네트워크 이미지는 Glide로 해결한다.
                                }                                                                                   //Glide를 쓰지 않으면 Thread + URL을 써야한다.
                            });
                            EmailText.setText(user.getEmail());
                            nameTextView.setText(document.getData().get("name").toString());
                            phoneNumberTextView.setText(document.getData().get("phoneNumber").toString());
                            addrText.setText(document.getData().get("address").toString());
                        } else {
                            Log.d(TAG, "No such document"); Log.d(TAG, "No such document");
                        }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        return view;
    }

}