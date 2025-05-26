package com.example.musicapp.di.listeningcounts;

import com.example.musicapp.data.repository.listeningcounts.ListeningCountsRepository;
import com.example.musicapp.data.repository.listeningcounts.ListeningCountsRepositoryImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class ListeningCountsRepositoryModule {
    @Binds
    public abstract ListeningCountsRepository bindRemoteListeningCountsRepository(ListeningCountsRepositoryImpl listeningCountsRepository);
}
