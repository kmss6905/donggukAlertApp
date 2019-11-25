package com.example.donggukalertapp.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class PagerAdapter extends FragmentPagerAdapter {


    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    // 해당 메소드를 통해 어뎁터에 프래그먼트를 추가시킵니다.
    public void addFragment(Fragment fragment, String title){
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    public PagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }





    // 선택된 포지션 마다 관리되고 있는 프래그먼트 리스트에서 프래그먼트를 반환함
    @Override
    public Fragment getItem(int position) { return mFragmentList.get(position); } //


    // 어뎁터가 관리하고 있는 프래그먼트 총 개수
    @Override
    public int getCount() {
        return mFragmentList.size() ;
    }

}
