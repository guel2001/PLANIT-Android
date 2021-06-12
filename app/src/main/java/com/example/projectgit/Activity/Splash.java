package com.example.projectgit.Activity;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
;

import android.os.Bundle;
import android.util.Log;


import com.example.projectgit.Login.LoginActivity;
import com.example.projectgit.main.MainActivity;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        SharedPreferences pref = getSharedPreferences("mine", MODE_PRIVATE);

        if(pref.contains("login")) {
            if (pref.getString("login", null).contains("true")) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            } else {
                Log.d("로그인", "노우노우");
            }
        }
        finish();
    }
    @Override
    public void onBackPressed() {
        //초반 플래시 화면에서 넘어갈때 뒤로가기 버튼 못누르게 함
    }

}
