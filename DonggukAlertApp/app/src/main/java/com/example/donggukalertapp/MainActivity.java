package com.example.donggukalertapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.Toast;

import com.example.donggukalertapp.Adapter.PagerAdapter;
import com.example.donggukalertapp.Fragment.AdmissionNoticeFragment;
import com.example.donggukalertapp.Fragment.BachelorNoticeFragment;
import com.example.donggukalertapp.Fragment.EventNoticeFragment;
import com.example.donggukalertapp.Fragment.GeneralNoticeFragment;
import com.example.donggukalertapp.Fragment.InternationalNoticeFragment;
import com.example.donggukalertapp.Fragment.ScholarshipNoticeFragment;
import com.example.donggukalertapp.Service.MyService;
import com.example.donggukalertapp.Setting.AddwordActivity;
import com.example.donggukalertapp.Setting.SettingsActivity;
import com.example.donggukalertapp.Whether.AirPollutionActivity;
import com.example.donggukalertapp.Whether.ShortWeather;
import com.example.donggukalertapp.Whether.ShortWeatherActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            NotificationChannel channel1 = new NotificationChannel(String.valueOf(R.string.default_notification_channel_id), "게시물 알림", NotificationManager.IMPORTANCE_HIGH);
            NotificationChannel channel2 = new NotificationChannel(String.valueOf(R.string.default_notification_channel_id_weather), "날씨 알림", NotificationManager.IMPORTANCE_HIGH);
            ArrayList<NotificationChannel> chans = new ArrayList<>();
            chans.add(channel1);
            chans.add(channel2);

            notificationManager.createNotificationChannels(chans);


//
//            // Create channel to show notifications.
//            String channelId  = getString(R.string.default_notification_channel_id);
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
//                    "게시물 알람", NotificationManager.IMPORTANCE_HIGH));
//
//
//            String channelId2  = getString(R.string.default_notification_channel_id_weather);
//            NotificationManager notificationManager =
//                    getSystemService(NotificationManager.class);
//            notificationManager2.createNotificationChannel(new NotificationChannel(channelId2,
//                    "날씨 알람", NotificationManager.IMPORTANCE_HIGH));
        }

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }
        // [END handle_data_extras]

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(task.isSuccessful()){
                            String token=task.getResult().getToken();
                            Log.d(TAG, "onComplete: Token: "+token);
                        }else{
                            Log.d(TAG, "onComplete: " + "Token generation failed");
                        }

                    }
                });



        FirebaseMessaging.getInstance().subscribeToTopic("test-topic")
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                        Log.d(TAG, "onCreate: " + "success");
                    else
                        Log.d(TAG, "onCreate: " + "fail");
                });

        FirebaseMessaging.getInstance().subscribeToTopic("weather")
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                        Log.d(TAG, "onCreate: " + "success");
                    else
                        Log.d(TAG, "onCreate: " + "fail");
                });




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

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        // 저장된 설정값 가져옴
        Boolean keyword = sharedPreferences.getBoolean("keyword",true);
        Log.d(TAG, "onCreatePreferences: keyword : " + keyword);
        Boolean keyword_all = sharedPreferences.getBoolean("keyword_all", true);
        Log.d(TAG, "onCreatePreferences: keyword_all : " + keyword_all);
        Boolean weather = sharedPreferences.getBoolean("weather", true);
        Log.d(TAG, "onCreatePreferences: weather : " + weather);


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
                intent2.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION); // 액티비티 전환 효과 없앰
                startActivity(intent2);
                break;
            case R.id.setting_setting: // 설정
                Intent intent3 = new Intent(getApplicationContext(), SettingsActivity.class);
                intent3.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION); // 액티비티 전환 효과 없앰

                startActivity(intent3);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
