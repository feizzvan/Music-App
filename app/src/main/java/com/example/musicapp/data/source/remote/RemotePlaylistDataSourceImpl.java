package com.example.musicapp.data.source.remote;

import androidx.annotation.NonNull;

import com.example.musicapp.R;
import com.example.musicapp.data.model.playlist.CreatePlaylist;
import com.example.musicapp.data.model.playlist.Playlist;
import com.example.musicapp.data.model.playlist.PlaylistById;
import com.example.musicapp.data.model.playlist.PlaylistByUserId;
import com.example.musicapp.data.model.playlist.PlaylistUpdateTitle;
import com.example.musicapp.data.source.PlaylistDataSource;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemotePlaylistDataSourceImpl implements PlaylistDataSource.Remote {
    @Inject
    public RemotePlaylistDataSourceImpl() {
    }

    @Override
    public Single<PlaylistById> createPlaylist(String token, CreatePlaylist createPlaylist) {
        return Single.create(emitter -> {
            AppService appService = RetrofitHelper.getInstance();
            Call<PlaylistById> call = appService.createPlaylist(token, createPlaylist);

            call.enqueue(new Callback<PlaylistById>() {
                public void onResponse(@NonNull Call<PlaylistById> call,
                                       @NonNull Response<PlaylistById> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        emitter.onSuccess(response.body());
                    } else {
                        emitter.onError(new Exception((R.string.playlist_creation_failed) + response.message()));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PlaylistById> call, @NonNull Throwable t) {
                    emitter.onError(t);
                }
            });
        });
    }

    @Override
    public Single<PlaylistByUserId> loadPlaylistByUserId(String token, int userId) {
        return Single.create(emitter -> {
            AppService appService = RetrofitHelper.getInstance();
            Call<PlaylistByUserId> call = appService.getPlaylistByUserId(token, userId);

            call.enqueue(new Callback<PlaylistByUserId>() {
                @Override
                public void onResponse(@NonNull Call<PlaylistByUserId> call,
                                       @NonNull Response<PlaylistByUserId> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        emitter.onSuccess(response.body());
                    } else {
                        emitter.onError(new Exception("Load playlists failed: " + response.message()));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PlaylistByUserId> call,
                                      @NonNull Throwable throwable) {
                    emitter.onError(throwable);
                }
            });
        });
    }

    @Override
    public Single<PlaylistById> loadPlaylistById(String token, int playlistId) {
        return Single.create(emitter -> {
            AppService appService = RetrofitHelper.getInstance();
            Call<PlaylistById> call = appService.getPlaylistById(token, playlistId);

            call.enqueue(new Callback<PlaylistById>() {
                @Override
                public void onResponse(@NonNull Call<PlaylistById> call,
                                       @NonNull Response<PlaylistById> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        emitter.onSuccess(response.body());
                    } else {
                        emitter.onError(new Exception("Load playlist failed: " + response.message()));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PlaylistById> call,
                                      @NonNull Throwable throwable) {
                    emitter.onError(throwable);
                }
            });
        });
    }

    @Override
    public Single<PlaylistById> updatePlaylist(String token, int playlistId, CreatePlaylist createPlaylist) {
        return Single.create(emitter -> {
            AppService appService = RetrofitHelper.getInstance();
            Call<PlaylistById> call = appService.updatePlaylist(token, playlistId, createPlaylist);

            call.enqueue(new Callback<PlaylistById>() {
                @Override
                public void onResponse(@NonNull Call<PlaylistById> call,
                                       @NonNull Response<PlaylistById> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        emitter.onSuccess(response.body());
                    } else {
                        emitter.onError(new Exception("Failed to update playlist: " + response.message()));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PlaylistById> call, @NonNull Throwable t) {
                    emitter.onError(t);
                }
            });
        });
    }

    @Override
    public Single<PlaylistById> deletePlaylist(String token, int playlistId) {
        return Single.create(emitter -> {
            AppService appService = RetrofitHelper.getInstance();
            Call<PlaylistById> call = appService.deletePlaylist(token, playlistId);

            call.enqueue(new Callback<PlaylistById>() {
                @Override
                public void onResponse(@NonNull Call<PlaylistById> call,
                                       @NonNull Response<PlaylistById> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        emitter.onSuccess(response.body());
                    } else {
                        emitter.onError(new Exception("Delete playlist failed: " + response.message()));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PlaylistById> call,
                                      @NonNull Throwable throwable) {
                    emitter.onError(throwable);
                }
            });
        });
    }

    @Override
    public Single<Playlist> updatePlaylistTitle(String token, int playlistId, PlaylistUpdateTitle playlistUpdateTitle) {
        return Single.create(emitter -> {
            AppService appService = RetrofitHelper.getInstance();
            Call<Playlist> call = appService.updatePlaylistTitle(token, playlistId, playlistUpdateTitle);

            call.enqueue(new Callback<Playlist>() {
                @Override
                public void onResponse(@NonNull Call<Playlist> call,
                                       @NonNull Response<Playlist> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        emitter.onSuccess(response.body());
                    } else {
                        emitter.onError(new Exception("Update playlist title failed: " + response.message()));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Playlist> call,
                                      @NonNull Throwable throwable) {
                    emitter.onError(throwable);
                }
            });
        });
    }
}