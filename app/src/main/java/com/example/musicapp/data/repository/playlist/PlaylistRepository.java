package com.example.musicapp.data.repository.playlist;

import com.example.musicapp.data.model.playlist.CreatePlaylist;
import com.example.musicapp.data.model.playlist.Playlist;
import com.example.musicapp.data.model.playlist.PlaylistById;
import com.example.musicapp.data.model.playlist.PlaylistByUserId;
import com.example.musicapp.data.model.playlist.PlaylistUpdateTitle;

import io.reactivex.rxjava3.core.Single;

// Định nghĩa API cho ViewModel gọi đến, giúp ViewModel không cần quan tâm dữ liệu lấy từ đâu (Quản lý nguồn dữ liệu)
public interface PlaylistRepository {
    interface Local {
//        Flowable<List<Playlist>> getAll();
//
//        Single<Playlist> findByName(String playlistName);
//
//        Flowable<List<PlaylistWithSongs>> getAllPlaylistWithSongs();
//
//        Single<PlaylistWithSongs> findPlaylistWithSongByPlaylistId(int playlistId);

//        Completable insertPlaylistSongCrossRef(PlaylistSongCrossRef object);
//
//        Completable update(Playlist playlist);
//
//        Completable delete(Playlist playlist);
    }

    interface Remote {
        Single<PlaylistById> createPlaylist(String token, CreatePlaylist createPlaylist);

        Single<PlaylistByUserId> loadPlaylistByUserId(String token, int userId);

        Single<PlaylistById> loadPlaylistById(String token, int playlistId);

        Single<PlaylistById> updatePlaylist(String token, int playListId, CreatePlaylist createPlaylist);

        Single<PlaylistById> deletePlaylist(String token, int playlistId);

        Single<Playlist> updatePlaylistTitle(String token, int playlistId, PlaylistUpdateTitle playlistUpdateTitle);
    }
}
