package com.example.musicapp.data.source.local.song;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.musicapp.data.model.song.Song;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import kotlinx.coroutines.flow.Flow;

@Dao
public interface SongDAO {
    @Query("SELECT * FROM songs")
    Single<List<Song>> getAllSongs();

    @Query("SELECT * FROM songs WHERE song_id = :songId")
    Flowable<Song> getSongById(int songId);

//    @Query("SELECT * FROM songs WHERE artist LIKE :key")
//    List<Song> getSongsByArtistName(String key);

//    @Query("SELECT * FROM songs ORDER BY counter DESC LIMIT :limit")
//    Flowable<List<Song>> getTopNMostHeardSongs(int limit);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertSongs(Song... song);

    @Delete
    Completable deleteSong(Song song); // Không trả về giá trị khi hoàn thành

    @Update
    Completable updateSong(Song song);
}
