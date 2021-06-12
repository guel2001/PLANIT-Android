package com.example.projectgit.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.projectgit.Frag.FragFall;
import com.example.projectgit.Frag.FragSpring;
import com.example.projectgit.Frag.FragSummer;
import com.example.projectgit.Frag.FragWinter;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }


    //프래그먼트 교체를 보여주는 처리 구현
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return FragSpring.newInstance();
            case 1:
                return FragSummer.newInstance();
            case 2:
                return FragFall.newInstance();
            case 3:
                return FragWinter.newInstance();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 4;
    }

    //상단 탭 레이아웃 인디케이터 쪽 텍스트 선언
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0:
                return "Spring";
            case 1:
                return "Summer";
            case 2:
                return "Fall";
            case 3:
                return "Winter";
            default:
                return null;
        }
    }
}
