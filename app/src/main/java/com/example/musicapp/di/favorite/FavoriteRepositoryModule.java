package com.example.musicapp.di.favorite;

import com.example.musicapp.data.repository.favorite.FavoriteRepository;
import com.example.musicapp.data.repository.favorite.FavoriteRepositoryImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class FavoriteRepositoryModule {
    @Binds
    public abstract FavoriteRepository bindRemoteFavoriteRepository(FavoriteRepositoryImpl favoriteRepository);
}
