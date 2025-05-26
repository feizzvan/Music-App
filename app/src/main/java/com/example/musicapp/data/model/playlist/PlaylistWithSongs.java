//package com.example.musicapp.data.model.playlist;
//
//import androidx.room.Embedded;
//import androidx.room.Junction;
//import androidx.room.Relation;
//
//import com.example.musicapp.data.model.song.Song;
//
//import java.util.List;
//
//// public dữ liệu ra client để lấy dữ liệu ra hiển thị hoặc tương tác
//// Liên kết 1 playlist với nhiều song (quan hệ nhiều - nhiều)
//public class PlaylistWithSongs {
//    @Embedded //Chèn toàn bộ thông tin của bảng playlist vào đối tượng này
//    public Playlist playlist;
//
//    @Relation(
//            parentColumn = "playlist_id",
//            entityColumn = "song_id",
//            associateBy = @Junction(PlaylistSongCrossRef.class) //Dùng bảng trung gian
//    )
//
//    public List<Song> songs;
//}
//