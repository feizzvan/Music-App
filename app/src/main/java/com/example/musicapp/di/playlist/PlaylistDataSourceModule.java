package com.example.musicapp.di.playlist;

import com.example.musicapp.data.source.PlaylistDataSource;
import com.example.musicapp.data.source.local.playlist.LocalPlaylistDataSource;
import com.example.musicapp.data.source.remote.RemotePlaylistDataSourceImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class PlaylistDataSourceModule {
    @Binds
    public abstract PlaylistDataSource.Remote bindRemotePlaylistDataSource(RemotePlaylistDataSourceImpl remotePlaylistDataSource);

    @Binds
    public abstract PlaylistDataSource.Local bindLocalPlaylistDataSource(LocalPlaylistDataSource localPlaylistDataSource);
}
