package com.example.musicapp.data.source.local.searching;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.musicapp.data.model.history.HistorySearchedKey;
import com.example.musicapp.data.model.history.HistorySearchedSong;
import com.example.musicapp.data.model.song.Song;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface SearchingDAO {
    @Query("SELECT * FROM history_searched_keys ORDER BY created_at DESC limit 100")
    Flowable<List<HistorySearchedKey>> getAllKeys();

    @Query("SELECT * FROM history_searched_songs ORDER BY selected_at DESC limit 100")
    Flowable<List<HistorySearchedSong>> getHistorySearchedSongs();

    @Query("SELECT * FROM songs WHERE title LIKE '%' || :key || '%' OR artist_id LIKE '%' || :key || '%'")
    Flowable<List<Song>> search(String key);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertKeys(List<HistorySearchedKey> keys);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertSongs(List<HistorySearchedSong> songs);

    @Query("DELETE FROM history_searched_keys")
    Completable clearAllKeys();

    @Query("DELETE FROM history_searched_songs")
    Completable clearAllSongs();
}
