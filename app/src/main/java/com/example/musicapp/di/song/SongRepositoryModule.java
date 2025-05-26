package com.example.musicapp.di.song;

import com.example.musicapp.data.repository.song.SongRepository;
import com.example.musicapp.data.repository.song.SongRepositoryImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class SongRepositoryModule {
    @Binds
    public abstract SongRepository.Local bindLocalSongRepository(SongRepositoryImpl songRepository);

    @Binds
    public abstract SongRepository.Remote bindRemoteSongRepository(SongRepositoryImpl songRepository);
}
