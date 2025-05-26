package com.example.musicapp.di.listeningcounts;

import com.example.musicapp.data.source.ListeningCountsDataSource;
import com.example.musicapp.data.source.remote.RemoteListeningCountsDataSourceImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class ListeningCountsDataSourceModule {
    @Binds
    public abstract ListeningCountsDataSource bindRemoteListeningCountsDataSource(RemoteListeningCountsDataSourceImpl remoteListeningCountsDataSource);
}
