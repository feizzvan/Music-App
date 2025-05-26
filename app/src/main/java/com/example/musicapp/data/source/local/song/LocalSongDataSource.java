package com.example.musicapp.data.source.local.song;

import com.example.musicapp.data.model.song.Song;
import com.example.musicapp.data.source.SongDataSource;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class LocalSongDataSource implements SongDataSource.Local {
    private final SongDAO mSongDAO;

    @Inject
    public LocalSongDataSource(SongDAO songDAO) {
        this.mSongDAO = songDAO;
    }

    @Override
    public Single<List<Song>> getSongs() {
        return mSongDAO.getAllSongs();
    }

//    @Override
//    public Flowable<List<Song>> getFavoriteSongs() {
//        return mSongDAO.getFavoriteSongs();
//    }
//
//    @Override
//    public Flowable<List<Song>> getTopNMostHeardSongs(int limit) {
//        return mSongDAO.getTopNMostHeardSongs(limit);
//    }
//
//    @Override
//    public Flowable<List<Song>> getTopNForYouSongs(int limit) {
//        return mSongDAO.getTopNForYouSongs(limit);
//    }

    @Override
    public Completable saveSongs(Song... songs) {
        return mSongDAO.insertSongs(songs);
    }

    @Override
    public Completable updateSong(Song song) {
        return mSongDAO.updateSong(song);
    }
}
