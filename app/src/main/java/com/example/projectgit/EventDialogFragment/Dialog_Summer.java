package com.example.projectgit.EventDialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.projectgit.R;
import com.example.projectgit.Activity.RewardActivity;
import com.example.projectgit.main.MainActivity;

import javax.annotation.Nullable;

public class Dialog_Summer extends DialogFragment implements View.OnClickListener{

    public static final String TAG_EVENT_DIALOG = "dialog_event";
    int pos;

    public Dialog_Summer() {

    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }


    public static Dialog_Summer getInstance(){
        Dialog_Summer e = new Dialog_Summer();
        return e;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.constellation_dialog, container, false);
        ImageView  imageView = (ImageView) view.findViewById(R.id.imageView3);
        TextView textView  = (TextView) view.findViewById(R.id.cons_infoView);
        TextView consnametv  = (TextView) view.findViewById(R.id.consnametv);
        Button mConfirmBtn = (Button) view.findViewById(R.id.btn_confirm);
        Button rewardBtn = (Button) view.findViewById(R.id.btn_reward);
        getPosition(pos,imageView,textView,rewardBtn,consnametv);
        mConfirmBtn.setOnClickListener(this);
        setCancelable(false);
        return view;
    }
    @Override
    public void onClick(View view){

        dismiss();
    }

    public void getPosition(int pos,ImageView imageView,TextView textView,Button button,TextView consname){
        switch(pos){
            case 0:
                consname.setText("전갈자리");
               imageView.setImageResource(R.drawable.book_scorpius);
               textView.setText(getString(R.string.scorpio_info));
                btn_reward(button,9); //별자리코드 =pos
               break;
            case 1:
                consname.setText("궁수자리");
                imageView.setImageResource(R.drawable.book_sagittarius);
                textView.setText(getString(R.string.sagittarius_info));
                btn_reward(button,8); //별자리코드 =pos
                break;
            case 2:
                consname.setText("염소자리");
                imageView.setImageResource(R.drawable.book_capricorn);
                textView.setText(getString(R.string.capricorn_info));
                btn_reward(button,3); //별자리코드 =pos
                break;

        }
    }
    protected void btn_reward(Button button,int current_cons_Code){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RewardActivity.class);
                startActivity(intent);
                MainActivity.cons_reward= current_cons_Code;
            }
        });
    }
}
