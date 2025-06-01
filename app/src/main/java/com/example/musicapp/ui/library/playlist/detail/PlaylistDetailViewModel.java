package com.example.musicapp.ui.library.playlist.detail;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicapp.data.model.playlist.Playlist;
import com.example.musicapp.data.model.song.Song;
import com.example.musicapp.data.repository.playlist.PlaylistRepositoryImpl;
import com.example.musicapp.utils.TokenManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class PlaylistDetailViewModel extends ViewModel {
    private final MutableLiveData<Playlist> mPlaylist = new MutableLiveData<>();
    private final TokenManager tokenManager;
    private Playlist mPlaylistForPlayback;
    private final MutableLiveData<List<Song>> mSongs = new MutableLiveData<>();
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private final PlaylistRepositoryImpl mPlaylistRepository;

    @Inject
    public PlaylistDetailViewModel(PlaylistRepositoryImpl playlistRepository, TokenManager tokenManager) {
        mPlaylistRepository = playlistRepository;
        this.tokenManager = tokenManager;
    }

    public void loadPlaylistById(int playlistId) {
        String token = "Bearer " + tokenManager.getToken();
        mDisposable.add(mPlaylistRepository.loadPlaylistById(token, playlistId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(playlistResponse -> {
                            if (playlistResponse.getData() != null) {
                                Playlist playlist = playlistResponse.getData();
                                mPlaylist.postValue(playlist);

                                List<Song> songs = playlist.getSongs() != null ? playlist.getSongs() : new ArrayList<>();
                                mSongs.postValue(songs);

                                // Tạo playlist cho phát nhạc
                                mPlaylistForPlayback = new Playlist(playlist.getId(), playlist.getName());
                                mPlaylistForPlayback.updateSongs(songs);
                            } else {
                                mPlaylist.postValue(null);
                            }
                        }, throwable -> mPlaylist.postValue(null)
                )
        );
    }

    public void setPlaylist(int id, String name, Integer userId) {
        Playlist playlist = new Playlist();
        playlist.setId(id);
        playlist.setName(name);
        playlist.setUserId(userId);
        mPlaylist.setValue(playlist);
        mSongs.setValue(new ArrayList<>());
        mPlaylistForPlayback = new Playlist(id, name);
        mPlaylistForPlayback.updateSongs(new ArrayList<>());
    }

    public void createPlaylist(String playlistName) {
        mPlaylistForPlayback = new Playlist(-1, playlistName);
        List<Song> songs = mSongs.getValue();
        mPlaylistForPlayback.updateSongs(Objects.requireNonNullElseGet(songs, ArrayList::new));
    }

    public void setSongs(List<Song> songs) {
        mSongs.setValue(songs);
        if (mPlaylistForPlayback != null) {
            mPlaylistForPlayback.updateSongs(songs != null ? songs : new ArrayList<>());
        }
    }

    public LiveData<Playlist> getPlaylist() {
        return mPlaylist;
    }

    public LiveData<List<Song>> getSongs() {
        return mSongs;
    }

    public Playlist getPlaylistForPlayback() {
        return mPlaylistForPlayback;
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
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(PlaylistDetailViewModel.class)) {
                return (T) new PlaylistDetailViewModel(mPlaylistRepository, tokenManager);
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
