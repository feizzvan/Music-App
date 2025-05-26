package com.example.musicapp.service;


import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.session.MediaSession;
import androidx.media3.session.MediaSessionService;

import com.example.musicapp.data.model.PlayingSong;
import com.example.musicapp.data.model.song.Song;
import com.example.musicapp.data.repository.recent.RecentSongRepository;
import com.example.musicapp.data.repository.song.SongRepository;
import com.example.musicapp.ui.playing.NowPlayingActivity;
import com.example.musicapp.utils.SharedDataUtils;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

// Lớp PlaybackService để cung cấp và quản lý phát nhạc cho ứng dụng
@AndroidEntryPoint
public class PlaybackService extends MediaSessionService {
    // ID của thông báo phát nhạc (dùng để cập nhật hoặc hủy thông báo khi cần thiết)
    public static final int NOTIFICATION_ID = 9999;

    public static final String CHANNEL_ID = "music_app_notification_channel_id";

    // MediaSession quản lý các sự kiện điều khiển phát nhạc (play, pause, skip)
    private MediaSession mMediaSession;

    // Listener để theo dõi các thay đổi từ ExoPlayer
    private Player.Listener mPlayerListener;

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Inject
    public RecentSongRepository recentSongRepository;

//    @Inject
//    SongRepository.Local localSongRepository;

    @OptIn(markerClass = UnstableApi.class)
    @Override
    public void onCreate() {
        super.onCreate();
        // Khởi tạo MediaSession và ExoPlayer khi dịch vụ được tạo
        initSessionAndPlayer();

        // Thiết lập listener để theo dõi các sự kiện từ ExoPlayer
        setupListener();
    }

    @Nullable
    public MediaSession onGetSession(@NonNull MediaSession.ControllerInfo controllerInfo) {
        // Cung cấp MediaSession cho hệ thống, cho phép điều khiển nhạc từ UI khác (thông báo, màn hình khóa)
        return mMediaSession;
    }

    @Override
    public void onDestroy() {
        mMediaSession.getPlayer().removeListener(mPlayerListener); // Xoá listener khỏi ExoPlayer
        mMediaSession.getPlayer().release(); // Giải phóng tài nguyên của ExoPlayer
        mMediaSession.release(); // Giải phóng tài nguyên của MediaSession
        mMediaSession = null;
        mDisposable.dispose();
        super.onDestroy();
    }

    private void initSessionAndPlayer() {
        // Tạo ExoPlayer với cấu hình mặc định
        ExoPlayer player = new ExoPlayer.Builder(this)
                .setAudioAttributes(AudioAttributes.DEFAULT, true) // Thiết lập thuộc tính âm thanh mặc định,
                // cho phép nhạc tạm dừng khi có cuộc gọi hặc thông báo ưu tiên
                .build();

        // Tạo MediaSession và liên kết với ExoPlayer
        MediaSession.Builder mediaSessionBuilder = new MediaSession.Builder(this, player);

        PendingIntent pendingIntent = getSingleTopActivity();
        if(pendingIntent != null){
            mediaSessionBuilder.setSessionActivity(pendingIntent);
        }
        // Gán MediaSession đã được xây dựng vào biến mMediaSession
        mMediaSession = mediaSessionBuilder.build();
    }

    private PendingIntent getSingleTopActivity(){
        Intent intent = new Intent(getApplicationContext(), NowPlayingActivity.class);
        return PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    private void setupListener() {
        Player player = mMediaSession.getPlayer();
        mPlayerListener = new Player.Listener() {
            @Override
            public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
                boolean isPlaylistChanged = reason == Player.MEDIA_ITEM_TRANSITION_REASON_PLAYLIST_CHANGED;
                Integer indexToPlay = SharedDataUtils.getIndexToPlay().getValue();
                if (!isPlaylistChanged || indexToPlay != null && indexToPlay == 0) {
                    // Cập nhật chỉ số bài hát đang phát trong NowPlayingViewModel
                    SharedDataUtils.setPlayingSong(player.getCurrentMediaItemIndex());
                    saveDataToDB();
                }
            }

            // Gọi khi trạng thái phát nhạc thay đổi (bắt đầu hoặc tạm dừng).
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                Player.Listener.super.onIsPlayingChanged(isPlaying);
            }
        };

        player.addListener(mPlayerListener);
    }

    private void saveDataToDB() {
        Song song = extractSong();
        if (song != null) {
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                Player player = mMediaSession.getPlayer();
                if (player.isPlaying()) {
                    mDisposable.add(SharedDataUtils.saveRecentSong(song, recentSongRepository)
                            .subscribeOn(Schedulers.io())
                            .subscribe());
//                    saveCounterToDB();
                }
            }, 5000);
        }
    }

    private void saveCounterToDB() {
        Song song = extractSong();
        if (song != null) {
            HandlerThread handlerThread = new HandlerThread("Thread Save Counter",
                    Process.THREAD_PRIORITY_BACKGROUND);
            handlerThread.start();
            Handler handler = new Handler(handlerThread.getLooper());
//            handler.post(() -> mDisposable.add(SharedDataUtils.updateSongInDB(song, localSongRepository)
//                    .subscribeOn(Schedulers.io())
//                    .subscribe()));
        }
    }

    private Song extractSong() {
        PlayingSong playingSong = SharedDataUtils.getPlayingSong().getValue();
        if (playingSong == null) {
            return null;
        }
        return playingSong.getSong();
    }
}