package com.example.musicapp.ui.library.playlist;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicapp.data.model.playlist.CreatePlaylist;
import com.example.musicapp.data.model.playlist.Playlist;
import com.example.musicapp.data.model.playlist.PlaylistById;
import com.example.musicapp.data.model.playlist.PlaylistUpdateTitle;
import com.example.musicapp.data.model.song.Song;
import com.example.musicapp.data.repository.playlist.PlaylistRepositoryImpl;
import com.example.musicapp.utils.TokenManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class PlaylistViewModel extends ViewModel {
    private final PlaylistRepositoryImpl mPlaylistRepository;
    private final TokenManager tokenManager;
    private final MutableLiveData<List<Playlist>> mPlaylists = new MutableLiveData<>();
    private final CompositeDisposable mDisposable = new CompositeDisposable();

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
        String token = "Bearer " + tokenManager.getToken();
        CreatePlaylist createPlaylist = new CreatePlaylist(playlistName, userId, null);
        return mPlaylistRepository.createPlaylist(token, createPlaylist)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void loadPlaylistByUserId(int userId) {
        String token = "Bearer " + tokenManager.getToken();
        mDisposable.add(mPlaylistRepository.loadPlaylistByUserId(token, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(playlistByUserId -> {
                            List<Playlist> playlists = playlistByUserId.getPlaylists();
                            mPlaylists.postValue(playlists);
                        }, throwable -> mPlaylists.postValue(new ArrayList<>())
                )
        );
    }

//    public void loadPlaylistById(int playlistId) {
//        mPlaylistRepository.loadPlaylistById(playlistId, new Callback<PlaylistById>() {
//            @Override
//            public void onResponse(@NonNull Call<PlaylistById> call, @NonNull Response<PlaylistById> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    Playlist playlist = response.body().getData();
//                    List<Playlist> currentPlaylists = mPlaylists.getValue() != null ? new ArrayList<>(mPlaylists.getValue()) : new ArrayList<>();
//                    int index = -1;
//                    for (int i = 0; i < currentPlaylists.size(); i++) {
//                        if (currentPlaylists.get(i).getId() == playlistId) {
//                            index = i;
//                            break;
//                        }
//                    }
//                    if (index != -1) {
//                        currentPlaylists.set(index, playlist);
//                    } else {
//                        currentPlaylists.add(playlist);
//                    }
//                    mPlaylists.postValue(currentPlaylists);
//                    Log.d("PlaylistViewModel", "Playlist fetched by ID: " + playlist.getName() + ", Song count: " + (playlist.getSongs() != null ? playlist.getSongs().size() : 0));
//                } else {
//                    Log.e("PlaylistViewModel", "Failed to fetch playlist: " + response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<PlaylistById> call, @NonNull Throwable throwable) {
//                Log.e("PlaylistViewModel", "Error fetching playlist: " + throwable.getMessage());
//            }
//        });
//    }

    public Completable addSongToPlaylist(int playlistId, int currentSongId) {
        String token = "Bearer " + tokenManager.getToken();
        return mPlaylistRepository.loadPlaylistById(token, playlistId)
                .subscribeOn(Schedulers.io())
                .flatMapCompletable(playlistById -> {
                    Playlist playlist = playlistById.getData();
                    List<Integer> songIds = playlist.getSongs() != null
                            ? playlist.getSongs().stream().map(Song::getId).collect(Collectors.toList())
                            : new ArrayList<>();
                    if (!songIds.contains(currentSongId)) {
                        songIds.add(currentSongId);
                    }
                    CreatePlaylist createPlaylist = new CreatePlaylist(
                            playlist.getName(),
                            tokenManager.getUserId(),
                            songIds
                    );
                    return mPlaylistRepository.updatePlaylist(token, playlistId, createPlaylist)
                            .subscribeOn(Schedulers.io())
                            .doOnSuccess(updatedPlaylist -> {
                                Log.d("PlaylistViewModel", "Playlist updated: " + updatedPlaylist.getData().getName() +
                                        ", Song count: " + (updatedPlaylist.getData().getSongs() != null ? updatedPlaylist.getData().getSongs().size() : 0));
                                loadPlaylistByUserId(tokenManager.getUserId());
                            })
                            .doOnError(throwable -> Log.e("PlaylistViewModel", "Failed to update playlist: " + throwable.getMessage()))
                            .ignoreElement();
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void deletePlaylist(int playlistId) {
        // Xóa khỏi LiveData ngay
        List<Playlist> current = mPlaylists.getValue();
        if (current != null) {
            List<Playlist> updated = new ArrayList<>(current);
            for (int i = 0; i < updated.size(); i++) {
                if (updated.get(i).getId() == playlistId) {
                    updated.remove(i);
                    break;
                }
            }
            mPlaylists.postValue(updated);
        }

        String token = "Bearer " + tokenManager.getToken();
        mDisposable.add(mPlaylistRepository.deletePlaylist(token, playlistId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            Log.d("PlaylistViewModel", "Playlist deleted: " + playlistId);
                            loadPlaylistByUserId(tokenManager.getUserId());
                        },
                        throwable -> Log.e("PlaylistViewModel", "Failed to delete playlist: " + throwable.getMessage())
                )
        );
    }

    public void renamePlaylist(int playlistId, String newTitle) {
        String token = "Bearer " + tokenManager.getToken();
        PlaylistUpdateTitle updateTitle = new PlaylistUpdateTitle(newTitle);
        mDisposable.add(mPlaylistRepository.updatePlaylistTitle(token, playlistId, updateTitle)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        updatedPlaylist -> {
                            Log.d("PlaylistViewModel", "Playlist renamed: " + playlistId + " -> " + newTitle);
                            loadPlaylistByUserId(tokenManager.getUserId());
                        },
                        throwable -> Log.e("PlaylistViewModel", "Failed to rename playlist: " + throwable.getMessage())
                )
        );
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

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }
}