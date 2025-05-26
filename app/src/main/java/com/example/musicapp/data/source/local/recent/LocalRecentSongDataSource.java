package com.example.musicapp.data.source.local.recent;

import com.example.musicapp.data.model.RecentSong;
import com.example.musicapp.data.source.RecentSongDataSource;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

// Triển khai các phương thức được định nghĩa trong RecentSongDataSource bằng cách sử dụng RecentSongDAO
public class LocalRecentSongDataSource implements RecentSongDataSource {
    private final RecentSongDAO mRecentSongDAO;

    // Hàm khởi tạo RecentSongDataSource.
    @Inject
    public LocalRecentSongDataSource(RecentSongDAO recentSongDAO) {
        this.mRecentSongDAO = recentSongDAO;
    }

    @Override
    public Flowable<List<RecentSong>> getAllRecentSongs() {
        return mRecentSongDAO.getAllRecentSongs();
    }

    @Override
    public Completable insertRecentSong(RecentSong... recentSong) {
        return mRecentSongDAO.insertRecentSong(recentSong);
    }
}
