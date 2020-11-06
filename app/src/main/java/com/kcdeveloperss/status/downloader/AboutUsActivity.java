package com.kcdeveloperss.status.downloader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.kcdeveloperss.status.downloader.util.Utils;

public class AboutUsActivity extends AppCompatActivity {

    RelativeLayout RLPrivacyPolicy;
    ImageView imBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        RLPrivacyPolicy = findViewById(R.id.RLPrivacyPolicy);
        imBack = findViewById(R.id.imBack);

        RLPrivacyPolicy.setOnClickListener(v -> {
            Intent intent = new Intent(AboutUsActivity.this, WebViewActivity.class);
            intent.putExtra("URL", Utils.PrivacyPolicyUrl);
            intent.putExtra("Title", "Privacy Policy");
            startActivity(intent);
        });

        imBack.setOnClickListener(v -> onBackPressed());

    }
}