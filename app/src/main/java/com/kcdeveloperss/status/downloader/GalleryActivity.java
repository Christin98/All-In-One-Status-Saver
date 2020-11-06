package com.kcdeveloperss.status.downloader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;
import com.kcdeveloperss.status.downloader.fragment.FBDownloadedFragment;
import com.kcdeveloperss.status.downloader.util.AdsUtils;
import com.kcdeveloperss.status.downloader.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabs;
    ImageView imBack;
    AdView adView;

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        adView = findViewById(R.id.adView);
        AdsUtils.showGoogleBannerAd(this, adView);
        AdsUtils.showGoogleInterstitialAd(this);
        initViews();
    }

    public void initViews() {
        viewPager = findViewById(R.id.viewpager);
        tabs = findViewById(R.id.tabs);
        imBack = findViewById(R.id.imBack);
        setupViewPager(viewPager);
        tabs.setupWithViewPager(viewPager);
        imBack.setOnClickListener(v -> onBackPressed());
        for (int i = 0; i < tabs.getTabCount(); i++) {
            tabs.getTabAt(i).setCustomView(LayoutInflater.from(this).inflate(R.layout.custom_tab, null));
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        Utils.createFileFolder();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 1);
        viewPagerAdapter.addFragment(new FBDownloadedFragment(), "Facebook");
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(4);
    }
}