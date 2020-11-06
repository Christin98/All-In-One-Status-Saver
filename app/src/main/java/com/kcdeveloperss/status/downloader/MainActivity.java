package com.kcdeveloperss.status.downloader;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

import com.kcdeveloperss.status.downloader.util.ClipboardListener;
import com.kcdeveloperss.status.downloader.util.Utils;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String CopyKey = "";
    String CopyValue = "";

    private ClipboardManager clipboardManager;
    boolean doubleBackToExitPressedOnce = false;
    String[] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};

    public  RelativeLayout rvAbout;
    public  RelativeLayout rvFB;
    public  RelativeLayout rvGallery;
    public  RelativeLayout rvInsta;
    public  RelativeLayout rvMoreApp;
    public  RelativeLayout rvRateApp;
    public  RelativeLayout rvShareApp;
    public  RelativeLayout rvTwitter;
    public  RelativeLayout rvWhatsApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvInsta = findViewById(R.id.rvInsta);
        rvWhatsApp = findViewById(R.id.rvWhatsApp);
        rvFB = findViewById(R.id.rvFB);
        rvTwitter = findViewById(R.id.rvTwitter);
        rvGallery = findViewById(R.id.rvGallery);
        rvAbout = findViewById(R.id.rvAbout);
        rvShareApp = findViewById(R.id.rvShareApp);
        rvRateApp = findViewById(R.id.rvRateApp);
        rvMoreApp = findViewById(R.id.rvMoreApp);
        initViews();
    }

    private void initViews() {
        clipboardManager = (ClipboardManager) this.getSystemService(CLIPBOARD_SERVICE);
        if (getIntent().getExtras() != null) {
            for (String str : getIntent().getExtras().getStringArray(CopyKey)) {
                CopyKey = str;
                String string = getIntent().getExtras().getString(CopyKey);
                if (CopyKey.equals("android.intent.extra.TEXT")) {
                    CopyValue = getIntent().getExtras().getString(CopyKey);
                } else {
                    CopyValue = "";
                }
                callText(string);
            }
        }

        if (clipboardManager != null) {
            clipboardManager.addPrimaryClipChangedListener(new ClipboardListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onPrimaryClipChanged() {
                    try {
                        showNotification(Objects.requireNonNull(clipboardManager.getPrimaryClip().getItemAt(0).getText()).toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermissions(0);
        }
        rvInsta.setOnClickListener(this);
        rvWhatsApp.setOnClickListener(this);
        rvFB.setOnClickListener(this);
        rvTwitter.setOnClickListener(this);
        rvGallery.setOnClickListener(this);
        rvAbout.setOnClickListener(this);
        rvShareApp.setOnClickListener(this);
        rvRateApp.setOnClickListener(this);
        rvMoreApp.setOnClickListener(this);
        Utils.createFileFolder();
    }

    public static List<String> extractUrls(String string) {
        ArrayList<String> arrayList = new ArrayList<>();
        Matcher matcher = Pattern.compile("((https?|ftp|gopher|telnet|file):((//)|(\\\\\\\\))+[\\\\w\\\\d:#@%/;$()~_?\\\\+-=\\\\\\\\\\\\.&]*)", 2).matcher(string);
        while (matcher.find()) {
            arrayList.add(string.substring(matcher.start(0), matcher.end(0)));
        }
        return arrayList;
    }

    private void callText(String string) {
        try {
            if (string.contains("instagram.com")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(101);
                } else {
                    callInstaActivity();
                }
            } else if (string.contains("facebook.com")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(104);
                } else {
                    callFacebookActivity();
                }
            } else if (!string.contains("twitter.com")) {
                return;
            } else {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(106);
                } else {
                    callTwitterActivity();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rvAbout:
                startActivity(new Intent(this, AboutUsActivity.class));
                break;
            case R.id.rvFB:
                if (Build.VERSION.SDK_INT >= 23 ) {
                    checkPermissions(104);
                } else {
                    callFacebookActivity();
                }
                break;
            case R.id.rvGallery:
                if (Build.VERSION.SDK_INT >= 23 ) {
                    checkPermissions(105);
                } else {
                    callGalleryActivity();
                }
                break;
            case R.id.rvInsta:
                if (Build.VERSION.SDK_INT >= 23 ) {
                    checkPermissions(101);
                } else {
                    callInstaActivity();
                }
                break;
            case R.id.rvTwitter:
                if (Build.VERSION.SDK_INT >= 23 ) {
                    checkPermissions(106);
                } else {
                    callTwitterActivity();
                }
                break;
            case R.id.rvWhatsApp:
                if (Build.VERSION.SDK_INT >= 23 ) {
                    checkPermissions(102);
                } else {
                    callWhatsappActivity();
                }
                break;
            case R.id.rvMoreApp:
                Utils.moreApp(this);
                break;
            case R.id.rvRateApp:
                Utils.rateApp(this);
                break;
            case R.id.rvShareApp:
                Utils.shareApp(this);
                break;
            default:
                break;
        }
    }

    public void callInstaActivity() {
        Intent intent = new Intent(this, InstagramActivity.class);
        intent.putExtra("CopyIntent", this.CopyValue);
        startActivity(intent);
    }

    public void callWhatsappActivity() {
        startActivity(new Intent(this, WhatsappActivity.class));
    }


    public void callFacebookActivity() {
        Intent intent = new Intent(this, FacebookActivity.class);
        intent.putExtra("CopyIntent", this.CopyValue);
        startActivity(intent);
    }

    public void callTwitterActivity() {
        Intent intent = new Intent(this, TwitterActivity.class);
        intent.putExtra("CopyIntent", this.CopyValue);
        startActivity(intent);
    }

    public void callGalleryActivity() {
        startActivity(new Intent(this, GalleryActivity.class));
    }

    public void showNotification(String string) {
        if (string.contains("instagram.com") || string.contains("facebook.com") || string.contains("twitter.com")) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("Notification", string);
            PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            NotificationManager notificationManager = (NotificationManager) MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= 26) {
                NotificationChannel notificationChannel = new NotificationChannel(getResources().getString(R.string.app_name), getResources().getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.enableLights(true);
                notificationChannel.setLockscreenVisibility(1);
                notificationManager.createNotificationChannel(notificationChannel);
            }
            Notification notification = new NotificationCompat.Builder(MainActivity.this, getResources().getString(R.string.app_name))
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setColor(getResources().getColor(R.color.black))
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setContentTitle("Copied Text")
                    .setContentText(string)
                    .setChannelId(getResources().getString(R.string.app_name))
                    .setFullScreenIntent(pendingIntent, true)
                    .build();
            notificationManager.notify(1,  notification);
        }
    }

    private boolean checkPermissions(int i) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (String str : permissions) {
            if (ContextCompat.checkSelfPermission(this, str) != 0) {
                arrayList.add(str);
            }
        }
        if (!arrayList.isEmpty()) {
            ActivityCompat.requestPermissions(this, (String[]) arrayList.toArray(new String[arrayList.size()]), i);
            return false;
        }
        if (i == 101) {
            callInstaActivity();
        } else if (i == 102) {
            callWhatsappActivity();
        } else if (i == 104) {
            callFacebookActivity();
        } else if (i == 105) {
            callGalleryActivity();
        } else if (i == 106) {
            callTwitterActivity();
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == 0) {
                callInstaActivity();
            }
        } else if (requestCode == 102) {
            if (grantResults.length > 0 && grantResults[0] == 0) {
                callWhatsappActivity();
            }
        } else if (requestCode == 104) {
            if (grantResults.length > 0 && grantResults[0] == 0) {
                callFacebookActivity();
            }
        } else if (requestCode == 105) {
            if (grantResults.length > 0 && grantResults[0] == 0) {
                callGalleryActivity();
            }
        } else {
            if (requestCode == 106 && grantResults.length > 0 && grantResults[0] == 0) {
                callTwitterActivity();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        doubleBackToExitPressedOnce = true;
        Utils.setToast(this, "Please click/swipe BACK again to exit");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void update() {
        try {
            if (!new VersionChecker().execute(new String[0]).get().equals(getPackageManager().getPackageInfo(getPackageName(), 0).versionName)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                View inflate = getLayoutInflater().inflate(R.layout.update_dialog, null);
                builder.setView(inflate);
                (inflate.findViewById(R.id.btnUpdate)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String packageName = getPackageName();
                        try {
                            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + packageName)));
                        } catch (ActivityNotFoundException unused) {
                            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
                        }
                    }
                });
                AlertDialog create = builder.create();
                create.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                create.setCancelable(false);
                create.show();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public class VersionChecker extends AsyncTask<String, String, String> {
        String newVersion;
        public VersionChecker() {

        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String url = "https://play.google.com/store/apps/details?id=" +
                        getPackageName() +
                        "&hl=en";
                newVersion = Jsoup.connect(url).timeout(30000).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6").referrer("http://www.google.com").get().select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)").first().ownText();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return newVersion;
        }
    }
}