package com.example.musicapp.utils;

import android.content.Context;

import com.example.musicapp.R;

public class AppConfig {
    public static String BASE_URL;

    public static void init(Context context) {
        BASE_URL = context.getString(R.string.base_url);
    }
}
