package com.example.musicapp.di.recommended;

import com.example.musicapp.data.repository.recommended.RecommendedRepository;
import com.example.musicapp.data.repository.recommended.RecommendedRepositoryImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class RecommendedRepositoryModule {
    @Binds
    public abstract RecommendedRepository bindRecommendedRepository(RecommendedRepositoryImpl recommendedRepository);
}
