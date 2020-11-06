package com.kcdeveloperss.status.downloader;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kcdeveloperss.status.downloader.adapter.ShowImagesAdapter;
import com.kcdeveloperss.status.downloader.util.Utils;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;

public class FullViewActivity extends AppCompatActivity {

    private int Position = 0;

    public ArrayList<File> fileArrayList;
    ShowImagesAdapter showImagesAdapter;

    ViewPager vpView;
    FloatingActionButton imDelete;
    FloatingActionButton imShare;
    FloatingActionButton imWhatsappShare;
    ImageView imClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_view);

        if (getIntent().getExtras() != null) {
            fileArrayList = (ArrayList) getIntent().getSerializableExtra("ImageDataFile");
            Position = getIntent().getIntExtra("Position", 0);
        }
        initViews();
    }

    public void initViews() {
        showImagesAdapter = new ShowImagesAdapter(this, fileArrayList, this);
        vpView = findViewById(R.id.vp_view);
        imDelete = findViewById(R.id.imDelete);
        imShare = findViewById(R.id.imShare);
        imWhatsappShare = findViewById(R.id.imWhatsappShare);
        imClose = findViewById(R.id.im_close);

        vpView.setAdapter(showImagesAdapter);
        vpView.setCurrentItem(Position);
        vpView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Position = position;
                PrintStream printStream = System.out;
                String cp = "Current position==" + Position;
                printStream.println(cp);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        imDelete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(FullViewActivity.this);
            builder.setPositiveButton("YES", (dialog, which) -> {
                if (fileArrayList.get(Position).delete()) {
                    deleteFileAA(Position);
                }
            });
            builder.setNegativeButton("NO", (dialog, which) -> dialog.cancel());
            AlertDialog dialog = builder.create();
            dialog.setTitle("Do you want to delete this file?");
            dialog.show();
        });
        imShare.setOnClickListener(v -> {
            if (fileArrayList.get(Position).getName().contains(".mp4")) {
                String sb = "onClick: " + fileArrayList.get(Position) + "";
                Log.d("SSSSS", sb);
                Utils.shareVideo(FullViewActivity.this, fileArrayList.get(Position).getPath());
                return;
            }
            Utils.shareImage(FullViewActivity.this, fileArrayList.get(Position).getPath());
        });
        imWhatsappShare.setOnClickListener(v -> Utils.shareImageVideoOnWhatsapp(FullViewActivity.this, fileArrayList.get(Position).getPath(), fileArrayList.get(Position).getName().contains(".mp4")));
        imClose.setOnClickListener(v -> onBackPressed());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void deleteFileAA(int i) {
        fileArrayList.remove(i);
        showImagesAdapter.notifyDataSetChanged();
        Utils.setToast(this, "File Deleted");
        if (fileArrayList.size() == 0) {
            onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}