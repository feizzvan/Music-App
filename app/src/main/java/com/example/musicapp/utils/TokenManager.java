package com.example.musicapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {
    private static final String PREFS_NAME = "auth";
    private static final String KEY_TOKEN = "access_token";
    private static final String KEY_USER_ID = "user_id";
    private final SharedPreferences mSharedPreferences;

    public TokenManager(Context context) {
        mSharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveToken(String token) {
        mSharedPreferences.edit().putString(KEY_TOKEN, token).apply();
    }

    public void saveUserId(int userId) {
        mSharedPreferences.edit().putInt(KEY_USER_ID, userId).apply();
    }

    public String getToken() {
        return mSharedPreferences.getString(KEY_TOKEN, null);
    }

    public int getUserId() {
        return mSharedPreferences.getInt(KEY_USER_ID, -1);
    }

    public void clearToken() {
        mSharedPreferences.edit().remove(KEY_TOKEN).apply();
    }

    public void clearUserId() {
        mSharedPreferences.edit().remove(KEY_USER_ID).apply();
    }
}
