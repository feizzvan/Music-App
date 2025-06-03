package com.example.musicapp.data.repository.searching;

import com.example.musicapp.data.model.history.HistorySearchedKey;
import com.example.musicapp.data.model.history.HistorySearchedSong;
import com.example.musicapp.data.model.song.Song;
import com.example.musicapp.data.source.SearchingDataSource;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class SearchingRepositoryImpl implements SearchingRepository.Local, SearchingRepository.Remote {
    private final SearchingDataSource.Local mLocalSearchingDataSource;
    private final SearchingDataSource.Remote mRemoteSearchingDataSource;

    @Inject
    public SearchingRepositoryImpl(SearchingDataSource.Local localSearchingDataSource,
                                   SearchingDataSource.Remote remoteSearchingDataSource) {
        mLocalSearchingDataSource = localSearchingDataSource;
        mRemoteSearchingDataSource = remoteSearchingDataSource;
    }

    @Override
    public Flowable<List<HistorySearchedKey>> getAllKeys() {
        return mLocalSearchingDataSource.getAllKeys();
    }

    @Override
    public Flowable<List<HistorySearchedSong>> getHistorySearchedSongs() {
        return mLocalSearchingDataSource.getHistorySearchedSongs();
    }

    @Override
    public Completable insertKeys(List<HistorySearchedKey> keys) {
        return mLocalSearchingDataSource.insertKeys(keys);
    }

    @Override
    public Completable insertSongs(List<HistorySearchedSong> songs) {
        return mLocalSearchingDataSource.insertSongs(songs);
    }

    @Override
    public Completable clearAllKeys() {
        return mLocalSearchingDataSource.clearAllKeys();
    }

    @Override
    public Completable clearAllSongs() {
        return mLocalSearchingDataSource.clearAllSongs();
    }

    @Override
    public Single<List<Song>> search(String key) {
        return mRemoteSearchingDataSource.search(key);
    }
}
