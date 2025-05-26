package com.example.musicapp.di.album;

import com.example.musicapp.data.repository.album.AlbumRepository;
import com.example.musicapp.data.repository.album.AlbumRepositoryImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class AlbumRepositoryModule {
    @Binds
    public abstract AlbumRepository bindAlbumRepository(AlbumRepositoryImpl albumRepository);
}
