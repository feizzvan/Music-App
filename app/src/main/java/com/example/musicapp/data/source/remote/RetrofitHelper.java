package com.example.musicapp.data.source.remote;

import com.example.musicapp.utils.AppConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// `RetrofitHelper` là một lớp tiện ích để tạo một thể hiện của Retrofit với cấu hình sẵn.
// Mục tiêu là tách logic tạo và cấu hình Retrofit ra riêng để tái sử dụng và dễ dàng quản lý.

public abstract class RetrofitHelper {
//    private static final String BASE_URL = "http://192.168.0.101:8080";
    private static final String BASE_URL = AppConfig.BASE_URL;

    private static AppService instance;

    public static AppService getInstance() {
        if (instance == null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .create();

            instance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                    .create(AppService.class);
        }
        return instance;
    }
}
