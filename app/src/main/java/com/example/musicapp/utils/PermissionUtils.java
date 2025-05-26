package com.example.musicapp.utils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public final class PermissionUtils {
    private static final MutableLiveData<Boolean> sPermissionAsked = new MutableLiveData<>();
    private static final MutableLiveData<Boolean> sPermissionGranted = new MutableLiveData<>();
    public static boolean isRegistered = false;
    public static LiveData<Boolean> getPermissionAsked() {
        return sPermissionAsked;
    }
    public static LiveData<Boolean> getPermissionGranted() {
        return sPermissionGranted;
    }

    public static void setPermissionAsked(boolean permissionAsked) {
        sPermissionAsked.setValue(permissionAsked);
    }

    public static void setPermissionGranted(boolean permissionGranted) {
        sPermissionGranted.setValue(permissionGranted);
    }
}
