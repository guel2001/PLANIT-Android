package com.example.projectgit.Frag;


import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.app.Dialog;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.ViewGroup;

import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import androidx.viewpager.widget.ViewPager;


import com.bumptech.glide.Glide;
import com.example.projectgit.Data.Model;

import androidx.fragment.app.FragmentTransaction;


import com.example.projectgit.Adapter.FragmentAdapter;
import com.example.projectgit.Constellation.SetConstellation;


import com.example.projectgit.view.ClickableViewPager;
import com.example.projectgit.R;
import com.example.projectgit.Activity.SoundManager;
import com.example.projectgit.main.MainActivity;
import com.example.projectgit.Activity.TaskActivity;
import com.google.android.gms.tasks.OnCompleteListener;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.projectgit.main.MainActivity.finished_cons_code;


//
public class FragmentHome extends Fragment implements ViewPager.OnPageChangeListener {
    private static final String TAG = " FragmentHome";

    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String onlineUserID;
    public static int finished_position;
    SoundPool soundPool;
    SoundManager soundManager;
    boolean play;
    int playSoundId;

    public static int resId;

    String current_background;
    ConstraintLayout constraintLayout;
    public int[] con_line = {R.drawable.aquarius_line, R.drawable.aries_line, R.drawable.cancer_line,
            R.drawable.capricorn_line, R.drawable.gemini_line, R.drawable.leo_line,
            R.drawable.libra_line, R.drawable.pisecs_line, R.drawable.sagittarius_line,
            R.drawable.scorpius_line, R.drawable.taurus_line, R.drawable.virgo_line};

    public int[] bg_list = {R.drawable.main_screen,R.drawable.main_screen10,R.drawable.main_screen12,
            R.drawable.main_screen15,R.drawable.main_screen16, R.drawable.main_screen17,
            R.drawable.main_screen18,R.drawable.main_screen19, R.drawable.main_screen20,
            R.drawable.main_screen21,R.drawable.main_screen22};

    FragmentAdapter fragmentAdapter = null;
    public static TextView stextView;
    private int count = 0;
    private int bgcount = 0;
    public static ArrayList<String> finished_cons = new ArrayList<String>(); //완성한 별자리 임시 저장
    public static MediaPlayer mediaPlayer;
    public FragmentHome() {

    }

    public FragmentHome(String todo) {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        checkTodo();
        /*
        try {
            updateStar_hmap();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */
        updateFragmentHomeViewPagerView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        onlineUserID = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference();

    }


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        View myView = inflater.inflate(R.layout.input_file, null);

        constraintLayout = view.findViewById(R.id.HomeBackground);

