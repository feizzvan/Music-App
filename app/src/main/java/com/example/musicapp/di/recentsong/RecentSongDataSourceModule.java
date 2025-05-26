package com.example.musicapp.di.recentsong;

import com.example.musicapp.data.source.RecentSongDataSource;
import com.example.musicapp.data.source.local.recent.LocalRecentSongDataSource;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class RecentSongDataSourceModule {
    @Binds
    public abstract RecentSongDataSource bindRecentSongDataSource(LocalRecentSongDataSource localRecentSongDataSource);
}
