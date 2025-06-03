package com.example.musicapp.di;

import android.content.Context;

import androidx.room.Room;

import com.example.musicapp.data.source.local.AlbumDAO;
import com.example.musicapp.data.source.local.AppDatabase;
import com.example.musicapp.data.source.local.artist.ArtistDAO;
import com.example.musicapp.data.source.local.playlist.PlaylistDAO;
import com.example.musicapp.data.source.local.recent.RecentSongDAO;
import com.example.musicapp.data.source.local.searching.SearchingDAO;
import com.example.musicapp.data.source.local.song.SongDAO;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

//Module cách thức tạo lập cho database
@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {
    @Provides
    @Singleton
    public AppDatabase provideAppDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, "music.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    public SongDAO provideSongDAO(AppDatabase appDatabase) {
        return appDatabase.songDAO();
    }

    @Provides
    public PlaylistDAO providePlaylistDAO(AppDatabase appDatabase) {
        return appDatabase.playlistDAO();
    }

    @Provides
    public ArtistDAO provideArtistDAO(AppDatabase appDatabase) {
        return appDatabase.artistDAO();
    }

    @Provides
    public RecentSongDAO provideRecentSongDAO(AppDatabase appDatabase) {
        return appDatabase.recentSongDAO();
    }

    @Provides
    public AlbumDAO provideAlbumDAO(AppDatabase appDatabase) {
        return appDatabase.albumDAO();
    }

    @Provides
    public SearchingDAO provideSearchingDAO(AppDatabase appDatabase) {
        return appDatabase.searchingDAO();
    }
}
