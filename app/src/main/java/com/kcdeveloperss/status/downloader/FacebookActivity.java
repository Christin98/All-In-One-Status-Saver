package com.kcdeveloperss.status.downloader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.kcdeveloperss.status.downloader.api.CommonClassForAPI;
import com.kcdeveloperss.status.downloader.util.AdsUtils;
import com.kcdeveloperss.status.downloader.util.SharePrefs;
import com.kcdeveloperss.status.downloader.util.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class FacebookActivity extends AppCompatActivity {

    public String VideoUrl;
    private ClipboardManager clipboardManager;
    CommonClassForAPI commonClassForAPI;
    private InterstitialAd mInterstitialAd;
    AdView adView;

    EditText etText;
    ImageView imInfo;
    ImageView imBack;
    View layout_how_to;
    LinearLayout LLHowToLayout;
    ImageView imHowto1;
    ImageView imHowto2;
    ImageView imHowto3;
    ImageView imHowto4;
    TextView tvHowTo1;
    TextView tvHowTo3;
    TextView loginBtn1;
    TextView tvPaste;
    RelativeLayout LLOpenFacebook;

    class callGetFacebookData extends AsyncTask<String, Void, Document> {
        Document facebookDoc;

        callGetFacebookData() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Document document) {
            String string = "";
            Utils.hideProgressDialog(FacebookActivity.this);
            try {
                VideoUrl = document.select("meta[property=\"og:video\"").last().attr(FirebaseAnalytics.Param.CONTENT);
                Log.e("onPostExecute: ", VideoUrl);
                if (!VideoUrl.equals(string)) {
                    try {
                        Utils.startDownload(VideoUrl, Utils.RootDirectoryFacebook, FacebookActivity.this, getFilenameFromURL(VideoUrl));
                        VideoUrl = string;
                        etText.setText(string);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Document doInBackground(String... strings) {
            try {
                facebookDoc = Jsoup.connect(strings[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("ContentValues", "doInBackground: Error");
            }
            return facebookDoc;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);
        commonClassForAPI = CommonClassForAPI.getInstance(this);
        adView = findViewById(R.id.adView);
        Utils.createFileFolder();
        initViews();
        AdsUtils.showGoogleBannerAd(this, adView);
        InterstitialAdsINIT();
    }

    @Override
    protected void onResume() {
        super.onResume();
        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        PasteText();
    }

    private void initViews() {
        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        etText = findViewById(R.id.et_text);
        layout_how_to = findViewById(R.id.layoutHowTo);
        imBack = findViewById(R.id.imBack);
        imInfo = findViewById(R.id.imInfo);
        tvPaste = findViewById(R.id.tv_paste);
        LLOpenFacebook = findViewById(R.id.LLOpenFacebbook);
        LLHowToLayout = layout_how_to.findViewById(R.id.LLHowToLayout);
        imHowto1 = layout_how_to.findViewById(R.id.im_howto1);
        imHowto2 = layout_how_to.findViewById(R.id.im_howto2);
        imHowto3 = layout_how_to.findViewById(R.id.im_howto3);
        imHowto4 = layout_how_to.findViewById(R.id.im_howto4);
        tvHowTo1 = layout_how_to.findViewById(R.id.tvHowTo1);
        tvHowTo3 = layout_how_to.findViewById(R.id.tvHowTo3);
        loginBtn1 = layout_how_to.findViewById(R.id.login_btn1);

        imBack.setOnClickListener(v -> onBackPressed());

        imInfo.setOnClickListener(v -> LLHowToLayout.setVisibility(View.VISIBLE));
        imHowto1.setImageResource(R.drawable.fb1);
        imHowto2.setImageResource(R.drawable.fb2);
        imHowto3.setImageResource(R.drawable.fb3);
        imHowto4.setImageResource(R.drawable.fb4);
        tvHowTo1.setText("1. Open Facebook");
        tvHowTo3.setText("1. Copy Video Link from Facebook");
        if (!SharePrefs.getInstance(this).getBoolean(SharePrefs.ISSHOWHOWTOFB)) {
            SharePrefs.getInstance(this).putBoolean(SharePrefs.ISSHOWHOWTOFB, true);
            LLHowToLayout.setVisibility(View.VISIBLE);
        } else {
            LLHowToLayout.setVisibility(View.GONE);
        }
        loginBtn1.setOnClickListener(v -> {
            String url = etText.getText().toString();
            if (url.equals("")) {
                Utils.setToast(FacebookActivity.this, "Enter Url");
            } else if (!Patterns.WEB_URL.matcher(url).matches()) {
                Utils.setToast(FacebookActivity.this, "Enter Valid Url");
            } else {
                showInterstitial();
                GetFacebookData();
            }
        });
        tvPaste.setOnClickListener(v -> PasteText());
        LLOpenFacebook.setOnClickListener(v -> Utils.openApp(FacebookActivity.this, "com.facebook.katana"));
    }

    private void GetFacebookData() {
        try {
            Utils.createFileFolder();
            String host = new URL(etText.getText().toString()).getHost();
            Log.e("initViews: ", host);
            if (host.contains("facebook.com")) {
                Utils.showProgressDialog(this);
                new callGetFacebookData().execute(new String[]{etText.getText().toString()});
                return;
            }
            Utils.setToast(this, "Enter Valid Url");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void PasteText() {
        String string = "";
        try {
            etText.setText(string);
            String stringExtra = getIntent().getStringExtra("CopyIntent");
            String string2 = "facebook.com";
            if (stringExtra.equals(string)) {
                if (clipboardManager.hasPrimaryClip()) {
                    if (clipboardManager.getPrimaryClipDescription().hasMimeType("text/plain")) {
                        ClipData.Item itemAt = clipboardManager.getPrimaryClip().getItemAt(0);
                        if (itemAt.getText().toString().contains(string2)) {
                            etText.setText(itemAt.getText().toString());
                        }
                    } else if (clipboardManager.getPrimaryClip().getItemAt(0).getText().toString().contains(string2)) {
                        etText.setText(clipboardManager.getPrimaryClip().getItemAt(0).getText().toString());
                    }
                }
            } else if (stringExtra.contains(string2)) {
                etText.setText(stringExtra);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFilenameFromURL(String string) {
        String string2 = ".mp4";
        try {
            return new File(new URL(string).getPath()).getName() + string2;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return System.currentTimeMillis() + string2;
        }
    }

    public void InterstitialAdsINIT() {
        MobileAds.initialize(this, initializationStatus -> {

        });
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.admob_interstitial_ad));
        mInterstitialAd.loadAd(new Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {

            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {

            }

            @Override
            public void onAdLeftApplication() {

            }

            @Override
            public void onAdOpened() {

            }

            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdClicked() {

            }
        });
    }

    private void showInterstitial() {
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

}