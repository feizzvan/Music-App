package com.example.musicapp.data.repository.recent;

import com.example.musicapp.data.model.RecentSong;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Completable;


// Định nghĩa các phương thức cần thiết để thao tác với dữ liệu bài hát gần đây (RecentSong).
// Kết nối giữa ViewModel và DataSource: ViewModel sử dụng RecentSongRepository để lấy hoặc lưu dữ liệu
public interface RecentSongRepository {
    Flowable<List<RecentSong>> getAllRecentSongs();

    Completable insertRecentSong(RecentSong... recentSong);
}
