package com.kcdeveloperss.status.downloader.api;

import com.google.gson.JsonObject;
import com.kcdeveloperss.status.downloader.model.TwitterResponse;
import com.kcdeveloperss.status.downloader.model.story.FullDetailModel;
import com.kcdeveloperss.status.downloader.model.story.StoryModel;

import io.reactivex.Observable;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface APIServices {
    @GET
    Observable<JsonObject> callResult(@Url String url, @Header("Cookie") String cookie, @Header("User-Agent") String userAgent);

    @FormUrlEncoded
    @POST
    Observable<TwitterResponse> callTwitter(@Url String url, @Field("id") String id);

    @GET
    Observable<FullDetailModel> getFullDetailInfoApi(@Url String url, @Header("Cookie") String cookie, @Header("User-Agent") String userAgent);

    @GET
    Observable<StoryModel> getStoriesApi(@Url String url, @Header("Cookie") String cookie, @Header("User-Agent") String userAgent);

}
