package com.kcdeveloperss.status.downloader.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.kcdeveloperss.status.downloader.FullViewActivity;
import com.kcdeveloperss.status.downloader.GalleryActivity;
import com.kcdeveloperss.status.downloader.R;
import com.kcdeveloperss.status.downloader.adapter.FileListAdapter;
import com.kcdeveloperss.status.downloader.interfaces.FileListClickInterface;
import com.kcdeveloperss.status.downloader.util.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class FBDownloadedFragment extends Fragment implements FileListClickInterface {
    private GalleryActivity activity;
    private ArrayList<File> fileArrayList;
    private FileListAdapter fileListAdapter;

    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvFileList;

    public static FBDownloadedFragment newInstance(String str) {
        FBDownloadedFragment fBDownloadedFragment = new FBDownloadedFragment();
        Bundle bundle = new Bundle();
        bundle.putString("m", str);
        fBDownloadedFragment.setArguments(bundle);
        return fBDownloadedFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (GalleryActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            getArguments().getString("m");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        activity = (GalleryActivity) getActivity();
        getAllFiles();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history, container, false);
        initViews();
        return view;
    }

    private void initViews() {
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        rvFileList = view.findViewById(R.id.rv_fileList);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllFiles();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void getAllFiles() {
        fileArrayList = new ArrayList<>();
        File[] listFiles = Utils.RootDirectoryFacebookShow.listFiles();
        if (listFiles != null) {
            Collections.addAll(fileArrayList, listFiles);
            fileListAdapter = new FileListAdapter(activity, fileArrayList, this);
            rvFileList.setAdapter(fileListAdapter);
        }
    }

    @Override
    public void getPosition(int i, File file) {
        Intent intent = new Intent(activity, FullViewActivity.class);
        intent.putExtra("ImageDataFile", fileArrayList);
        intent.putExtra("Position", i);
        activity.startActivity(intent);
    }
}
