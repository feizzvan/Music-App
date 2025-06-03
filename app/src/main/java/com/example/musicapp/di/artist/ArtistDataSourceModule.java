package com.example.musicapp.di.artist;

import com.example.musicapp.data.source.ArtistDataSource;
import com.example.musicapp.data.source.local.artist.LocalArtistDataSource;
import com.example.musicapp.data.source.remote.RemoteArtistDataSource;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class ArtistDataSourceModule {
    @Binds
    public abstract ArtistDataSource.Local bindLocalArtistDataSource(LocalArtistDataSource localArtistDataSource);

    @Binds
    public abstract ArtistDataSource.Remote bindRemoteArtistDataSource(RemoteArtistDataSource remoteArtistDataSource);
}