        ImageView Cons_scatch = view.findViewById(R.id.cons_line); //별자리 선 이미지
        ImageView bgm_icon = view.findViewById(R.id.bgmView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder().build();
        }else{
            //롤리팝 이하 버전일 경우
            // new SoundPool(1번,2번,3번)
            // 1번 - 음악 파일 갯수
            // 2번 - 스트림 타입
            // 3번 - 음질
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC,0);
        }
        soundManager = new SoundManager(getActivity(),soundPool);
        soundManager.addSound(0,R.raw.star_open);  //효과음 추가


        bgm_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!play){
                    bgm_icon.setImageResource(R.drawable.bgm_icon);
                    mediaPlayer = MediaPlayer.create(getActivity(), R.raw.starrynight);
                    mediaPlayer.start();
                    play = true;
                }else{
                    bgm_icon.setImageResource(R.drawable.bgm2_icon);
                    mediaPlayer.pause();
                    play = false;
                }


            }

        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                try {
                    current_background = task.getResult().get("currentConstellation").toString();
                    resId = Integer.parseInt(current_background);
                    Cons_scatch.setBackgroundResource(con_line[resId]);  // 임시로 별자리사진받아옴 별자리 선 그려져있는걸로 바꿔야됨.
                } catch (NullPointerException e) {
                }
                //other stuff
            } else {
                //deal with error
            }
        });

        constraintLayout = view.findViewById(R.id.HomeBackground);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        onlineUserID = user.getUid();

        reference = FirebaseDatabase.getInstance().getReference();

        View v = inflater.inflate(R.layout.fragment_todo, container, false);

        final ImageView star1View = view.findViewById(R.id.star1View);
        final Button button = view.findViewById(R.id.starViewButton);
        final Button changebg = view.findViewById(R.id.changeBg);

        ClickableViewPager viewPager = (ClickableViewPager) view.findViewById(R.id.viewPager);

        fragmentAdapter = new FragmentAdapter(getChildFragmentManager());// ViewPager와  FragmentAdapter 연결
        viewPager.setAdapter(fragmentAdapter);


        //뷰페이저마진조절
        viewPager.setClipToPadding(false);
        int dpValue = 16;
        float d = getResources().getDisplayMetrics().density;
        int margin = (int) (dpValue * d);
        viewPager.setPadding(margin, 0, margin, 0);
        viewPager.setPageMargin(margin / 2);
        //뷰페이저 마진조절끝

        for (int i = 0; i < 3; i++) {
            TodoFragment todoFragment = new TodoFragment();
            stextView = new TextView(this.getContext());
            Bundle bundle = new Bundle();
            ArrayList<Model> var = new ArrayList<>();
            if (i == 0) {
                String str = "";
                var = MainActivity.sta_hmap_model.get("yesterday_task");
                if (var != null) {
                    for (int k = 0; k < var.size(); k++) {
                        str = str + var.get(k).getTask() + "\n";
                    }
                }
                bundle.putString("task", str);
                //bundle.putString("yesterday_bundle", str);
            } else if (i == 1) {
                String str = "";
                var = MainActivity.sta_hmap_model.get("today_task");
                if (var != null) {
                    for (int k = 0; k < var.size(); k++) {
                        str = str + var.get(k).getTask() + "\n";
                    }
                    Log.d("testing i==1", str);
                }
                bundle.putString("task", str);
            } else {
                String str = "";
                var = MainActivity.sta_hmap_model.get("tomorrow_task");
                if (var != null) {
                    for (int k = 0; k < var.size(); k++) {
                        str = str + var.get(k).getTask() + "\n";
                        bundle.putString("text", str);

                    }
                    Log.d("testing i==2", str);
                }
                bundle.putString("task", str);
            }
            todoFragment.setArguments(bundle);
            fragmentAdapter.addItem(todoFragment);
        }
        fragmentAdapter.notifyDataSetChanged();
        viewPager.setCurrentItem(1);

        Dialog dialog;
        dialog = new Dialog(getContext());       // Dialog 초기화
        dialog.setContentView(R.layout.task_dialog);             // xml 레이아웃 파일과 연결
        ImageView finished_image = dialog.findViewById(R.id.Finished_ConsView);

        drawStar();

        changebg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                constraintLayout.setBackgroundResource(bg_list[bgcount]);
                bgcount++;
                if(bgcount==bg_list.length)
                {
                    bgcount = 0;
                }
                Log.d("이거",""+constraintLayout.getBackground());
            }
        });

        button.setOnClickListener(new View.OnClickListener() {   //달성완료 버튼을 누를시 일어나는 작업들,추후 수정
            @Override
            public void onClick(View view) { //지금은 별 눌러서 찍지만 (task가 다채워짐 + 하루가지났다)는 조건이후 찍히도록 바꿔야됨

                if (count < MainActivity.constellation.getScount()) { //하루지난거랑 연계작업
                    createImageView(count);
                    count++;
                    playSoundId=soundManager.playSound(0);

                    if (count == MainActivity.constellation.getScount()) {

                        finished_cons_code.add(MainActivity.constellation.getCons_code()); //별을 다찍을시 해당 별코드를 memberinfo에 정수배열 저장
                        Log.d("별별", "" + finished_cons);
                        db.collection("users").document(user.getUid()).update("finished_cons", finished_cons_code); //memberinfo 필드 업데이트

                        for (int index = 0; index < finished_cons.size(); index++) {
                            System.out.println("별자리코드 출력" + finished_cons.get(index));    //잘 누적되는지 chk
                        }
                        SetConstellation setConstellation = new SetConstellation();
                        for (int i = 0; i < MainActivity.Cons_Code.length; i++) {
                            if (String.valueOf(i).equals(MainActivity.constellation.getCons_code())) {
                                finished_position = i;//완성한 별자리의 포지션 가져옴
                                System.out.println("포지션 : " + finished_position);
                                System.out.println("con : " + setConstellation.con[finished_position]);
                                finished_image.setImageResource((setConstellation.con[finished_position])); //다이얼로그에 이미지뷰 완성된 별자리로 set
                            }
                        }

                        dialog.show();
                        dialog.setCancelable(false);
                        Button OkButton = dialog.findViewById(R.id.OkButton);
                        OkButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                Intent intent = new Intent(getActivity(), SetConstellation.class);
                                startActivity(intent);
                            }

                        });

                    }
                } else {
                    Log.d("지우", "꽉참");
                }
                //star1View.setVisibility(View.VISIBLE); //1일차 달성완료버튼을 누르면 star1 이 켜짐
            }
        });

        viewPager.setOnItemClickListener(new ClickableViewPager.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), TaskActivity.class);
                switch (position) {
                    case 0:
                        intent.putExtra("check_yesterday", true);
                        startActivity(intent);
                        break;
                    case 1:
                        intent.putExtra("check_today", true);
                        startActivity(intent);
                        break;
                    case 2:
                        intent.putExtra("check_tomorrow", true);
                        startActivity(intent);
                        break;
                }
            }
        });
        return view;
    }


    private void createImageView(int a) {
        FrameLayout frameLayout = getView().findViewById(R.id.frameLayout);
        ImageView imageViewNm = new ImageView(getActivity());
        if(a%3 == 0){
            Glide.with(this).load(R.raw.stargif3).into(imageViewNm);
        }
        else if(a%3 == 1) {
            Glide.with(this).load(R.raw.stargif5).into(imageViewNm);
        }
        else if(a%3 == 2) {
            Glide.with(this).load(R.raw.stargif4).into(imageViewNm);
        }

        DisplayMetrics dm = getResources().getDisplayMetrics();
        int position_x = Math.round((MainActivity.constellation.getSx()[a])*dm.density);
        int position_y = Math.round((MainActivity.constellation.getSy()[a])*dm.density);

/*
        ImageView imageViewNm = new ImageView(getActivity());

        imageViewNm.setImageResource(R.drawable.star_ex);
 */
        imageViewNm.setId(MainActivity.constellation.getCnum());

        FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(130, 130, 0);
        param.leftMargin = position_x;
        param.topMargin =  position_y;
        imageViewNm.setLayoutParams(param);

        frameLayout.addView(imageViewNm);


    }


    public void updateStar_hmap() throws InterruptedException {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //현재 날짜 구하기
        Calendar cur_cal = Calendar.getInstance();
        SimpleDateFormat cur_format = new SimpleDateFormat("yyyy.MM.dd.");
        cur_format.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        String cur_date = cur_format.format(cur_cal.getTime());

        //어제 날짜 구하기
        Calendar ye_cal = Calendar.getInstance();
        SimpleDateFormat ye_format = new SimpleDateFormat("yyyy.MM.dd.");
        ye_format.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        ye_cal.add(Calendar.DATE, -1);
        String ye_date = ye_format.format(ye_cal.getTime());

        //엊그제 날짜 구하기기
        Calendar twoago_cal = Calendar.getInstance();
        SimpleDateFormat twoago_format = new SimpleDateFormat("yyyy.MM.dd.");
        twoago_format.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        twoago_cal.add(Calendar.DATE, -2);
        String twoago_date = twoago_format.format(twoago_cal.getTime());


        if ((MainActivity.sta_hmap_model.get("today_task").get(0).getDate()).equals(ye_date)) { //하루가 지난 상황

            //yesterday_task DB 싹 삭제
            reference.child(onlineUserID).child("yesterday_task").setValue(null);
            //today -> yesterday DB
            moveRecord(reference.child(onlineUserID).child("today_task"), reference.child(onlineUserID).child("yesterday_task"));
            Thread.sleep(3000);
            //today -> yesterday Model
            MainActivity.sta_hmap_model.put("yesterday_task", MainActivity.sta_hmap_model.get("today_task"));

            //today_task DB 싹 삭제
            reference.child(onlineUserID).child("today_task").setValue(null);
            MainActivity.sta_hmap_model.put("today_task", null);
            moveRecord(reference.child(onlineUserID).child("tomorrow_task"), reference.child(onlineUserID).child("today_task"));
            Thread.sleep(3000);
            reference.child(onlineUserID).child("tomorrow_task").setValue(null);
            //today = tomorrow모델 집어넣기
            MainActivity.sta_hmap_model.put("today_task", MainActivity.sta_hmap_model.get("tomorrow_task"));

            MainActivity.sta_hmap_model.put("tomorrow_task", null);
            reference.child(onlineUserID).child("tomorrow_task").setValue(null);
            db.collection("users").document(user.getUid()).update("checkStar",false);

        } else if ((MainActivity.sta_hmap_model.get("tomorrow_task").get(0).getDate()).equals(twoago_date)) {

            //yesterday = tomorrow model 집어넣기
            reference.child(onlineUserID).child("yesterday_task").setValue(null);
            moveRecord(reference.child(onlineUserID).child("tomorrow_task"), reference.child(onlineUserID).child("yesterday_task"));
            Thread.sleep(3000);
            reference.child(onlineUserID).child("tomorrow_task").setValue(null);
            MainActivity.sta_hmap_model.put("yesterday_task", MainActivity.sta_hmap_model.get("tomorrow_task"));

            // 오늘 내일 모델비워주기
            reference.child(onlineUserID).child("today_task").setValue(null);
            reference.child(onlineUserID).child("tomorrow_task").setValue(null);
            MainActivity.sta_hmap_model.put("today_task", null);
            MainActivity.sta_hmap_model.put("tomorrow_task", null);
            db.collection("users").document(user.getUid()).update("checkStar",false);
        } else if ((MainActivity.sta_hmap_model.get("today_task").get(0).getDate()).compareTo(cur_date) == 1) {

            reference.child(onlineUserID).child("yesterday_task").setValue(null);
            reference.child(onlineUserID).child("today_task").setValue(null);
            reference.child(onlineUserID).child("tomorrow_task").setValue(null);
            MainActivity.sta_hmap_model.put("yesterday_task", null);
            MainActivity.sta_hmap_model.put("today_task", null);
            MainActivity.sta_hmap_model.put("tomorrow_task", null);
            db.collection("users").document(user.getUid()).update("checkStar",false);
        }
    }


    public void updateFragmentHomeViewPagerView() {

        if (fragmentAdapter == null) {
            return;
        }
        fragmentAdapter.clearItem();

        for (int i = 0; i < 3; i++) {
            TodoFragment todoFragment = new TodoFragment();
            Bundle bundle = new Bundle();
            ArrayList<Model> var = new ArrayList<>();

            if (i == 0) {
                String str = "";
                var = MainActivity.sta_hmap_model.get("yesterday_task");
                if (var != null) {
                    for (int k = 0; k < var.size(); k++) {
                        str = str + var.get(k).getTask() + "\n";
                    }
                }
                bundle.putString("task", str);
                bundle.putString("day","Yesterday");
            } else if (i == 1) {
                String str = "";
                var = MainActivity.sta_hmap_model.get("today_task");
                if (var != null) {
                    for (int k = 0; k < var.size(); k++) {
                        str = str + var.get(k).getTask() + "\n";
                    }
                }
                Log.d("지우", str);
                bundle.putString("task", str);
                bundle.putString("day","Today");
            } else {
                String str = "";
                var = MainActivity.sta_hmap_model.get("tomorrow_task");
                if (var != null) {
                    for (int k = 0; k < var.size(); k++) {
                        str = str + var.get(k).getTask() + "\n";
                        //Log.d("지우",str);
                    }
                }
                bundle.putString("task", str);
                bundle.putString("day","Tomorrow");
            }
            todoFragment.setArguments(bundle);
            fragmentAdapter.addItem(todoFragment);
        }
        fragmentAdapter.notifyDataSetChanged();

    }

    private void moveRecord(DatabaseReference fromPath, final DatabaseReference toPath) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            Log.d(TAG, "Success!");
                        } else {
                            Log.d(TAG, "Copy failed!");
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage()); //Don't ignore potential errors!
            }
        };
        fromPath.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 1:
                refresh();
                break;
        }

    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }

    public void checkTodo() {
        AtomicInteger checkcount = new AtomicInteger();
        Dialog dialog;
        dialog = new Dialog(getContext());       // Dialog 초기화
        dialog.setContentView(R.layout.task_dialog);             // xml 레이아웃 파일과 연결
        ImageView finished_image = dialog.findViewById(R.id.Finished_ConsView);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // db.collection("users").document(user.getUid()).update("currentCount",count);

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                try {
                    if((Boolean) task.getResult().get("checkStar") == false){
                        for (int i = 0; i < MainActivity.sta_hmap_model.get("today_task").size(); i++) {
                            if (MainActivity.sta_hmap_model.get("today_task").get(i).isCheck() == true) {
                                checkcount.getAndIncrement();
                                if (checkcount.get() == MainActivity.sta_hmap_model.get("today_task").size()) {
                                    if (Integer.parseInt(String.valueOf(task.getResult().get("currentCount"))) < MainActivity.constellation.getScount()) { //하루지난거랑 연계작업
                                        playSoundId=soundManager.playSound(0);
                                        createImageView(Integer.parseInt(String.valueOf(task.getResult().get("currentCount"))));
                                        db.collection("users").document(user.getUid()).update("checkStar",true);
                                        db.collection("users").document(user.getUid()).update("currentCount",(Integer.parseInt(String.valueOf(task.getResult().get("currentCount"))))+1);
                                        if (Integer.parseInt(String.valueOf(task.getResult().get("currentCount"))) == MainActivity.constellation.getScount()) {
                                            db.collection("users").document(user.getUid()).update("currentCount",0);
                                            finished_cons_code.add(MainActivity.constellation.getCons_code()); //별을 다찍을시 해당 별코드를 memberinfo에 정수배열 저장
                                            Log.d("별별", "" + finished_cons);
                                            db.collection("users").document(user.getUid()).update("finished_cons", finished_cons_code); //memberinfo 필드 업데이트

                                            for (int index = 0; index < finished_cons.size(); index++) {
                                                System.out.println("별자리코드 출력" + finished_cons.get(index));    //잘 누적되는지 chk
                                            }
                                            SetConstellation setConstellation = new SetConstellation();
                                            for (int j = 0; j < MainActivity.Cons_Code.length; j++) {
                                                if (String.valueOf(j).equals(MainActivity.constellation.getCons_code())) {
                                                    finished_position = j;//완성한 별자리의 포지션 가져옴
                                                    System.out.println("포지션 : " + finished_position);
                                                    System.out.println("con : " + setConstellation.con[finished_position]);
                                                    finished_image.setImageResource((setConstellation.con[finished_position])); //다이얼로그에 이미지뷰 완성된 별자리로 set
                                                }
                                            }

                                            dialog.show();
                                            dialog.setCancelable(false);
                                            Button OkButton = dialog.findViewById(R.id.OkButton);
                                            OkButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    dialog.dismiss();
                                                    Intent intent = new Intent(getActivity(), SetConstellation.class);
                                                    startActivity(intent);
                                                }

                                            });

                                        }
                                    } else {
                                        Log.d("지우", "꽉참");
                                    }
                                }
                            }
                        }
                    }

                } catch (NullPointerException e) {
                }
                //other stuff
            } else {
                //deal with error
            }
        });
    }

    public void drawStar(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task_1) {
                if (task_1.isSuccessful()) {
                    DocumentSnapshot document = task_1.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
                                if (task.isSuccessful() && (task.getResult() != null)) {
                                    for(int i=0; i<(Integer.parseInt(String.valueOf(task.getResult().get("currentCount"))));i++)
                                    {
                                        createImageView(i);
                                    }
                                }
                            });

                        } else {
                            Log.d(TAG, "No such document");

                        }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task_1.getException());
                }
            }
        });


    }

    public void refresh() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }


}





