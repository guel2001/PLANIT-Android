package com.example.projectgit.Constellation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

import android.os.Bundle;

import android.util.Log;
import android.view.View;

import com.example.projectgit.Adapter.RcvAdapter;
import com.example.projectgit.Data.Constellation;
import com.example.projectgit.Data.ItemClickListener;
import com.example.projectgit.Data.Model;
import com.example.projectgit.Frag.FragmentHome;
import com.example.projectgit.R;
import com.example.projectgit.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;


public class SetConstellation extends AppCompatActivity implements RcvAdapter.MyRecyclerViewClickListener{

   public static ArrayList<Constellation> al_const = new ArrayList<>();
   public int[] con = {R.drawable.book_aquarius, R.drawable.book_aries, R.drawable.book_cancer,
            R.drawable.book_capricorn, R.drawable.book_gemini, R.drawable.book_leo,
            R.drawable.book_libra, R.drawable.book_pisces, R.drawable.book_sagittarius,
            R.drawable.book_scorpius, R.drawable.book_taurus, R.drawable.book_virgo
    };

    RcvAdapter adapter = new RcvAdapter(al_const,this);
    private static final String TAG = "SetConstellation";
    public String[] con_name = {"물병자리","양자리","게자리","염소자리","쌍둥이자리","사자자리","천칭자리","물고기자리","궁수자리","전갈자리","황소자리","처녀자리"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_constellation);
        RecyclerView rcv = findViewById(R.id.main_rcv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                // 세로 : LinearLayoutManager.VERTICAL,
                LinearLayoutManager.HORIZONTAL, false);

        for (int i=0; i<12; i++){
            al_const.add(new Constellation(con[i],"Title"+i,con_name[i],String.valueOf(i)));

        }
        rcv.setAdapter(adapter);
        adapter.setOnClickListener(this);
        // 어댑터에 데이터 넣어줘야함

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();;

        adapter.setOnItemClickListener(new ItemClickListener<Constellation>() {
            @Override
            public void onItemClick(int position, Constellation const_value) {
                db.collection("users").document(user.getUid()).update("currentConstellation", position);

                myStartActivity(MainActivity.class);
            }
        });

        rcv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
//                updateState(scrollState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
//                mPositionText.setText("First: " + mRecyclerViewPager.getFirstVisiblePosition());
                int childCount = rcv.getChildCount();
                int width = rcv.getChildAt(0).getWidth();
                int padding = (rcv.getWidth() - width) / 2;
//                mCountText.setText("Count: " + childCount);

                for (int j = 0; j < childCount; j++) {
                    View v = recyclerView.getChildAt(j);
                    float rate = 0;

                    if (v.getLeft() <= padding) {
                        if (v.getLeft() >= padding - v.getWidth()) {
                            rate = (padding - v.getLeft()) * 1f / v.getWidth();
                        } else {
                            rate = 1;
                        }
                        v.setScaleY(1 - rate * 0.1f);
                        v.setScaleX(1 - rate * 0.1f);

                    } else {
                        if (v.getLeft() <= recyclerView.getWidth() - padding) {
                            rate = (recyclerView.getWidth() - padding - v.getLeft()) * 1f / v.getWidth();
                        }
                        v.setScaleY(0.9f + rate * 0.1f);
                        v.setScaleX(0.9f + rate * 0.1f);
                    }
                }
            }
        });


        rcv.addItemDecoration(new HorizontalItemDecoration(10));

        // 여백을 얼만큼 줄건지 설정도 할 수 있다.

        rcv.setAdapter(adapter);
        rcv.setLayoutManager(layoutManager);


        DocumentReference documentReference = FirebaseFirestore.getInstance().
                collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            MainActivity.finished_cons_code.add((document.getData().get("finished_cons").toString())); //db값 완료된것 ex 101 10

                            //setconstellation에서 뭐 check할지 고르는 함수
                            for (int i = 0; i < al_const.size(); i++) {
                                for (int j = 0; j < MainActivity.finished_cons_code.size(); j++) {
                                    if (al_const.get(i).getCons_code().equals(MainActivity.finished_cons_code.get(j))) { //새로만든조건문
                                        Log.d("피니쉬드콘스",MainActivity.finished_cons_code.get(j)+"");
                                        Log.d("피니쉬드콘스",al_const.get(i).getCons_code()+"");
                                        switch (String.valueOf(MainActivity.finished_cons_code.get(j)))
                                        {
                                            case "0" :
                                                al_const.get(i).setImage(R.drawable.book_aquarius_check);
                                                break;
                                            case "1" :
                                                al_const.get(i).setImage(R.drawable.book_aries_check);
                                                break;
                                            case "2" :
                                                al_const.get(i).setImage(R.drawable.book_cancer_check);
                                                break;
                                            case "3" : al_const.get(i).setImage(R.drawable.book_capricorn_check);
                                                break;
                                            case "4" : al_const.get(i).setImage(R.drawable.book_gemini_check);
                                                break;
                                            case "5" : al_const.get(i).setImage(R.drawable.book_leo_check);
                                                break;
                                            case "6" : al_const.get(i).setImage(R.drawable.book_libra_check);
                                                break;
                                            case "7" : al_const.get(i).setImage(R.drawable.book_pisces_check);
                                                break;
                                            case "8" : al_const.get(i).setImage(R.drawable.book_sagittarius_check);
                                                break;
                                            case "9" : al_const.get(i).setImage(R.drawable.book_scorpius_check);
                                                break;
                                            case "10" : al_const.get(i).setImage(R.drawable.book_taurus_check);
                                                break;
                                            case "11" : al_const.get(i).setImage(R.drawable.book_virgo_check);
                                                break;
                                        }
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            }


                        }else{
                            Log.d(TAG, "No such document"); Log.d(TAG, "No such document");
                        }
                    }
                }else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        }
        );


    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onItemClicked(int position) {

    }

    public void onTitleClicked(int position) {

    }

    public void onContentClicked(int position) {

    }

    @Override
    public void onItemLongClicked(int position) {

    }

    @Override
    public void onImageViewClicked(int position) {

    }


}