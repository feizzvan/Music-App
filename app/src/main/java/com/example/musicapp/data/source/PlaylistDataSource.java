package com.example.musicapp.data.source;

import com.example.musicapp.data.model.playlist.CreatePlaylist;
import com.example.musicapp.data.model.playlist.Playlist;
import com.example.musicapp.data.model.playlist.PlaylistById;
import com.example.musicapp.data.model.playlist.PlaylistByUserId;
import com.example.musicapp.data.model.playlist.PlaylistUpdateTitle;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

// Định nghĩa các nguồn dữ liệu cho Playlist
public interface PlaylistDataSource {
    interface Local {
        Flowable<List<Playlist>> getAll();

        Single<Playlist> findByName(String playlistName);

        Completable insert(Playlist playlist);

        Completable update(Playlist playlist);
    }

    interface Remote {
        Single<PlaylistById> createPlaylist(String token, CreatePlaylist createPlaylist);

        Single<PlaylistByUserId> loadPlaylistByUserId(String token, int userId);

        Single<PlaylistById> loadPlaylistById(String token, int playlistId);

        Single<PlaylistById> updatePlaylist(String token, int playlistId, CreatePlaylist createPlaylist);

        Single<PlaylistById> deletePlaylist(String token, int playlistId);

        Single<Playlist> updatePlaylistTitle(String token, int playlistId, PlaylistUpdateTitle playlistUpdateTitle);
    }
}
