package com.example.donggukalertapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;

import com.example.donggukalertapp.Adapter.PagerAdapter;
import com.example.donggukalertapp.Fragment.AdmissionNoticeFragment;
import com.example.donggukalertapp.Fragment.BachelorNoticeFragment;
import com.example.donggukalertapp.Fragment.EventNoticeFragment;
import com.example.donggukalertapp.Fragment.GeneralNoticeFragment;
import com.example.donggukalertapp.Fragment.InternationalNoticeFragment;
import com.example.donggukalertapp.Fragment.ScholarshipNoticeFragment;
import com.example.donggukalertapp.Setting.AddwordActivity;
import com.example.donggukalertapp.Whether.AirPollutionActivity;
import com.example.donggukalertapp.Whether.ShortWeather;
import com.example.donggukalertapp.Whether.ShortWeatherActivity;
import com.google.android.material.tabs.TabLayout;

/**
 * 2019 / 11 / 15
 *  초기 목록 화면입니다
 *  텝레이아웃 + 뷰페이저 + 프래그먼트
 */

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private ViewPager mViewPager;
    PagerAdapter adapter;  // 프래그먼트 페이저 어뎁터


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        adapter = new PagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(mViewPager);


    }

    // 퓨페이저 세팅
    public void setupViewPager(ViewPager viewPager) {
        // 뷰페이저어뎁터에 관리할 프래그먼트6개를 추가
        adapter.addFragment(new GeneralNoticeFragment(), "일반");
        adapter.addFragment(new BachelorNoticeFragment(), "학사");
        adapter.addFragment(new ScholarshipNoticeFragment(), "장학");
        adapter.addFragment(new AdmissionNoticeFragment(), "입시");
        adapter.addFragment(new InternationalNoticeFragment(), "국제");
        adapter.addFragment(new EventNoticeFragment(), "학술/행사");

        // 뷰페이저에 어뎁터를 세팅합니다.
        viewPager.setAdapter(adapter);
    }



    // 옵션 메뉴
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }



    // 메뉴 클릭
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.weather: // 날씨 클릭
                Intent intent1 = new Intent(getApplicationContext(), ShortWeatherActivity.class);
                startActivity(intent1);
                break;

            case R.id.air_poll: // 대기오염 클릭
                Intent intent = new Intent(getApplicationContext(), AirPollutionActivity.class);
                startActivity(intent);
                break;

            case R.id.setting_like: // 관심사 목록
                Intent intent2 = new Intent(getApplicationContext(), AddwordActivity.class);
                startActivity(intent2);
                break;
            case R.id.setting_setting: // 설정
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
