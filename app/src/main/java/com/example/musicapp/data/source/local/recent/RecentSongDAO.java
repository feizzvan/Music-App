package com.example.musicapp.data.source.local.recent;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.musicapp.data.model.RecentSong;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

// DAO này cung cấp các phương thức truy cập cơ sở dữ liệu liên quan đến bảng recent_songs
@Dao
public interface RecentSongDAO {
    // Lấy danh sách 30 bài hát phát gần đây nhất (theo thời gian gần nhất), phục vụ cho việc hiển thị danh sách này trên giao diện.
    @Query("SELECT * FROM recent_songs ORDER BY play_at DESC LIMIT 30")
    //Sắp xếp theo giảm dần theo thời gian phát
    Flowable<List<RecentSong>> getAllRecentSongs();

    // Thêm 1 hoặc nhiều bài hát vào danh sách phát gần đây. Nếu bài hát đã tồn tại, dữ liệu sẽ được cập nhật (thay thế).
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertRecentSong(RecentSong... recentSong);
}
