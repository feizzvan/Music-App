package com.example.musicapp.data.repository.playlist;

import com.example.musicapp.data.model.playlist.CreatePlaylist;
import com.example.musicapp.data.model.playlist.Playlist;
import com.example.musicapp.data.model.playlist.PlaylistById;
import com.example.musicapp.data.model.playlist.PlaylistByUserId;
import com.example.musicapp.data.model.playlist.PlaylistUpdateTitle;
import com.example.musicapp.data.source.PlaylistDataSource;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class PlaylistRepositoryImpl implements PlaylistRepository.Local, PlaylistRepository.Remote {
    //    private final PlaylistDataSource.Local mLocalDataSource;
    private final PlaylistDataSource.Remote mRemoteDataSource;

    @Inject
    public PlaylistRepositoryImpl(PlaylistDataSource.Remote remoteDataSource) {
        mRemoteDataSource = remoteDataSource;
    }

    @Override
    public Single<PlaylistById> createPlaylist(String token, CreatePlaylist createPlaylist) {
        return mRemoteDataSource.createPlaylist(token, createPlaylist);
    }

    @Override
    public Single<PlaylistByUserId> loadPlaylistByUserId(String token, int userId) {
        return mRemoteDataSource.loadPlaylistByUserId(token, userId);
    }

    @Override
    public Single<PlaylistById> loadPlaylistById(String token, int playlistId) {
        return mRemoteDataSource.loadPlaylistById(token, playlistId);
    }

    @Override
    public Single<PlaylistById> updatePlaylist(String token, int playListId, CreatePlaylist createPlaylist) {
        return mRemoteDataSource.updatePlaylist(token, playListId, createPlaylist);
    }

    @Override
    public Single<PlaylistById> deletePlaylist(String token, int playlistId) {
        return mRemoteDataSource.deletePlaylist(token, playlistId);
    }

    @Override
    public Single<Playlist> updatePlaylistTitle(String token, int playlistId, PlaylistUpdateTitle playlistUpdateTitle) {
        return mRemoteDataSource.updatePlaylistTitle(token, playlistId, playlistUpdateTitle);
    }
}
