package com.example.musicapp.di.recommended;

import com.example.musicapp.data.source.RecommendedDataSource;
import com.example.musicapp.data.source.remote.RemoteRecommendedDataSourceImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class RecommendedDataSourceModule {
    @Binds
    public abstract RecommendedDataSource bindRecommendedDataSource(RemoteRecommendedDataSourceImpl remoteRecommendedDataSource);
}
