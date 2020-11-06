package com.kcdeveloperss.status.downloader;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;

public class SplashActivity extends AppCompatActivity {

    AppUpdateManager appUpdateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        appUpdateManager = AppUpdateManagerFactory.create(this);
        updateApp();
    }

    private void homeScreen() {
        new Handler().postDelayed(() -> startActivity(new Intent(SplashActivity.this, MainActivity.class)), 5000);
    }

    private void updateApp() {
        try {
            appUpdateManager.getAppUpdateInfo().addOnSuccessListener(result -> {
                if (result.updateAvailability() != UpdateAvailability.UPDATE_AVAILABLE || !result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    homeScreen();
                    return;
                }
                try {
                    appUpdateManager.startUpdateFlowForResult(result, AppUpdateType.IMMEDIATE, SplashActivity.this, 101);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }).addOnFailureListener(e -> {
                e.printStackTrace();
                homeScreen();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(result -> {
            if (result.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                try {
                    appUpdateManager.startUpdateFlowForResult(result, AppUpdateType.IMMEDIATE, SplashActivity.this, 101);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 101) {
            return;
        }
        homeScreen();
    }
}