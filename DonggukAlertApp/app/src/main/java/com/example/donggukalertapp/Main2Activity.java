package com.example.donggukalertapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Main2Activity extends AppCompatActivity {
    private static final String TAG = "Main2Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    @Override
    protected void onResume() {
        super.onResume();

        new test().execute();
    }

    public class test extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                Connection.Response execute = Jsoup.connect("https://search.naver.com/search.naver?sm=tab_sug.top&where=nexearch&query=%EC%84%9C%EC%9A%B8+%EC%A4%91%EA%B5%AC+%EB%82%A0%EC%94%A8&oquery=%EB%82%A0%EC%94%A8&tqi=UOXr%2FdprvxZsslXWmdhssssstKZ-114707&acq=%EC%A4%91%EA%B5%AC+%EB%82%A0%EC%94%A8&acr=2&qdt=0").execute();
                Document document = Jsoup.parse(execute.body());
                Log.d(TAG, "doInBackground: " + document.select("ul.list_area"));

            }catch (IOException e) {
                e.printStackTrace();
            }
            return  null;
        }
    }
}
