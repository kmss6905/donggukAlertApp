package com.example.donggukalertapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import com.example.donggukalertapp.Whether.ShortWeather;
import com.example.donggukalertapp.Whether.ShortWeatherActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Log.d(TAG, "onMessageReceived: called");
        Log.d(TAG, "onMessageReceived: Message received from: " + remoteMessage.getFrom());
        Log.d(TAG, "onMessageReceived: \n" +
                remoteMessage.getData().get("content") + "\n" + // 타이틀(툴바 서브 타이틀) ex 2020년 상반기 ...
                remoteMessage.getData().get("link") + "\n" +  // url
                remoteMessage.getData().get("title") + "\n"); // 타입( 툴바 제목 ) ex [일반공지]

        // 저장된 설정값 가져옴
        Boolean keyword = sharedPreferences.getBoolean("keyword",true);
        Log.d(TAG, "onCreatePreferences: keyword : " + keyword);
        Boolean keyword_all = sharedPreferences.getBoolean("keyword_all", false);
        Log.d(TAG, "onCreatePreferences: keyword_all : " + keyword_all);
        Boolean weather = sharedPreferences.getBoolean("weather", true);
        Log.d(TAG, "onCreatePreferences: weather : " + weather);

        if(remoteMessage.getFrom().equals("/topics/test-topic")){
            if(keyword_all){ // 모든 알람 받기
                sendNotification(remoteMessage.getData().get("content"), remoteMessage.getData().get("title"), remoteMessage.getData().get("link"));
            }else{
                if(keyword){ //키워드 알람 받기
                    /**
                     *  만약 저장된 값이 하나도 없으면 ? 모든 알람 다 보냄
                     *  저장된 값이 하나라도 있으면 그것에 대한 것만 보냄
                     */
                     SharedPreferences sharedPreferences_addword = getSharedPreferences("Keyword", MODE_PRIVATE);
                    if(sharedPreferences_addword.getString("count","").equals("")){ // 아무것도 없으면

                    }else{
                        String count = sharedPreferences_addword.getString("count", "");
                        ArrayList<String> strings = new ArrayList<>();

                        for(int i = 1; i <= Integer.parseInt(count); i++) {
                            strings.add(sharedPreferences_addword.getString("keyword" + i,""));
                        }

                        switch (strings.size()){
                            case 1:
                                if(remoteMessage.getData().get("content").contains(strings.get(0))) {
                                    Log.d(TAG, "onMessageReceived: 비교중 ");
                                    sendNotification(
                                            remoteMessage.getData().get("content"),
                                            remoteMessage.getData().get("title"),
                                            remoteMessage.getData().get("link"));
                                }else
                                    Log.d(TAG, "onMessageReceived: " + "넘김");
                                break;
                            case 2:
                                if(remoteMessage.getData().get("content").contains(strings.get(0))
                                    || remoteMessage.getData().get("content").contains(strings.get(1))
                                ) {
                                    Log.d(TAG, "onMessageReceived: 비교중 ");
                                    sendNotification(
                                            remoteMessage.getData().get("content"),
                                            remoteMessage.getData().get("title"),
                                            remoteMessage.getData().get("link"));
                                }else
                                    Log.d(TAG, "onMessageReceived: " + "넘김");
                                break;
                            case 3:
                                if(remoteMessage.getData().get("content").contains(strings.get(0))
                                        || remoteMessage.getData().get("content").contains(strings.get(1))
                                        || remoteMessage.getData().get("content").contains(strings.get(2))
                                ) {
                                    Log.d(TAG, "onMessageReceived: 비교중 ");
                                    sendNotification(
                                            remoteMessage.getData().get("content"),
                                            remoteMessage.getData().get("title"),
                                            remoteMessage.getData().get("link"));
                                }else
                                    Log.d(TAG, "onMessageReceived: " + "넘김");
                                break;
                            case 4:
                                if(remoteMessage.getData().get("content").contains(strings.get(0))
                                        || remoteMessage.getData().get("content").contains(strings.get(1))
                                        || remoteMessage.getData().get("content").contains(strings.get(2))
                                        || remoteMessage.getData().get("content").contains(strings.get(3))
                                ) {
                                    Log.d(TAG, "onMessageReceived: 비교중 ");
                                    sendNotification(
                                            remoteMessage.getData().get("content"),
                                            remoteMessage.getData().get("title"),
                                            remoteMessage.getData().get("link"));
                                }else
                                    Log.d(TAG, "onMessageReceived: " + "넘김");
                                break;
                            case 5:
                                if(remoteMessage.getData().get("content").contains(strings.get(0))
                                        || remoteMessage.getData().get("content").contains(strings.get(1))
                                        || remoteMessage.getData().get("content").contains(strings.get(2))
                                        || remoteMessage.getData().get("content").contains(strings.get(3))
                                        || remoteMessage.getData().get("content").contains(strings.get(4))
                                ) {
                                    Log.d(TAG, "onMessageReceived: 비교중 ");
                                    sendNotification(
                                            remoteMessage.getData().get("content"),
                                            remoteMessage.getData().get("title"),
                                            remoteMessage.getData().get("link"));
                                }else
                                    Log.d(TAG, "onMessageReceived: " + "넘김");
                                break;
                        }


                    }
                }
            }

        }else if(remoteMessage.getFrom().equals("/topics/weather")){

            if(weather){ // 날씨 알람설정 on
//            "to": "/topics/weather"
//                    ,
//                    "data": {
//                        "title": title_,
//                        "content": content,
//                        "max" : max(a),
//                        "icon:": "ic_new",
//            }
                Log.d(TAG, "onMessageReceived: " + "test1");
                sendNotification_weather(remoteMessage.getData().get("title"), remoteMessage.getData().get("content"));

            }
        }











