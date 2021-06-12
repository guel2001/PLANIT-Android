package com.example.projectgit.Activity;



import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectgit.R;
import com.example.projectgit.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.panxw.android.imageindicator.ImageIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class RewardActivity extends AppCompatActivity {
    private ImageIndicatorView imageIndicatorView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Main에서 reward_cons 번호 업데이트됨 별자리북 에서 선택한 별자리 코드 static으로 담아옴
        setContentView(R.layout.activity_reward);
        this.imageIndicatorView = (ImageIndicatorView) findViewById(R.id.guide_indicate_view);
        Button btn_order = findViewById(R.id.orderbutton);
        //별자리코드마다 명함 3개씩 리스트 저장
        btn_order.setEnabled(false);

        Map<String, ArrayList<Integer>> card_list= new HashMap<String,ArrayList<Integer>>();

        //별자리 명함 디자인 사진 리스트 넣기
        ArrayList<Integer> aquarius = new ArrayList<Integer>();
        aquarius.add(R.drawable.card_aquarius1);    //aquarius 별자리의 카드 id 넣으면 됨
        aquarius.add(R.drawable.card_aquarius2);
        aquarius.add(R.drawable.card_aquarius3);
        card_list.put("0",aquarius);    //이런식으로 12개 별자리 put 해준다 card_list 해시맵에....

        ArrayList<Integer> aries = new ArrayList<Integer>();
        aries.add(R.drawable.card_aries1);
        aries.add(R.drawable.card_aries2);
        aries.add(R.drawable.card_aries3);
        card_list.put("1",aries);

        ArrayList<Integer> cancer = new ArrayList<Integer>();
        cancer.add(R.drawable.card_cancer1);
        cancer.add(R.drawable.card_cancer2);
        cancer.add(R.drawable.card_cancer3);
        card_list.put("2",cancer);

        ArrayList<Integer> capricorn = new ArrayList<Integer>();
        capricorn.add(R.drawable.card_capricorn1);
        capricorn.add(R.drawable.card_capricorn2);
        capricorn.add(R.drawable.card_capricorn3);
        card_list.put("3",capricorn);

        ArrayList<Integer> gemini = new ArrayList<Integer>();
        gemini.add(R.drawable.card_gemini1);
        gemini.add(R.drawable.card_gemini2);
        gemini.add(R.drawable.card_gemini3);
        card_list.put("4",gemini);

        ArrayList<Integer> leo = new ArrayList<Integer>();
        leo.add(R.drawable.card_leo1);
        leo.add(R.drawable.card_leo2);
        leo.add(R.drawable.card_leo3);
        card_list.put("5",leo);

        ArrayList<Integer> libra = new ArrayList<Integer>();
        libra.add(R.drawable.card_libra1);
        libra.add(R.drawable.card_libra2);
        libra.add(R.drawable.card_libra3);
        card_list.put("6",libra);

        ArrayList<Integer> pisces = new ArrayList<Integer>();
        pisces.add(R.drawable.card_pisces1);
        pisces.add(R.drawable.card_pisces2);
        pisces.add(R.drawable.card_pisces3);
        card_list.put("7",pisces);

        ArrayList<Integer> sagittarius= new ArrayList<Integer>();
        sagittarius.add(R.drawable.card_sagittarius1);
        sagittarius.add(R.drawable.card_sagittarius2);
        sagittarius.add(R.drawable.card_sagittarius3);
        card_list.put("8",sagittarius);

        ArrayList<Integer> scorpio = new ArrayList<Integer>();
        scorpio.add(R.drawable.card_scorpio1);
        scorpio.add(R.drawable.card_scorpio2);
        scorpio.add(R.drawable.card_scorpio3);
        card_list.put("9",scorpio);

        ArrayList<Integer> taurus = new ArrayList<Integer>();
        taurus.add(R.drawable.card_taurus1);
        taurus.add(R.drawable.card_taurus2);
        taurus.add(R.drawable.card_taurus3);
        card_list.put("10",taurus);

        ArrayList<Integer> virgo = new ArrayList<Integer>();
        virgo.add(R.drawable.card_virgo1);
        virgo.add(R.drawable.card_virgo2);
        virgo.add(R.drawable.card_virgo3);
        card_list.put("11",virgo);


        if(card_list.containsKey(String.valueOf(MainActivity.cons_reward))){
            final Integer[] resArray = new Integer[] {
                    card_list.get(String.valueOf(MainActivity.cons_reward)).get(0),
                    card_list.get(String.valueOf(MainActivity.cons_reward)).get(1),
                    card_list.get(String.valueOf(MainActivity.cons_reward)).get(2)
            };

            imageIndicatorView.setupLayoutByDrawable(resArray);
            imageIndicatorView.setIndicateStyle(ImageIndicatorView.INDICATE_USERGUIDE_STYLE);
            imageIndicatorView.show();
        }

        //배송 정보 처리
        EditText addrText = findViewById(R.id.addrEdit);
        EditText nameText = findViewById(R.id.addrName);
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            addrText.setText(document.getData().get("address").toString());
                            nameText.setText(document.getData().get("name").toString());
                        }
                    }
                }
            }
        });

        //스피너 처리
        final Spinner spinner_field = (Spinner) findViewById(R.id.spinner_field);
        String[] str = getResources().getStringArray(R.array.spinnerArray);


        final ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,R.layout.spinner_item,str);

        spinner_field.setSelection(0,false);

        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        spinner_field.setAdapter(adapter);

        //spinner 이벤트 리스너

        spinner_field.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(spinner_field.getSelectedItemPosition() > 0){
                    //선택된 항목
                    btn_order.setEnabled(true); //명함을 선택해야만 주문버튼이 활성화되게함
                    Log.v("알림",spinner_field.getSelectedItem().toString()+ "is selected");

                }
                if(spinner_field.getSelectedItemPosition() == 0){
                    btn_order.setEnabled(false);
                }

            }

            @Override

            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });



        btn_order.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Dialog dialog;
                dialog = new Dialog(RewardActivity.this);       // Dialog 초기화
                dialog.setContentView(R.layout.reward_dialog);
                // dlg.setIcon(R.drawable.deum); // 아이콘 설정
//                버튼 클릭시 동작
                dialog.show();
                dialog.setCancelable(false);
                Button OkButton = dialog.findViewById(R.id.dialog_button_ok);
                OkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        finish();
                        /*
                        Intent intent = new Intent(RewardActivity.this, MainActivity.class);
                        startActivity(intent);
                         */
                    }
            }
        );

    }
});
    }
}

