package com.example.musicapp.ui.library.playlist;

import android.annotation.SuppressLint;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicapp.data.model.playlist.CreatePlaylist;
import com.example.musicapp.data.model.playlist.Playlist;
import com.example.musicapp.data.model.playlist.PlaylistById;
import com.example.musicapp.data.model.playlist.PlaylistByUserId;
import com.example.musicapp.data.model.song.Song;
import com.example.musicapp.data.repository.playlist.PlaylistRepositoryImpl;
import com.example.musicapp.utils.TokenManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class PlaylistViewModel extends ViewModel {
    private final PlaylistRepositoryImpl mPlaylistRepository;
    private final TokenManager tokenManager;
    private final MutableLiveData<List<Playlist>> mPlaylists = new MutableLiveData<>();

    @Inject
    public PlaylistViewModel(PlaylistRepositoryImpl playlistRepository, TokenManager tokenManager) {
        mPlaylistRepository = playlistRepository;
        this.tokenManager = tokenManager;
    }

    public LiveData<List<Playlist>> getPlaylists() {
        return mPlaylists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        mPlaylists.postValue(playlists);
    }

    public Single<PlaylistById> createPlaylist(String playlistName) {
        int userId = tokenManager.getUserId();
        CreatePlaylist createPlaylist = new CreatePlaylist(playlistName, userId, null);
        return mPlaylistRepository.createPlaylist(createPlaylist);
    }

    public Single<PlaylistById> updatePlaylist(String playlistName) {
        int userId = tokenManager.getUserId();
        CreatePlaylist createPlaylist = new CreatePlaylist(playlistName, userId, null);
        return mPlaylistRepository.createPlaylist(createPlaylist);
    }

    public void loadPlaylistByUserId(int userId) {
        mPlaylistRepository.loadPlaylistByUserId(userId, new Callback<PlaylistByUserId>() {
            @Override
            public void onResponse(@NonNull Call<PlaylistByUserId> call, @NonNull Response<PlaylistByUserId> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Playlist> playlists = response.body().getPlaylists();
                    mPlaylists.postValue(playlists);
                    Log.d("PlaylistViewModel", "Loaded playlists for user " + userId + ": " + playlists.size());
                } else {
                    mPlaylists.postValue(new ArrayList<>());
                    Log.e("PlaylistViewModel", "Failed to load playlists: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<PlaylistByUserId> call, @NonNull Throwable throwable) {
                mPlaylists.postValue(new ArrayList<>());
                Log.e("PlaylistViewModel", "Error loading playlists: " + throwable.getMessage());
            }
        });
    }

    public void loadPlaylistById(int playlistId) {
        mPlaylistRepository.loadPlaylistById(playlistId, new Callback<PlaylistById>() {
            @Override
            public void onResponse(@NonNull Call<PlaylistById> call, @NonNull Response<PlaylistById> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Playlist playlist = response.body().getData();
                    List<Playlist> currentPlaylists = mPlaylists.getValue() != null ? new ArrayList<>(mPlaylists.getValue()) : new ArrayList<>();
                    int index = -1;
                    for (int i = 0; i < currentPlaylists.size(); i++) {
                        if (currentPlaylists.get(i).getId() == playlistId) {
                            index = i;
                            break;
                        }
                    }
                    if (index != -1) {
                        currentPlaylists.set(index, playlist);
                    } else {
                        currentPlaylists.add(playlist);
                    }
                    mPlaylists.postValue(currentPlaylists);
                    Log.d("PlaylistViewModel", "Playlist fetched by ID: " + playlist.getName() + ", Song count: " + (playlist.getSongs() != null ? playlist.getSongs().size() : 0));
                } else {
                    Log.e("PlaylistViewModel", "Failed to fetch playlist: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<PlaylistById> call, @NonNull Throwable throwable) {
                Log.e("PlaylistViewModel", "Error fetching playlist: " + throwable.getMessage());
            }
        });
    }

    public Completable addSongToPlaylist(int playlistId, int currentSongId) {
        return Completable.create(emitter -> {
            mPlaylistRepository.loadPlaylistById(playlistId, new Callback<PlaylistById>() {
                @SuppressLint("CheckResult")
                @Override
                public void onResponse(@NonNull Call<PlaylistById> call, @NonNull Response<PlaylistById> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Playlist playlist = response.body().getData();
                        List<Integer> songIds = playlist.getSongs() != null ?
                                playlist.getSongs().stream()
                                        .map(Song::getId)
                                        .collect(Collectors.toList()) :
                                new ArrayList<>();
                        if (!songIds.contains(currentSongId)) {
                            songIds.add(currentSongId);
                        }
                        CreatePlaylist createPlaylist = new CreatePlaylist(
                                playlist.getName(),
                                tokenManager.getUserId(),
                                songIds
                        );
                        mPlaylistRepository.updatePlayList(playlistId, createPlaylist)
                                .subscribe(
                                        updatedPlaylist -> {
                                            Log.d("PlaylistViewModel", "Playlist updated: " + updatedPlaylist.getData().getName() + ", Song count: " + (updatedPlaylist.getData().getSongs() != null ? updatedPlaylist.getData().getSongs().size() : 0));
                                            loadPlaylistByUserId(tokenManager.getUserId());
                                            emitter.onComplete();
                                        },
                                        throwable -> {
                                            Log.e("PlaylistViewModel", "Failed to update playlist: " + throwable.getMessage());
                                            emitter.onError(throwable);
                                        }
                                );
                    } else {
                        Log.e("PlaylistViewModel", "Failed to fetch playlist: " + response.message());
                        emitter.onError(new Exception("Failed to fetch playlist: " + response.message()));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PlaylistById> call, @NonNull Throwable throwable) {
                    Log.e("PlaylistViewModel", "Error fetching playlist: " + throwable.getMessage());
                    emitter.onError(throwable);
                }
            });
        });
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final PlaylistRepositoryImpl mPlaylistRepository;
        private final TokenManager tokenManager;

        @Inject
        public Factory(PlaylistRepositoryImpl playlistRepository, TokenManager tokenManager) {
            mPlaylistRepository = playlistRepository;
            this.tokenManager = tokenManager;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(PlaylistViewModel.class)) {
                return (T) new PlaylistViewModel(mPlaylistRepository, tokenManager);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}