package com.example.donggukalertapp.Setting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.donggukalertapp.R;
import com.example.donggukalertapp.Whether.AirPollutionActivity;
import com.example.donggukalertapp.Whether.ShortWeatherActivity;

import org.w3c.dom.Text;

import java.sql.Array;
import java.util.LinkedList;

public class AddwordActivity extends AppCompatActivity {
    private static final String TAG = "AddwordActivity";


    EditText insertKeyWord;
    Button btnDelete;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    int toggle; // 삭제버튼 눌렀는지 판별
    int count;

    // 리스트뷰 추가
    LinkedList<String> keywordList;
    ArrayAdapter<String> mAdapter;
    ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addword);
        init(); //변수 선언 메소드
        delete(); // 단어 삭제 메소드
    }

    //단어 추가
    public void addKeyWord(View v){ // 단어 추가
        int a = 0; // 목록에 입력한 단어와 중복되면 1, 중복 안되면 0으로 판단, 버튼 누를 때마다 0으로 초기화
        String getKeyword = insertKeyWord.getText().toString();
        getKeyword = getKeyword.trim();
        if(getKeyword.getBytes().length <= 0){
            Toast.makeText(getApplicationContext(), "단어를 입력해주세요", Toast.LENGTH_SHORT).show();
        } else {
            if (count == 0) {
                count++;   // keyword1 ~ keyword5 : 총 5개
                editor.putString("count", Integer.toString(count));
                editor.putString("keyword" + count, getKeyword);
                editor.commit(); // 입력한 단어 쉐어드에 저장 : keyword1
                keywordList.add(settings.getString("keyword"+count, "")); // 리스트뷰에 추가
                mAdapter.notifyDataSetChanged();
                insertKeyWord.setText("");
            } else if (count > 0 && count < 5) {
                count++;
                editor.putString("keyword" + count, getKeyword);
                for (int i = 1; i < count; i++) { // 입력한 단어가 목록에 있는지 중복 검사
                    if (settings.getString("keyword"+i, "").equals(getKeyword)) {
                        Toast.makeText(getApplicationContext(), "이미 추가하신 관심사입니다", Toast.LENGTH_SHORT).show();
                        insertKeyWord.setText("");
                        count--;
                        a = 1; // 중복되었을 때  a= 1 , 중복 안되면 a=0 이므로 밑의 코드로 이동
                    }
                }
                if (a == 0) {
                    editor.putString("keyword" + count, getKeyword);
                    editor.putString("count", Integer.toString(count));
                    editor.commit(); // 쉐어드에 저장
                    keywordList.add(settings.getString("keyword"+count, "")); // 리스트뷰에 추가
                    mAdapter.notifyDataSetChanged();
                    insertKeyWord.setText("");
                }
            } else if (count > 4) {  // 단어가 5개 넘어가는 경우
                //keyword[count].setText("");
                Toast.makeText(getApplicationContext(), "최대 5개까지 등록 가능합니다.", Toast.LENGTH_SHORT).show();
                insertKeyWord.setText("");
            }
        }
    }

    //단어 삭제
    public void deleteKeyword(View v) {
        if (count > 0) {
            if (toggle == 0) { //삭제 시작한 경우
                editor.clear();
                editor.commit(); // 쉐어드 내용 전부 초기화
                toggle = 1;
                btnDelete.setText("완료");
                Toast.makeText(getApplicationContext(), "삭제할 단어를 누르고 완료버튼을 눌러주세요!", Toast.LENGTH_LONG).show();
            } else if(toggle ==1 ) { // 삭제 완료한 경우
                for(int i = 1; i <= count ; i++){
                    editor.putString("keyword" + i, mAdapter.getItem(i-1));
                    editor.commit();
                }
                editor.putString("count", Integer.toString(count));
                editor.commit(); // 다시 값 넣어주기
                toggle = 0;
                Toast.makeText(getApplicationContext(), "삭제가 완료되었습니다", Toast.LENGTH_LONG).show();
                btnDelete.setText("삭제");
            }
        } else if(count <=0){
            if(toggle== 1) { //전부 삭제한 경우
                Toast.makeText(getApplicationContext(), "삭제가 완료되었습니다", Toast.LENGTH_LONG).show();
                btnDelete.setText("삭제");
            } else { // 애초에 단어가 없는경우
                Toast.makeText(getApplicationContext(), "삭제할 단어가 없습니다.", Toast.LENGTH_LONG).show();
            }

        }
    }

    public void delete() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(toggle) {
                    case 1:
                        keywordList.remove(position);
                        mAdapter.notifyDataSetChanged();
                        count--;
                        editor.putString("count", Integer.toString(count));
                        editor.commit();
                }
            }
        });
    }

//    public void show(View view){ // 저장된 거 확인하는 코드
//        for(int i = 1; i <=count; i ++){
//            keywordList.add(settings.getString("keyword"+i, ""));
//            mAdapter.notifyDataSetChanged();
//        }
//        keywordList.add(settings.getString("count", ""));
//    }

    //변수 선언
    public void init(){
        insertKeyWord = (EditText)findViewById(R.id.insertKeyword);
        btnDelete = (Button)findViewById(R.id.deleteButton);

        count = 0;
        toggle = 0;
        settings = getSharedPreferences("Keyword", 0);
        editor = settings.edit();

        //listview
        mListView = (ListView)findViewById(R.id.listView);
        keywordList = new LinkedList<>();
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,keywordList);
        mListView.setAdapter(mAdapter);


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

            case R.id.setting_setting:

                break;

            case R.id.setting_like:
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0); // 액티비티 전환효과 없앰
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences = getSharedPreferences("Keyword", MODE_PRIVATE);
        String count = sharedPreferences.getString("count","");
        Log.d(TAG, "onStop: count : " + count);
        for(int i = 1; i <= Integer.parseInt(count); i++){
            Log.d(TAG, "onStop: " + i + " 번쨰 " + sharedPreferences.getString("keyword"+i,""));
        }
    }
}