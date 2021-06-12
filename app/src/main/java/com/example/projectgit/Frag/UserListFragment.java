package com.example.projectgit.Frag;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectgit.Data.Memberinfo;
import com.example.projectgit.R;
import com.example.projectgit.Adapter.UserListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class UserListFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private FirebaseFirestore firebaseFirestore;
    private UserListAdapter userListAdapter;
    private ArrayList<Memberinfo> userList;
    RecyclerView recyclerView;
    private FirebaseUser user;

    public static String A;
    EditText search_users;
    public UserListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_friendlist, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        userList = new ArrayList<>();
        userListAdapter = new UserListAdapter(getActivity(), userList);

        recyclerView = view.findViewById(R.id.users_recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        userList = new ArrayList<>();

        postsUpdate();
        search_users = view.findViewById(R.id.search_users);
        search_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                searchUsers(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private  void searchUsers(String s){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("users").orderBy("name")
                .startAt(s)
                .endAt(s+"\uf0ff");

        query
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            userList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Log.d(TAG, document.getId() + " => " + document.getData());
                                try {
                                    userList.add(new Memberinfo(
                                            document.getData().get("name").toString(),
                                            document.getData().get("email").toString(),
                                            document.getData().get("uid").toString(),
                                            document.getData().get("photoUrl") == null ? null : document.getData().get("photoUrl").toString()));

                                }catch(NullPointerException e){}
                            }
                            userListAdapter = new UserListAdapter(getActivity(),userList);
                            recyclerView.setAdapter(userListAdapter);
                            userListAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }


    private void postsUpdate() {

        //Date date = userList.size() == 0 || clear ? new Date() : userList.get(userList.size() - 1).getCreatedAt();
        CollectionReference collectionReference = firebaseFirestore.collection("users");
        collectionReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(search_users.getText().toString().equals("")) {
                                userList.clear();

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    try {
                                        userList.add(new Memberinfo(
                                                document.getData().get("name").toString(),
                                                document.getData().get("email").toString(),
                                                document.getData().get("uid").toString(),
                                                document.getData().get("photoUrl") == null ? null : document.getData().get("photoUrl").toString()));


                                    }catch(NullPointerException e){}
                                }

                                userListAdapter = new UserListAdapter(getActivity(), userList);
                                recyclerView.setAdapter(userListAdapter);
                                userListAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });


    }



}