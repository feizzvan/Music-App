package com.example.musicapp.di.artist;

import com.example.musicapp.data.repository.artist.ArtistRepository;
import com.example.musicapp.data.repository.artist.ArtistRepositoryImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class ArtistRepositoryModule {
    @Binds
    public abstract ArtistRepository.Remote bindRemoteArtistRepository(ArtistRepositoryImpl artistRepository);

    @Binds
    public abstract ArtistRepository.Local bindLocalArtistRepository(ArtistRepositoryImpl artistRepository);
}
