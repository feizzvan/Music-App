package com.example.musicapp.data.source;

import com.example.musicapp.data.model.RecentSong;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

// Định nghĩa các chức năng truy cập cơ sở dữ liệu liên quan đến bảng recent_songs
public interface RecentSongDataSource {
    // Lấy danh sách các bài hát đã nghe gần đây từ nguồn dữ liệu
    Flowable<List<RecentSong>> getAllRecentSongs();

    // Thêm một hoặc nhiều bài hát vào danh sách recent_songs
    Completable insertRecentSong(RecentSong... recentSong);
}
