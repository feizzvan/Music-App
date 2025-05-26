package com.example.musicapp.di.recentsong;

import com.example.musicapp.data.repository.recent.RecentSongRepository;
import com.example.musicapp.data.repository.recent.RecentSongRepositoryImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class RecentSongRepositoryModule {
    @Binds
    public abstract RecentSongRepository bindRecentSongRepository(RecentSongRepositoryImpl recentSongRepository);
}
