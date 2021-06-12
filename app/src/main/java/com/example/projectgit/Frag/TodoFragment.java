package com.example.projectgit.Frag;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.projectgit.Data.Model;
import com.example.projectgit.R;
import com.example.projectgit.main.MainActivity;

import java.util.ArrayList;

public class TodoFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);
        Log.d("지우","TodoFrag가 언제지");

        TextView textView = view.findViewById(R.id.todotv);
        TextView daytextView = view.findViewById(R.id.daytv);
        textView.setMovementMethod(new ScrollingMovementMethod());


        if (getArguments() != null) {
            Bundle task_bundle = getArguments();
            Bundle day_bundle =getArguments();

            final SpannableStringBuilder ssb = new SpannableStringBuilder(task_bundle.getString("task"));
            int index = 0;
            int ilength = 0;

            ArrayList<Model> var = new ArrayList<>();
            ArrayList<Model> var2 = new ArrayList<>();
            ArrayList<Model> var3 = new ArrayList<>();

            var = MainActivity.sta_hmap_model.get("yesterday_task");
            var2 = MainActivity.sta_hmap_model.get("today_task");
            var3 = MainActivity.sta_hmap_model.get("tomorrow_task");
            if (var != null) {
                for (int k = 0; k < var.size(); k++) {
                    if(task_bundle.getString("task").contains(var.get(k).getTask()) && var.get(k).isCheck() == true){
                        index = task_bundle.getString("task").indexOf(var.get(k).getTask());
                        ilength = var.get(k).getTask().length();                    }
                    ssb.setSpan(new StrikethroughSpan(),index,index + ilength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

            }

            if (var2 != null) {
                for (int k = 0; k < var2.size(); k++) {
                    if(task_bundle.getString("task").contains(var2.get(k).getTask()) && var2.get(k).isCheck() == true){
                        index = task_bundle.getString("task").indexOf(var2.get(k).getTask());
                        ilength = var2.get(k).getTask().length();                    }
                    ssb.setSpan(new StrikethroughSpan(),index,index + ilength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

            }

            if (var3 != null) {
                for (int k = 0; k < var3.size(); k++) {
                    if(task_bundle.getString("task").contains(var3.get(k).getTask()) && var3.get(k).isCheck() == true){
                        index = task_bundle.getString("task").indexOf(var3.get(k).getTask());
                        ilength = var3.get(k).getTask().length();                    }
                    ssb.setSpan(new StrikethroughSpan(),index,index + ilength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            textView.setText(task_bundle.getString("task"));
            daytextView.setText(day_bundle.getString("day"));
            textView.setText(ssb);
        }

        return view;
    }

}
