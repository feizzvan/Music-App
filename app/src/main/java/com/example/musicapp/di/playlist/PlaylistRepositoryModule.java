package com.example.musicapp.di.playlist;

import com.example.musicapp.data.repository.playlist.PlaylistRepository;
import com.example.musicapp.data.repository.playlist.PlaylistRepositoryImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class PlaylistRepositoryModule {
    @Binds
    public abstract PlaylistRepository.Remote bindRemotePlaylistRepository(PlaylistRepositoryImpl playlistRepository);
}
