package com.example.musicapp.di.searching;

import com.example.musicapp.data.source.SearchingDataSource;
import com.example.musicapp.data.source.local.searching.LocalSearchingDataSource;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class SearchingDataSourceModule {
    @Binds
    public abstract SearchingDataSource bindSearchingDataSource(LocalSearchingDataSource localSearchingDataSource);
}
