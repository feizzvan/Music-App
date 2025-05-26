package com.example.musicapp.data.source;

import com.example.musicapp.data.model.playlist.CreatePlaylist;
import com.example.musicapp.data.model.playlist.Playlist;
import com.example.musicapp.data.model.playlist.PlaylistById;
import com.example.musicapp.data.model.playlist.PlaylistByUserId;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Callback;

// Định nghĩa các nguồn dữ liệu cho Playlist
public interface PlaylistDataSource {
    interface Local {
    }

    interface Remote {
        Single<PlaylistById> createPlaylist(CreatePlaylist createPlaylist);

        void loadPlaylistByUserId(int userId, Callback<PlaylistByUserId> callback);

        void loadPlaylistById(int playlistId, Callback<PlaylistById> callback);

        Single<PlaylistById> updatePlayList(int playlistId, CreatePlaylist createPlaylist);
    }
}
