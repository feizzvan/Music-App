package com.example.musicapp.data.repository.album;

import com.example.musicapp.data.model.album.Album;
import com.example.musicapp.data.model.album.AlbumById;
import com.example.musicapp.data.model.album.AlbumList;
import com.example.musicapp.data.source.AlbumDataSource;

import javax.inject.Inject;

import retrofit2.Callback;

// `AlbumRepositoryImplement` là một lớp thực thi của `AlbumRepository`. Nó chịu trách nhiệm gọi đến tầng dữ liệu từ xa thông qua lớp `AlbumRemoteDataSouceImplement`.

public class AlbumRepositoryImpl implements AlbumRepository{
    private final AlbumDataSource.Remote mRemoteAlbumDataSource;

    @Inject
    public AlbumRepositoryImpl(AlbumDataSource.Remote remoteAlbumDataSource) {
        mRemoteAlbumDataSource = remoteAlbumDataSource;
    }

    @Override
    public void loadAlbums(Callback<AlbumList> callback) {
        mRemoteAlbumDataSource.loadAlbums(callback);
    }

    @Override
    public void loadAlbumById(int id, Callback<AlbumById> callback) {
        mRemoteAlbumDataSource.loadAlbumById(id, callback);
    }
}
