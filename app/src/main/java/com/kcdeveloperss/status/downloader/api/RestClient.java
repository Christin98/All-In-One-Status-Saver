package com.kcdeveloperss.status.downloader.api;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class RestClient {

    private static Activity mActivity;
    private static final RestClient restClient = new RestClient();
    private static Retrofit retrofit;

    public static RestClient getInstance(Activity activity) {
        mActivity = activity;
        return restClient;
    }

    private RestClient() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient build = new Builder().readTimeout(2, TimeUnit.MINUTES).connectTimeout(2, TimeUnit.MINUTES).writeTimeout(2, TimeUnit.MINUTES).addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                Response response = null;
                try {
                    response = chain.proceed(chain.request());
                    if (response.code() == 200) {
                        try {
                            String jSONObject = new JSONObject(response.body().string()).toString();
                            printMsg(jSONObject + "");
                            return response.newBuilder().body(ResponseBody.create(response.body().contentType(), jSONObject)).build();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                }
                return response;
            }
        }).addInterceptor(httpLoggingInterceptor).build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl("https://www.instagram.com").addConverterFactory(GsonConverterFactory.create(new Gson())).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).client(build).build();
        }
    }

    public APIServices getService() {
        return retrofit.create(APIServices.class);
    }

    private void printMsg(String string) {
        int length = string.length() / 4050;
        int i = 0;
        while (i <= length) {
            int i2 = i + 1;
            int i3 = i2 * 4050;
            String string2 = "Response::";
            if (i3 >= string.length()) {
                Log.d(string2, string.substring(i * 4050));
            } else {
                Log.d(string2, string.substring(i * 4050, i3));
            }
            i = i2;
        }
    }

}
