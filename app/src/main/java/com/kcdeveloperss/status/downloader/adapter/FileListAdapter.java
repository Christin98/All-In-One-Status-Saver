package com.kcdeveloperss.status.downloader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kcdeveloperss.status.downloader.R;
import com.kcdeveloperss.status.downloader.interfaces.FileListClickInterface;

import java.io.File;
import java.util.ArrayList;

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<File> fileArrayList;
    public FileListClickInterface fileListClickInterface;
    private LayoutInflater layoutInflater;

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rlMain;
        ImageView ivImage;
        ImageView ivPlay;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rlMain = itemView.findViewById(R.id.rl_main);
            ivImage = itemView.findViewById(R.id.iv_image);
            ivPlay = itemView.findViewById(R.id.iv_play);
        }
    }

    public FileListAdapter(Context context , ArrayList<File> arrayList, FileListClickInterface fileListClickInterface) {
        this.context = context;
        fileArrayList = arrayList;
        this.fileListClickInterface = fileListClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        return new ViewHolder(layoutInflater.inflate(R.layout.items_file_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final File file = fileArrayList.get(position);
        try {
            if (file.getName().substring(file.getName().lastIndexOf(".")).equals(".mp4")) {
                holder.ivPlay.setVisibility(View.VISIBLE);
            } else {
                holder.ivPlay.setVisibility(View.GONE);
            }
            Glide.with(context).load(file.getPath()).into(holder.ivImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.rlMain.setOnClickListener(v -> fileListClickInterface.getPosition(position, file));
    }

    @Override
    public int getItemCount() {
        if (fileArrayList == null) {
            return 0;
        }
        return fileArrayList.size();
    }

}
