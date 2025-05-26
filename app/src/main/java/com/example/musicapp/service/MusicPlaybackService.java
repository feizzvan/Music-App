package com.example.musicapp.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.media3.session.MediaController;
import androidx.media3.session.SessionToken;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.ExecutionException;

public class MusicPlaybackService extends Service {
    private ListenableFuture<MediaController> mControllerFuture;
    private MediaController mMediaController;
    private final MutableLiveData<Boolean> mIsMediaControllerInitialized = new MutableLiveData<>();
    private LocalBinder mBinder;

    @Override
    public void onCreate() {
        super.onCreate();

        setupMediaSession();
        mBinder = new LocalBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaController != null) {
            mMediaController.release();
        }
        MediaController.releaseFuture(mControllerFuture);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    private void setupMediaSession() {
        SessionToken sessionToken = new SessionToken(getApplicationContext(),
                new ComponentName(getApplicationContext(), PlaybackService.class));
        mControllerFuture = new MediaController.Builder(getApplicationContext(), sessionToken).buildAsync();
        Runnable listener = () -> {
            if (mControllerFuture.isDone() && !mControllerFuture.isCancelled()) {
                try {
                    mMediaController = mControllerFuture.get();
                    if (mMediaController != null) {
                        mIsMediaControllerInitialized.postValue(true);
                    }
                } catch (ExecutionException | InterruptedException ignored) {
                }
            } else {
                mMediaController = null;
            }
        };
        mControllerFuture.addListener(listener, MoreExecutors.directExecutor());

    }

    public class LocalBinder extends Binder {
        public MediaController getMediaSession() {
            return mMediaController;
        }

        public LiveData<Boolean> isMediaControllerInitialized() {
            return mIsMediaControllerInitialized;
        }
    }

}