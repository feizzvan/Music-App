package com.example.musicapp.data.source;

import com.example.musicapp.data.model.history.HistorySearchedKey;
import com.example.musicapp.data.model.history.HistorySearchedSong;
import com.example.musicapp.data.model.song.Song;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface SearchingDataSource {
    interface Local {
        Flowable<List<HistorySearchedKey>> getAllKeys();

        Flowable<List<HistorySearchedSong>> getHistorySearchedSongs();

        Completable insertKeys(List<HistorySearchedKey> keys);

        Completable insertSongs(List<HistorySearchedSong> songs);

        Completable clearAllKeys();

        Completable clearAllSongs();
    }

    interface Remote {
        Single<List<Song>> search(String key);
    }
}
