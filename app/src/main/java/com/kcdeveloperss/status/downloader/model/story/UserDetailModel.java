package com.kcdeveloperss.status.downloader.model.story;

import com.google.gson.annotations.SerializedName;

public class UserDetailModel {
    @SerializedName("user")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
