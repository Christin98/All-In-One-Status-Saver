package com.kcdeveloperss.status.downloader.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.kcdeveloperss.status.downloader.FullViewActivity;
import com.kcdeveloperss.status.downloader.R;
import com.kcdeveloperss.status.downloader.util.Utils;

import java.io.File;
import java.util.ArrayList;

public class ShowImagesAdapter extends PagerAdapter {
    private Context context;
    FullViewActivity fullViewActivity;

    public ArrayList<File> imageList;
    private LayoutInflater inflater;

    @Override
    public int getItemPosition(@NonNull Object object) {
        return -2;
    }

    @Override
    public void restoreState(@Nullable Parcelable state, @Nullable ClassLoader loader) {
    }

    @Nullable
    @Override
    public Parcelable saveState() {
        return null;
    }

    public ShowImagesAdapter(Context context, ArrayList<File> imageList, FullViewActivity fullViewActivity) {
        this.context = context;
        this.imageList = imageList;
        this.fullViewActivity = fullViewActivity;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.slidingimages_layout, container, false);
        ImageView imageView = view.findViewById(R.id.im_vpPlay);
        ImageView imageView1 = view.findViewById(R.id.im_share);
        ImageView imageView2 = view.findViewById(R.id.im_delete);
        Glide.with(context).load(imageList.get(position).getPath()).into((ImageView) view.findViewById(R.id.im_fullViewImage));
        imageView.setOnClickListener(v -> {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setDataAndType(Uri.parse(imageList.get(position).getPath()), "video/*");
            context.startActivity(intent);
        });

        imageView2.setOnClickListener(v -> {
            if (imageList.get(position).delete()) {
                fullViewActivity.deleteFileAA(position);
            }
        });

        imageView1.setOnClickListener(v -> {
            if (imageList.get(position).getName().substring(imageList.get(position).getName().lastIndexOf(".")).equals(".mp4")) {
                Utils.shareVideo(context, imageList.get(position).getPath());
            } else {
                Utils.shareImage(context, imageList.get(position).getPath());
            }
        });
        return view;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
}
