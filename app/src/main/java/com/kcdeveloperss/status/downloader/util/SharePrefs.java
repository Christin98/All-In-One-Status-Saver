package com.kcdeveloperss.status.downloader.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePrefs {
    public static String COOKIES = "Cookies";
    public static String CSRF = "csrf";
    public static String ISINSTALOGIN = "IsInstaLogin";
    public static String ISSHOWHOWTOFB = "IsShoHowToFB";
    public static String ISSHOWHOWTOINSTA = "IsShoHowToInsta";
    public static String ISSHOWHOWTOLIKEE = "IsShoHowToLikee";
    public static String ISSHOWHOWTOTT = "IsShoHowToTT";
    public static String ISSHOWHOWTOTWITTER = "IsShoHowToTwitter";
    public static String PREFERENCE = "AllInOneDownloader";
    public static String SESSIONID = "session_id";
    public static String USERID = "user_id";
    private static SharePrefs instance;
    private Context ctx;
    private SharedPreferences sharedPreferences;

    public SharePrefs(Context context) {
        ctx = context;
        sharedPreferences = context.getSharedPreferences(PREFERENCE, 0);
    }

    public static SharePrefs getInstance(Context context) {
        if (instance == null) {
            instance = new SharePrefs(context);
        }
        return instance;
    }

    public void putString(String str, String str2) {
        this.sharedPreferences.edit().putString(str, str2).apply();
    }

    public String getString(String str) {
        return this.sharedPreferences.getString(str, "");
    }

    public void putInt(String str, Integer num) {
        this.sharedPreferences.edit().putInt(str, num).apply();
    }

    public void putBoolean(String str, Boolean bool) {
        this.sharedPreferences.edit().putBoolean(str, bool).apply();
    }

    public Boolean getBoolean(String str) {
        return sharedPreferences.getBoolean(str, false);
    }

    public int getInt(String str) {
        return sharedPreferences.getInt(str, 0);
    }

    public void clearSharePrefs() {
        sharedPreferences.edit().clear().apply();
    }

}
