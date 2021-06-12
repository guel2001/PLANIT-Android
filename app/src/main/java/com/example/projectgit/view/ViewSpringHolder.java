package com.example.projectgit.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectgit.Data.DataSpring;
import com.example.projectgit.R;

public class ViewSpringHolder extends  RecyclerView.ViewHolder {

    ImageView item_spring_iv;


    public ViewSpringHolder(@NonNull View itemView) {
        super(itemView);

        item_spring_iv = itemView.findViewById(R.id.item_spring_iv);

    }

    public void onBind(DataSpring data){
        item_spring_iv.setImageResource(data.getImage());
    }
}
