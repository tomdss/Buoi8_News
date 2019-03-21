package com.t3h.buoi8_news;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.t3h.buoi8_news.utils.DialogUtils;

public class WebviewActivity extends AppCompatActivity {

    private WebView webView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);
        try {
            getSupportActionBar().hide();
            webView = findViewById(R.id.webView);
            webView.setWebViewClient(new myBrowser());
        }catch (Exception e){
            e.printStackTrace();
        }
        showUrl(getExtra());


    }


    private void showUrl(String url) {
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(url);
    }

    private class myBrowser extends WebViewClient {


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String Url) {
//            return super.shouldOverrideUrlLoading(view, request);
            view.loadUrl(Url);
//            edtUrl.setText(Url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

//            Toast.makeText(WebviewActivity.this, "on START", Toast.LENGTH_SHORT).show();

            DialogUtils.showCancel(WebviewActivity.this);
            super.onPageStarted(view, url, favicon);
        }


        @Override
        public void onPageFinished(WebView view, String url) {

//            Toast.makeText(WebviewActivity.this, "on FINISH", Toast.LENGTH_SHORT).show();
            super.onPageFinished(view, url);
            DialogUtils.dissmiss();

        }
    }

    public String getExtra() {

        Intent intent = this.getIntent();
        String link = intent.getStringExtra(MainActivity.REQUEST_LINK);

        return link;


//        tvHello.setText("Hello! "+id+" - "+pass);
    }



}


