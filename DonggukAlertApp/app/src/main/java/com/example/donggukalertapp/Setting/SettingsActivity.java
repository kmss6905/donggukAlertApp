package com.example.donggukalertapp.Setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import com.example.donggukalertapp.R;

public class SettingsActivity extends AppCompatActivity{
    private static final String TAG = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("설정");
        }
    }


    public static class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            Boolean keyword = sharedPreferences.getBoolean("keyword", true);
            Log.d(TAG, "onCreatePreferences: keyword : " + keyword);
        }


        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("keyword")) {
                Log.i(TAG, "Preference value was updated to: " + sharedPreferences.getBoolean(key, true));
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();

        overridePendingTransition(0, 0); // 전환 애니메이션 없앰

    }



    // 메뉴
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            Boolean keyword = sharedPreferences.getBoolean("keyword", true);
            Log.d(TAG, "onCreatePreferences: keyword : " + keyword);

            Boolean keyword_all = sharedPreferences.getBoolean("keyword_all", true);
            Log.d(TAG, "onCreatePreferences: keyword_all : " + keyword_all);

            Boolean weather = sharedPreferences.getBoolean("weather", true);
            Log.d(TAG, "onCreatePreferences: weather : " + weather);

    }
}