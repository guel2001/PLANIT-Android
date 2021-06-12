package com.example.projectgit.Frag;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.projectgit.R;

//
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.example.projectgit.Adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class FragmentCollection extends Fragment {
    private FragmentPagerAdapter fragmentPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection, container, false);

        //뷰페이저 세팅
        ViewPager viewPager = view.findViewById(R.id.viewPager);
        fragmentPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }
}