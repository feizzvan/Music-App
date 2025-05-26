package com.example.musicapp.data.model.playlist;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;

// Tương tác với DB (bảng trung gian)
@Entity(
        tableName = "playlist_song_cross_ref",
        primaryKeys = { //Một bài hát có thể có trong nhiều playlist. Một playlist có thể chứa nhiều bài hát
                "playlist_id",
                "song_id"
        },
        indices = { //Room sẽ tạo chỉ mục (index) để tăng hiệu năng truy vấn theo playlist_id hoặc song_id.
                @Index("playlist_id"),
                @Index("song_id")
        }
)
public class PlaylistSongCrossRef {
    @NonNull
    @ColumnInfo(name = "song_id")
    public int songId = -1;

    @ColumnInfo(name = "playlist_id")
    public int playlistId = 0;
}
