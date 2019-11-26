package com.example.donggukalertapp.Setting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import java.util.LinkedList;

public class AddwordActivity extends AppCompatActivity {

    TextView[] keyword = new TextView[10];
    EditText insertKeyWord;
    LinearLayout layout;
    Button btnDelete;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    int toggle;
    int count;

    // 리스트뷰 추가
    LinkedList<String> keywordList;
    ArrayAdapter<String> mAdapter;
    ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addword);

        getSupportActionBar().setTitle("관심사목록");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
        delete();
    }

    //단어 추가
    public void addKeyWord(View v){
        int a = 0;
        String getKeyword = insertKeyWord.getText().toString();
        getKeyword = getKeyword.trim();
        if(getKeyword.getBytes().length <= 0){
            Toast.makeText(getApplicationContext(), "단어를 입력해주세요", Toast.LENGTH_SHORT).show();
        } else {
            if (count == 0) {
                count++;
                keyword[count].setText(getKeyword);
                keywordList.add(keyword[count].getText().toString());
                mAdapter.notifyDataSetChanged();
                editor.putString("keyword" + count, getKeyword);
                editor.commit();
                insertKeyWord.setText("");
            } else if (count > 0 && count <= 5) {
                count++;
                keyword[count].setText(getKeyword);
                for (int i = 1; i < count; i++) {
                    if (keyword[i].getText().toString().equals(keyword[count].getText().toString())) {
                        Toast.makeText(getApplicationContext(), "이미 추가하신 관심사입니다", Toast.LENGTH_SHORT).show();
                        insertKeyWord.setText("");
                        a = 1;
                    }
                }
                if (a == 0) {
                    keywordList.add( keyword[count].getText().toString());
                    mAdapter.notifyDataSetChanged();
                    editor.putString("keyword" + count, getKeyword);
                    editor.commit();
                    insertKeyWord.setText("");
                }
            } else if (count > 5) {
                keyword[count].setText("");
                Toast.makeText(getApplicationContext(), "최대 5개까지 등록 가능합니다.", Toast.LENGTH_SHORT).show();
                insertKeyWord.setText("");
            }
        }
    }

    //단어 삭제
    public void deleteKeyword(View v) {
        if (count > 0) {
            if (toggle == 0) { //삭제를 하려는 경우
                //keyword[i] , shared 초기화
                for(int i = 1; i <= count ; i++){
                    keyword[i].setText("");
                    editor.putString("keyword" + i, "");
                }
                toggle = 1;
                btnDelete.setText("완료");
                Toast.makeText(getApplicationContext(), "삭제할 단어를 누르고 완료버튼을 눌러주세요!", Toast.LENGTH_LONG).show();
            } else if(toggle ==1 ) { // 삭제 완료한 경우
                //다시 넣어주기
                for(int i = 1; i <= count ; i++){
                    keyword[i].setText(keywordList.get(i));
                    editor.putString("keyword" + i, keywordList.get(i));
                }
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
                }
            }
        });
    }

    public void show(View v){

        for(int i =1 ; i <= count; i++){
            keywordList.add(settings.getString("keyword"+i,""));
            mAdapter.notifyDataSetChanged();
        }
    }

    //변수 선언
    public void init(){
        insertKeyWord = (EditText)findViewById(R.id.insertKeyword);
        keyword = new TextView[7];
        btnDelete = (Button)findViewById(R.id.deleteButton);


        for(int i = 1; i <7; i ++){
            keyword[i] = new TextView(this);
            keyword[i].setTextSize(30);
            keyword[i].setTextColor(Color.BLACK);
        }
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

                break;

            case android.R.id.home: // 뒤로가기
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
