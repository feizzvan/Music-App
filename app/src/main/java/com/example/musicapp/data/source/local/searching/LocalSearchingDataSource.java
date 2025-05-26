package com.example.musicapp.data.source.local.searching;

import com.example.musicapp.data.model.history.HistorySearchedKey;
import com.example.musicapp.data.model.history.HistorySearchedSong;
import com.example.musicapp.data.model.song.Song;
import com.example.musicapp.data.source.SearchingDataSource;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public class LocalSearchingDataSource implements SearchingDataSource {
    private final SearchingDAO mSearchingDAO;

    @Inject
    public LocalSearchingDataSource(SearchingDAO searchingDAO) {
        mSearchingDAO = searchingDAO;
    }

    @Override
    public Flowable<List<HistorySearchedKey>> getAllKeys() {
        return mSearchingDAO.getAllKeys();
    }

    @Override
    public Flowable<List<HistorySearchedSong>> getHistorySearchedSongs() {
        return mSearchingDAO.getHistorySearchedSongs();
    }

    @Override
    public Flowable<List<Song>> search(String key) {
        return mSearchingDAO.search(key);
    }

    @Override
    public Completable insertKeys(List<HistorySearchedKey> keys) {
        return mSearchingDAO.insertKeys(keys);
    }

    @Override
    public Completable insertSongs(List<HistorySearchedSong> songs) {
        return mSearchingDAO.insertSongs(songs);
    }

    @Override
    public Completable clearAllKeys() {
        return mSearchingDAO.clearAllKeys();
    }

    @Override
    public Completable clearAllSongs() {
        return mSearchingDAO.clearAllSongs();
    }
}
