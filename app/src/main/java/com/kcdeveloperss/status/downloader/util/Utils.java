package com.kcdeveloperss.status.downloader.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.kcdeveloperss.status.downloader.R;

import java.io.File;

public class Utils {
    public static String PrivacyPolicyUrl = "https://theknowledgeset.com/status/privacy-policy.html";
    public static String RootDirectoryFacebook = "/StatusSaver/Facebook/";
    public static File RootDirectoryFacebookShow = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/Facebook");
    public static String RootDirectoryInsta = "/StatusSaver/Instagram/";
    public static File RootDirectoryInstaShow = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/Instagram");
    public static String RootDirectoryTwitter = "/StatusSaver/Twitter/";
    public static File RootDirectoryTwitterShow = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/Twitter");
    public static File RootDirectoryWhatsappShow = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/Whatsapp");
    private Context context;
    public static Dialog customDialog;

    static {
        String sb = Environment.getExternalStorageDirectory() +
                "/Download/StatusSaver/Facebook";
        RootDirectoryFacebookShow = new File(sb);
        String sb2 = Environment.getExternalStorageDirectory() +
                "/Download/StatusSaver/Insta";
        RootDirectoryInstaShow = new File(sb2);
        String sb4 = Environment.getExternalStorageDirectory() +
                "/Download/StatusSaver/Twitter";
        RootDirectoryTwitterShow = new File(sb4);
        String sb5 = Environment.getExternalStorageDirectory() +
                "/Download/StatusSaver/Whatsapp";
        RootDirectoryWhatsappShow = new File(sb5);
    }

    public Utils(Context context) {
        this.context = context;
    }

    public static void setToast(Context context, String string) {
        Toast toast = Toast.makeText(context, string, Toast.LENGTH_SHORT);
        toast.setGravity(17, 0, 0);
        toast.show();
    }

    public static void createFileFolder() {
        if (!RootDirectoryFacebookShow.exists()) {
            RootDirectoryFacebookShow.mkdirs();
        }
        if (!RootDirectoryInstaShow.exists()) {
            RootDirectoryInstaShow.mkdirs();
        }
        if (!RootDirectoryTwitterShow.exists()) {
            RootDirectoryTwitterShow.mkdirs();
        }
        if (!RootDirectoryWhatsappShow.exists()) {
            RootDirectoryWhatsappShow.mkdirs();
        }
    }

    public static void showProgressDialog(Activity activity) {
        if (customDialog != null ) {
            customDialog.dismiss();
            customDialog = null;
        }
        customDialog = new Dialog(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.progress_dialog, null);
        customDialog.setCancelable(false);
        customDialog.setContentView(view);
        if (!customDialog.isShowing() && !activity.isFinishing()) {
            customDialog.show();
        }
    }

    public static void hideProgressDialog(Activity activity) {
        if (customDialog != null && customDialog.isShowing()) {
            customDialog.dismiss();
        }
    }

    public boolean isNetworkAvailable() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void startDownload(String url, String str1, Context context, String title) {
        setToast(context, "Download Started");
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(3);
        request.setNotificationVisibility(1);
        request.setTitle(title + "");
        request.setVisibleInDownloadsUi(true);
        String directory = Environment.DIRECTORY_DOWNLOADS;
        request.setDestinationInExternalPublicDir(directory, str1 + title);
        ((DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request);
        try {
            if (Build.VERSION.SDK_INT >= 19) {
                MediaScannerConnection.scanFile(context, new String[]{new File(Environment.DIRECTORY_DOWNLOADS + "/" + str1 + title).getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String s, Uri uri) {

                    }
                });
                return;
            }
            context.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.fromFile(new File(Environment.DIRECTORY_DOWNLOADS + "/" + str1 + title))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void shareImage(Context context, String path) {
        try {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.putExtra("android.intent.extra.TEXT", context.getResources().getString(R.string.share_txt));
            intent.putExtra("android.intent.extra.STREAM", Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), path, "", null)));
            intent.setType("image/*");
            context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string.share_image_via)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void shareImageVideoOnWhatsapp(Context context, String path, boolean isVideo) {
        Uri uri = Uri.parse(path);
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.setPackage("com.whatsapp");
        intent.putExtra("android.intent.extra.TEXT", "");
        intent.putExtra("android.intent.extra.STREAM", uri);
        if (isVideo) {
            intent.setType("video/*");
        } else {
            intent.setType("image/*");
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context.startActivity(intent);
        } catch (Exception unused) {
            setToast(context, "Whatsapp not installed.");
        }
    }

    public static void shareVideo(Context context, String path) {
        Uri uri = Uri.parse(path);
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("video/mp4");
        intent.putExtra("android.intent.extra.STREAM", uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context.startActivity(Intent.createChooser(intent, "Share Video Using"));
        } catch (ActivityNotFoundException unused) {
            setToast(context, "No application found to open this file");
        }
    }

    public static void rateApp(Context context) {
        String action = "android.intent.action.VIEW";
        String packageName = context.getPackageName();
        try {
            String uri = "market://details?id=" + packageName;
            context.startActivity(new Intent(action, Uri.parse(uri)));
        } catch (ActivityNotFoundException unused) {
            String url = "http://play.google.com/store/apps/details?id=" + packageName;
            context.startActivity(new Intent(action, Uri.parse(url)));
        }
    }

    public static void moreApp(Context context) {
        String action = "android.intent.action.VIEW";
        String packageName = context.getPackageName();
        try {
            String uri = "market://details?id=" + packageName;
            context.startActivity(new Intent(action, Uri.parse(uri)));
        } catch (ActivityNotFoundException unused) {
            String url = "http://play.google.com/store/apps/details?id=" + packageName;
            context.startActivity(new Intent(action, Uri.parse(url)));
        }
    }

    public static void shareApp(Context context) {
        String url = "\nhttps://play.google.com/store/apps/details?id=" +
                context.getPackageName();
        Intent intent = new Intent("android.intent.action.SEND");
        intent.putExtra("android.intent.extra.SUBJECT", context.getString(R.string.app_name));
        String text = context.getString(R.string.share_app_message) +
                url;
        intent.putExtra("android.intent.extra.TEXT", text);
        intent.setType("text/plain");
        context.startActivity(Intent.createChooser(intent, "Share"));
    }

    public static void openApp(Context context, String packageName) {
        Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (launchIntentForPackage != null) {
            context.startActivity(launchIntentForPackage);
        } else {
            setToast(context, "App Not Available.");
        }
    }

    public static boolean isNullOrEmpty(String string) {
        return string == null || string.length() == 0 || string.equalsIgnoreCase("null") || string.equalsIgnoreCase("0");
    }

}
