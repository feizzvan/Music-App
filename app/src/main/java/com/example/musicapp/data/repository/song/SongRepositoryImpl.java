package com.example.musicapp.data.repository.song;

import com.example.musicapp.data.model.song.Song;
import com.example.musicapp.data.model.song.SongList;
import com.example.musicapp.data.source.SongDataSource;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Callback;

public class SongRepositoryImpl implements SongRepository.Local, SongRepository.Remote {
    private final SongDataSource.Local mLocalSongDataSource;
    private final SongDataSource.Remote mRemoteSongDataSource;

    @Inject
    public SongRepositoryImpl(SongDataSource.Local localSongDataSource,
                              SongDataSource.Remote remoteSongDataSource) {
        mLocalSongDataSource = localSongDataSource;
        mRemoteSongDataSource = remoteSongDataSource;
    }

    @Override
    public void loadSongs(Callback<SongList> callback) {
        mRemoteSongDataSource.loadSongs(callback);
    }

    @Override
    public Single<List<Song>> getSongs() {
        return mLocalSongDataSource.getSongs();
    }

    @Override
    public Completable saveSongs(Song... songs) {
        return mLocalSongDataSource.saveSongs(songs);
    }

    @Override
    public Completable updateSong(Song song) {
        return mLocalSongDataSource.updateSong(song);
    }
}
