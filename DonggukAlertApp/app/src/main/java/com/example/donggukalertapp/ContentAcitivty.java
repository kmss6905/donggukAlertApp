package com.example.donggukalertapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.donggukalertapp.Adapter.PagerAdapter;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Objects;

/** 공지사항에 있는 글을 클릭한 뒤에 나오는 액티비티
 *  목록 엑티비티로부터 클릭했을 떄의 제목과 포함되는 카테고리를 가져온다.
 *  가져온 카테고리이름과 제목은 각각 툴바의 타이틀과 서브 타이틀로 들어간다.
 */

public class ContentAcitivty extends AppCompatActivity {
    private static final String TAG = "ContentAcitivty";
    private WebView mWebView; // 웹 뷰
    private WebSettings mWebSettings; // 웹뷰 세팅
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_acitivty);

        // 전 목록화면에서 데이터 가져와야함
        intent = getIntent();

        // 웹뷰
        mWebView = findViewById(R.id.webView);




        //해당 특정 아이템 클릭시 해당 타이틀의 제목이 툴바의 제목이 됨
        Objects.requireNonNull(getSupportActionBar()).setTitle(intent.getStringExtra("type"));
        getSupportActionBar().setSubtitle(intent.getStringExtra("title"));

        //액션바 뒤로가기 버튼 추가
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mWebView.setWebViewClient(new WebViewClient()); // 클릭시 새창 안뜨게 ( 리다이렉트 방지)
        mWebSettings = mWebView.getSettings(); //세부 세팅 등록
        mWebSettings.setJavaScriptEnabled(true); // 웹페이지 자바스클비트 허용 여부
        mWebSettings.setSupportMultipleWindows(false); // 새창 띄우기 허용 여부
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(false); // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
        mWebSettings.setLoadWithOverviewMode(true); // 메타태그 허용 여부
        mWebSettings.setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
        mWebSettings.setSupportZoom(true); // 화면 줌 허용 여부
        mWebSettings.setBuiltInZoomControls(true); // 화면 확대 축소 허용 여부
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠 사이즈 맞추기
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 허용 여부
        mWebSettings.setDomStorageEnabled(true); // 로컬저장소 허용 여부
        mWebView.loadUrl("https://www.dongguk.edu/mbs/kr/jsp/board/" + intent.getStringExtra("url")); // 웹뷰에 표시할 웹사이트 주소, 웹뷰 시작


    }


    // 옵션 메뉴
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();

        new syncTaskParse().execute(); // 파싱

        mWebView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {

                try {
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

                    request.setMimeType(mimeType);
                    //------------------------COOKIE!!------------------------
                    String cookies = CookieManager.getInstance().getCookie(url);
                    request.addRequestHeader("cookie", cookies);
                    //------------------------COOKIE!!------------------------
                    request.addRequestHeader("User-Agent", userAgent);
                    request.setDescription("Downloading file...");
                    request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType));
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimeType));
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    dm.enqueue(request);
                    Toast.makeText(getApplicationContext(), "Downloading File", Toast.LENGTH_LONG).show();
                } catch (Exception e) {

                    if (ContextCompat.checkSelfPermission(ContentAcitivty.this,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(ContentAcitivty.this,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            Toast.makeText(getBaseContext(), "첨부파일 다운로드를 위해\n동의가 필요합니다.", Toast.LENGTH_LONG).show();
                            ActivityCompat.requestPermissions(ContentAcitivty.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    110);
                        } else {
                            Toast.makeText(getBaseContext(), "첨부파일 다운로드를 위해\n동의가 필요합니다.", Toast.LENGTH_LONG).show();
                            ActivityCompat.requestPermissions(ContentAcitivty.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    110);
                        }
                    }



                }
            }
        });
    }

    /**
     * 옵션 메뉴 클릭
     * @param item 날씨메뉴, 설정메뉴, 뒤로가기
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.weather: //날씨
                break;
            case R.id.setting: // 설정
                break;
            case android.R.id.home: // 뒤로가기
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public class syncTaskParse extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Connection.Response execute = Jsoup.connect("https://www.dongguk.edu/mbs/kr/jsp/board/" + intent.getStringExtra("url")).execute();
                Document document = Jsoup.parse(execute.body());
                Elements elements = document.select("tr.bottom_line td");
                for(Element element : elements){
                    for(String str : element.getElementsByTag("a").eachText()){
                        Log.d(TAG, "doInBackground: 내부 " + str);
                    }

                    /*element.getElementsByTag("")*/


                    for(String str : element.getElementsByTag("a").eachAttr("onclick")){
//                        Log.d(TAG, "doInBackground: 내부 경로" + str);
                        String[] strArray = str.split("'");
                        Log.d(TAG, "doInBackground: 내부 경로 : https://www.dongguk.edu/mbs/kr/jsp/board" +  strArray[1]);
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
