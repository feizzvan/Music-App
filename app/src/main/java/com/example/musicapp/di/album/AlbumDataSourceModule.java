package com.example.musicapp.di.album;

import com.example.musicapp.data.source.AlbumDataSource;
import com.example.musicapp.data.source.remote.RemoteAlbumDataSourceImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

//Khai báo cách thức tạo lập các đối tượng triển khai cho interface
@Module
@InstallIn(SingletonComponent.class)
public abstract class AlbumDataSourceModule {
    @Binds
    public abstract AlbumDataSource.Remote bindRemoteAlbumDataSource(RemoteAlbumDataSourceImpl remoteAlbumDataSource);
}
