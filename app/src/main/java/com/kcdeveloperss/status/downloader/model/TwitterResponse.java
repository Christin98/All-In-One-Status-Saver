package com.kcdeveloperss.status.downloader.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TwitterResponse {
    @SerializedName("videos")
    private ArrayList<TwitterResponseModel> videos;

    public ArrayList<TwitterResponseModel> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<TwitterResponseModel> arrayList) {
        videos = arrayList;
    }
}
