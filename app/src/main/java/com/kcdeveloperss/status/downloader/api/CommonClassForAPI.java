package com.kcdeveloperss.status.downloader.api;

import android.app.Activity;

import com.google.gson.JsonObject;
import com.kcdeveloperss.status.downloader.model.TwitterResponse;
import com.kcdeveloperss.status.downloader.model.story.FullDetailModel;
import com.kcdeveloperss.status.downloader.model.story.StoryModel;
import com.kcdeveloperss.status.downloader.util.Utils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class CommonClassForAPI {
    private static CommonClassForAPI CommonClassForAPI;
    private static Activity mActivity;

    public static CommonClassForAPI getInstance(Activity activity) {
        if (CommonClassForAPI == null) {
            CommonClassForAPI = new CommonClassForAPI();
        }
        mActivity = activity;
        return CommonClassForAPI;
    }

    public void callResult(final DisposableObserver disposableObserver, String str, String str2) {
        if (Utils.isNullOrEmpty(str2)) {
            str2 = "";
        }
        RestClient.getInstance(mActivity).getService().callResult(str, str2, "Instagram 9.5.2 (iPhone7,2; iPhone OS 9_3_3; en_US; en-US; scale=2.00; 750x1334) AppleWebKit/420+").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe( new Observer<JsonObject>() {
            public void onSubscribe(Disposable disposable) {
            }

            public void onNext(JsonObject jsonObject) {
                disposableObserver.onNext(jsonObject);
            }

            public void onError(Throwable th) {
                disposableObserver.onError(th);
            }

            public void onComplete() {
                disposableObserver.onComplete();
            }
        });
    }

    public void callTwitterApi(final DisposableObserver<TwitterResponse> disposableObserver, String str, String str2) {
        RestClient.getInstance(mActivity).getService().callTwitter(str, str2).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe( new Observer<TwitterResponse>() {
            public void onSubscribe(Disposable disposable) {
            }

            public void onNext(TwitterResponse twitterResponse) {
                disposableObserver.onNext(twitterResponse);
            }

            public void onError(Throwable th) {
                disposableObserver.onError(th);
            }

            public void onComplete() {
                disposableObserver.onComplete();
            }
        });
    }

    public void getStories(final DisposableObserver<StoryModel> disposableObserver, String str) {
        if (Utils.isNullOrEmpty(str)) {
            str = "";
        }
        RestClient.getInstance(mActivity).getService().getStoriesApi("https://i.instagram.com/api/v1/feed/reels_tray/", str, "\"Instagram 9.5.2 (iPhone7,2; iPhone OS 9_3_3; en_US; en-US; scale=2.00; 750x1334) AppleWebKit/420+\"").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe( new Observer<StoryModel>() {
            public void onSubscribe(Disposable disposable) {
            }

            public void onNext(StoryModel storyModel) {
                disposableObserver.onNext(storyModel);
            }

            public void onError(Throwable th) {
                disposableObserver.onError(th);
            }

            public void onComplete() {
                disposableObserver.onComplete();
            }
        });
    }

    public void getFullDetailFeed(final DisposableObserver<FullDetailModel> disposableObserver, String str, String str2) {
        APIServices service = RestClient.getInstance(mActivity).getService();
        String sb = "https://i.instagram.com/api/v1/users/" +
                str +
                "/full_detail_info?max_id=";
        service.getFullDetailInfoApi(sb, str2, "\"Instagram 9.5.2 (iPhone7,2; iPhone OS 9_3_3; en_US; en-US; scale=2.00; 750x1334) AppleWebKit/420+\"").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe( new Observer<FullDetailModel>() {
            public void onSubscribe(Disposable disposable) {
            }

            public void onNext(FullDetailModel fullDetailModel) {
                disposableObserver.onNext(fullDetailModel);
            }

            public void onError(Throwable th) {
                disposableObserver.onError(th);
            }

            public void onComplete() {
                disposableObserver.onComplete();
            }
        });
    }
}
