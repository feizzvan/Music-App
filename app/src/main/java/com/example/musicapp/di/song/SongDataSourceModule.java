package com.example.musicapp.di.song;

import com.example.musicapp.data.source.SongDataSource;
import com.example.musicapp.data.source.local.song.LocalSongDataSource;
import com.example.musicapp.data.source.remote.RemoteSongDataSourceImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class SongDataSourceModule {
    @Binds
    public abstract SongDataSource.Local bindLocalSongDataSource(LocalSongDataSource localSongDataSource);

    @Binds
    public abstract SongDataSource.Remote bindRemoteSongDataSource(RemoteSongDataSourceImpl remoteSongDataSource);
}
