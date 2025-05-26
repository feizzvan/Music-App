package com.example.musicapp.data.repository.searching;

import com.example.musicapp.data.model.history.HistorySearchedKey;
import com.example.musicapp.data.model.history.HistorySearchedSong;
import com.example.musicapp.data.model.song.Song;
import com.example.musicapp.data.source.SearchingDataSource;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public class SearchingRepositoryImpl implements SearchingRepository {
    private final SearchingDataSource mSearchingDataSource;

    @Inject
    public SearchingRepositoryImpl(SearchingDataSource searchingDataSource) {
        mSearchingDataSource = searchingDataSource;
    }

    @Override
    public Flowable<List<HistorySearchedKey>> getAllKeys() {
        return mSearchingDataSource.getAllKeys();
    }

    @Override
    public Flowable<List<HistorySearchedSong>> getHistorySearchedSongs() {
        return mSearchingDataSource.getHistorySearchedSongs();
    }

    @Override
    public Flowable<List<Song>> search(String key) {
        return mSearchingDataSource.search(key);
    }

    @Override
    public Completable insertKeys(List<HistorySearchedKey> keys) {
        return mSearchingDataSource.insertKeys(keys);
    }

    @Override
    public Completable insertSongs(List<HistorySearchedSong> songs) {
        return mSearchingDataSource.insertSongs(songs);
    }

    @Override
    public Completable clearAllKeys() {
        return mSearchingDataSource.clearAllKeys();
    }

    @Override
    public Completable clearAllSongs() {
        return mSearchingDataSource.clearAllSongs();
    }
}
