package com.example.projectgit.main;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.projectgit.Activity.ReminderActivity;
import com.example.projectgit.Constellation.SetConstellation;
import com.example.projectgit.Data.Constellation;


import com.example.projectgit.Data.Model;
import com.example.projectgit.Frag.FragmentCollection;
import com.example.projectgit.Frag.UserListFragment;
import com.example.projectgit.Frag.FragmentHome;
import com.example.projectgit.Frag.FragmentMyInfo;

import com.example.projectgit.Login.LoginActivity;
import com.example.projectgit.Login.MemberinitActivity;
import com.example.projectgit.R;

import com.example.projectgit.Activity.ScreenService;
import com.example.projectgit.Activity.SettingActivity;
import com.example.projectgit.Activity.SpaceActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    public static ArrayList<Model> sta_al_model = new ArrayList<>();
    MediaPlayer mediaPlayer;

    private DatabaseReference reference;
    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private final UserListFragment fragmentFriendList = new UserListFragment();
    private final FragmentMyInfo fragmentMyInfo = new FragmentMyInfo();

    private final FragmentHome fragmentHome = new FragmentHome();

    private final FragmentCollection fragmentCollection = new FragmentCollection();
    private FirebaseAuth mAuth;
    private FirebaseUser auser;
    private String onlineUserID;

    public static HashMap<String, ArrayList<Model>> sta_hmap_model = new HashMap<>();

    //별을 찍기위해 constellation 생성
    public static int[] sx = {};
    public static int[] sy = {};

    public static Constellation constellation = new Constellation(1, sx, sy, 1);
    public static String[] Cons_Code = {"0","1","2","3","4","5","6","7","8","9","10","11"}; // position 순으로 code 정렬
    public static ArrayList<String> finished_cons_code = new ArrayList<>(); //메인에 저장된 배열
    public static int cons_reward;

    private ArrayList b = new ArrayList(); //DB값 잠시 저장할 Arraylist

    public static boolean bgmCheck;

    //public static Constellation constellation = new Constellation(1000,sx, sy,7);

    Toolbar myToolbar;

    private AlarmManager alarmManager;
    private GregorianCalendar mCalender;

    private NotificationManager notificationManager;
    NotificationCompat.Builder builder;


    @Override
    protected void onResume() {
        super.onResume();
        // fragmentHome.updateFragmentHomeViewPagerView();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        mAuth = FirebaseAuth.getInstance();
        auser = mAuth.getCurrentUser();

        CheckTypesTask Loading = new CheckTypesTask();
        Loading.execute();

        findViewById(R.id.fab).setOnClickListener(onClickListener);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(user.getUid());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        } else {
                            Log.d(TAG, "No such document");
                            myStartActivity((MemberinitActivity.class));
                        }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener(task -> {
            if(task.isSuccessful() && task.getResult() != null){
                try {
                    String position = task.getResult().get("currentConstellation").toString();

                    reference.child("Constellation").child(position).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.e("firebase", "Error getting data", task.getException());
                            } else {
                                for (DataSnapshot item_list : Objects.requireNonNull(task.getResult().getChildren())) {
                                    for (DataSnapshot item : item_list.getChildren()) {
                                        b.add(String.valueOf(item.getValue())); // b에 DB에서 갖고온값저장
                                    }
                                }

                                constellation.setScount(Integer.parseInt(String.valueOf(b.get(0)))); //scount 세팅
                                int sxcount = constellation.getScount(); //Array 나누기위해서 변수선언
                                int sycount = constellation.getScount() + constellation.getScount();
                                Object[] olist = b.toArray(); //arraylist를 object array로변환
                                int[] intlist = new int[olist.length - 3]; //int array로 다시변환
                                for (int i = 0; i < olist.length - 3; i++) {
                                    intlist[i] = Integer.parseInt(String.valueOf(olist[i + 3])); //값집어넣기
                                }
                                int[] sxlist = Arrays.copyOfRange(intlist, 0, sxcount);
                                int[] sylist = Arrays.copyOfRange(intlist, sxcount, sycount);
                                constellation.setSx(sxlist);
                                constellation.setSy(sylist);
                                constellation.setCons_code(position);   // 별자리 코드저장
                                System.out.println("메인에서 별자리코드 get: "+constellation.getCons_code());
                            }
                        }
                    });

                }catch(NullPointerException e){}
                //other stuff
            }else{
                //deal with error
            }
        });


        //처음을 위해서 초기화
        ArrayList<Model> first_model = new ArrayList<>();
        ArrayList<Model> second_model = new ArrayList<>();
        ArrayList<Model> third_model = new ArrayList<>();
        sta_hmap_model.put("yesterday_task",first_model);
        sta_hmap_model.put("today_task",second_model);
        sta_hmap_model.put("tomorrow_task",third_model);

         BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
         bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

            if (user == null) {
                myStartActivity(LoginActivity.class);
            } else {
                onlineUserID = auser.getUid();
                reference = FirebaseDatabase.getInstance().getReference();

                reference.child(onlineUserID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        } else {
                            for (DataSnapshot item_list : Objects.requireNonNull(task.getResult()).getChildren()) {

                                ArrayList<Model> al_model = new ArrayList<>();
                                for (DataSnapshot item : item_list.getChildren()) {
                                    Log.d("testing inner", String.valueOf(item.getValue()));
                                    al_model.add(item.getValue(Model.class));
                                }
                                sta_hmap_model.put(item_list.getKey(), al_model);
                            }
                            transaction.replace(R.id.frameLayout, fragmentHome).commitAllowingStateLoss();
                        }
                    }
                });

                DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                StorageReference rootRef = firebaseStorage.getReference();

                ArrayList<String> conarr;
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document!= null) {
                                if (document.exists()) {
                                    if (document.getData().get("finished_cons")!=null ) {
                                    finished_cons_code = (ArrayList<String>) document.getData().get("finished_cons");
                                    Log.d("코드",""+finished_cons_code);
                                } else {
                                    Log.d(TAG, "No such document"); Log.d(TAG, "No such document");
                                    }
                                }
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });


        }

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            if(document.getData().get("lockscreen").toString().equals("false")){
                                Intent intent = new Intent(getApplicationContext(), ScreenService.class);
                                stopService(intent);


                            }else {
                                Intent intent = new Intent(getApplicationContext(), ScreenService.class);
                                startService(intent);


                            }
                        }
                    }
                }
            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // MediaPlayer 해지
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog asyncDialog = new ProgressDialog(MainActivity.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("로딩중..");
            asyncDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                for (int i = 0; i < 5; i++) {
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            asyncDialog.dismiss();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.actionReminder:

                myStartActivity(ReminderActivity.class);

                break;
            case R.id.actionConstellation:
                myStartActivity(SetConstellation.class);

                break;
            case R.id.actionSetting:
                myStartActivity(SettingActivity.class);

                break;

        }
        return super.onOptionsItemSelected(item);
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (menuItem.getItemId()) {
                case R.id.myInfoItem:
                    displayFragmentMyinfo();
                    // transaction.replace(R.id.frameLayout, fragmentMyInfo).commitAllowingStateLoss();
                    break;
                case R.id.userListItem:
                    displayFragmentFriend();
                    //transaction.replace(R.id.frameLayout, fragmentFriendList).commitAllowingStateLoss();
                    break;
                case R.id.homeItem:
                    displayFragmentHome();
                    //transaction.replace(R.id.frameLayout, fragmentHome).commitAllowingStateLoss();
                    break;

                case R.id.CollectionItem:
                    displayFragmentCollection();

                    //transaction.replace(R.id.frameLayout, fragmentCollection).commitAllowingStateLoss();
                    break;

            }
            return true;
        }

    }

    protected void displayFragmentHome() {
        //FragmentTransaction transaction = fragmentManager.beginTransaction();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragmentHome.isAdded()) {
            ft.show(fragmentHome);

        } else {
            ft.add(R.id.frameLayout, fragmentHome, "HOME");
        }

        if (fragmentFriendList.isAdded()) {
            ft.hide(fragmentFriendList);
        }
        if (fragmentMyInfo.isAdded()) {
            ft.hide(fragmentMyInfo);
        }
        if (fragmentCollection.isAdded()) {
            ft.hide(fragmentCollection);
        }
        //transaction.commit();
        ft.commit();

    }

    protected void displayFragmentMyinfo() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragmentMyInfo.isAdded()) {
            ft.show(fragmentMyInfo);
        } else {
            ft.add(R.id.frameLayout, fragmentMyInfo, "Myinfo");
        }
        if (fragmentHome.isAdded()) {
            ft.hide(fragmentHome);
        }
        if (fragmentFriendList.isAdded()) {
            ft.hide(fragmentFriendList);
        }
        if (fragmentCollection.isAdded()) {
            ft.hide(fragmentCollection);
        }

        ft.commit();
    }

    protected void displayFragmentFriend() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragmentFriendList.isAdded()) {
            ft.show(fragmentFriendList);
        } else {
            ft.add(R.id.frameLayout, fragmentFriendList, "friend");
        }
        if (fragmentHome.isAdded()) {
            ft.hide(fragmentHome);
        }
        if (fragmentMyInfo.isAdded()) {
            ft.hide(fragmentMyInfo);
        }
        if (fragmentCollection.isAdded()) {
            ft.hide(fragmentCollection);
        }
        ft.commit();
    }

    protected void displayFragmentCollection() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragmentCollection.isAdded()) {
            ft.show(fragmentCollection);
        } else {
            ft.add(R.id.frameLayout, fragmentCollection, "collection");
        }
        if (fragmentFriendList.isAdded()) {
            ft.hide(fragmentFriendList);
        }
        if (fragmentMyInfo.isAdded()) {
            ft.hide(fragmentMyInfo);
        }
        if (fragmentHome.isAdded()) {
            ft.hide(fragmentHome);
        }

        ft.commit();
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
    View.OnClickListener onClickListener = v -> {
        switch(v.getId()){

            case R.id.fab:
                myStartActivity(SpaceActivity.class);
                break;

        }
    };


}


