package com.kcdeveloperss.status.downloader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

public class WebViewActivity extends AppCompatActivity {

    String IntentTitle = "";
    String IntentURL;
    ImageView imBack;
    TextView TVTitle;
    WebView webView1;
    SwipeRefreshLayout swipeRefreshLayout;

    private class MyBrowser extends WebViewClient {
        private MyBrowser() {

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        IntentURL = getIntent().getStringExtra("URL");
        IntentTitle = getIntent().getStringExtra("Title");

        imBack = findViewById(R.id.imBack);
        TVTitle = findViewById(R.id.TVTitle);
        webView1 = findViewById(R.id.webView1);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        imBack.setOnClickListener(v -> onBackPressed());

        TVTitle.setText(IntentTitle);
        swipeRefreshLayout.post(() -> {
            swipeRefreshLayout.setRefreshing(true);
            LoadPage(IntentURL);
        });

        swipeRefreshLayout.setOnRefreshListener(() -> LoadPage(IntentURL));

    }

    public void LoadPage(String url) {
        webView1.setWebViewClient(new MyBrowser());
        webView1.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                swipeRefreshLayout.setRefreshing(newProgress != 100);
            }
        });
        webView1.getSettings().setLoadsImagesAutomatically(true);
        webView1.getSettings().setJavaScriptEnabled(true);
        webView1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView1.loadUrl(url);
    }

}