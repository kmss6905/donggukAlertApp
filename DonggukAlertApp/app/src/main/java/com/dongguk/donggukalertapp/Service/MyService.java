package com.dongguk.donggukalertapp.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.dongguk.donggukalertapp.DataModel.NoticeUrl;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MyService extends Service {
    private static final String TAG = "MyService";
    private Thread mThread;
    private int mCount = 0;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(mThread == null){
            mThread = new Thread("My Thread") {
                @Override
                public void run() {
                    for (int i = 0; i < 100; i++) {
                        try {
                            getHtml();
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            break;
                        }

                    }
                }
            };
            mThread.start();
        }

        return START_STICKY;
    }

    public void getHtml(){
        try{
            Connection.Response execute = Jsoup.connect(NoticeUrl.getUrl("admission")).execute();
            Document document = Jsoup.parse(execute.body());
            Elements elements = document.getElementsByTag("tr");
            for(Element element : elements){
                Log.d(TAG, "getHtml: " + element.toString());
            }
        }catch (IOException e){
            Log.d(TAG, "getHtml: " + e.getMessage());
        }


    }

}
