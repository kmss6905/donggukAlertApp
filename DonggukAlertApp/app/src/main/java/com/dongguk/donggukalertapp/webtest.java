package com.dongguk.donggukalertapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class webtest extends AppCompatActivity {
    private static final String TAG = "webtest";
    private WebView mWebView; // 웹 뷰
    private WebSettings mWebSettings; // 웹뷰 세팅

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webtest);


        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView = findViewById(R.id.webView);
                mWebView.setWebViewClient(new WebViewClient()); // 클릭시 새창 안뜨게 ( 리다이렉트 방지)
                mWebSettings = mWebView.getSettings(); //세부 세팅 등록
                mWebSettings.setJavaScriptEnabled(true); // 웹페이지 자바스클비트 허용 여부
//        mWebSettings.setSupportMultipleWindows(false); // 새창 띄우기 허용 여부
//        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(false); // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
//        mWebSettings.setLoadWithOverviewMode(true); // 메타태그 허용 여부
//        mWebSettings.setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
//        mWebSettings.setSupportZoom(true); // 화면 줌 허용 여부
//        mWebSettings.setBuiltInZoomControls(true); // 화면 확대 축소 허용 여부
//        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠 사이즈 맞추기
//                mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 허용 여부
//        mWebSettings.setDomStorageEnabled(true); // 로컬저장소 허용 여부
                mWebView.loadUrl("https://www.naver.com"); // 웹뷰에 표시할 웹사이트 주소, 웹뷰 시작
            }
        });
    }
}
