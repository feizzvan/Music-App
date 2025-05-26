package com.example.musicapp.data.repository.recent;

import com.example.musicapp.data.model.RecentSong;
import com.example.musicapp.data.source.RecentSongDataSource;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

// Xử lý các yêu cầu dữ liệu bài hát gần đây từ tầng ViewModel,
// sau đó ủy thác (delegate) các yêu cầu này cho tầng nguồn dữ liệu (RecentSongDataSource).
public class RecentSongRepositoryImpl implements RecentSongRepository {
    private final RecentSongDataSource mRecentSongDataSource;

    @Inject
    public RecentSongRepositoryImpl(RecentSongDataSource recentSongDataSource) {
        this.mRecentSongDataSource = recentSongDataSource;
    }

    @Override
    public Flowable<List<RecentSong>> getAllRecentSongs() {
        return mRecentSongDataSource.getAllRecentSongs();
    }

    @Override
    public Completable insertRecentSong(RecentSong... recentSong) {
        return mRecentSongDataSource.insertRecentSong(recentSong);
    }
}
