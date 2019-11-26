package com.example.donggukalertapp.Whether;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.donggukalertapp.R;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;

public class ShortWeatherActivity extends AppCompatActivity {

    //TextView textView_shortWeather;

    TextView textView_day, textView_condition, textView_rainfall, textView_ozonzisu, textView_pm10value, textView_pm25value;
    TextView textView_hour_1,textView_temp1, textView_hour_2, textView_temp2, textView_hour_3, textView_temp3, textView_hour_4, textView_temp4, textView_hour_5, textView_temp5, textView_hour_6, textView_temp6, textView_hour_7, textView_temp7, textView_hour_8, textView_temp8;
    TextView textView_rainfall_1, textView_rainfall_2, textView_rainfall_3, textView_rainfall_4, textView_rainfall_5, textView_rainfall_6, textView_rainfall_7, textView_rainfall_8;
    TextView textView_condition_1, textView_condition_2, textView_condition_3, textView_condition_4, textView_condition_5, textView_condition_6, textView_condition_7, textView_condition_8;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_weather);
/*
        textView_day = (TextView)findViewById(R.id.Day); textView_condition = (TextView)findViewById(R.id.Condition); textView_rainfall = (TextView)findViewById(R.id.rainfall);
        textView_ozonzisu = (TextView)findViewById(R.id.ozonzisu); textView_pm10value = (TextView)findViewById(R.id.pm10value); textView_pm25value = (TextView)findViewById(R.id.pm25value);
*/

        this.getSupportActionBar().setTitle("날씨정보");
        //액션바 뒤로가기 버튼 추가
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textView_hour_1 = (TextView)findViewById(R.id.hour_1); textView_temp1 = (TextView)findViewById(R.id.temp1);
        textView_hour_2 = (TextView)findViewById(R.id.hour_2); textView_temp2 = (TextView)findViewById(R.id.temp2);
        textView_hour_3 = (TextView)findViewById(R.id.hour_3); textView_temp3 = (TextView)findViewById(R.id.temp3);
        textView_hour_4 = (TextView)findViewById(R.id.hour_4); textView_temp4 = (TextView)findViewById(R.id.temp4);
        textView_hour_5 = (TextView)findViewById(R.id.hour_5); textView_temp5 = (TextView)findViewById(R.id.temp5);
        textView_hour_6 = (TextView)findViewById(R.id.hour_6); textView_temp6 = (TextView)findViewById(R.id.temp6);
        textView_hour_7 = (TextView)findViewById(R.id.hour_7); textView_temp7 = (TextView)findViewById(R.id.temp7);
        textView_hour_8 = (TextView)findViewById(R.id.hour_8); textView_temp8 = (TextView)findViewById(R.id.temp8);

        textView_rainfall_1 = (TextView)findViewById(R.id.rainfall1);
        textView_rainfall_2 = (TextView)findViewById(R.id.rainfall2);
        textView_rainfall_3 = (TextView)findViewById(R.id.rainfall3);
        textView_rainfall_4 = (TextView)findViewById(R.id.rainfall4);
        textView_rainfall_5 = (TextView)findViewById(R.id.rainfall5);
        textView_rainfall_6 = (TextView)findViewById(R.id.rainfall6);
        textView_rainfall_7 = (TextView)findViewById(R.id.rainfall7);
        textView_rainfall_8 = (TextView)findViewById(R.id.rainfall8);

        textView_condition_1 = (TextView)findViewById(R.id.condition1);
        textView_condition_2 = (TextView)findViewById(R.id.condition2);
        textView_condition_3 = (TextView)findViewById(R.id.condition3);
        textView_condition_4 = (TextView)findViewById(R.id.condition4);
        textView_condition_5 = (TextView)findViewById(R.id.condition5);
        textView_condition_6 = (TextView)findViewById(R.id.condition6);
        textView_condition_7 = (TextView)findViewById(R.id.condition7);
        textView_condition_8 = (TextView)findViewById(R.id.condition8);

        //textView_shortWeather = (TextView)findViewById(R.id.shortWeather);

        new ReceiveShortWeather().execute();
    }

    public class ReceiveShortWeather extends AsyncTask<URL, Integer, Long> {

        ArrayList<ShortWeather> shortWeathers = new ArrayList<ShortWeather>();

        protected Long doInBackground(URL... urls) {

            String url = "http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=1114057000";

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = null;

            try {
                response = client.newCall(request).execute();
                parseXML(response.body().string());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Long result) {

            String hour1,hour2,hour3,hour4,hour5,hour6,hour7,hour8 = "";
            String temp1,temp2,temp3,temp4,temp5,temp6,temp7,temp8 = "";
            String rf1,rf2,rf3,rf4,rf5,rf6,rf7,rf8 = "";
            String cd1,cd2,cd3,cd4,cd5,cd6,cd7,cd8 = "";


            //shortWeathers.size()

            /*for(int i=0; i<8; i++) {
                data += shortWeathers.get(i).getHour() + "" + "시" + "          " + //시간
                        shortWeathers.get(i).getDay() + "" + "일" + "          " +// day, 현재 day:0
                        shortWeathers.get(i).getTemp() + "" + "℃" + "          " +// 온도
                        shortWeathers.get(i).getWfKor() + "" + "          " +// 상태
                        shortWeathers.get(i).getPop() + "%" + "\n"; //강수확률
            }*/

            /////////////////////////////////////////////

            hour1 = shortWeathers.get(0).getHour() + "시";
            textView_hour_1.setText(hour1);

            temp1 = shortWeathers.get(0).getTemp() + "℃";
            textView_temp1.setText(temp1);

            cd1 = shortWeathers.get(0).getWfKor();
            textView_condition_1.setText(cd1);

            rf1 = shortWeathers.get(0).getPop() + "%";
            textView_rainfall_1.setText(rf1);

            ///////////////////////////////////////////////

            hour2 = shortWeathers.get(1).getHour() + "시";
            textView_hour_2.setText(hour2);

            temp2 = shortWeathers.get(1).getTemp() + "℃";
            textView_temp2.setText(temp2);

            cd2 = shortWeathers.get(1).getWfKor();
            textView_condition_2.setText(cd2);

            rf2 = shortWeathers.get(1).getPop() + "%";
            textView_rainfall_2.setText(rf2);

            ///////////////////////////////////////////////

            hour3 = shortWeathers.get(2).getHour() + "시";
            textView_hour_3.setText(hour3);

            temp3 = shortWeathers.get(2).getTemp() + "℃";
            textView_temp3.setText(temp3);

            cd3 = shortWeathers.get(2).getWfKor();
            textView_condition_3.setText(cd3);

            rf3 = shortWeathers.get(2).getPop() + "%";
            textView_rainfall_3.setText(rf3);

            ///////////////////////////////////////////////

            hour4 = shortWeathers.get(3).getHour() + "시";
            textView_hour_4.setText(hour4);

            temp4 = shortWeathers.get(3).getTemp() + "℃";
            textView_temp4.setText(temp4);

            cd4 = shortWeathers.get(3).getWfKor();
            textView_condition_4.setText(cd4);

            rf4 = shortWeathers.get(3).getPop() + "%";
            textView_rainfall_4.setText(rf4);

            ///////////////////////////////////////////////

            hour5 = shortWeathers.get(4).getHour() + "시";
            textView_hour_5.setText(hour5);

            temp5 = shortWeathers.get(4).getTemp() + "℃";
            textView_temp5.setText(temp5);

            cd5 = shortWeathers.get(4).getWfKor();
            textView_condition_5.setText(cd5);

            rf5 = shortWeathers.get(4).getPop() + "%";
            textView_rainfall_5.setText(rf5);

            ///////////////////////////////////////////////

            hour6 = shortWeathers.get(5).getHour() + "시";
            textView_hour_6.setText(hour6);

            temp6 = shortWeathers.get(5).getTemp() + "℃";
            textView_temp6.setText(temp6);

            cd6 = shortWeathers.get(5).getWfKor();
            textView_condition_6.setText(cd6);

            rf6 = shortWeathers.get(5).getPop() + "%";
            textView_rainfall_6.setText(rf6);

            ///////////////////////////////////////////////

            hour7 = shortWeathers.get(6).getHour() + "시";
            textView_hour_7.setText(hour7);

            temp7 = shortWeathers.get(6).getTemp() + "℃";
            textView_temp7.setText(temp7);

            cd7 = shortWeathers.get(6).getWfKor();
            textView_condition_7.setText(cd7);

            rf7 = shortWeathers.get(6).getPop() + "%";
            textView_rainfall_7.setText(rf7);

            ///////////////////////////////////////////////

            hour8 = shortWeathers.get(7).getHour() + "시";
            textView_hour_8.setText(hour8);

            temp8 = shortWeathers.get(7).getTemp() + "℃";
            textView_temp8.setText(temp8);

            cd8 = shortWeathers.get(7).getWfKor();
            textView_condition_8.setText(cd8);

            rf8 = shortWeathers.get(7).getPop() + "%";
            textView_rainfall_8.setText(rf8);

            ///////////////////////////////////////////////
        }

        void parseXML(String xml) {
            try {
                String tagName = "";
                boolean onHour = false;
                boolean onDay = false;
                boolean onTem = false;
                boolean onWfKor = false;
                boolean onPop = false;
                boolean onEnd = false;
                boolean isItemTag1 = false;
                int i = 0;

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();

                parser.setInput(new StringReader(xml));

                int eventType = parser.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        tagName = parser.getName();
                        if (tagName.equals("data")) {
                            shortWeathers.add(new ShortWeather());
                            onEnd = false;
                            isItemTag1 = true;
                        }
                    } else if (eventType == XmlPullParser.TEXT && isItemTag1) {
                        if (tagName.equals("hour") && !onHour) {
                            shortWeathers.get(i).setHour(parser.getText());
                            onHour = true;
                        }
                        if (tagName.equals("day") && !onDay) {
                            shortWeathers.get(i).setDay(parser.getText());
                            onDay = true;
                        }
                        if (tagName.equals("temp") && !onTem) {
                            shortWeathers.get(i).setTemp(parser.getText());
                            onTem = true;
                        }
                        if (tagName.equals("wfKor") && !onWfKor) {
                            shortWeathers.get(i).setWfKor(parser.getText());
                            onWfKor = true;
                        }
                        if (tagName.equals("pop") && !onPop) {
                            shortWeathers.get(i).setPop(parser.getText());
                            onPop = true;
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        if (tagName.equals("s06") && onEnd == false) {
                            i++;
                            onHour = false;
                            onDay = false;
                            onTem = false;
                            onWfKor = false;
                            onPop = false;
                            isItemTag1 = false;
                            onEnd = true;
                        }
                    }

                    eventType = parser.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

            case R.id.setting: // 설정 클릭
                break;

            case android.R.id.home: // 뒤로가기
                finish();
                break;


        }
        return super.onOptionsItemSelected(item);
    }
}
