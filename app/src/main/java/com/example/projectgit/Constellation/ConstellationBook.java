package com.example.projectgit.Constellation;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.projectgit.R;
import com.example.projectgit.Adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
public class ConstellationBook extends AppCompatActivity {
    private FragmentPagerAdapter fragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constellation_book);


        //뷰페이저 세팅
        ViewPager viewPager = findViewById(R.id.viewPager_todo);
        fragmentPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }
}