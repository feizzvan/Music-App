package com.example.musicapp.data.repository.playlist;

import com.example.musicapp.data.model.playlist.CreatePlaylist;
import com.example.musicapp.data.model.playlist.Playlist;
import com.example.musicapp.data.model.playlist.PlaylistById;
import com.example.musicapp.data.model.playlist.PlaylistByUserId;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Callback;

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
        Single<PlaylistById> createPlaylist(CreatePlaylist createPlaylist);

        void loadPlaylistByUserId(int userId, Callback<PlaylistByUserId> callback);

        void loadPlaylistById(int playlistId, Callback<PlaylistById> callback);

        Single<PlaylistById> updatePlayList(int playListId, CreatePlaylist createPlaylist);
    }
}
