package com.example.musicapp.data.source.remote;

import androidx.annotation.NonNull;

import com.example.musicapp.R;
import com.example.musicapp.data.model.playlist.CreatePlaylist;
import com.example.musicapp.data.model.playlist.PlaylistById;
import com.example.musicapp.data.model.playlist.PlaylistByUserId;
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
    public Single<PlaylistById> createPlaylist(CreatePlaylist createPlaylist) {
        return Single.create(emitter -> {
            AppService appService = RetrofitHelper.getInstance();
            Call<PlaylistById> call = appService.createPlaylist(createPlaylist);

            call.enqueue(new Callback<PlaylistById>() {
                public void onResponse(@NonNull Call<PlaylistById> call, @NonNull Response<PlaylistById> response) {
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
    public void loadPlaylistByUserId(int userId, Callback<PlaylistByUserId> callback) {
        AppService appService = RetrofitHelper.getInstance();
        Call<PlaylistByUserId> call = appService.getPlaylistByUserId(userId);

        call.enqueue(new Callback<PlaylistByUserId>() {
            @Override
            public void onResponse(@NonNull Call<PlaylistByUserId> call, @NonNull Response<PlaylistByUserId> response) {
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(@NonNull Call<PlaylistByUserId> call, @NonNull Throwable throwable) {
                callback.onFailure(call, throwable);
            }
        });
    }

    @Override
    public void loadPlaylistById(int playlistId, Callback<PlaylistById> callback) {
        AppService appService = RetrofitHelper.getInstance();
        Call<PlaylistById> call = appService.getPlaylistById(playlistId);

        call.enqueue(new Callback<PlaylistById>() {
            @Override
            public void onResponse(@NonNull Call<PlaylistById> call, @NonNull Response<PlaylistById> response) {
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(@NonNull Call<PlaylistById> call, @NonNull Throwable throwable) {
                callback.onFailure(call, throwable);
            }
        });
    }

    @Override
    public Single<PlaylistById> updatePlayList(int playlistId, CreatePlaylist createPlaylist) {
        return Single.create(emitter -> {
            AppService appService = RetrofitHelper.getInstance();
            Call<PlaylistById> call = appService.updatePlayList(playlistId, createPlaylist);

            call.enqueue(new Callback<PlaylistById>() {
                @Override
                public void onResponse(@NonNull Call<PlaylistById> call, @NonNull Response<PlaylistById> response) {
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
}