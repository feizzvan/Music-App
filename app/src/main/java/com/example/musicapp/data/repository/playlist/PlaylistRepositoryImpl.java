package com.example.musicapp.data.repository.playlist;

import com.example.musicapp.data.model.playlist.CreatePlaylist;
import com.example.musicapp.data.model.playlist.Playlist;
import com.example.musicapp.data.model.playlist.PlaylistById;
import com.example.musicapp.data.model.playlist.PlaylistByUserId;
import com.example.musicapp.data.source.PlaylistDataSource;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Callback;

// Kết nối với LocalPlaylistDataSource hoặc RemotePlaylistDataSource để lấy dữ liệu (Cài đặt repository)
public class PlaylistRepositoryImpl implements PlaylistRepository.Local, PlaylistRepository.Remote {
    //    private final PlaylistDataSource.Local mLocalDataSource;
    private final PlaylistDataSource.Remote mRemoteDataSource;

    @Inject
    public PlaylistRepositoryImpl(PlaylistDataSource.Remote remoteDataSource) {
        mRemoteDataSource = remoteDataSource;
    }

    @Override
    public Single<PlaylistById> createPlaylist(CreatePlaylist createPlaylist) {
        return mRemoteDataSource.createPlaylist(createPlaylist);
    }

    @Override
    public void loadPlaylistByUserId(int userId, Callback<PlaylistByUserId> callback) {
        mRemoteDataSource.loadPlaylistByUserId(userId, callback);
    }

    @Override
    public void loadPlaylistById(int playlistId, Callback<PlaylistById> callback) {
        mRemoteDataSource.loadPlaylistById(playlistId, callback);
    }

    @Override
    public Single<PlaylistById> updatePlayList(int playListId, CreatePlaylist createPlaylist) {
        return mRemoteDataSource.updatePlayList(playListId, createPlaylist);
    }


}
