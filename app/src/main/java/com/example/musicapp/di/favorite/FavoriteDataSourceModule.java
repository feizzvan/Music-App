package com.example.musicapp.di.favorite;

import com.example.musicapp.data.source.FavoriteDataSource;
import com.example.musicapp.data.source.remote.RemoteFavoriteDataSourceImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class FavoriteDataSourceModule {
    @Binds
    public abstract FavoriteDataSource bindRemoteFavoriteDataSource(RemoteFavoriteDataSourceImpl remoteFavoriteDataSource);
}
