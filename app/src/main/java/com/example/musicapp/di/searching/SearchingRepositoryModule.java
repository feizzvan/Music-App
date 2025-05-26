package com.example.musicapp.di.searching;

import com.example.musicapp.data.repository.searching.SearchingRepository;
import com.example.musicapp.data.repository.searching.SearchingRepositoryImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class SearchingRepositoryModule {
    @Binds
    public abstract SearchingRepository bindSearchingRepository(SearchingRepositoryImpl repository);
}