//        if(remoteMessage.getNotification() != null){
//            String title = remoteMessage.getData().get("title");
//            String body = remoteMessage.getData().get("content");
//            Log.d(TAG, "onMessageReceived: " + remoteMessage.getNotification().getImageUrl());
//
//            Notification notification = new NotificationCompat.Builder(this, FCM_CHANNEL_ID)
//                    .setSmallIcon(R.drawable.ic_new)
//                    .setContentTitle(title)
//                    .setContentText(body)
//                    .setColor(Color.BLUE)
//                    .build();
//            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//            manager.notify(1002, notification);
//        }




        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "onMessageReceived: Data: " + remoteMessage.getData().toString());
        }

    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
        Log.d(TAG, "onDeletedMessages: called");
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d(TAG, "onNewToken: called");
        //upload this token on the app server
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     * 게시물 알람
     * @param messageBody FCM message body received.*
     */
    private void sendNotification(String messageBody, String title, String link) {
        Intent intent = new Intent(this, ContentAcitivty.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("type",title); // get(type) -> [일반공지] 툴바 타이틀
        intent.putExtra("title", messageBody); // get(content) -> 2020 년 .. 안내 툴바 서브타이틀
        intent.putExtra("url",link);  // url

        Log.d(TAG, "sendNotification: intent 내용 : \n" +
                "툴바제목 : " + intent.getStringExtra("type") + "\n" +
                "서브툴바제목 : " + intent.getStringExtra("title") + "\n" +
                "링크 : " + intent.getStringExtra("url") );

        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int)(System.currentTimeMillis()/1000) /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT );

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_new)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL) // 알림, 사운드 진동 설정
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "동국대 게시물알람",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify((int)(System.currentTimeMillis()/1000) /* ID of notification */, notificationBuilder.build());
        }
    }




    private void sendNotification_weather(String title,String messageBody){
        Log.d(TAG, "sendNotification_weather: " + 1);
        Intent intent = new Intent(this, ShortWeatherActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);



        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int)(System.currentTimeMillis()/1000) /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT );

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_new)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL) // 알림, 사운드 진동 설정
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "동국대 날씨알람",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify((int)(System.currentTimeMillis()/1000) /* ID of notification */, notificationBuilder.build());
        }
    }




}
