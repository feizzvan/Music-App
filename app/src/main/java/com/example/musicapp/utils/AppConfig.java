package com.example.musicapp.utils;

import android.content.Context;

import com.example.musicapp.R;

public class AppConfig {
    public static String BASE_URL;

    public static void init() {
//        BASE_URL = context.getString(R.string.base_url);
//        BASE_URL = "https://trusty-optimal-fowl.ngrok-free.app";
        BASE_URL = "http://192.168.0.103:8080";
    }
}
