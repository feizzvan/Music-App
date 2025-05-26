//package com.example.musicapp.data.source.local.playlist;
//
//import androidx.room.Dao;
//import androidx.room.Delete;
//import androidx.room.Insert;
//import androidx.room.Query;
//import androidx.room.Transaction;
//import androidx.room.Update;
//
//import com.example.musicapp.data.model.playlist.Playlist;
//import com.example.musicapp.data.model.playlist.PlaylistSongCrossRef;
//import com.example.musicapp.data.model.playlist.PlaylistWithSongs;
//
//import java.util.Date;
//import java.util.List;
//
//import io.reactivex.rxjava3.core.Completable;
//import io.reactivex.rxjava3.core.Flowable;
//import io.reactivex.rxjava3.core.Single;
//
//@Dao
//public interface PlaylistDAO {
//    @Query("SELECT * FROM playlists")
//    Flowable<List<Playlist>> getAll();
//
//    @Query("SELECT * FROM playlists WHERE name = :name")
//    Single<Playlist> findByName(String name);
//
//    //Transaction dùng để đảm bảo toàn vẹn dữ liệu
//    //Lấy tất cả playlist với các bài hát trong nó
//    @Transaction()
//    @Query("SELECT * FROM playlists")
//    Flowable<List<PlaylistWithSongs>> getAllPlaylistWithSongs();
//
//    //Truy vấn một playlist theo ID, kèm luôn danh sách các bài hát trong nó
//    @Transaction
//    @Query("SELECT * FROM playlists WHERE playlist_id = :playlistId")
//    Single<PlaylistWithSongs> findPlaylistWithSongByPlaylistId(int playlistId);
//
//    @Query("INSERT INTO playlists(name, artwork, created_at) VALUES(:name, :artwork, :createdAt)")
//    Completable insert(String name, String artwork, Date createdAt);
//
//    //Dùng để gán một bài hát vào một playlist bằng cách thêm bản ghi vào bảng trung gian playlist_song_cross_ref.
//    @Insert
//    Completable insertPlaylistSongCrossRef(PlaylistSongCrossRef object);
//
//    @Update
//    Completable update(Playlist playlist);
//
//    @Delete
//    Completable delete(Playlist playlist);
//}
