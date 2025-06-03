package com.example.musicapp;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.example.musicapp.ui.settings.SettingsFragment;
import com.example.musicapp.utils.AppConfig;

import java.util.Locale;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class MusicApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        AppConfig.init();
        setupLanguage();
    }

    private void setupLanguage() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String defaultLanguage = Locale.getDefault().getLanguage();
        String language = sharedPreferences.getString(SettingsFragment.KEY_PREF_LANGUAGE, defaultLanguage);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SettingsFragment.KEY_PREF_LANGUAGE, language);
        editor.apply();
    }
}
