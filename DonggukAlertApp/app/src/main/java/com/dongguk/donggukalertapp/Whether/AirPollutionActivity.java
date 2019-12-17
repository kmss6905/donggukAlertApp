package com.dongguk.donggukalertapp.Whether;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dongguk.donggukalertapp.R;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;

public class AirPollutionActivity extends AppCompatActivity {

    TextView textView_time, textView_pm10value, textView_pm25value, textView_o3value, textView_covalue, textView_no2value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_air_pollution);

        textView_time = (TextView) findViewById(R.id.time);
        textView_pm10value = (TextView)findViewById(R.id.pm_10value);
        textView_pm25value = (TextView)findViewById(R.id.pm_25value);
        textView_o3value = (TextView)findViewById(R.id.o3value);
        textView_covalue = (TextView)findViewById(R.id.covalue) ;
        textView_no2value = (TextView)findViewById(R.id.no2value);

        new ReceiveAirPollution().execute();
    }

    public class ReceiveAirPollution extends AsyncTask<URL, Integer, Long> {

        ArrayList<AirPollution> airPollutions = new ArrayList<AirPollution>();

        protected Long doInBackground(URL... urls) {

            String url = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst?serviceKey=LP%2BiONutuQgCx65Z70TzAnhOxRYDH9PhFIXuMYNuTc5aIcGRPvVDZTt0luiiBnHv076RcEmaKSls2qU1koxfhg%3D%3D&numOfRows=25&pageNo=1&sidoName=%EC%84%9C%EC%9A%B8&searchCondition=DAILY";

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
            String t,pm10, pm25, o3, co, no2 = "";
            String express = "";

            // i=23 : CityName = 중구
            t = airPollutions.get(23).getDataTime();
            textView_time.setText(t);

            pm10 = airPollutions.get(23).getPm10Value() + "㎍/㎥";
            textView_pm10value.setText(pm10);

            int int_pm10 = Integer.parseInt(airPollutions.get(23).getPm10Value());

            if(int_pm10 >= 0 && int_pm10 <= 30) {
                express = "좋음";
                textView_pm10value.setText(pm10 + " / " + express);
            }
            else if(int_pm10 > 30 && int_pm10 <= 80){
                express = "보통";
                textView_pm10value.setText(pm10 + " / " + express);
            }
            else if(int_pm10 > 80 && int_pm10 <= 150){
                express = "나쁨";
                textView_pm10value.setText(pm10 + " / " + express);
            }
            else if(int_pm10 > 150){
                express = "매우나쁨";
                textView_pm10value.setText(pm10 + " / " + express);
            }

            pm25 = airPollutions.get(23).getPm25Value() + "㎍/㎥";
            textView_pm25value.setText(pm25);

            int int_pm25 = Integer.parseInt(airPollutions.get(23).getPm25Value());

            if(int_pm25 >= 0 && int_pm25 <= 15) {
                express = "좋음";
                textView_pm25value.setText(pm25 + " / " + express);
            }
            else if(int_pm25 > 15 && int_pm25 <= 35){
                express = "보통";
                textView_pm25value.setText(pm25 + " / " + express);
            }
            else if(int_pm25 > 35 && int_pm25 <= 75){
                express = "나쁨";
                textView_pm25value.setText(pm25 + " / " + express);
            }
            else if(int_pm25 > 75 ){
                express = "매우 나쁨";
                textView_pm25value.setText(pm25 + " / " + express);
            }

            o3 = airPollutions.get(23).getO3Value() +"ppm";
            textView_o3value.setText(o3);

            float f_o3 = Float.parseFloat(airPollutions.get(23).getO3Value());

            if(f_o3 >= 0 && f_o3 <= 0.03) {
                express = "좋음";
                textView_o3value.setText(o3 + " / " + express);
            }
            else if(f_o3 > 0.030 && f_o3 <= 0.09){
                express = "보통";
                textView_o3value.setText(o3 + " / " + express);
            }
            else if(f_o3 > 0.090 && f_o3 <= 0.15){
                express = "나쁨";
                textView_o3value.setText(o3 + " / " + express);
            }
            else if(f_o3 > 0.15 ){
                express = "매우 나쁨";
                textView_o3value.setText(o3 + " / " + express);
            }

            co = airPollutions.get(23).getCoValue() +"ppm";
            textView_covalue.setText(co);

            float f_co = Float.parseFloat(airPollutions.get(23).getCoValue());

            if(f_co >= 0 && f_co <= 2) {
                express = "좋음";
                textView_covalue.setText(co + " / " + express);
            }
            else if(f_co > 2 && f_co <= 9){
                express = "보통";
                textView_covalue.setText(co + " / " + express);
            }
            else if(f_co > 9 && f_co <= 15){
                express = "나쁨";
                textView_covalue.setText(co + " / " + express);
            }
            else if(f_co > 15 ){
                express = "매우 나쁨";
                textView_covalue.setText(co + " / " + express);
            }

            no2 = airPollutions.get(23).getNo2Value() + "ppm";
            textView_no2value.setText(no2);

            float f_no2 = Float.parseFloat(airPollutions.get(23).getNo2Value());

            if(f_no2 >= 0 && f_no2 <= 0.03) {
                express = "좋음";
                textView_no2value.setText(no2 + " / " + express);
            }
            else if(f_no2 > 0.03 && f_no2 <= 0.06){
                express = "보통";
                textView_no2value.setText(no2 + " / " + express);
            }
            else if(f_no2 > 0.06 && f_no2 <= 0.2){
                express = "나쁨";
                textView_no2value.setText(no2 + " / " + express);
            }
            else if(f_no2 > 0.2 ){
                express = "매우 나쁨";
                textView_no2value.setText(no2 + " / " + express);
            }
        }

        void parseXML(String xml) {
            try {
                String tagName = "";

                boolean onDataTime = false;
                boolean onPm10Value = false;
                boolean onPm25Value = false;
                boolean onO3Value = false;
                boolean onCoValue = false;
                boolean onNo2Value = false;
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
                        if (tagName.equals("item")) {
                            airPollutions.add(new AirPollution());
                            onEnd = false;
                            isItemTag1 = true;
                        }
                    } else if (eventType == XmlPullParser.TEXT && isItemTag1) {
                        if (tagName.equals("dataTime") && !onDataTime) {
                            airPollutions.get(i).setDataTime(parser.getText());
                            onDataTime = true;
                        }
                        if (tagName.equals("pm10Value") && !onPm10Value) {
                            airPollutions.get(i).setPm10Value(parser.getText());
                            onPm10Value = true;
                        }
                        if (tagName.equals("pm25Value") && !onPm25Value) {
                            airPollutions.get(i).setPm25Value(parser.getText());
                            onPm25Value = true;
                        }
                        if (tagName.equals("o3Value") && !onO3Value) {
                            airPollutions.get(i).setO3Value(parser.getText());
                            onO3Value = true;
                        }
                        if (tagName.equals("coValue") && !onCoValue) {
                            airPollutions.get(i).setCoValue(parser.getText());
                            onCoValue = true;
                        }
                        if (tagName.equals("no2Value") && !onNo2Value) {
                            airPollutions.get(i).setNo2Value(parser.getText());
                            onNo2Value = true;
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        if (tagName.equals("pm25Value") && onEnd == false) {
                            i++;
                            onDataTime = false;
                            onPm10Value = false;
                            onPm25Value = false;
                            onO3Value = false;
                            onCoValue = false;
                            onNo2Value = false;
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
}